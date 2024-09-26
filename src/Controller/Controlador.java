package Controller;

import java.util.HashMap;

import Model.IAccesoDatos;

import Model.Pelicula;
import Model.fileManager.ficheroBinario;
import Model.fileManager.ficheroTexto;
import Model.fileManager.ficheroXML;
import View.Vista;

public class Controlador {

	private Vista vista;
	private IAccesoDatos accesoDatos;

	public Controlador(Vista vista, IAccesoDatos accesoDatos) {
		this.vista = vista;
		this.accesoDatos = accesoDatos;
	}

	public void iniciar() {
		boolean salir = false;

		while (!salir) {
			// Primer menú: Selección de acceso a datos
			int tipoAcceso = vista.mostrarMenuTipoAcceso();
			switch (tipoAcceso) {
			case 1:
				accesoDatos = new ficheroTexto("Ficheros/peliculas.txt");
				break;
			case 2:
				accesoDatos = new ficheroBinario("Ficheros/peliculas.dat");
				break;
			case 3:
				accesoDatos = new ficheroXML("Ficheros/peliculas.xml");
				break;
			case 0:
				System.out.println("El programa ha finalizado.");
				salir = true; // Cambia salir a true para salir del bucle principal
				continue; // No es necesario, pero puedes usarlo para evitar mostrar el segundo menú
			default:
				System.out.println("Opción no válida. Intente de nuevo.");
				continue; // Volver a mostrar el menú
			}

			// Segundo menú: Acciones sobre las películas
			boolean enMenuAcciones = true;
			while (enMenuAcciones) {
				int accion = vista.mostrarMenuAcciones();
				try {
					switch (accion) {
					case 1: // Añadir
						Pelicula nuevaPelicula = vista.obtenerDatosPelicula();
						accesoDatos.añadir(nuevaPelicula);
						System.out.println("Película añadida exitosamente.");
						break;

					case 2: // Leer
						HashMap<Integer, Pelicula> peliculas = accesoDatos.leerTodos();
						if (peliculas.isEmpty()) {
							System.out.println("No hay películas disponibles.");
						} else {
							for (Pelicula pelicula : peliculas.values()) {
								System.out.println(pelicula);
							}
						}
						break;

					case 3: // Modificar
						Pelicula peliculaModificar = vista.obtenerDatosPelicula();
						accesoDatos.modificar(peliculaModificar);
						System.out.println("Película modificada exitosamente.");
						break;

					case 4: // Borrar
						System.out.print("Ingrese el ID de la película a borrar: ");
						int idBorrar = vista.obtenerId();
						accesoDatos.borrar(idBorrar);
						System.out.println("Película borrada exitosamente.");
						break;

					case 5: // Salir
						enMenuAcciones = false; // Salir del bucle de acciones
						break;

					default:
						System.out.println("Opción no válida. Intente de nuevo.");
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

}
