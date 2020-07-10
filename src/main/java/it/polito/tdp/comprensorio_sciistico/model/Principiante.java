package it.polito.tdp.comprensorio_sciistico.model;

public class Principiante extends Livello {

	private static double velocitaBlu = 10.0;
	
	public Principiante(String livello) {
		super(livello);
		// TODO Auto-generated constructor stub
	}

	public static double getVelocitaBlu() {
		return velocitaBlu;
	}
	

}
