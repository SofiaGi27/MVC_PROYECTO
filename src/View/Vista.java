package View;

import java.util.Scanner;

import Model.Pelicula;

public class Vista {

	private Scanner scanner;

	public Vista() {
		this.scanner = new Scanner(System.in);
	}

	// Método que muestra el menú para seleccionar el tipo de acceso a datos y
	// devuelve la opción seleccionada
	public int mostrarMenuTipoAcceso() {
		System.out.println("Seleccione el tipo de acceso a datos:");
		System.out.println("1) Fichero de Texto");
		System.out.println("2) Fichero Binario");
		System.out.println("3) Fichero XML");
		System.out.println("4) Base de Datos MySQL");
		System.out.println("5) Base de Datos SQLite");
		System.out.println("6) Hibernate");
		System.out.println("0) Salir");
		System.out.print("Ingrese su opción: ");
		return scanner.nextInt();
	}

	// Método que muestra el menú de acciones disponibles sobre las películas y
	// devuelve la opción seleccionada
	public int mostrarMenuAcciones() {
		System.out.println("¿Qué desea hacer?");
		System.out.println("1) Añadir película");
		System.out.println("2) Leer películas");
		System.out.println("3) Modificar película");
		System.out.println("4) Borrar película");
		System.out.println("5) Buscar Película");
		System.out.println("6) Copia de Seguridad");
		System.out.println("7) Salir");
		System.out.print("Ingrese su opción: ");
		return scanner.nextInt();
	}

	// Método que recoge los datos necesarios para crear un objeto Pelicula a partir
	// de la entrada del usuario
	public Pelicula obtenerDatosPelicula() {
		System.out.print("Ingrese ID: ");
		int id = scanner.nextInt();
		scanner.nextLine();
		System.out.print("Ingrese Título: ");
		String titulo = scanner.nextLine();
		System.out.print("Ingrese Director: ");
		String director = scanner.nextLine();
		System.out.print("Ingrese Año: ");
		int anio = scanner.nextInt();
		scanner.nextLine();		
	    String genero;
	    while (true) {
	        System.out.println("Seleccione un género de las siguientes opciones:");
	        System.out.println(String.join(", ", Pelicula.getGenerosValidos())); // Mostrar los géneros válidos
	        System.out.print("Ingrese Género: ");
	        genero = scanner.nextLine();
	        if (Pelicula.getGenerosValidos().contains(genero)) {
	            break; // Salir del bucle si el género es válido
	        }
	        System.out.println("Género no válido. Por favor, intente de nuevo.");
	    }

	    return new Pelicula(id, titulo, director, anio, genero);
	}

	// Método que solicita y devuelve el ID de la película que se desea buscar,
	// modificar o borrar
	public int obtenerId() {
		System.out.print("Ingrese ID de la película: ");
		return scanner.nextInt();
	}
	
	
	  

}
