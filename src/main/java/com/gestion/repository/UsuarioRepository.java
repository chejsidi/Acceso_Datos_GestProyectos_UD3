package com.gestion.repository;

import com.gestion.model.Usuario;
import org.hibernate.Session;
import com.gestion.util.HibernateUtil;

public class UsuarioRepository extends RepositorioBase<Usuario, String> {

    public UsuarioRepository() {
        super(Usuario.class);
    }

    public long contar() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT COUNT(u) FROM Usuario u", Long.class)
                    .uniqueResult();
        }
    }
}