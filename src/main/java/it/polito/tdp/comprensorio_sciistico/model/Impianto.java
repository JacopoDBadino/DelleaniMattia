package it.polito.tdp.comprensorio_sciistico.model;

import java.time.LocalTime;

public class Impianto extends Tratta{
	
	private int id;
	private String nome;
	private String idMonte;
	private String idValle;
	private String localita;
	private String tipologia;
	private boolean iniziale;
	private int posti;
	private double tempoRisalita;
	private double intervallo;
	private LocalTime oraApertura;
	private LocalTime oraChiusura;
	/**
	 * @param id
	 * @param nome
	 * @param stazioneMonte
	 * @param stazioneValle
	 * @param localita
	 * @param tipologia
	 * @param iniziale
	 * @param posti
	 * @param tempoRisalita
	 * @param intervallo
	 * @param oraApertura
	 * @param oraChiusura
	 */
	public Impianto(int id, String nome, String idMonte, String idValle, String localita,
			String tipologia, boolean iniziale, int posti, double tempoRisalita, double intervallo,
			LocalTime oraApertura, LocalTime oraChiusura,String tipo) {
		super(tipo);
		this.id = id;
		this.nome = nome;
		this.idMonte = idMonte;
		this.idValle = idValle;
		this.localita = localita;
		this.tipologia = tipologia;
		this.iniziale = iniziale;
		this.posti = posti;
		this.tempoRisalita = tempoRisalita;
		this.intervallo = intervallo;
		this.oraApertura = oraApertura;
		this.oraChiusura = oraChiusura;
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
	public String getLocalita() {
		return localita;
	}
	public String getTipologia() {
		return tipologia;
	}
	public boolean isIniziale() {
		return iniziale;
	}
	public int getPosti() {
		return posti;
	}
	public double getTempoRisalita() {
		return tempoRisalita;
	}
	public double getIntervallo() {
		return intervallo;
	}
	public LocalTime getOraApertura() {
		return oraApertura;
	}
	public LocalTime getOraChiusura() {
		return oraChiusura;
	}
	@Override
	public String toString() {
		return ""+ nome  + " (" + tipologia+")";
	}
	
	
	

	
	
}
