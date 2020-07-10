package it.polito.tdp.comprensorio_sciistico.model;

public class Intermedio extends Livello {
	
	private static double velocitaBlu = 20.0;
	private static double velocitaRossa = 10.0;
	
	public Intermedio(String livello) {
		super(livello);
				
	}

	public static double getVelocitaBlu() {
		return velocitaBlu;
	}

	public static double getVelocitaRossa() {
		return velocitaRossa;
	}

}
