package com.gestion.repository;

import com.gestion.exception.EntidadNoEncontradaException;
import com.gestion.exception.GestionException;
import com.gestion.exception.IntegridadException;
import com.gestion.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

// Operaciones CRUD comunes a todas las entidades. T = entidad, ID = tipo de su clave primaria
public abstract class RepositorioBase<T, ID> {

    protected final Class<T> clase;

    protected RepositorioBase(Class<T> clase) {
        this.clase = clase;
    }

    public void guardar(T entidad) {
        ejecutarEnTransaccion(session -> session.persist(entidad));
    }

    public void actualizar(T entidad) {
        ejecutarEnTransaccion(session -> session.merge(entidad));
    }

    // Buscamos y borramos en la misma sesión: remove() solo funciona con entidades gestionadas
    public void eliminarPorId(ID id) {
        ejecutarEnTransaccion(session -> {
            T entidad = session.find(clase, id);
            if (entidad == null) {
                throw new EntidadNoEncontradaException("No existe ningún registro con código " + id + ".");
            }
            session.remove(entidad);
        });
    }

    public Optional<T> buscarPorId(ID id) {
        return consultar(session -> Optional.ofNullable(session.find(clase, id)));
    }

    public boolean existe(ID id) {
        return buscarPorId(id).isPresent();
    }

    // JPQL: se consulta sobre el nombre de la clase (Proveedor), no de la tabla (proveedores)
    public List<T> listarTodos() {
        return consultar(session ->
                session.createQuery("FROM " + clase.getSimpleName(), clase).getResultList());
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

    // Convierte los errores técnicos de Hibernate en excepciones con mensaje para el usuario
    private GestionException traducirError(RuntimeException e) {
        Throwable causa = e;
        while (causa != null) {
            if (causa instanceof ConstraintViolationException) {
                return new IntegridadException(
                        "La operación no es posible: rompería la integridad de los datos (registro duplicado o en uso).");
            }
            causa = causa.getCause();
        }
        return new GestionException("Error al acceder a la base de datos. Comprueba que MySQL está arrancado.", e);
    }
}