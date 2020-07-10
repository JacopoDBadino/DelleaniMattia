package it.polito.tdp.comprensorio_sciistico.model;

public class Esperto extends Livello {

	private static int velocitaBlu = 50; 
	private static int velocitaRossa = 30;
	private static int velocitaNera = 15;
	
	public Esperto(String livello) {
		super(livello);
				
	}

	public static int getVelocitaBlu() {
		return velocitaBlu;
	}

	public static int getVelocitaRossa() {
		return velocitaRossa;
	}

	public static int getVelocitaNera() {
		return velocitaNera;
	}

	
	
}
