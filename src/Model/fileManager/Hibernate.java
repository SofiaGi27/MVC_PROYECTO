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

    /* Constructor para inicializar la fábrica de sesiones de Hibernate.
     Crea una SessionFactory a partir de la configuración de hibernate.cfg.xml*/
    public Hibernate() {
        try {
            sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Error al crear la SessionFactory: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

 // Añade una nueva película a la base de datos.
    // Inicia una transacción, guarda la película y luego realiza un commit para confirmar los cambios.
    @Override
    public void añadir(Pelicula pelicula) {
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            session.save(pelicula);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback(); // Si ocurre un error, se hace rollback de la transacción.
            e.printStackTrace();
        }
    }

 // Modifica una película existente en la base de datos.
    // Inicia una transacción, actualiza la película y luego realiza un commit para confirmar los cambios.
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

 // Lee todas las películas de la base de datos.
    // Crea una consulta para obtener todas las películas y las almacena en un HashMap utilizando el ID como clave.
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

 // Escribe todas las películas en la base de datos, guardando o actualizando las que ya existen.
    // Inicia una transacción, guarda o actualiza cada película en el HashMap y luego realiza un commit.
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

 // Borra una película de la base de datos usando su ID.
    // Inicia una transacción, obtiene la película por su ID y la elimina, luego hace commit para confirmar los cambios.
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

 // Busca una película en la base de datos usando su ID.
    // Obtiene la película por su ID y la retorna.
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
