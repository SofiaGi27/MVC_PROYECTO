package Model; 

import java.util.HashMap; 

// Interfaz que define los métodos necesarios para el acceso a datos de películas
public interface IAccesoDatos {

	// Método para añadir una nueva película
	void añadir(Pelicula pelicula);

	// Método para modificar una película existente
	void modificar(Pelicula pelicula);
	
	// Método para leer todas las películas y retornar un mapa con ellas
	HashMap<Integer, Pelicula> leerTodos();
	
	// Método para escribir todas las películas desde un mapa
	void escribirTodos(HashMap<Integer, Pelicula> lista);

	// Método para borrar una película por su ID
	void borrar(int id);
	
	// Método para buscar una película por su ID
	Pelicula buscar(int id); 

	// Método por defecto para conectar a la base de datos, no implementado en esta interfaz
	default Object conectar() {
		// Lanza una excepción si se intenta usar este método sin implementarlo
		throw new UnsupportedOperationException("Este método no es soportado en esta clase."); 
		// O simplemente se puede retornar null si no es necesario
	}  
}
