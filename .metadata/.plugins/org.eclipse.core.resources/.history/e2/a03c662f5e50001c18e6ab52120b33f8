package ar.edu.unlu.Ejecutable;


import ar.edu.unlu.Controlador.Controlador;
import ar.edu.unlu.Modelo.Juego;
import ar.edu.unlu.Modelo.Mazo;
import ar.edu.unlu.Modelo.Mesa;
import ar.edu.unlu.Vista.VistaConsola;

public class Aplication {

	public static void main(String[] args) {
		Mazo mazo = new Mazo();
		Mesa mesa = new Mesa();
		Juego escoba = new Juego(mazo, mesa);
		VistaConsola vista = new VistaConsola();
		try {
			Controlador controlador = new Controlador(vista, escoba);
			vista.setControlador(controlador);
			vista.empezarJuego();
		} catch (Exception e) {
			e.printStackTrace();
		}

}
}
