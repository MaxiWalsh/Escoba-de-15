package ar.edu.unlu.Modelo;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import ar.edu.unlu.Controlador.EventosEscoba;


public class Juego {
	private Mazo mazo;
	private List<Jugador> jugadores = new ArrayList<>();
	private Ronda ronda;
	private Reglas reglas;
	private Mesa mesa;
//	private boolean juegoIniciado = false; 
	private List<Observador> observadores = new ArrayList<>();
	private Estado estadoJuego = Estado.CONFIGURANDO;
	private int jugadorActual;
	private int puntajeObjetivo = 15;
	private Jugador ganador;
	private Jugador ganadorRonda;
	private Jugador ultimoJugadorEnLevantar;
	
	public Juego(Mazo mazo, Mesa mesa) {
		this.setMazo(mazo);
		//this.setJugadores(jugadores);
		this.reglas = new Reglas(this.ronda);
		this.ronda = new Ronda();
		this.setMesa(mesa);
		//this.setObservadores(observadores);
	}

	public Mazo getMazo() {
		return mazo;
	}

	public void setMazo(Mazo mazo) {
		this.mazo = mazo;
	}

	public List<Jugador> getJugadores() {
		return jugadores;
	}

	public void setJugadores(List<Jugador> jugadores) {
		this.jugadores = jugadores;
	}

	public Ronda getRonda() {
		return ronda;
	}

	public void setRonda(Ronda ronda) {
		this.ronda = ronda;
	}

	public Reglas getReglas() {
		return reglas;
	}

	public void setReglas(Reglas reglas) {
		this.reglas = reglas;
	}

	public Mesa getMesa() {
		return mesa;
	}

	public void setMesa(Mesa mesa) {
		this.mesa = mesa;
	}
	
	public void setObservadores(List<Observador> observadores) {
		this.observadores = observadores;
	}
	
	public Estado getEstadoJuego() {
		return this.estadoJuego;
	}
	
	public Integer getJugadorActual() {
		return jugadorActual;
	}

	public void setJugadorActual(int jugadorActual) {
		this.jugadorActual = jugadorActual;
	}
	
	
	public void addObservador(Observador observador) {
		observadores.add(observador);
	}

	public void repartirCartas() {
		for(Jugador jugador : this.jugadores) {
			Integer contador = 1;
			while (contador <= 3) {
				jugador.agregarCartas(this.mazo.darCarta());
				contador++;
			}
		}
	}
	
	

	public void tirarCarta(Integer numero) {
		this.mesa.agregarCarta(recuperarJugadorActual().tirarCarta(numero));
		if (ronda.comprobarFinDeRonda(this.mazo, this.jugadores)) {
			juntarCartasSobrantes();
			actualizarPuntosJugadores();
			if (this.estadoJuego != Estado.FINALIZADO) {
				actualizarGanadorRonda();	
				notificarEvento(EventosEscoba.RONDAFINALIDAZA);
				reparteGeneral();
			}
			else {
				actualizarGanadorJuego();
				notificarGanador(ganador);
			}
		}
		else {
			cambiarJugador();
			if (reglas.esFinDeMano(this.jugadores)) {
				notificarEvento(EventosEscoba.MANOFINALIZADA);
				repartirCartas();
			}
			mensajesGenerales();
		}
	}
	
	
	
	
	
	public void levantarCartas(Integer numero, ArrayList<Integer> cartas){			
		List<Carta> cartasDeLaMesa = mesa.juntarCartas(cartas);
		List<Carta> posibleLevantada = recuperarJugadorActual().levantarCartas(numero, cartasDeLaMesa);
		Jugador jugadorActual = recuperarJugadorActual();
		if (reglas.verificarSuma15(posibleLevantada)){															//Si cumple con la condicion
			recuperarJugadorActual().agregarABaza(posibleLevantada);											//Se guarda en la baza las cartas
			this.ultimoJugadorEnLevantar = recuperarJugadorActual();											//Guarda el ultimo jugador en levantar
			notificarMensaje("Cartas levantadas.");	
			recuperarJugadorActual().sacarCarta(numero);														//Saco de la mano del jugador la carta que uso para levantar
			mesa.sacarCartas(cartasDeLaMesa);																	//Saco de la mesa las cartas que eligi� el jugador
			if (mesa.cantidadDeCartas() == 0) {																	//Verificacion para saber si hizo escoba
				recuperarJugadorActual().aumentarEscobas();														//Aumento la cantidad de escobas
				recuperarJugadorActual().incrementarPuntos();													//Incremento la cantidad de puntos
				notificarMensaje("Ha hecho una escoba!");
			}
			if (reglas.levanto7DeOro(posibleLevantada)) {														//Verificacion para saber si levant� el 7 de oro
				recuperarJugadorActual().incrementarPuntos();													//En caso de que si, aumento los puntos
				notificarMensaje("Ha levantado el 7 de oro!");
			}
			
			if (ronda.comprobarFinDeRonda(this.mazo, this.jugadores)) {										//Verifico si se llego al fin de la RONDA
				juntarCartasSobrantes();																		//Aqui le otorgo al ultimo jugador en levantar las cartas sobrantes en la mesa
				actualizarPuntosJugadores();																	//Llamo al metodo para actualizar los puntos de los jugadores
				if (this.estadoJuego != Estado.FINALIZADO) {													//Si anteriormente NO se cambio el estado del juego, entonces solo termino la ronda
					actualizarGanadorRonda();																		//Actualizo el ganador de la ronda y limpio el juego para la proxima ronda(dentro del metodo)
					notificarEvento(EventosEscoba.RONDAFINALIDAZA);													//Notifico al controlador que termino la ronda
					reparteGeneral();																				//Luego, se vuelve a repartir
				}		
				else {																							//En el caso de que SI haya cambiado el estado del juego																							
					actualizarGanadorJuego();																	//Se actualiza el ganador del juego
					notificarGanador(ganador);																	//Y se notifica el ganador de la PARTIDA
				}
			}
			else {																							//En caso de que no se haya llegado al fin de la ronda
				cambiarJugador();																				//Cambio el turno del jugador
				if (reglas.esFinDeMano(this.jugadores)) {														//Verifico si fue el fin de la MANO
					notificarEvento(EventosEscoba.MANOFINALIZADA);													//Se notifica que fue el fin de la mano
					repartirCartas();																				//Se reparten nuevamente cartas para el comienzo de una nueva mano
				}
				mensajesGenerales();																		//Muestro mensajes con la cartas de la mesa, de quien es el turno y las cartas de este ultimo
			}
		} else {
			notificarMensaje("Las cartas no suman 15. No se han levantado.");
			mensajesGenerales();
					}
	}
	
	public List<Carta> seleccionarCartasMesa(){
		return null;
	}

	public Integer cantidadCartasMesa() {
		return mesa.cantidadDeCartas();
	}
	
	public String nombreJugadorActual() {
		return recuperarJugadorActual().getNombre();
	}

	public boolean turnoJugadorActual() {
		return recuperarJugadorActual().isTurno();
	}

	public Jugador getGanador() {
		return this.ganador;
	}
	
	public void actualizarGanadorJuego() {
		Integer i = 0;
		Integer puntaje = -99;
		while (i < this.jugadores.size()) {
			if (this.jugadores.get(i).getPuntos() > puntaje){
				puntaje = this.jugadores.get(i).getPuntos();
				ganador = this.jugadores.get(i);
			}
			i++;
		}
	}
	
	public Jugador recuperarJugadorActual() {
		return this.jugadores.get(jugadorActual);
	}
	
	
	public Integer cantidadCartasJugadorActual() {
		
		return this.jugadores.get(jugadorActual).cantidadDeCartas();
	}
	
	
	
	public void juntarCartasSobrantes() {
		if (mesa.cantidadDeCartas() != 0) {														//Si al finalizar la ronda hay cartas en la mesa
			this.ultimoJugadorEnLevantar.agregarABaza(mesa.getCartas());						//Se las queda el ultimo jugador que realizo una levantada
			mesa.limpiarMesa();															//Y luego se eliminan de la mesa
			
		}
	}

	public void cartasBazaJugador() {
		for (Jugador jugador : this.jugadores) {
			jugador.mostrarBaza();
		}
	}
	
	public String getNombreGanadorRonda() {
		return this.ganadorRonda.getNombre();
	}
	
	public void actualizarGanadorRonda() {
		Integer puntosGanador = -99;
		Integer i = 0;
		while ((i < jugadores.size())) {
			if ( jugadores.get(i).getPuntos() >= puntosGanador) { 
				puntosGanador = jugadores.get(i).getPuntos();
				this.ganadorRonda = jugadores.get(i);
			}
			i++;
		}
		limpiarJuego();
	}
	
	
	public boolean huboEscobaDeMano() {
		return reglas.verificarSuma15(mesa.getCartas());
	}
	public void darCartasALaMesa() {
		Integer contador = 1;
		while (contador <= 4) {
			this.mesa.agregarCarta(this.mazo.darCarta());
			contador++;
		}
	}
	
	
	public void iniciarJuego() {
		if (this.jugadores.size() < 2) {
			notificarMensaje("Debe agregar jugadores");
		}
		else {
			notificarMensaje("Juego iniciado");
			this.estadoJuego = Estado.JUGANDO;
			reparteGeneral();
			notificarEvento(EventosEscoba.JUEGOINICIADO);
		}
	}
	
	public void reparteGeneral() {
		repartirCartas();
		darCartasALaMesa();
		mensajesGenerales();
		if (huboEscobaDeMano()) {
			recuperarJugadorActual().agregarABaza(mesa.getCartas());
			mesa.limpiarMesa();
			recuperarJugadorActual().incrementarPuntos();
			recuperarJugadorActual().aumentarEscobas();
			notificarEvento(EventosEscoba.ESCOBA_DE_MANO);
			mensajesGenerales();
		}
	}
	
	
	public void mensajesGenerales() {
		notificarCartasDeLaMesa(mesa.getCartas());
		notificarTurno(recuperarJugadorActual().getNombre());
		notificarCartasJugador();
	}
	
	
	
	
	private void notificarCartasJugador() {
		for (Observador o : observadores) {
			o.notificarCartasJugador(this.recuperarJugadorActual().getCartas());
		}
		
	}

	private void notificarCartasDeLaMesa(List<Carta> cartas) {
		for (Observador o : observadores) {
			o.notificarCartasDeLaMesa(cartas);
		}
		
	}

	private void notificarTurno(String nombre) {
		for (Observador o : observadores) {
			o.notificarTurno(nombre);
		}
		
	}
	
	private void notificarGanador(Jugador jugador) {
		for (Observador o : observadores) {
			o.notificarGanador(this.ganador);
		}
	}
	
	

	public void notificarEvento(EventosEscoba evento) {
		for (Observador o : observadores) {
			o.notificarEvento(evento);
			}
	}

	public void agregarJugador(String nombre) {
		if (jugadores.size() < 2) {
			jugadores.add(new Jugador(nombre));
			notificarMensaje("Jugador agregado");
		} else {
			notificarMensaje("Maximo de jugadores agregados.");
		}
		
		
	}

	private void notificarMensaje(String mensaje) {
		for (Observador o : observadores) {
			o.notificarMensaje(mensaje);
		}
	}
	
	public void cambiarAJuegoFinalizado() {
		boolean juegoTerminado = false;
		Integer i = 0;
		while ((i < jugadores.size()) && !(juegoTerminado)) {
			if ( jugadores.get(i).getPuntos() >= this.puntajeObjetivo) { 
				juegoTerminado = true;
				this.ganador = jugadores.get(i);
				this.estadoJuego = Estado.FINALIZADO;
				notificarEvento(EventosEscoba.JUEGOFINALIZADO);
			}
			i++;
		}
	}
	
	
	public void actualizarPuntosJugadores() {
		ronda.actualizarPuntos(this.jugadores);
		cambiarAJuegoFinalizado();
	}
	
	
	

	public List<Carta> mostrarCartasMesa() {
		return mesa.getCartas();
	}
	
	public void cambiarJugador() {
		jugadorActual++;
		if (jugadorActual == jugadores.size())
			jugadorActual = 0;
		
	}

	public List<Carta> cartasJugadorActual() {
		return jugadores.get(jugadorActual).getCartas();
		
	}
	
	
	public void limpiarJuego() {
		for (Jugador jugador : this.jugadores) {
			jugador.limpiarMano();
			jugador.limpiarBaza();
		}
		this.mazo.limpiar();
		this.mazo.llenarMazo();
		this.mazo.mezclarMazo();
		this.mesa.limpiarMesa();
	}

}
	


