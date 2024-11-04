package Model.fileManager; // Declara el paquete donde se encuentra esta clase

import java.sql.Connection; // Importa la clase para manejar conexiones a la base de datos
import Auxiliares.ConexionProperties; // Asegúrate de importar la clase ConexionProperties para manejar las propiedades de conexión

import java.sql.DriverManager; // Importa la clase para gestionar la conexión a la base de datos
import java.sql.PreparedStatement; // Importa la clase para ejecutar sentencias SQL preparadas
import java.sql.ResultSet; // Importa la clase para manejar los resultados de una consulta SQL
import java.sql.SQLException; // Importa la clase para manejar excepciones relacionadas con SQL
import java.util.HashMap; // Importa la clase para utilizar mapas que almacenan pares clave-valor

import Model.IAccesoDatos; // Importa la interfaz que define métodos para el acceso a datos
import Model.Pelicula; // Importa la clase Pelicula que representa una película

// Clase que implementa la interfaz IAccesoDatos para manejar la conexión y operaciones con una base de datos SQLite
public class ConexionSQLite implements IAccesoDatos {
	private Connection conexion; // Variable para almacenar la conexión a la base de datos
	private ConexionProperties propiedades; // Variable para manejar las propiedades de conexión a la base de datos

	// Constructor que inicializa las propiedades de conexión
    public ConexionSQLite() {
    	this.propiedades = new ConexionProperties(); // Crea una nueva instancia de ConexionProperties
    }

	// Método para establecer la conexión a la base de datos
	public Connection conectar() {
		try {
			// Verifica si la conexión es null o está cerrada
			if (this.conexion == null || this.conexion.isClosed()) {
				// Intenta establecer la conexión utilizando la URL de SQLite obtenida de las propiedades
				this.conexion = DriverManager.getConnection(propiedades.getsqliteurl());
			}
			return this.conexion; // Retorna la conexión establecida
		} catch (SQLException e) {
			// Manejo de excepciones en caso de error al intentar conectar
			System.out.println("Error al conectar a la base de datos: " + e.getMessage());
			return null; // Retorna null si hay un error en la conexión
		}
	}

	// Método para cerrar la conexión a la base de datos (opcional)
	public void cerrar() {
		if (conexion != null) { // Verifica si la conexión no es null
			try {
				conexion.close(); // Cierra la conexión
			} catch (SQLException e) {
				// Manejo de excepciones si hay un error al cerrar la conexión
				e.printStackTrace();
			}
		}
	}

	// Método para añadir una nueva película a la base de datos
	public void añadir(Pelicula pelicula) {
		// Verifica si la conexión está establecida
		if (conectar() == null) {
			throw new IllegalStateException("La conexión no está inicializada. Asegúrate de conectar primero.");
		}
		// Consulta SQL para insertar una nueva película
		String sql = "INSERT INTO peliculas (id, titulo, director, anio, genero) VALUES (?, ?, ?, ?, ?)";
		try (PreparedStatement statement = conexion.prepareStatement(sql)) { // Prepara la sentencia SQL
			// Asigna los valores a la sentencia SQL
			statement.setInt(1, pelicula.getId()); // Establece el ID de la película
			statement.setString(2, pelicula.getTitulo()); // Establece el título de la película
			statement.setString(3, pelicula.getDirector()); // Establece el director de la película
			statement.setInt(4, pelicula.getAnio()); // Establece el año de la película
			statement.setString(5, pelicula.getGenero()); // Establece el género de la película
			statement.executeUpdate(); // Ejecuta la actualización (inserción) en la base de datos
			System.out.println("Película añadida a la base de datos exitosamente."); // Mensaje de éxito
		} catch (SQLException e) {
			// Manejo de excepciones si hay un error al intentar añadir la película
			e.printStackTrace();
		} finally {
			cerrar(); // Cierra la conexión después de usarla
		}
	}

	// Método para leer todas las películas de la base de datos
	public HashMap<Integer, Pelicula> leerTodos() {
		HashMap<Integer, Pelicula> peliculas = new HashMap<>(); // Mapa para almacenar las películas leídas
		// Verifica si la conexión está establecida
		if (conectar() == null) {
			throw new IllegalStateException("La conexión no está inicializada. Asegúrate de conectar primero.");
		}
		// Consulta SQL para seleccionar todas las películas
		String sql = "SELECT * FROM peliculas";
		try (PreparedStatement statement = conexion.prepareStatement(sql);
				ResultSet resultSet = statement.executeQuery()) { // Ejecuta la consulta y obtiene los resultados

			// Recorre los resultados de la consulta
			while (resultSet.next()) {
				int id = resultSet.getInt("id"); // Obtiene el ID de la película
				String titulo = resultSet.getString("titulo"); // Obtiene el título de la película
				String director = resultSet.getString("director"); // Obtiene el director de la película
				int anio = resultSet.getInt("anio"); // Obtiene el año de la película
				String genero = resultSet.getString("genero"); // Obtiene el género de la película
				// Añade la película al mapa utilizando el ID como clave
				peliculas.put(id, new Pelicula(id, titulo, director, anio, genero));
			}
		} catch (SQLException e) {
			// Manejo de excepciones si hay un error al intentar leer las películas
			e.printStackTrace();
		} finally {
			cerrar(); // Cierra la conexión después de usarla
		}
		return peliculas; // Retorna el mapa de películas leídas
	}

	// Método para modificar una película existente en la base de datos
	public void modificar(Pelicula pelicula) {
		// Verifica si la conexión está establecida
		if (conectar() == null) {
			throw new IllegalStateException("La conexión no está inicializada. Asegúrate de conectar primero.");
		}
		// Consulta SQL para actualizar los datos de una película
		String sql = "UPDATE peliculas SET titulo = ?, director = ?, anio = ?, genero = ? WHERE id = ?";
		try (PreparedStatement statement = conexion.prepareStatement(sql)) { // Prepara la sentencia SQL
			// Asigna los valores a la sentencia
			statement.setString(1, pelicula.getTitulo()); // Establece el nuevo título
			statement.setString(2, pelicula.getDirector()); // Establece el nuevo director
			statement.setInt(3, pelicula.getAnio()); // Establece el nuevo año
			statement.setString(4, pelicula.getGenero()); // Establece el nuevo género
			statement.setInt(5, pelicula.getId()); // Establece el ID de la película que se va a modificar
			int filasActualizadas = statement.executeUpdate(); // Ejecuta la actualización (modificación)
			// Verifica si se actualizó alguna fila
			if (filasActualizadas > 0) {
				System.out.println("Película modificada exitosamente."); // Mensaje de éxito
			} else {
				System.out.println("No se encontró una película con el ID especificado."); // Mensaje de error si no se encuentra
			}
		} catch (SQLException e) {
			// Manejo de excepciones si hay un error al intentar modificar la película
			e.printStackTrace();
		} finally {
			cerrar(); // Cierra la conexión después de usarla
		}
	}

	// Método para buscar una película en la base de datos por su ID
	public Pelicula buscar(int id) {
		// Verifica si la conexión está establecida
		if (conectar() == null) {
			throw new IllegalStateException("La conexión no está inicializada. Asegúrate de conectar primero.");
		}
		// Consulta SQL para seleccionar una película por ID
		String sql = "SELECT * FROM peliculas WHERE id = ?";
		Pelicula pelicula = null; // Variable para almacenar la película encontrada
		try (PreparedStatement statement = conexion.prepareStatement(sql)) {
			statement.setInt(1, id); // Establece el ID en la consulta
			ResultSet resultSet = statement.executeQuery(); // Ejecuta la consulta y obtiene los resultados
			if (resultSet.next()) { // Verifica si se encontró un resultado
				String titulo = resultSet.getString("titulo"); // Obtiene el título de la película
				String director = resultSet.getString("director"); // Obtiene el director de la película
				int anio = resultSet.getInt("anio"); // Obtiene el año de la película
				String genero = resultSet.getString("genero"); // Obtiene el género de la película
				// Crea una nueva instancia de Pelicula con los datos obtenidos
				pelicula = new Pelicula(id, titulo, director, anio, genero);
			} else {
				System.out.println("No se encontró una película con el ID especificado."); // Mensaje de error si no se encuentra
			}
		} catch (SQLException e) {
			// Manejo de excepciones si hay un error al intentar buscar la película
			e.printStackTrace();
		} finally {
			cerrar(); // Cierra la conexión después de usarla
		}
		return pelicula; // Retorna la película encontrada o null si no se encontró
	}

	// Método para borrar una película de la base de datos por su ID
	public void borrar(int id) {
		// Verifica si la conexión está establecida
		if (conectar() == null) {
			throw new IllegalStateException("La conexión no está inicializada. Asegúrate de conectar primero.");
		}
		// Consulta SQL para eliminar una película por ID
		String sql = "DELETE FROM peliculas WHERE id = ?";
		try (PreparedStatement statement = conexion.prepareStatement(sql)) { // Prepara la sentencia SQL
			statement.setInt(1, id); // Establece el ID en la consulta
			int filasBorradas = statement.executeUpdate(); // Ejecuta la actualización (borrado)
			// Verifica si se borró alguna fila
			if (filasBorradas > 0) {
				System.out.println("Película borrada exitosamente."); // Mensaje de éxito
			} else {
				System.out.println("No se encontró una película con el ID especificado."); // Mensaje de error si no se encuentra
			}
		} catch (SQLException e) {
			// Manejo de excepciones si hay un error al intentar borrar la película
			e.printStackTrace();
		} finally {
			cerrar(); // Cierra la conexión después de usarla
		}
	}

	@Override
	public void escribirTodos(HashMap<Integer, Pelicula> lista) {
		// Método que aún no está implementado para escribir todas las películas en la base de datos
		// TODO Auto-generated method stub
	}
}
