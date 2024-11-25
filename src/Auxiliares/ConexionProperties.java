package Auxiliares;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

public class ConexionProperties {

	private HashMap<String, String> hashProperties;
	private Properties prop;

	public ConexionProperties() {
		hashProperties = new HashMap<>();
		prop = new Properties();
		InputStream input = null;

		try {
			input = new FileInputStream("Ficheros/db.properties");
			prop.load(input);

			// Itera sobre las propiedades y las almacena en el HashMap.
			Enumeration<?> e = prop.propertyNames();
			while (e.hasMoreElements()) {
				String key = (String) e.nextElement();
				String value = prop.getProperty(key);
				hashProperties.put(key, value);
			}

			// Si existe una ruta de propiedades adicionales, las carga también.
			String additionalPropertiesPath = hashProperties.get("properties.path");
			if (additionalPropertiesPath != null) {
				try (InputStream additionalInput = new FileInputStream(additionalPropertiesPath)) {
					prop.load(additionalInput);
				}
			}

		} catch (IOException ex) {
			System.out.println("Error leyendo el fichero de propiedades: " + ex.getMessage());
			System.exit(1);
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// Devuelve el HashMap con las propiedades cargadas.
	public HashMap<String, String> getHash() {
		return hashProperties;
	}

	// Métodos para obtener propiedades específicas.
	public String getmysqlurl() {
		return hashProperties.get("mysqlurl");
	}

	// Devuelve el usuario de MySQL.
	public String mysqluser() {
		return hashProperties.get("mysqluser");
	}

	// Devuelve la contraseña de MySQL
	public String getmysqlpassword() {
		return hashProperties.get("mysqlpassword");
	}

	// Devuelve la URL de SQLite.
	public String getsqliteurl() {
		return hashProperties.get("sqliteurl");
	}
}
