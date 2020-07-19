package it.polito.tdp.comprensorio_sciistico.model;

public class Livello {
	
	private String livello;

	/**
	 * @param livello
	 */
	public Livello(String livello) {
		super();
		this.livello = livello;
	}

	public String getLivello() {
		return livello;
	}

	@Override
	public String toString() {
		return ""+ this.livello.toUpperCase();
	}
	
	
	
	

}
