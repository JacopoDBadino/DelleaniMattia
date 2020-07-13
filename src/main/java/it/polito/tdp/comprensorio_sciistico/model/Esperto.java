package it.polito.tdp.comprensorio_sciistico.model;

public class Esperto extends Livello {

	private static int velocitaBlu = 50; 
	private static int velocitaRossa = 30;
	private static int velocitaNera = 15;
	
	private static double kNera= 0.8;
	private static double kRossa= 0.5;
	private static double kBlu= 0.2;
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

	public static double getkNera() {
		return kNera;
	}

	public static double getkRossa() {
		return kRossa;
	}

	public static double getkBlu() {
		return kBlu;
	}

	
	
	
}
