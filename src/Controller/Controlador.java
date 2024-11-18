package Controller;

import java.util.HashMap;

import Model.IAccesoDatos;
import Model.Pelicula;
import Model.fileManager.ConexionMySQL;
import Model.fileManager.ConexionSQLite;
import Model.fileManager.ficheroBinario;
import Model.fileManager.ficheroTexto;
import Model.fileManager.ficheroXML;
import Model.fileManager.Hibernate; // Asegúrate de importar la clase Hibernate
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
			case 4:
				// Crear una nueva instancia de ConexionMySQL y verificar conexión
				accesoDatos = new ConexionMySQL();
				if (accesoDatos.conectar() != null) { // Si la conexión es exitosa
					System.out.println("Conexión a la base de datos MySQL exitosa.");
				} else {
					System.out.println("Error al conectar a la base de datos.");
					continue; // Regresar al menú principal
				}
				break;
			case 5:
				// Crear una nueva instancia de ConexionSQLite y verificar conexión
				accesoDatos = new ConexionSQLite();
				if (accesoDatos.conectar() != null) { // Si la conexión es exitosa
					System.out.println("Conexión a la base de datos SQLite exitosa.");
				} else {
					System.out.println("Error al conectar a la base de datos.");
					continue; // Regresar al menú principal
				}
				break;
			case 6:
				// Inicializar Hibernate
				accesoDatos = new Hibernate(); // Crear una nueva instancia de Hibernate
				System.out.println("Usando Hibernate para el acceso a datos.");
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
						int idBorrar = vista.obtenerId();
						accesoDatos.borrar(idBorrar);
						System.out.println("Película borrada exitosamente.");
						break;

					case 5: // Buscar
						int idBuscar = vista.obtenerId();
						Pelicula peliculaBuscada = accesoDatos.buscar(idBuscar);
						if (peliculaBuscada != null) {
							System.out.println("Película encontrada: " + peliculaBuscada);
						} else {
							System.out.println("No se encontró ninguna película con el ID " + idBuscar);
						}
						break;

					// Salir

					// MENU PARA ELEGIR ACCESO SECUNDARIO
					// SWITCH PARA CREAR OBJETO SECUNDARIO
					// ACCESOSECUNDARIO.ESCRIBIRTODOS(ACCESODATOS.LEERTODOS());

					case 6: // Salir
					    // Menú para elegir el tipo de acceso secundario (fichero de destino)
					    int tipoAccesoSecundario = vista.mostrarMenuTipoAcceso();
					    IAccesoDatos accesoSecundario = null;

					    switch (tipoAccesoSecundario) {
					        case 1:
					            accesoSecundario = new ficheroTexto("Ficheros/peliculas.txt");
					            break;
					        case 2:
					            accesoSecundario = new ficheroBinario("Ficheros/peliculas.dat");
					            break;
					        case 3:
					            accesoSecundario = new ficheroXML("Ficheros/peliculas.xml");
					            break;
					        case 4:
					            accesoSecundario = new ConexionMySQL();
					            if (accesoSecundario.conectar() != null) {
					                System.out.println("Conexión a la base de datos MySQL secundaria exitosa.");
					            } else {
					                System.out.println("Error al conectar a la base de datos secundaria.");
					                continue; // Volver al menú si falla la conexión
					            }
					            break;
					        case 5:
					            accesoSecundario = new ConexionSQLite();
					            if (accesoSecundario.conectar() != null) {
					                System.out.println("Conexión a la base de datos SQLite secundaria exitosa.");
					            } else {
					                System.out.println("Error al conectar a la base de datos secundaria.");
					                continue; // Volver al menú si falla la conexión
					            }
					            break;
					        case 6:
					            accesoSecundario = new Hibernate();
					            System.out.println("Usando Hibernate como acceso de datos secundario.");
					            break;
					        default:
					            System.out.println("Opción no válida para acceso secundario.");
					            continue; // Volver a mostrar el menú
					    }

					    // Transferir datos del acceso primario al acceso secundario con mensajes de depuración
					    try {
					        HashMap<Integer, Pelicula> peliculasOrigen = accesoDatos.leerTodos();
					        System.out.println("Número de películas leídas del acceso primario: " + peliculasOrigen.size());

					        accesoSecundario.escribirTodos(peliculasOrigen);
					        System.out.println("Datos transferidos exitosamente al acceso secundario.");
					    } catch (Exception e) {
					        System.out.println("Error al transferir los datos: " + e.getMessage());
					    }
					    break;

					case 7:
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
