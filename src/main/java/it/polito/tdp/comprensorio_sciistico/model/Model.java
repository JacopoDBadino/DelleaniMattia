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
	double punteggioMax;
	Stazione partenzaRicorsivo;
	Stazione arrivoRicorsivo;
	
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
				
				Graphs.addEdgeWithVertices(this.grafo, idMap.get(i.getIdValle()), idMap.get(i.getIdMonte()), i.getTempoRisalita()*60);
			}
			
		}
		 impiantiDiscesa = dao.listaImpiantiDiscesa(mappaImpianti);
		System.out.println("IMPIANTI: "+mappaImpianti.size()+"\nDiscesa: "+impiantiDiscesa.size());
		
		caricaPiste(livello);
	
		
		
		//System.out.println("PISTE: "+ mappaPiste.size());
		System.out.println("NUMERO VERTCI: "+ this.grafo.vertexSet().size());
		System.out.println("NUMERO ARCHI: "+ this.grafo.edgeSet().size());
		
	}
	
	
	public String camminiMinimo(Impianto inizio, Impianto fine) {
		
		Stazione partenza = idMap.get(inizio.getIdValle());
		Stazione arrivo = idMap.get(fine.getIdValle());
		System.out.println(partenza.getCodice()+"   "+arrivo.getCodice());
		DijkstraShortestPath<Stazione, DefaultWeightedEdge> dij = new DijkstraShortestPath<Stazione, DefaultWeightedEdge>(this.grafo);

		GraphPath<Stazione, DefaultWeightedEdge> cammino = dij.getPath(partenza, arrivo);
		System.out.println("CAMMINO "+ cammino.getVertexList().size());
		double tempo= dij.getPathWeight(partenza, arrivo);
		
		String result = "Cammino minimo con durata "+Math.ceil(tempo/60)+" minuti. Partenza: "+ inizio.getLocalita() +" ("+inizio.getNome()+") - Arrivo: " + fine.getLocalita() +" ("+fine.getNome()+")";
		result +="\nPercorso:";

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
				result+="\nPista: "+lista.get(0).toString();
			}else {
				result+="\nImpianto: "+ mappaImpianti.get(chiave).toString();
			}
			
		}
		
		return result;
		
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
	
	public List<Tratta> trovaMassimoPercorso(Livello livello, int tempoMax, Stazione partenza, Stazione arrivo){
		
		List<Tratta> parziale = new ArrayList<>();
		
		partenzaRicorsivo = partenza;
		arrivoRicorsivo = arrivo;
		
		bestSoluzione = new ArrayList<>();
		punteggioMax = Double.MIN_VALUE;
		
		cercaRicorsivo(parziale, tempoMax);
		
		
		return bestSoluzione;
	}
	
	

	private void cercaRicorsivo(List<Tratta> parziale, int tempoMax) {
		// TODO Auto-generated method stub
		
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
	
	public Set<String> getLocatita(){
		
		Set<String> localita = new HashSet<>();
		for(Impianto i: mappaImpianti.values()) {
			if(!localita.contains(i.getLocalita())) {
				localita.add(i.getLocalita());
			}
			
		}
		return localita;
	}
	
	

}
