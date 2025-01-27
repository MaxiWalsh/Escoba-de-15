package ar.edu.unlu.Vista;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import ar.edu.unlu.Controlador.Controlador;
import ar.edu.unlu.Modelo.Carta;
import ar.edu.unlu.Modelo.Estado;
//import ar.edu.unlu.Modelo.EventosEscoba;
import ar.edu.unlu.Modelo.Juego;
import ar.edu.unlu.Modelo.Jugador;

public class VistaConsola {
	private Controlador controlador;
	private Scanner teclado = new Scanner(System.in);
	
	
	
	public void setControlador(Controlador controlador) {
		this.controlador = controlador;
		
	}
		
	
	public void empezarJuego() {
		Integer opcion = -1;
		while (opcion != 0) {
            opcion = mostrarMenu();
            switch (opcion) {
            case 1:
            	// Comenzar Juego
                iniciarJuego();
                //juegoIniciado();
                break;
            case 2:
            	agregarJugador();
            	break;
            }
        }
        System.out.println("Juego finalizado.");

    }

	public void juegoIniciado() {
		Integer opcionJugador = -1;
		//loop del juego
		 while (controlador.conocerEstado() == Estado.JUGANDO) {
			 opcionJugador = mostrarMenuJugador();
			 
			 switch (opcionJugador) {
			 	 case 1:
			 		 tirarCarta();
			 		 break;
			 	 case 2:
			 		 levantarCartas();
			 		 break;		 
			 }		 	 
		 }
		 System.out.println("La partida ya finaliz�, que desea hacer ahora? ");
	}
	

	
	
	public void agregarJugador() {
		Scanner sc = new Scanner(System.in);
		System.out.println("------------------------------------------------------------------------------");
		System.out.println("--                          Agregando Jugador                               --");
		System.out.println("------------------------------------------------------------------------------");
		System.out.print("-- Ingresar nombre : ");
		String nombre = sc.nextLine();
		controlador.crearJugador(nombre);
	}

	//METODOS PRINCIPALES: LEVANTAR Y TIRAR
	
	private void levantarCartas() {
		
		Integer numeroCarta;
		System.out.println("Ingrese la posicion de la carta de su mano a levantar (1-" + cantidadCartasJugador() + ")");
		numeroCarta = teclado.nextInt();
		while (numeroCarta < 1 || numeroCarta > cantidadCartasJugador() ) {												//Validacion de la opcion ingresada
			System.out.println("Por favor ingrese una posicion correcta");
			numeroCarta = teclado.nextInt();
		}
		
		Integer numero;
		System.out.println("Elija las cartas de la mesa(1-" + cantidadCartasMesa() + ")");
		numero = teclado.nextInt();
		while (numero < 1 || numero > cantidadCartasMesa() ) {												//Validacion de la opcion ingresada
			System.out.println("Por favor ingrese una posicion correcta");
			numero = teclado.nextInt();
		}
		
		ArrayList<Integer> cartasALevantar = new ArrayList<>();
		cartasALevantar.add(numero - 1);
		
		Integer opcion = menuOpcionesLevantada();
		
		Integer cartaAnterior = numero;						//Entero para guardar la carta elegida anteriormente y no poder volver a elegirla.
		
		while(opcion == 1) {
			System.out.println("Elijas las cartas de la mesa(1-" + cantidadCartasMesa() + ")");
			numero = teclado.nextInt();
			while (numero < 1 || numero > cantidadCartasMesa()  || numero == cartaAnterior ) {												//Validacion de la opcion ingresada
				if (numero == cartaAnterior) {
					System.out.println("No puede elegir la misma carta, elija otra.");
					numero = teclado.nextInt();
				} else {
					System.out.println("Por favor ingrese una posicion correcta");
					numero = teclado.nextInt();
				}
			}
			cartasALevantar.add(numero - 1);
			cartaAnterior = numero;						//Ac� guardo la carta anterior
			opcion = menuOpcionesLevantada();
		}
		
		controlador.levantarCartas(numeroCarta - 1, cartasALevantar);
		
	}


	private void tirarCarta() {
		Integer numero;
		System.out.println("Ingrese la posicion de la carta a tirar (1-" + cantidadCartasJugador() + ")");
		numero = teclado.nextInt();
		while (numero < 1 || numero > cantidadCartasJugador() ) {												//Validacion de la opcion ingresada
			System.out.println("Por favor ingrese una posicion correcta");
			numero = teclado.nextInt();
		}
		
		controlador.tirarCarta(numero);													//Una vez ingresada una opcion correta,
		 																				//se procede a llamar a los metodos correspondientes para tirar la carta
								;														//Una vez hecho, se cambia el turno
	}
	
	
	//FIN METODOS PRINCIPALES
	

	//AQUI SE ENCUENTRAN LOS MENUS

	private int menuOpcionesLevantada() {
        int opcion = -1;
        while (opcion != 1 && opcion != 2) {
            System.out.println("-------------------------------------------------------------------------------");
            System.out.println("--                             �Que quiere hacer?                           --");
            System.out.println("-------------------------------------------------------------------------------");
            System.out.println("-- 1 - Levantar otra                                                         --");
            System.out.println("-------------------------------------------------------------------------------");
            System.out.println("-- 2 - Dejar de levantar                                                     --");
            System.out.println("-------------------------------------------------------------------------------");
			opcion = teclado.nextInt();
			
			if (opcion != 1 && opcion != 2) {
				System.out.println("La opcion elegida no es valida. Por favor, elija una valida:");
			}
        }
        return opcion;
    }
	
	private int mostrarMenuJugador() {
        int opcion = -1;
        while (opcion != 1 && opcion != 2) {
            System.out.println("------------------------------------------------------------------------------");
            System.out.println("--                             �Que desea hacer?                            --");
            System.out.println("------------------------------------------------------------------------------");
            System.out.println("-- 1 - Tirar carta                                                          --");
            System.out.println("------------------------------------------------------------------------------");
            System.out.println("-- 2 - Levantar carta                                                        --");
            System.out.println("------------------------------------------------------------------------------");
			opcion = teclado.nextInt();
			
			if (opcion != 1 && opcion != 2) {
				System.out.println("La opcion elegida no es valida. Por favor, elija una valida:");
			}
        }
        return opcion;
    }
	
	
	private int mostrarMenu() {
        int opcion = -1;
        while (opcion < 0 || opcion > 2) {
            System.out.println("------------------------------------------------------------------------------");
            System.out.println("--                             Escoba de 15                                 --");
            System.out.println("------------------------------------------------------------------------------");
            System.out.println("-- 1 - Iniciar Juego                                                        --");
            System.out.println("------------------------------------------------------------------------------");
            System.out.println("-- 2 - Agregar Jugador                                                      --");
            System.out.println("------------------------------------------------------------------------------");
            System.out.println("-- 0 - Salir del Juego                                                      --");
            System.out.println("------------------------------------------------------------------------------");
			opcion = teclado.nextInt();
        }
        return opcion;
    }


	
	//FIN DE LOS MENUS
	
	//METODOS AUXILIARES
	public void mostrarCartasMesa(List<Carta> cartas) {
		Integer i = 1;
		System.out.println("---------------------------CARTAS DE LA MESA----------------------------------");	 
		for (Carta carta : cartas) {
			System.out.println(i + " - " + carta.mostrarCarta());
			i++;
		
		}
		System.out.println("------------------------------------------------------------------------------");
	}



	public void mostrarCartasJugador(List<Carta> cartas) {
		System.out.println("---------------------------CARTAS DEL JUGADOR----------------------------------");
		Integer i = 1;
		for (Carta carta : cartas) {
			System.out.println(i + " - " + carta.mostrarCarta());
			i++;
		}
	}
	
	
	public void notificarEscobaDeMano() {
		System.out.println("Hubo escoba de mano.");	
	}
	
	private Integer cantidadCartasMesa() {
		return controlador.cantidadCartasMesa();
	}
	
	private void mostrarCartasBazaJugador() {
		controlador.cartasBazaJugador();
	}
	
	private Integer cantidadCartasJugador() {
		return controlador.cantidadCartasJugadorActual();
	}
	
	public void repartir() {
		System.out.println("Se han repartido nuevas cartas. ");
		esperarEnter();
	}
	
	private void esperarEnter() {
		System.out.print("Presione ENTER para continuar...");
		Scanner sc = new Scanner(System.in);
		String pausa = sc.nextLine();
	}
	
	public void puntosActualizados() {
		System.out.println("La ronda termino y los resultados son estos: ");
		System.out.println("Ganador : " + controlador.getNombreGanadorRonda());
		puntos();
		//controlador.limpiarJuego();
	}

	public void finJuego(){
		System.out.println("-----------------------");
		System.out.println("Escoba de 15 finalizada");
		System.out.println("-----------------------");

	}
		
	public void mostrarGanador(Jugador jugador) {
		System.out.println("El ganador es " + jugador.getNombre());
		puntos();
	}
	
	public void puntos() {
		for (Jugador jugador : controlador.getJugadores()) {
			System.out.println("Puntos de " + jugador.getNombre() + " : " + jugador.getPuntos());
		}
	}
	private void iniciarJuego() {
		controlador.iniciarJuego();
	}

	public void notificarMensaje(String mensaje) {
		System.out.println(mensaje);
	}
	
	public void turno(String nombre) {
		System.out.println("Es el turno del jugador : " + nombre);
	}
	
}
	
