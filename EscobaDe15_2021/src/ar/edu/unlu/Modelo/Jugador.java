package ar.edu.unlu.Modelo;

import java.util.ArrayList;
import java.util.List;



public class Jugador {
	private String nombre;
	private Integer puntos;
	private Integer escobas;
	private List<Carta> cartas = new ArrayList<>();
	private boolean turno = false;
	private List<Carta> baza = new ArrayList<>();
	
	//constructor
	public Jugador(String nombre) {
		this.setNombre(nombre);
		this.setPuntos(0);
		this.setEscobas(0);
	}


	//Inicio setters & getters
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public Integer getPuntos() {
		return puntos;
	}
	public void setPuntos(Integer puntos) {
		this.puntos = puntos;
	}
	public List<Carta> getCartas() {
		return cartas;
	}

	public boolean isTurno() {
		return turno;
	}
	public void setTurno(boolean turno) {
		this.turno = turno;
	}
	
	public List<Carta> getBaza() {
		return baza;
	}
	public void setBaza(List<Carta> baza) {
		this.baza = baza;
	}
	
	public Integer getEscobas() {
		return escobas;
	}


	public void setEscobas(Integer escobas) {
		this.escobas = escobas;
	}
	
	//fin de setters & getters
	
	//inicio de metodos del jugador
	
	//funcion para agregar cartas a la mano del jugador
	public void agregarCartas(Carta carta) {
		this.cartas.add(carta);						//agregar la carta a la mano del jugador
	}
	
	//funcion con la cual el jugador tira una carta de su mano
	public Carta tirarCarta(Integer numero) {
		Carta carta = cartas.get(numero - 1);					//Se guarda la carta elegida
		cartas.remove(carta);									//Se elimina de la mano del jugador
		return carta;											//retorna una carta que ser� agregada a la mesa
	}
	
	//funcion con la cual el jugador, con una carta de su mano y cartas que eligi� de la mesa para hacer baza
	
	public List<Carta> levantarCartas(Integer numero, List<Carta> cartasMesa){
		cartasMesa.add(recuperarCarta(numero));															//agrega a la lista de cartas que eligio de la mesa la carta de su mano
		//ArrayList<Carta> cartasLevantadas = cartasMesa;								//se guarda esta lista en una nueva
		//cartas.remove(carta);															//se elimina la carta del jugador de su mano
		return cartasMesa;																//se retorna una lista que luego se verificara si suma 15
																						//en caso de que si, se agregara a la cartas de la baza
	}
	
	public Carta recuperarCarta(Integer numero) {
		return this.cartas.get(numero);
	}
	
	
	public void agregarABaza(List<Carta> cartas) {
		for (Carta carta : cartas) {
			this.baza.add(carta);
		}
	}
	
	
	public void incrementarPuntos() {
		this.puntos++;
	}
	
	public void mostrarMano() {
		for (Carta carta : cartas) {
			System.out.println(carta.mostrarCarta());
		}
	
	}
	
	public void mostrarBaza() {
		System.out.println("Cartas de: " + this.getNombre());
		for (Carta carta : this.baza) {
			System.out.println(carta.mostrarCarta());
		}
	}
	
	public void limpiarMano() {
		this.cartas.clear();
	}
	
	public void limpiarBaza() {
		this.baza.clear();
	}
	
	
	public Integer cantidadDeCartas() {
		return this.cartas.size();
	}
	
	public void sacarCarta(Integer numero) {
		this.cartas.remove(this.cartas.get(numero));
	}

	public void aumentarEscobas() {
		this.escobas++;
	}
}
