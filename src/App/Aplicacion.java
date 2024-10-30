package App;

import java.util.Scanner;

import Controller.Controlador;
import Model.IAccesoDatos;
import View.Vista;
public class Aplicacion {

    public static void main(String[] args) {
        Vista vista = new Vista();
        IAccesoDatos accesoDatos = null; // Se seleccionará más adelante
        Controlador controlador = new Controlador(vista, accesoDatos);
        
        controlador.iniciar(); // Iniciar la aplicación
        //hOLA SOFIA
        //hola gean
        // hola stefany
    }
}

