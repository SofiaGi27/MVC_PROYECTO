package Model.fileManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import Model.IAccesoDatos;
import Model.Pelicula;

public class ficheroTexto implements IAccesoDatos {
	private String rutaFichero;
	

	public ficheroTexto(String rutaFichero) {
		this.rutaFichero = rutaFichero;
	}

	public HashMap<Integer, Pelicula> leerTodos() {
		HashMap<Integer, Pelicula> peliculas = new HashMap<>();

		try (BufferedReader br = new BufferedReader(new FileReader(rutaFichero))) {
			String linea;

			while ((linea = br.readLine()) != null) {
				String[] partes = linea.split(",");
				Pelicula pelicula = new Pelicula(Integer.parseInt(partes[0]), partes[1], partes[2],
						Integer.parseInt(partes[3]), partes[4]);
				peliculas.put(pelicula.getId(), pelicula);

			}

		} catch (IOException e) {
			e.printStackTrace();
		}
		return peliculas;

	}

	public void escribirTodos(HashMap<Integer, Pelicula> lista) {
		try (BufferedWriter escritor = new BufferedWriter(new FileWriter(rutaFichero))) {
			for (Pelicula pelicula : lista.values()) {
				escritor.write(pelicula.getId() + "," + pelicula.getTitulo() + "," + pelicula.getDirector() + ","
						+ pelicula.getAnio() + "," + pelicula.getGenero());
				escritor.newLine();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void añadir(Pelicula pelicula) {

		try (BufferedWriter bw = new BufferedWriter(new FileWriter(rutaFichero, true))) {
			bw.write(pelicula.getId() + "," + pelicula.getTitulo() + "," + pelicula.getDirector() + ","
					+ pelicula.getAnio() + "," + pelicula.getGenero());
			bw.newLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void modificar(Pelicula pelicula) {
		HashMap<Integer, Pelicula> peliculas = leerTodos();
		peliculas.put(pelicula.getId(), pelicula);
		escribirTodos(peliculas);

	}

	@Override
	public void borrar(int id) {
		HashMap<Integer, Pelicula> peliculas = leerTodos();
		peliculas.remove(id);
		escribirTodos(peliculas);

	}

}
