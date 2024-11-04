package Model.fileManager;

import java.util.HashMap;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import Model.IAccesoDatos;
import Model.Pelicula;

public class Hibernate implements IAccesoDatos {

    private SessionFactory sessionFactory;

    // Constructor para inicializar la fábrica de sesiones
    public Hibernate() {
        try {
            sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Error al crear la SessionFactory: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    @Override
    public void añadir(Pelicula pelicula) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            session.save(pelicula);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void modificar(Pelicula pelicula) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            session.update(pelicula);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public HashMap<Integer, Pelicula> leerTodos() {
        HashMap<Integer, Pelicula> peliculas = new HashMap<>();
        try (Session session = sessionFactory.openSession()) {
            List<Pelicula> lista = session.createQuery("FROM Pelicula", Pelicula.class).list();
            for (Pelicula pelicula : lista) {
                peliculas.put(pelicula.getId(), pelicula);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return peliculas;
    }

    @Override
    public void escribirTodos(HashMap<Integer, Pelicula> lista) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            for (Pelicula pelicula : lista.values()) {
                session.saveOrUpdate(pelicula);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public void borrar(int id) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            Pelicula pelicula = session.get(Pelicula.class, id);
            if (pelicula != null) {
                session.delete(pelicula);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        }
    }

    @Override
    public Pelicula buscar(int id) {
        Pelicula pelicula = null;
        try (Session session = sessionFactory.openSession()) {
            pelicula = session.get(Pelicula.class, id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pelicula;
    }
}
