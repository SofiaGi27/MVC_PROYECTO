package App;

import Controller.Controlador;
import View.Vista;

public class Aplicacion {

    public static void main(String[] args) {
        
        Vista vista = new Vista();

        // Se crea una instancia del controlador 
        Controlador controlador = new Controlador(vista);

        // Inicia la aplicación
        controlador.iniciar();
    }
}
