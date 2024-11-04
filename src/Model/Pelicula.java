package Model;

import java.io.Serializable;
import java.util.Set;

//NUEVA PLANTILLA CON VALIDACIONES
public class Pelicula implements Serializable {

    private int id;
    private String titulo;
    private String director;
    private int anio;
    private String genero;
    
    private static final Set<String> GENEROS_VALIDOS = Set.of("Drama", "Ciencia Ficción", "Musical", "Acción", "Comedia", "Terror");//hay que cambiarlo

    
    
    public Pelicula() {
		//Hibernate
	}

	public Pelicula(int id, String titulo, String director, int anio, String genero) {
        setId(id);
        setTitulo(titulo);
        setDirector(director);
        setAnio(anio);
        setGenero(genero);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("El id debe ser un valor positivo.");
        }
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        if (titulo == null || titulo.trim().isEmpty()) {
            throw new IllegalArgumentException("El título no puede estar vacío.");
        }
        this.titulo = titulo;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        if (director == null || director.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del director no puede estar vacío.");
        }
        this.director = director;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        int currentYear = java.time.Year.now().getValue();
        if (anio < 1888 || anio > currentYear) {
            throw new IllegalArgumentException("El año debe ser entre 1888 y " + currentYear + ".");
        }
        this.anio = anio;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        if (genero == null || genero.trim().isEmpty()) {
            throw new IllegalArgumentException("El género no puede estar vacío.");
        }
        if (!GENEROS_VALIDOS.contains(genero)) {
            throw new IllegalArgumentException("El género no es válido.");
        }
        this.genero = genero;
    }

    @Override
    public String toString() {
        return "Pelicula [id=" + id + ", titulo=" + titulo + ", director=" + director + ", anio=" + anio + ", genero=" + genero + "]";
    }
}

