package it.polito.tdp.comprensorio_sciistico.model;

public class Intermedio extends Livello {
	
	private static double velocitaBlu = 20.0;
	private static double velocitaRossa = 10.0;
	
	private static double kRossa= 0.8;
	private static double kBlu= 0.4;
	
	public Intermedio(String livello) {
		super(livello);
				
	}

	public static double getVelocitaBlu() {
		return velocitaBlu;
	}

	public static double getVelocitaRossa() {
		return velocitaRossa;
	}

	public static double getkRossa() {
		return kRossa;
	}

	public static double getkBlu() {
		return kBlu;
	}

	
}
