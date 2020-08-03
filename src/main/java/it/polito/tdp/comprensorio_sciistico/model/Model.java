package it.polito.tdp.comprensorio_sciistico.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.DirectedWeightedMultigraph;

import it.polito.tdp.comprensorio_sciistico.db.ComprensorioDAO;


public class Model {
	
	private Graph<Stazione, DefaultWeightedEdge> grafo;
	private ComprensorioDAO dao;
	private Map<String, Stazione> idMap;
	private Map<String, Impianto> mappaImpianti;
	private Map<String, List<Pista>> mappaPiste;
	@SuppressWarnings("unused")
	private Set<String> impiantiDiscesa;
	
	private List<Tratta> bestSoluzione;
	private double punteggioMax;
	private Stazione arrivoRicorsivo;
	private int numeroUtenti;
	private double tempoMax;
	private double tempoBest;
	
	private double tempoMinimo;
	
	
	public Model() {
		idMap= new HashMap<>();
		dao = new ComprensorioDAO();
		mappaImpianti = new HashMap<>();
		mappaPiste = new HashMap<>();
		impiantiDiscesa = new HashSet<>();
	}
	
	public void creaGrafo(Livello livello) {
		
		this.grafo = new DirectedWeightedMultigraph<Stazione, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		dao.caricaStazioni(idMap);
		Graphs.addAllVertices(this.grafo,idMap.values());
		//aggiunta impianti
		for(Impianto i: dao.caricaImpianti()) {
			
			if(idMap.containsKey(i.getIdMonte()) && idMap.containsKey(i.getIdValle())) {
				//aggiungo alla mappa degli impianti
				String chiave = ""+i.getIdValle()+"-"+i.getIdMonte();
				mappaImpianti.put(chiave, i);
				
				Graphs.addEdgeWithVertices(this.grafo, idMap.get(i.getIdValle()), idMap.get(i.getIdMonte()), i.getTempoRisalita());
			}
			
		}
		impiantiDiscesa = dao.listaImpiantiDiscesa(mappaImpianti);
		
		caricaPiste(livello);
		
	}
	
	
	public List<Tratta> camminoMinimo(String inizio, String fine, int numeroUtenti) {
		
		this.tempoMinimo = 0.0;
		List<Tratta> soluzione = new ArrayList<>();
		
		Stazione partenza = idMap.get(inizio);
		Stazione arrivo = idMap.get(fine);
		DijkstraShortestPath<Stazione, DefaultWeightedEdge> dij = new DijkstraShortestPath<Stazione, DefaultWeightedEdge>(this.grafo);

		GraphPath<Stazione, DefaultWeightedEdge> cammino = dij.getPath(partenza, arrivo);
		if(cammino!=null) {
		
			for(int i = 0; i< cammino.getVertexList().size()-1; i++) {
				
				String chiave = cammino.getVertexList().get(i).getCodice()+"-"+cammino.getVertexList().get(i+1).getCodice();
				
				if(mappaPiste.containsKey(chiave)) {
					//mi da una lista, devo scegliere la prima pista in ordine decresente, perche è la piu breve in termini di tempo
					List<Pista> lista = new ArrayList<>( mappaPiste.get(chiave));
					Collections.sort(lista, new Comparator<Pista>() {
	
						@Override
						public int compare(Pista o1, Pista o2) {
							return -(int)(o1.getTempoPercorrenza()-o2.getTempoPercorrenza());
						}
						
					});		
					
					tempoMinimo+= lista.get(0).getTempoPercorrenza();
					soluzione.add(lista.get(0));
				}else {
					
					Impianto impianto = mappaImpianti.get(chiave);
					
					tempoMinimo+= impianto.getTempoRisalita() + Math.ceil(numeroUtenti/impianto.getPosti())*(impianto.getIntervallo()*60);
							
					soluzione.add(mappaImpianti.get(chiave));
				}
			
			}
		}	
		
		return soluzione;
		
	}
	
	private void caricaPiste(Livello livello) {
	
		switch(livello.getLivello()) {
		
		case "Esperto":
			
			for(Pista p: dao.caricaPiste()) {
				if(idMap.containsKey(p.getIdMonte()) && idMap.containsKey(p.getIdValle())) {
					//aggiungo alla mappa delle piste
					String chiave = ""+p.getIdMonte()+"-"+p.getIdValle();
					if(!mappaPiste.containsKey(chiave)) {
						List<Pista> lista = new ArrayList<>();
						lista.add(p);
						mappaPiste.put(chiave, lista);
						
					}else {
						
						mappaPiste.get(chiave).add(p);
						
					}
					
					double tempo =0;
					if(p.getColore().equals("ROSSA")) {
						tempo = p.getLunghezza()/(Esperto.getVelocitaRossa()/3.6);	
					}else if(p.getColore().equals("BLU")) {
						tempo = p.getLunghezza()/(Esperto.getVelocitaBlu()/3.6);
					}else {
						tempo = p.getLunghezza()/(Esperto.getVelocitaNera()/3.6);
						
					}
					tempo= Math.ceil(tempo);
					p.setTempoPercorrenza(tempo);
					Graphs.addEdgeWithVertices(this.grafo, idMap.get(p.getIdMonte()),idMap.get(p.getIdValle()), tempo);
				}
				
			}
			break;
			
		case "Principiante":
			
			for(Pista p: dao.caricaPiste()) {
				if(p.getColore().equals("BLU")) {
					if(idMap.containsKey(p.getIdMonte()) && idMap.containsKey(p.getIdValle())) {
						//aggiungo alla mappa delle piste
						String chiave = ""+p.getIdMonte()+"-"+p.getIdValle();
						if(!mappaPiste.containsKey(chiave)) {
							List<Pista> lista = new ArrayList<>();
							lista.add(p);
							mappaPiste.put(chiave, lista);
							
						}else {
							
							mappaPiste.get(chiave).add(p);
							
							
						}
						
						double tempo = p.getLunghezza()/(Principiante.getVelocitaBlu()/3.6);
						tempo= Math.ceil(tempo);
						p.setTempoPercorrenza(tempo);
						Graphs.addEdgeWithVertices(this.grafo, idMap.get(p.getIdMonte()),idMap.get(p.getIdValle()), tempo);
					}
				}
				
			}
			
			break;
		case "Intermedio":
			
			for(Pista p: dao.caricaPiste()) {
				if(!p.getColore().equals("NERA")) {
					if(idMap.containsKey(p.getIdMonte()) && idMap.containsKey(p.getIdValle())) {
						//aggiungo alla mappa delle piste
						String chiave = ""+p.getIdMonte()+"-"+p.getIdValle();
						if(!mappaPiste.containsKey(chiave)) {
							List<Pista> lista = new ArrayList<>();
							lista.add(p);
							mappaPiste.put(chiave, lista);
							
						}else {
							mappaPiste.get(chiave).add(p);
							
						}
						
						double tempo = 0.0;
						if(p.getColore().equals("ROSSA")) {
							tempo = p.getLunghezza()/(Intermedio.getVelocitaRossa()/3.6);	
						}else {
							tempo = p.getLunghezza()/(Intermedio.getVelocitaBlu()/3.6);
						}
						tempo= Math.ceil(tempo);
						p.setTempoPercorrenza(tempo);
						Graphs.addEdgeWithVertices(this.grafo, idMap.get(p.getIdMonte()),idMap.get(p.getIdValle()), tempo);
					}
				}
				
			}
			
			break;
			
		}
		
		
		
	}
	
	public List<Tratta> trovaMassimoPercorso(Livello livello, int numeroUtenti, int tempoMax, Impianto partenza, Impianto arrivo){
		
		
		//se esiste un cammino minimo procedo
		List<Tratta> camminoMinimo = this.camminoMinimo(partenza.getIdValle(), arrivo.getIdValle(), numeroUtenti);
		
		if((this.tempoMinimo <= tempoMax && camminoMinimo.size()!=0) || (partenza.equals(arrivo))) {
			
			List<Tratta> parziale = new ArrayList<>();
			
			arrivoRicorsivo = idMap.get(arrivo.getIdValle());
			this.numeroUtenti = numeroUtenti;
			
			parziale.add(partenza);
		
			bestSoluzione = new ArrayList<>();
			punteggioMax = 0.0;
			this.tempoMax = tempoMax;
			//come ultima inserita metto la stazione di monte dell'impianto di partenza
			double tempoIniziale=  partenza.getTempoRisalita() + (partenza.getIntervallo()*(Math.ceil(numeroUtenti/partenza.getPosti())))*60;
			cercaRicorsivo(parziale, tempoMax,tempoIniziale, idMap.get(partenza.getIdMonte()), livello);
			
			
			return bestSoluzione;
		}else {
			return null;
		}
	}
	
	

	private void cercaRicorsivo(List<Tratta> parziale, int tempoMax, double tempo, Stazione ultimaInserita, Livello livello) {


		if(tempo>this.tempoMax) {
			
			List<Tratta> soluzione = new ArrayList<>(parziale);
			boolean impianto = false;
			
			double tempoPercorrenza = 0.0;
			
			Tratta ultimaTratta = (soluzione.get(soluzione.size()-1));
			if(ultimaTratta.getTipo().equals("Pista")) {
				tempoPercorrenza = tempo - ((Pista)ultimaTratta).getTempoPercorrenza();
	
			}else {
				Impianto i =  ((Impianto)ultimaTratta);
				tempoPercorrenza = tempo - i.getTempoRisalita() - (i.getIntervallo()*(Math.ceil(this.numeroUtenti/i.getPosti())))*60;
			}
			//ultima tratta valida nel tempo
			Tratta ultimaTrattaValida = (parziale.get(parziale.size()-2));
			
			Stazione arrivo = null;
			if(ultimaTrattaValida.getTipo().equals("Pista")) {
				arrivo = idMap.get(((Pista)ultimaTrattaValida).getIdValle());		
	
			}else {
				Impianto i =((Impianto)ultimaTrattaValida);
				arrivo = idMap.get(i.getIdValle());
				impianto = true;
				tempoPercorrenza-= i.getTempoRisalita() - (i.getIntervallo()*(Math.ceil(this.numeroUtenti/i.getPosti())))*60;
				
			}
			if(arrivo.equals(arrivoRicorsivo)) {
				//parametro per valorizzare il tempo nel calcolo del punteggio
				double k = 0.05;
			
				double punteggio = calcolaPunteggio(soluzione, livello) + k*tempoPercorrenza;

				if(punteggio>punteggioMax) {
					
					
					//rimuovo quella non valida
					soluzione.remove(soluzione.size()-1);
					
					//se la penultima è impianto tolgo
					if(impianto) {
						soluzione.remove(soluzione.size()-1);

					}
										
					tempoBest = tempoPercorrenza;
					punteggioMax = punteggio;
									
					bestSoluzione = new ArrayList<>(soluzione);
					return;
				}
				
			}
			
			return;
			
		}else {
			
			for(Stazione prossimo: Graphs.successorListOf(this.grafo, ultimaInserita)) {
				
				String chiave = ""+ultimaInserita.getCodice()+"-"+prossimo.getCodice();
				
				if(mappaImpianti.containsKey(chiave)) {
					//aggiungo impianto
		
					Impianto i = mappaImpianti.get(chiave);
					if(!parziale.contains(i)) {
						parziale.add(i);
						
						double tempoImpianto = i.getTempoRisalita() + (i.getIntervallo()*(Math.ceil(this.numeroUtenti/i.getPosti())))*60;
						tempo+=tempoImpianto;
						//ricorsivo
						cercaRicorsivo(parziale, tempoMax, tempo, prossimo, livello);
						//backtracking
						tempo-=tempoImpianto;
						parziale.remove(parziale.size()-1);
						
					}
				}else if(mappaPiste.containsKey(chiave)) {
					
					List<Pista> lista = mappaPiste.get(chiave);
					for(Pista p: lista) {
						if(!parziale.contains(p)) {
							parziale.add(p);
							double tempoPista= p.getTempoPercorrenza();
							tempo+=tempoPista;
							
							cercaRicorsivo(parziale, tempoMax, tempo, prossimo, livello);
							
							//backtracking
							tempo-=tempoPista;
							parziale.remove(parziale.size()-1);
						}
						
					}
				}
				
			}
		}
			
		return;
	}


	private double calcolaPunteggio(List<Tratta> parziale, Livello livello) {
		
		Map<Pista, Integer>ripetizioni = new HashMap<>();
		double punteggio = 0.0;
		final int K = 3;
		//fino a parziale size-1 perchè ce ancora quella non valida
		for(int i = 0; i< parziale.size()-1; i++) {
			if(parziale.get(i).getTipo().equals("Pista")) {
				Pista pista = (Pista)parziale.get(i);
				if(!ripetizioni.containsKey(pista))
					ripetizioni.put(pista, 1);
				else {
					
					int rip = ripetizioni.get(pista) +1;
					ripetizioni.put(pista, rip);
				}
			}
		}
		
		switch(livello.getLivello()) {
			
		case "Principiante":
			
			for(Pista pista: ripetizioni.keySet()) {
					punteggio+= (K/ripetizioni.get(pista))+(Principiante.getkBlu()*(pista.getLunghezza()/1000));					
			}
			break;
		
		case "Intermedio":
			
			for(Pista pista: ripetizioni.keySet()) {
				if(pista.getColore().equals("BLU")) {
					
					punteggio+= ((K-1)/ripetizioni.get(pista))+(Intermedio.getkBlu()*(pista.getLunghezza()/1000));
	
				}else{
					
					punteggio+= (K/ripetizioni.get(pista))+(Intermedio.getkRossa()*(pista.getLunghezza()/1000));
				}
			}
			
			break;
			
		case "Esperto":
			
			for(Pista pista: ripetizioni.keySet()) {
				if(pista.getColore().equals("BLU")) {
					punteggio+= ((K-2)/ripetizioni.get(pista))+(Esperto.getkBlu()*(pista.getLunghezza()/1000));
					
				}else if(pista.getColore().equals("ROSSA")) {
					punteggio+= ((K-1)/ripetizioni.get(pista))+(Esperto.getkRossa()*(pista.getLunghezza()/1000));
				
				}else {
					punteggio+= (K/ripetizioni.get(pista))+(Esperto.getkNera()*(pista.getLunghezza()/1000));
				
				}
			}
			
			break;	
		}
				
		return punteggio;
	}

	public List<Impianto> getImpiantiIniziali(String localita){
		
		List<Impianto> lista = new ArrayList<>();
		
		for(Impianto i: mappaImpianti.values()) {
			if(i.isIniziale() && i.getLocalita().equals(localita)) {
				lista.add(i);
			}
		}
		
		Collections.sort(lista, new Comparator<Impianto>(){

			@Override
			public int compare(Impianto o1, Impianto o2) {
				
				return o1.getNome().compareTo(o2.getNome());
			}	
		});
		
		return lista;
		
	}
	
	public List<Impianto> getImpiantiPerLocalita(String localita){

		List<Impianto> lista = new ArrayList<>();		
		for(Impianto i: mappaImpianti.values()) {
			if(i.getLocalita().equals(localita)) {
				lista.add(i);
			}
		}
		
		Collections.sort(lista, new Comparator<Impianto>(){

			@Override
			public int compare(Impianto o1, Impianto o2) {
				return o1.getNome().compareTo(o2.getNome());
			}	
		});
		
		return lista;
	}
	
	public Set<String> getLocalita(){
		
		Set<String> localita = new HashSet<>();
		for(Impianto i: mappaImpianti.values()) {
			if(!localita.contains(i.getLocalita())) {
				localita.add(i.getLocalita());
			}
			
		}
		return localita;
	}

	public Map<String, Stazione> getIdMap() {
		return idMap;
	}
	
	public String convertiTempoInStringa(double tempo) {
	    	
	    	String risultato = "";
	    	
	    	int ore = (int) Math.floor(tempo/3600);
	    	
	    	if(ore > 0) {
	    		
	    		double tempoRestante = (tempo - ore*3600);
	    		int minuti = (int)Math.floor(tempoRestante/60);
	    		
	    		if(minuti > 0) {
	    			double secondi = (tempoRestante - (minuti*60));
	    			if(secondi >0) {
	    				risultato = ore + "h "+minuti+"' "+(int) secondi+"\"";
	    			}else {
	    				risultato = ore + "h "+minuti+"' ";
	    			}
	    		}else {
	    			int secondi = (int)tempoRestante;
	    			
	    			risultato = ore+"h " +secondi +"\"";
	    			
	    		}
	    		
	    		
	    	}else {
	    		int minuti = (int) Math.floor(tempo/60);
	    		
	    		if(minuti >0) {
	    			
	    			int secondi = (int)(tempo - (minuti*60));
	    			if(secondi>0) {
	    				risultato = ""+minuti+"' "+secondi+"\"";
	    			}else {
	    				risultato = ""+minuti+"' ";
	    			}
	    		}else {
	    			int secondi = (int)tempo;
	    			
	    			risultato =""+secondi +"\"";
	    			
	    		}
	    		
	    	}
	    	
	    	return risultato;
	    }

	public double getTempoBest() {
		return tempoBest;
	}
	
	public double getTempoMinimo() {
		return tempoMinimo;
	}
	
	

}
