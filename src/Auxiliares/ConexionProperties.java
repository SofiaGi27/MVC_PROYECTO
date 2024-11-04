package Auxiliares; // Declara que esta clase pertenece al paquete Auxiliares.

import java.io.FileInputStream; // Importa la clase FileInputStream para leer archivos.
import java.io.IOException; // Importa la clase IOException para manejar excepciones de entrada/salida.
import java.io.InputStream; // Importa la clase InputStream, que es una clase base para todas las clases que representan un flujo de entrada.
import java.util.Enumeration; // Importa la clase Enumeration para iterar sobre elementos.
import java.util.HashMap; // Importa la clase HashMap para almacenar pares clave-valor.
import java.util.Properties; // Importa la clase Properties, que representa un conjunto de propiedades.

public class ConexionProperties { // Declara la clase ConexionProperties.

    private HashMap<String, String> hashProperties; // Declara un HashMap para almacenar las propiedades.
    private Properties prop; // Declara un objeto Properties para cargar propiedades desde un archivo.

    // Constructor de la clase
    public ConexionProperties() { // Método constructor que inicializa la clase.
        hashProperties = new HashMap<>(); // Inicializa el HashMap para almacenar propiedades.
        prop = new Properties(); // Inicializa el objeto Properties.
        InputStream input = null; // Declara un InputStream para manejar la entrada del archivo.

        try {
            // Cargar el archivo de propiedades principal
            input = new FileInputStream("Ficheros/db.properties"); // Intenta abrir el archivo db.properties.
            prop.load(input); // Carga las propiedades desde el InputStream en el objeto Properties.

            // Almacenar las propiedades en el HashMap
            Enumeration<?> e = prop.propertyNames(); // Obtiene un Enumeration de las claves de las propiedades.
            while (e.hasMoreElements()) { // Mientras haya más elementos en el Enumeration,
                String key = (String) e.nextElement(); // Obtiene la siguiente clave.
                String value = prop.getProperty(key); // Obtiene el valor correspondiente a la clave.
                hashProperties.put(key, value); // Almacena la clave y el valor en el HashMap.
            }

            // Cargar propiedades adicionales desde la ruta especificada en el archivo
            String additionalPropertiesPath = hashProperties.get("properties.path"); // Obtiene la ruta de propiedades adicionales del HashMap.
            if (additionalPropertiesPath != null) { // Si la ruta no es nula,
                try (InputStream additionalInput = new FileInputStream(additionalPropertiesPath)) { // Intenta abrir el archivo de propiedades adicionales.
                    prop.load(additionalInput); // Carga las propiedades adicionales.
                }
            }

        } catch (IOException ex) { // Maneja las excepciones de entrada/salida.
            System.out.println("Error leyendo el fichero de propiedades: " + ex.getMessage()); // Imprime un mensaje de error.
            System.exit(1); // Termina el programa con un código de estado 1.
        } finally { // Bloque finally para asegurar el cierre del InputStream.
            if (input != null) { // Si el InputStream no es nulo,
                try {
                    input.close(); // Intenta cerrar el InputStream.
                } catch (IOException e) { // Maneja posibles excepciones al cerrar.
                    e.printStackTrace(); // Imprime la traza de la excepción.
                }
            }
        }
    }

    public HashMap<String, String> getHash() { // Método para obtener el HashMap de propiedades.
        return hashProperties; // Devuelve el HashMap.
    }

    // Métodos para obtener propiedades específicas
    public String getmysqlurl() { // Método para obtener la URL de MySQL.
        return hashProperties.get("mysqlurl"); // Devuelve el valor correspondiente a "mysqlurl".
    }

    public String mysqluser() { // Método para obtener el usuario de MySQL.
        return hashProperties.get("mysqluser"); // Devuelve el valor correspondiente a "mysqluser".
    }

    public String getmysqlpassword() { // Método para obtener la contraseña de MySQL.
        return hashProperties.get("mysqlpassword"); // Devuelve el valor correspondiente a "mysqlpassword".
    }

    public String getsqliteurl() { // Método para obtener la URL de SQLite.
        return hashProperties.get("sqliteurl"); // Devuelve el valor correspondiente a "sqliteurl".
    }
}
