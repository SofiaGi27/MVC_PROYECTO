package Model.fileManager; // Declara que esta clase pertenece al paquete Model.fileManager.

import java.sql.Connection; // Importa la clase para manejar conexiones a la base de datos.
import Auxiliares.ConexionProperties; // Importa la clase que gestiona las propiedades de conexión.

import java.sql.DriverManager; // Importa la clase para gestionar la conexión a la base de datos.
import java.sql.PreparedStatement; // Importa la clase para ejecutar sentencias SQL preparadas.
import java.sql.ResultSet; // Importa la clase para manejar los resultados de una consulta SQL.
import java.sql.SQLException; // Importa la clase para manejar excepciones relacionadas con SQL.
import java.util.HashMap; // Importa la clase para utilizar mapas de clave-valor.

import Model.IAccesoDatos; // Importa la interfaz que define el acceso a datos.
import Model.Pelicula; // Importa la clase Pelicula, que representa una película.

public class ConexionMySQL implements IAccesoDatos { // Clase que implementa la interfaz IAccesoDatos.
    private Connection conexion; // Variable para almacenar la conexión a la base de datos.
    private ConexionProperties propiedades; // Variable para manejar las propiedades de conexión.

    // Constructor que inicializa las propiedades.
    public ConexionMySQL() {
    	this.propiedades = new ConexionProperties(); // Crea una nueva instancia de ConexionProperties para cargar las configuraciones.
    }
    
    // Método para establecer la conexión a la base de datos.
    public Connection conectar() {
        try {
            // Verifica si la conexión es null o está cerrada.
            if (this.conexion == null || this.conexion.isClosed()) {
                // Intenta establecer la conexión usando la URL, usuario y contraseña desde las propiedades.
                this.conexion = DriverManager.getConnection(propiedades.getmysqlurl(), propiedades.mysqluser(), propiedades.getmysqlpassword());
            }
            return this.conexion; // Retorna la conexión si es exitosa.
        } catch (SQLException e) { // Captura excepciones de SQL.
            // Manejo de excepciones si hay un error en la conexión.
            System.out.println("Error al conectar a la base de datos: " + e.getMessage());
            return null; // Retorna null si hay un error en la conexión.
        }
    }

    // Método para cerrar la conexión (opcional).
    public void cerrar() {
        if (conexion != null) { // Verifica si la conexión no es null.
            try {
                conexion.close(); // Intenta cerrar la conexión.
            } catch (SQLException e) { // Captura excepciones de SQL al cerrar.
                // Manejo de excepciones si hay un error al cerrar la conexión.
                e.printStackTrace();
            }
        }
    }

    // Método para añadir una película a la base de datos.
    public void añadir(Pelicula pelicula) {
        // Verifica si la conexión está establecida.
        if (conectar() == null) {
            throw new IllegalStateException("La conexión no está inicializada. Asegúrate de conectar primero.");
        }
        // Consulta SQL para insertar una nueva película.
        String sql = "INSERT INTO peliculas (id, titulo, director, anio, genero) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement statement = conexion.prepareStatement(sql)) { // Prepara la sentencia SQL.
            // Asigna los valores a la sentencia.
            statement.setInt(1, pelicula.getId()); // Establece el ID.
            statement.setString(2, pelicula.getTitulo()); // Establece el título.
            statement.setString(3, pelicula.getDirector()); // Establece el director.
            statement.setInt(4, pelicula.getAnio()); // Establece el año.
            statement.setString(5, pelicula.getGenero()); // Establece el género.
            statement.executeUpdate(); // Ejecuta la actualización (inserción).
            System.out.println("Película añadida a la base de datos exitosamente."); // Mensaje de éxito.
        } catch (SQLException e) { // Captura excepciones de SQL al añadir la película.
            // Manejo de excepciones si hay un error al añadir la película.
            e.printStackTrace();
        } finally {
            cerrar(); // Cierra la conexión después de usarla.
        }
    }

    // Método para leer todas las películas de la base de datos.
    public HashMap<Integer, Pelicula> leerTodos() {
        HashMap<Integer, Pelicula> peliculas = new HashMap<>(); // Mapa para almacenar las películas.
        // Verifica si la conexión está establecida.
        if (conectar() == null) {
            throw new IllegalStateException("La conexión no está inicializada. Asegúrate de conectar primero.");
        }
        // Consulta SQL para seleccionar todas las películas.
        String sql = "SELECT * FROM peliculas";
        try (PreparedStatement statement = conexion.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) { // Ejecuta la consulta y obtiene los resultados.
             
            // Recorre los resultados.
            while (resultSet.next()) {
                int id = resultSet.getInt("id"); // Obtiene el ID.
                String titulo = resultSet.getString("titulo"); // Obtiene el título.
                String director = resultSet.getString("director"); // Obtiene el director.
                int anio = resultSet.getInt("anio"); // Obtiene el año.
                String genero = resultSet.getString("genero"); // Obtiene el género.
                // Añade la película al mapa.
                peliculas.put(id, new Pelicula(id, titulo, director, anio, genero));
            }
        } catch (SQLException e) { // Captura excepciones de SQL al leer las películas.
            // Manejo de excepciones si hay un error al leer las películas.
            e.printStackTrace();
        } finally {
            cerrar(); // Cierra la conexión después de usarla.
        }
        return peliculas; // Retorna el mapa de películas.
    }

    // Método para modificar una película en la base de datos.
    public void modificar(Pelicula pelicula) {
        // Verifica si la conexión está establecida.
        if (conectar() == null) {
            throw new IllegalStateException("La conexión no está inicializada. Asegúrate de conectar primero.");
        }
        // Consulta SQL para actualizar una película.
        String sql = "UPDATE peliculas SET titulo = ?, director = ?, anio = ?, genero = ? WHERE id = ?";
        try (PreparedStatement statement = conexion.prepareStatement(sql)) { // Prepara la sentencia SQL.
            // Asigna los valores a la sentencia.
            statement.setString(1, pelicula.getTitulo()); // Establece el título.
            statement.setString(2, pelicula.getDirector()); // Establece el director.
            statement.setInt(3, pelicula.getAnio()); // Establece el año.
            statement.setString(4, pelicula.getGenero()); // Establece el género.
            statement.setInt(5, pelicula.getId()); // Establece el ID.
            int filasActualizadas = statement.executeUpdate(); // Ejecuta la actualización (modificación).
            // Verifica si se actualizó alguna fila.
            if (filasActualizadas > 0) {
                System.out.println("Película modificada exitosamente."); // Mensaje de éxito.
            } else {
                System.out.println("No se encontró una película con el ID especificado."); // Mensaje si no se encontró la película.
            }
        } catch (SQLException e) { // Captura excepciones de SQL al modificar la película.
            // Manejo de excepciones si hay un error al modificar la película.
            e.printStackTrace();
        } finally {
            cerrar(); // Cierra la conexión después de usarla.
        }
    }

    // Método para buscar una película por ID.
    public Pelicula buscar(int id) {
        // Verifica si la conexión está establecida.
        if (conectar() == null) {
            throw new IllegalStateException("La conexión no está inicializada. Asegúrate de conectar primero.");
        }
        // Consulta SQL para seleccionar una película por ID.
        String sql = "SELECT * FROM peliculas WHERE id = ?";
        Pelicula pelicula = null; // Variable para almacenar la película encontrada.
        try (PreparedStatement statement = conexion.prepareStatement(sql)) {
            statement.setInt(1, id); // Establece el ID en la consulta.
            ResultSet resultSet = statement.executeQuery(); // Ejecuta la consulta.
            if (resultSet.next()) { // Verifica si se encontró un resultado.
                String titulo = resultSet.getString("titulo"); // Obtiene el título.
                String director = resultSet.getString("director"); // Obtiene el director.
                int anio = resultSet.getInt("anio"); // Obtiene el año.
                String genero = resultSet.getString("genero"); // Obtiene el género.
                // Crea una nueva instancia de Pelicula con los datos encontrados.
                pelicula = new Pelicula(id, titulo, director, anio, genero);
            } else {
                System.out.println("No se encontró una película con el ID especificado."); // Mensaje si no se encontró la película.
            }
        } catch (SQLException e) { // Captura excepciones de SQL al buscar la película.
            // Manejo de excepciones si hay un error al buscar la película.
            e.printStackTrace();
        } finally {
            cerrar(); // Cierra la conexión después de usarla.
        }
        return pelicula; // Retorna la película encontrada o null si no se encontró.
    }

    // Método para borrar una película de la base de datos.
    public void borrar(int id) {
        // Verifica si la conexión está establecida.
        if (conectar() == null) {
            throw new IllegalStateException("La conexión no está inicializada. Asegúrate de conectar primero.");
        }
        // Consulta SQL para eliminar una película por ID.
        String sql = "DELETE FROM peliculas WHERE id = ?";
        try (PreparedStatement statement = conexion.prepareStatement(sql)) { // Prepara la sentencia SQL.
            statement.setInt(1, id); // Establece el ID en la consulta.
            int filasBorradas = statement.executeUpdate(); // Ejecuta la actualización (borrado).
            // Verifica si se borró alguna fila.
            if (filasBorradas > 0) {
                System.out.println("Película borrada exitosamente."); // Mensaje de éxito.
            } else {
                System.out.println("No se encontró una película con el ID especificado."); // Mensaje si no se encontró la película.
            }
        } catch (SQLException e) { // Captura excepciones de SQL al borrar la película.
            // Manejo de excepciones si hay un error al borrar la película.
            e.printStackTrace();
        } finally {
            cerrar(); // Cierra la conexión después de usarla.
        }
    }

    @Override
    public void escribirTodos(HashMap<Integer, Pelicula> lista) { // Método para escribir todas las películas (no implementado).
        // TODO Auto-generated method stub
    }
}
