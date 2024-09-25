package View;
/*esto es una prueba para saber si funciona el repositorio*/

import java.util.Scanner;

import Model.Pelicula;

public class Vista {

	private Scanner scanner;

	public Vista() {
		this.scanner = new Scanner(System.in);
	}

	public int mostrarMenuTipoAcceso() {
		System.out.println("Seleccione el tipo de acceso a datos:");
		System.out.println("1) Fichero de Texto");
		System.out.println("2) Fichero Binario");
		System.out.println("3) Fichero XML");
		System.out.print("Ingrese su opción: ");
		return scanner.nextInt();
	}

	public int mostrarMenuAcciones() {
		System.out.println("¿Qué desea hacer?");
		System.out.println("1) Añadir película");
		System.out.println("2) Leer películas");
		System.out.println("3) Modificar película");
		System.out.println("4) Borrar película");
		System.out.println("5) Salir");
		System.out.print("Ingrese su opción: ");
		return scanner.nextInt();
	}

	// Métodos adicionales para obtener datos del usuario (título, director, etc.)
	public Pelicula obtenerDatosPelicula() {
		System.out.print("Ingrese ID: ");
		int id = scanner.nextInt();
		scanner.nextLine(); // Consumir el salto de línea
		System.out.print("Ingrese Título: ");
		String titulo = scanner.nextLine();
		System.out.print("Ingrese Director: ");
		String director = scanner.nextLine();
		System.out.print("Ingrese Año: ");
		int anio = scanner.nextInt();
		scanner.nextLine(); // Consumir el salto de línea
		System.out.print("Ingrese Género: ");
		String genero = scanner.nextLine();
		return new Pelicula(id, titulo, director, anio, genero);
	}
	
	public int obtenerId() {
	    System.out.print("Ingrese ID de la película: ");
	    return scanner.nextInt();
	}
	

}
