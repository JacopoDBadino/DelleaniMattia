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
	
	Graph<Stazione, DefaultWeightedEdge> grafo;
	ComprensorioDAO dao;
	Map<String, Stazione> idMap;
	Map<String, Impianto> mappaImpianti;
	Map<String, List<Pista>> mappaPiste;
	Set<String> impiantiDiscesa;
	
	List<Tratta> bestSoluzione;
	//List<DefaultWeightedEdge> bestSoluzione;
	double punteggioMax;
	Stazione partenzaRicorsivo;
	Stazione arrivoRicorsivo;
	int numeroUtenti;
	double tempoMax;
	public double tempoBest;
	
	double tempoMinimo;
	
	
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
		
		for(Impianto i: dao.caricaImpianti()) {
			
			if(idMap.containsKey(i.getIdMonte()) && idMap.containsKey(i.getIdValle())) {
				//aggiungo alla mappa degli impianti
				String chiave = ""+i.getIdValle()+"-"+i.getIdMonte();
				mappaImpianti.put(chiave, i);
				
				Graphs.addEdgeWithVertices(this.grafo, idMap.get(i.getIdValle()), idMap.get(i.getIdMonte()), i.getTempoRisalita());
			}
			
		}
		impiantiDiscesa = dao.listaImpiantiDiscesa(mappaImpianti);
		System.out.println("\nIMPIANTI: "+mappaImpianti.size()+"\nDiscesa: "+impiantiDiscesa.size());
		
		caricaPiste(livello);
	
		
		
		System.out.println("PISTE: "+ mappaPiste.size());
		System.out.println("NUMERO VERTCI: "+ this.grafo.vertexSet().size());
		System.out.println("NUMERO ARCHI: "+ this.grafo.edgeSet().size());
		
	}
	
	
	public List<Tratta> camminiMinimo(Impianto inizio, Impianto fine, int numeroUtenti) {
		
		this.tempoMinimo = 0.0;
		List<Tratta> soluzione = new ArrayList<>();
		
		Stazione partenza = idMap.get(inizio.getIdMonte());
		Stazione arrivo = idMap.get(fine.getIdValle());
		DijkstraShortestPath<Stazione, DefaultWeightedEdge> dij = new DijkstraShortestPath<Stazione, DefaultWeightedEdge>(this.grafo);

		GraphPath<Stazione, DefaultWeightedEdge> cammino = dij.getPath(partenza, arrivo);
		if(cammino!=null) {
			//Aggiungo il primo impianto, perchè selezionato da input e faccio il minimo cammino dalla stazione
			//di monte
			soluzione.add(inizio);
			this.tempoMinimo = inizio.getTempoRisalita() + Math.ceil(numeroUtenti/inizio.getPosti())*(inizio.getIntervallo()*60);
			for(int i = 0; i< cammino.getVertexList().size()-1; i++) {
				
				String chiave = cammino.getVertexList().get(i).getCodice()+"-"+cammino.getVertexList().get(i+1).getCodice();
				
				if(mappaPiste.containsKey(chiave)) {
					//mi da una lista, devo vedere il prossimo vertice e scegliere la pista giusta
					List<Pista> lista = new ArrayList<>( mappaPiste.get(chiave));
					Collections.sort(lista, new Comparator<Pista>() {
	
						@Override
						public int compare(Pista o1, Pista o2) {
							// TODO Auto-generated method stub
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
		
		//List<DefaultWeightedEdge> parziale = new ArrayList<>();
		List<Tratta> parziale = new ArrayList<>();
		partenzaRicorsivo = idMap.get(partenza.getIdValle());
		arrivoRicorsivo = idMap.get(arrivo.getIdValle());
		this.numeroUtenti = numeroUtenti;
		
		parziale.add(partenza);
		//DefaultWeightedEdge e= this.grafo.getEdge(idMap.get(partenza.getIdValle()), idMap.get(partenza.getIdMonte()));
		//parziale.add(e);
				
		bestSoluzione = new ArrayList<>();
		punteggioMax = 0.0;
		this.tempoMax = tempoMax;
		double tempoIniziale=  ((partenza.getTempoRisalita())+partenza.getIntervallo()*(Math.ceil(numeroUtenti/partenza.getPosti())))*60;
		cercaRicorsivo(parziale, tempoMax,tempoIniziale, idMap.get(partenza.getIdMonte()), livello);
		
		
		return bestSoluzione;
	}
	
	

	private void cercaRicorsivo(List<Tratta> parziale, int tempoMax, double tempo, Stazione ultimaInserita, Livello livello) {


		if(tempo>this.tempoMax) {
			
			boolean impianto=false;
			//ultima tratta valida nel tempo
			Tratta ultimaTratta = (parziale.get(parziale.size()-2));
			Stazione arrivo = null;
			if(ultimaTratta.getTipo().equals("Pista")) {
				arrivo = idMap.get(((Pista)ultimaTratta).getIdValle());
	
			}else {
				arrivo = idMap.get(((Impianto)ultimaTratta).getIdValle());
				impianto = true;
			}
			if(arrivo.equals(arrivoRicorsivo)) {
								
			
			double punteggio = calcolaPunteggio(parziale, livello);
				//System.out.println("PUNTEGGIO "+punteggio+" A: "+arrivo.getCodice() +" AR: "+arrivoRicorsivo.getCodice());

				if(punteggio>punteggioMax) {
					
					List<Tratta> lista = new ArrayList<>(parziale);
					lista.remove(lista.size()-1);
					if(impianto) {
						lista.remove(lista.size()-1);
					}
					//System.out.println("ENTRA "+punteggio);
				
					punteggioMax = punteggio;
					//lista.remove(lista.size()-1);
					//System.out.println("PARTITO DA :"+((Impianto)parziale.get(0)).getIdValle());
					//System.out.println("Arrivo:"+arrivo.getCodice()+" AR: "+arrivoRicorsivo.getCodice());
				
					
					bestSoluzione = new ArrayList<>(lista);
					return;
				}
				//
			}
			//System.out.println("Non coincide con l'arrivo");
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
				//System.out.println("esce: "+parziale.size());
			}
			}
			
		return;
	}

/*
	
	private void cercaRicorsivo(List<DefaultWeightedEdge> parziale, int tempoMax, double tempo, Stazione ultimaInserita, Livello livello) {
		
		if(tempo>tempoMax) {
			
			Stazione arrivo = this.grafo.getEdgeTarget(parziale.get(parziale.size()-2));
			
			if(arrivo.equals(arrivoRicorsivo)) {
				double punteggio = calcolaPunti(parziale,livello);
			}
			
			
		}else {
			
			for(DefaultWeightedEdge e: this.grafo.outgoingEdgesOf(ultimaInserita)) {
				parziale.add(e);
				tempo+=this.grafo.getEdgeWeight(e);
				cercaRicorsivo(parziale, tempoMax, tempo, this.grafo.getEdgeTarget(e), livello);
				tempo-=this.grafo.getEdgeWeight(e);
				//String chiave = ""+ultimaInserita.getCodice()+"-"+this.grafo.getEdgeTarget(e).getCodice();
				parziale.remove(parziale.size()-1);
				
			}
		}
		
	}
	private double calcolaPunti(List<DefaultWeightedEdge> parziale, Livello livello) {
		// TODO Auto-generated method stub
		
		
		
		Map<Pista, Integer>ripetizioni = new HashMap<>();
		double punteggio = 0.0;
		final int K = 3;
		
		for(DefaultWeightedEdge e: parziale) {
			Stazione partenza = this.grafo.getEdgeSource(e);
			Stazione arrivo = this.grafo.getEdgeTarget(e);
			
			String chiave = ""+partenza.getCodice()+"-"+arrivo.getCodice();
			
			if(mappaPiste.containsKey(chiave)) {
				for(Pista pista: mappaPiste.get(chiave)) {
					if(pista.getTempoPercorrenza()==this.grafo.getEdgeWeight(e)) {
						if(!ripetizioni.containsKey(pista))
							ripetizioni.put(pista, 1);
						else {
							
							int rip = ripetizioni.get(pista) +1;
							ripetizioni.put(pista, rip);
						}
						
					}
					
				}
				
			}		
		}
		
		switch(livello.getLivello()) {
			
		case "Principiante":
			
			//Principiante p = (Principiante)livello;
			for(Pista pista: ripetizioni.keySet()) {
	
					punteggio+= (K/ripetizioni.get(pista))+(Principiante.getkBlu()*(pista.getLunghezza()/1000));
								
			}
			break;
		
		case "Intermedio":
			
			//Intermedio i = (Intermedio)livello;
			
			for(Pista pista: ripetizioni.keySet()) {
				
				if(pista.getColore().equals("BLU")) {
					
					punteggio+= (K/ripetizioni.get(pista))+(Intermedio.getkBlu()*(pista.getLunghezza()/1000));
					
					
				}else{
					
					punteggio+= (K/ripetizioni.get(pista))+(Intermedio.getkRossa()*(pista.getLunghezza()/1000));
				}
			}
			
			break;
			
		case "Esperto":
			
			//Esperto e = (Esperto)livello;
			
			for(Pista pista: ripetizioni.keySet()) {
				
				if(pista.getColore().equals("BLU")) {
					
					punteggio+= (K/ripetizioni.get(pista))+(Esperto.getkBlu()*(pista.getLunghezza()/1000));
					
					
				}else if(pista.getColore().equals("ROSSA")) {
					
					punteggio+= (K/ripetizioni.get(pista))+(Esperto.getkRossa()*(pista.getLunghezza()/1000));
				}else {
					
					punteggio+= (K/ripetizioni.get(pista))+(Esperto.getkNera()*(pista.getLunghezza()/1000));
					
					
				}
			}
			
			
			break;
			
		}
		
		
		return punteggio;
		
	}
*/
	private double calcolaPunteggio(List<Tratta> parziale, Livello livello) {
		
		Map<Pista, Integer>ripetizioni = new HashMap<>();
		double punteggio = 0.0;
		final int K = 3;
		
		for(Tratta tratta: parziale) {
			if(tratta.getTipo().equals("Pista")) {
				Pista pista = (Pista)tratta;
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
			
			//Principiante p = (Principiante)livello;
			for(Pista pista: ripetizioni.keySet()) {
	
					punteggio+= (K/ripetizioni.get(pista))+(Principiante.getkBlu()*(pista.getLunghezza()/1000));
								
			}
			break;
		
		case "Intermedio":
			
			//Intermedio i = (Intermedio)livello;
			
			for(Pista pista: ripetizioni.keySet()) {
				
				if(pista.getColore().equals("BLU")) {
					
					punteggio+= (K/ripetizioni.get(pista))+(Intermedio.getkBlu()*(pista.getLunghezza()/1000));
					
					
				}else{
					
					punteggio+= (K/ripetizioni.get(pista))+(Intermedio.getkRossa()*(pista.getLunghezza()/1000));
				}
			}
			
			break;
			
		case "Esperto":
			
			//Esperto e = (Esperto)livello;
			
			for(Pista pista: ripetizioni.keySet()) {
				
				if(pista.getColore().equals("BLU")) {
					
					punteggio+= (K/ripetizioni.get(pista))+(Esperto.getkBlu()*(pista.getLunghezza()/1000));
					
					
				}else if(pista.getColore().equals("ROSSA")) {
					
					punteggio+= (K/ripetizioni.get(pista))+(Esperto.getkRossa()*(pista.getLunghezza()/1000));
				}else {
					
					punteggio+= (K/ripetizioni.get(pista))+(Esperto.getkNera()*(pista.getLunghezza()/1000));
					
					
				}
			}
			
			
			break;
			
		}
		
		
		return punteggio;
	}

	public String stampa() {
		
		String result="";
		int contaP = 0;
		int imp=0;
		for(DefaultWeightedEdge e: this.grafo.edgeSet()) {
		
			if(this.grafo.getEdgeSource(e).getQuota()<this.grafo.getEdgeTarget(e).getQuota()) {
				
				result+="\nImpianto con peso: " + this.grafo.getEdgeWeight(e) ; 
				imp++;
			}else {
				String chiave = this.grafo.getEdgeSource(e).getCodice()+"-"+this.grafo.getEdgeTarget(e).getCodice();
				
				if(impiantiDiscesa.contains(chiave)) {
					imp++;
				}else {
				
					result+="\nPista con peso: " + this.grafo.getEdgeWeight(e)+ "("+this.grafo.getEdgeSource(e).getCodice()+"-"+this.grafo.getEdgeTarget(e).getCodice(); 
					contaP++;
				}
			}
		}
		System.out.println("CONTA: "+contaP+" IMP:"+imp+"  "+ this.grafo.edgeSet().size());
		return result;
	}
	
	public String stampaPiste() {
		
		String r = "";
		int cont = 0;
		for(List<Pista> list: mappaPiste.values()) {
			cont++;
			r+="\nGruppo "+cont+" ("+list.get(0).getIdMonte()+"-"+list.get(0).getIdValle()+"):";
			for(Pista p: list) {
				
				r+="\n"+p.getNome()+"("+p.getColore()+")";
			}
			r+="\n";
			
		}
		
		return r;
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
				// TODO Auto-generated method stub
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
				// TODO Auto-generated method stub
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

	//----------------------------------------------------------------------------------------------------
	public Map<String, Stazione> getIdMap() {
		return idMap;
	}

	public double getTempoMinimo() {
		return tempoMinimo;
	}
	
	
	
	

}
