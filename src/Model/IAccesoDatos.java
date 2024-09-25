package Model;

import java.util.HashMap;

public interface IAccesoDatos {

	void añadir(Pelicula pelicula);

	void modificar(Pelicula pelicula);
	
	HashMap<Integer,Pelicula> leerTodos();
	
	void escribirTodos(HashMap<Integer,Pelicula>lista);

	void borrar(int id);

}
