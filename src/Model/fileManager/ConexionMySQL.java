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

public class ConexionMySQL implements IAccesoDatos {
	private Connection conexion;
	private ConexionProperties propiedades;

	// Constructor que inicializa las propiedades.
	public ConexionMySQL() {
		this.propiedades = new ConexionProperties(); // Crea una nueva instancia de ConexionProperties para cargar las
														// configuraciones.
	}

	// Método para establecer la conexión a la base de datos.
	public Connection conectar() {
		try {

			if (this.conexion == null || this.conexion.isClosed()) {

				this.conexion = DriverManager.getConnection(propiedades.getmysqlurl(), propiedades.mysqluser(),
						propiedades.getmysqlpassword());
			}
			return this.conexion;
		} catch (SQLException e) {

			System.out.println("Error al conectar a la base de datos: " + e.getMessage());
			return null;
		}
	}

	// Método para cerrar la conexión (opcional).
	public void cerrar() {
		if (conexion != null) { // Verifica si la conexión no es null.
			try {
				conexion.close(); // Intenta cerrar la conexión.
			} catch (SQLException e) {

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

	// Método para leer todas las películas de la base de datos.
	public HashMap<Integer, Pelicula> leerTodos() {
		HashMap<Integer, Pelicula> peliculas = new HashMap<>();
		// Verifica si la conexión está establecida.
		if (conectar() == null) {
			throw new IllegalStateException("La conexión no está inicializada. Asegúrate de conectar primero.");
		}
		// Consulta SQL para seleccionar todas las películas.
		String sql = "SELECT * FROM peliculas";
		try (PreparedStatement statement = conexion.prepareStatement(sql);
				ResultSet resultSet = statement.executeQuery()) {

			// Recorre los resultados.
			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				String titulo = resultSet.getString("titulo");
				String director = resultSet.getString("director");
				int anio = resultSet.getInt("anio");
				String genero = resultSet.getString("genero");

				peliculas.put(id, new Pelicula(id, titulo, director, anio, genero));
			}
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			cerrar();
		}
		return peliculas;
	}

	// Método para modificar una película en la base de datos.
	public void modificar(Pelicula pelicula) {

		if (conectar() == null) {
			throw new IllegalStateException("La conexión no está inicializada. Asegúrate de conectar primero.");
		}
		// Consulta SQL para actualizar una película.
		String sql = "UPDATE peliculas SET titulo = ?, director = ?, anio = ?, genero = ? WHERE id = ?";
		try (PreparedStatement statement = conexion.prepareStatement(sql)) {

			statement.setString(1, pelicula.getTitulo());
			statement.setString(2, pelicula.getDirector());
			statement.setInt(3, pelicula.getAnio());
			statement.setString(4, pelicula.getGenero());
			statement.setInt(5, pelicula.getId());
			int filasActualizadas = statement.executeUpdate();
			// Verifica si se actualizó alguna fila.
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

	// Método para buscar una película por ID.
	public Pelicula buscar(int id) {

		if (conectar() == null) {
			throw new IllegalStateException("La conexión no está inicializada. Asegúrate de conectar primero.");
		}
		// Consulta SQL para seleccionar una película por ID.
		String sql = "SELECT * FROM peliculas WHERE id = ?";
		Pelicula pelicula = null; // Variable para almacenar la película encontrada.
		try (PreparedStatement statement = conexion.prepareStatement(sql)) {
			statement.setInt(1, id);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				String titulo = resultSet.getString("titulo");
				String director = resultSet.getString("director");
				int anio = resultSet.getInt("anio");
				String genero = resultSet.getString("genero");
				// Crea una nueva instancia de Pelicula con los datos encontrados.
				pelicula = new Pelicula(id, titulo, director, anio, genero);
			} else {
				System.out.println("No se encontró una película con el ID especificado.");

			}
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			cerrar();
		}
		return pelicula;
	}

	// Método para borrar una película de la base de datos.
	public void borrar(int id) {

		if (conectar() == null) {
			throw new IllegalStateException("La conexión no está inicializada. Asegúrate de conectar primero.");
		}
		// Consulta SQL para eliminar una película por ID.
		String sql = "DELETE FROM peliculas WHERE id = ?";
		try (PreparedStatement statement = conexion.prepareStatement(sql)) { // Prepara la sentencia SQL.
			statement.setInt(1, id);
			int filasBorradas = statement.executeUpdate();
			// Verifica si se borró alguna fila.
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
