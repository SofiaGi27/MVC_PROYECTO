package Model.fileManager;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import Model.IAccesoDatos;
import Model.Pelicula;

public class ficheroBinario implements IAccesoDatos {
	private String rutaFichero;

	public ficheroBinario(String rutaFichero) {
		this.rutaFichero = rutaFichero;
	}

	// Leer todas las películas desde el archivo binario
	@Override
	@SuppressWarnings("unchecked")
	public HashMap<Integer, Pelicula> leerTodos() {
		HashMap<Integer, Pelicula> peliculas = new HashMap<>();

		try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(rutaFichero))) {
			peliculas = (HashMap<Integer, Pelicula>) ois.readObject();
		} catch (IOException | ClassNotFoundException e) {
			System.out.println("No se pudo leer el archivo o el archivo está vacío.");
		}

		return peliculas;
	}

	// Escribir todas las películas en el archivo binario
	public void escribirTodos(HashMap<Integer, Pelicula> listaPeliculas) {
		try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(rutaFichero))) {
			oos.writeObject(listaPeliculas);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Añadir una película al archivo binario
	@Override
	public void añadir(Pelicula pelicula) {
		HashMap<Integer, Pelicula> peliculas = leerTodos();
		peliculas.put(pelicula.getId(), pelicula);
		escribirTodos(peliculas);
		System.out.println("Película añadida exitosamente.");
	}

	// Modificar una película existente en el archivo binario
	@Override
	public void modificar(Pelicula pelicula) {
		HashMap<Integer, Pelicula> peliculas = leerTodos();
		if (peliculas.containsKey(pelicula.getId())) {
			peliculas.put(pelicula.getId(), pelicula);
			escribirTodos(peliculas);
			System.out.println("Película modificada exitosamente.");
		} else {
			System.out.println("Película no encontrada.");
		}
	}

	// Borrar una película del archivo binario
	@Override
	public void borrar(int id) {
		HashMap<Integer, Pelicula> peliculas = leerTodos();
		if (peliculas.remove(id) != null) {
			escribirTodos(peliculas);
			System.out.println("Película borrada exitosamente.");
		} else {
			System.out.println("Película no encontrada.");
		}
	}
}
