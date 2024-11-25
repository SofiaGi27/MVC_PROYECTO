package Model.fileManager;

import java.sql.Connection;
import Auxiliares.ConexionProperties;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import Model.IAccesoDatos;
import Model.Pelicula;

// Clase que implementa la interfaz IAccesoDatos para manejar la conexión y operaciones con una base de datos SQLite
public class ConexionSQLite implements IAccesoDatos {
	private Connection conexion;
	private ConexionProperties propiedades;

	// Constructor que inicializa las propiedades de conexión
	public ConexionSQLite() {
		this.propiedades = new ConexionProperties();
	}

	// Método para establecer la conexión a la base de datos
	public Connection conectar() {
		try {
			// Verifica si la conexión es null o está cerrada
			if (this.conexion == null || this.conexion.isClosed()) {
				// Intenta establecer la conexión utilizando la URL de SQLite obtenida de las
				// propiedades
				this.conexion = DriverManager.getConnection(propiedades.getsqliteurl());
			}
			return this.conexion;
		} catch (SQLException e) {

			System.out.println("Error al conectar a la base de datos: " + e.getMessage());
			return null;
		}
	}

	// Método para cerrar la conexión a la base de datos (opcional)
	public void cerrar() {
		if (conexion != null) {
			try {
				conexion.close();
			} catch (SQLException e) {

				e.printStackTrace();
			}
		}
	}

	// Método para añadir una nueva película a la base de datos
	public void añadir(Pelicula pelicula) {

		if (conectar() == null) {
			throw new IllegalStateException("La conexión no está inicializada. Asegúrate de conectar primero.");
		}
		// Consulta SQL para insertar una nueva película
		String sql = "INSERT INTO peliculas (id, titulo, director, anio, genero) VALUES (?, ?, ?, ?, ?)";
		try (PreparedStatement statement = conexion.prepareStatement(sql)) {
			// Asigna los valores a la sentencia SQL
			statement.setInt(1, pelicula.getId());
			statement.setString(2, pelicula.getTitulo());
			statement.setString(3, pelicula.getDirector());
			statement.setInt(4, pelicula.getAnio());
			statement.setString(5, pelicula.getGenero());
			statement.executeUpdate();
			System.out.println("Película añadida a la base de datos exitosamente.");
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			cerrar();
		}
	}

	// Método para leer todas las películas de la base de datos
	public HashMap<Integer, Pelicula> leerTodos() {
		HashMap<Integer, Pelicula> peliculas = new HashMap<>(); // Mapa para almacenar las películas leídas
		if (conectar() == null) {
			throw new IllegalStateException("La conexión no está inicializada. Asegúrate de conectar primero.");
		}
		// Consulta SQL para seleccionar todas las películas
		String sql = "SELECT * FROM peliculas";
		try (PreparedStatement statement = conexion.prepareStatement(sql);
				ResultSet resultSet = statement.executeQuery()) { // Ejecuta la consulta y obtiene los resultados

			// Recorre los resultados de la consulta
			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				String titulo = resultSet.getString("titulo");
				String director = resultSet.getString("director");
				int anio = resultSet.getInt("anio");
				String genero = resultSet.getString("genero");
				// Añade la película al mapa utilizando el ID como clave
				peliculas.put(id, new Pelicula(id, titulo, director, anio, genero));
			}
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			cerrar();
		}
		return peliculas; // Retorna el mapa de películas leídas
	}

	// Método para modificar una película existente en la base de datos
	public void modificar(Pelicula pelicula) {

		if (conectar() == null) {
			throw new IllegalStateException("La conexión no está inicializada. Asegúrate de conectar primero.");
		}
		// Consulta SQL para actualizar los datos de una película
		String sql = "UPDATE peliculas SET titulo = ?, director = ?, anio = ?, genero = ? WHERE id = ?";
		try (PreparedStatement statement = conexion.prepareStatement(sql)) {
			// Asigna los valores a la sentencia
			statement.setString(1, pelicula.getTitulo());
			statement.setString(2, pelicula.getDirector());
			statement.setInt(3, pelicula.getAnio());
			statement.setString(4, pelicula.getGenero());
			statement.setInt(5, pelicula.getId());
			int filasActualizadas = statement.executeUpdate();
			if (filasActualizadas > 0) {
				System.out.println("Película modificada exitosamente.");
			} else {
				System.out.println("No se encontró una película con el ID especificado.");
			}
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			cerrar();
		}
	}

	// Método para buscar una película en la base de datos por su ID
	public Pelicula buscar(int id) {

		if (conectar() == null) {
			throw new IllegalStateException("La conexión no está inicializada. Asegúrate de conectar primero.");
		}
		// Consulta SQL para seleccionar una película por ID
		String sql = "SELECT * FROM peliculas WHERE id = ?";
		Pelicula pelicula = null;
		try (PreparedStatement statement = conexion.prepareStatement(sql)) {
			statement.setInt(1, id);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) { // Verifica si se encontró un resultado
				String titulo = resultSet.getString("titulo");
				String director = resultSet.getString("director");
				int anio = resultSet.getInt("anio");
				String genero = resultSet.getString("genero");
				// Crea una nueva instancia de Pelicula con los datos obtenidos
				pelicula = new Pelicula(id, titulo, director, anio, genero);
			} else {
				System.out.println("No se encontró una película con el ID especificado.");
			}
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			cerrar();
		}
		return pelicula; // Retorna la película encontrada o null si no se encontró
	}

	// Método para borrar una película de la base de datos por su ID
	public void borrar(int id) {

		if (conectar() == null) {
			throw new IllegalStateException("La conexión no está inicializada. Asegúrate de conectar primero.");
		}
		// Consulta SQL para eliminar una película por ID
		String sql = "DELETE FROM peliculas WHERE id = ?";
		try (PreparedStatement statement = conexion.prepareStatement(sql)) {
			statement.setInt(1, id);
			int filasBorradas = statement.executeUpdate();

			if (filasBorradas > 0) {
				System.out.println("Película borrada exitosamente.");
			} else {
				System.out.println("No se encontró una película con el ID especificado.");
			}
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			cerrar();
		}
	}

	@Override
	public void escribirTodos(HashMap<Integer, Pelicula> lista) {

		if (conectar() == null) {
			throw new IllegalStateException("La conexión no está inicializada. Asegúrate de conectar primero.");
		}

		// Consulta SQL para eliminar todas las películas
		String sqlBorrar = "DELETE FROM peliculas";
		try (PreparedStatement statementBorrar = conexion.prepareStatement(sqlBorrar)) {
			statementBorrar.executeUpdate(); // Ejecuta el borrado de todas las películas
			System.out.println("Todas las películas existentes han sido eliminadas.");
		} catch (SQLException e) {

			System.out.println("Error al intentar borrar todas las películas: " + e.getMessage());
			return;
		}

		// Inserta cada película del HashMap en la base de datos
		lista.forEach((id, pelicula) -> añadir(pelicula));

		System.out.println("Se han añadido todas las películas de la lista.");
	}

}
