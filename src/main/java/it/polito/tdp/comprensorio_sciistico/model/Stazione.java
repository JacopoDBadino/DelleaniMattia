package it.polito.tdp.comprensorio_sciistico.model;

public class Stazione {
	
	

	private String codice;
	private int quota;
	
	public Stazione(String codice, int quota) {
		
		this.codice = codice;
		this.quota = quota;
		
		
	}

	public String getCodice() {
		return codice;
	}

	public int getQuota() {
		return quota;
	}
	
	
	

}
