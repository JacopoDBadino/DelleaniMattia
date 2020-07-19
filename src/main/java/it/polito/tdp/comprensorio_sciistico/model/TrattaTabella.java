package it.polito.tdp.comprensorio_sciistico.model;

public class TrattaTabella {
	
	private String tipo;
	private String nome;
	private String localita;
	private String tempo;
	private String descrizione;
	/**
	 * @param tipo
	 * @param nome
	 * @param localita
	 * @param tempo
	 * @param string 
	 */
	public TrattaTabella(String tipo,String descrizione, String nome, String localita, String tempo) {
		super();
		this.descrizione = descrizione;
		this.tipo = tipo;
		this.nome = nome;
		this.localita = localita;
		this.tempo = tempo;
	}
	public String getTipo() {
		return tipo;
	}
	public String getNome() {
		return nome;
	}
	public String getLocalita() {
		return localita;
	}
	public String getTempo() {
		return tempo;
	}
	public String getDescrizione() {
		return descrizione;
	}
	
	
	

}
