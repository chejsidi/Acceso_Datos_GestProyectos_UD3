package com.gestion.repository;

import com.gestion.exception.EntidadNoEncontradaException;
import com.gestion.exception.GestionException;
import com.gestion.exception.IntegridadException;
import com.gestion.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import com.gestion.model.RegistroOperacion;
import com.gestion.util.SesionActual;

// Operaciones CRUD comunes a todas las entidades. T = entidad, ID = tipo de su clave primaria
public abstract class RepositorioBase<T, ID> {

    protected final Class<T> clase;
    private static final Logger log = LoggerFactory.getLogger(RepositorioBase.class);

    protected RepositorioBase(Class<T> clase) {
        this.clase = clase;
    }

    public void guardar(T entidad) {
        ejecutarEnTransaccion(session -> {
            session.persist(entidad);
            auditar(session, "ALTA", session.getIdentifier(entidad), entidad);
        });
    }

    public void actualizar(T entidad) {
        ejecutarEnTransaccion(session -> {
            T actualizada = session.merge(entidad);
            auditar(session, "MODIFICACION", session.getIdentifier(actualizada), actualizada);
        });
    }

    public void eliminarPorId(ID id) {
        ejecutarEnTransaccion(session -> {
            T entidad = session.find(clase, id);
            if (entidad == null) {
                throw new EntidadNoEncontradaException("No existe ningún registro con código " + id + ".");
            }
            session.remove(entidad);
            auditar(session, "BAJA", id, entidad);
        });
    }

    // Los repositorios de los propios registros lo desactivan para no auditarse a sí mismos
    protected boolean auditable() {
        return true;
    }

    // Misma sesión y transacción que la operación: si la operación falla, tampoco queda registrada
    private void auditar(Session session, String operacion, Object id, T entidad) {
        if (auditable()) {
            session.persist(new RegistroOperacion(SesionActual.nombreUsuario(), operacion,
                    clase.getSimpleName(), String.valueOf(id), entidad.toString()));
        }
    }

    public Optional<T> buscarPorId(ID id) {
        return consultar(session -> Optional.ofNullable(session.find(clase, id)));
    }

    public boolean existe(ID id) {
        return buscarPorId(id).isPresent();
    }

    public List<T> listarTodos() {
        return consultar(session ->
                session.createQuery("FROM " + clase.getSimpleName(), clase).getResultList());
    }

    public long contar() {
        return consultar(session -> session.createQuery(
                 "SELECT COUNT(e) FROM " + clase.getSimpleName() + " e", Long.class).getSingleResult());
    }

    // Prepara el texto para un LIKE sin distinguir mayúsculas; null = sin filtro
    protected static String patronLike(String texto) {
        return texto == null ? null : "%" + texto.toLowerCase() + "%";
    }

    // Para lecturas: abre sesión, ejecuta la consulta y la cierra
    protected <R> R consultar(Function<Session, R> consulta) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return consulta.apply(session);
        } catch (GestionException e) {
            throw e;
        } catch (RuntimeException e) {
            throw traducirError(e);
        }
    }

    // Para escrituras: todo dentro de una transacción; si algo falla se hace rollback
    protected void ejecutarEnTransaccion(Consumer<Session> accion) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                accion.accept(session);
                tx.commit();
            } catch (RuntimeException e) {
                if (tx.isActive()) {
                    tx.rollback();
                }
                throw e;
            }
        } catch (GestionException e) {
            throw e;
        } catch (RuntimeException e) {
            throw traducirError(e);
        }
    }

    // Convierte los errores técnicos de Hibernate en excepciones con mensaje para el usuario.
    private GestionException traducirError(RuntimeException e) {
        Throwable causa = e;
        while (causa != null) {
            if (causa instanceof ConstraintViolationException) {
                log.warn("Violación de integridad en {}: {}", clase.getSimpleName(), causa.getMessage());
                return new IntegridadException(
                        "La operación no es posible: rompería la integridad de los datos (registro duplicado o en uso).");
            }
            causa = causa.getCause();
        }
        log.error("Error de base de datos en {}", clase.getSimpleName(), e);
        return new GestionException("Error al acceder a la base de datos. Comprueba que MySQL está arrancado.", e);
    }
}