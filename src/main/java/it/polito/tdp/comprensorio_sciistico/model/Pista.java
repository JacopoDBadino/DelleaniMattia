package it.polito.tdp.comprensorio_sciistico.model;

public class Pista extends Tratta {
	
	private int id;
	private String nome;
	private String idMonte;
	private String idValle;
	private String colore;
	private int lunghezza;
	private String localita;
	private double tempoPercorrenza;

	public Pista(int id, String nome, String idMonte, String idValle, String colore, int lunghezza, String localita, String tipo) {
		super(tipo);
		this.id = id;
		this.nome = nome;
		this.idMonte = idMonte;
		this.idValle = idValle;
		this.colore = colore;
		this.lunghezza = lunghezza;
		this.localita = localita;
		this.tempoPercorrenza=0.0;
	}
	public int getId() {
		return id;
	}
	public String getNome() {
		return nome;
	}
	
	public String getIdMonte() {
		return idMonte;
	}
	public String getIdValle() {
		return idValle;
	}
	public String getColore() {
		return colore;
	}
	public int getLunghezza() {
		return lunghezza;
	}
	public String getLocalita() {
		return localita;
	}
	public double getTempoPercorrenza() {
		return tempoPercorrenza;
	}
	public void setTempoPercorrenza(double tempoPercorrenza) {
		this.tempoPercorrenza = tempoPercorrenza;
	}
	@Override
	public String toString() {
		return "" + nome + " (" + colore + ")["+Math.ceil(this.tempoPercorrenza/60)+" min]," + localita + "";
	}
	
	
	

}
