package it.polito.tdp.comprensorio_sciistico.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;

public class TestModel {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Model model = new Model();
		Graph<Stazione, DefaultWeightedEdge> grafo;
		//model.creaGrafo();
		
		Esperto e = new Esperto("Esperto");
		Principiante p = new Principiante("Principiante");
		Intermedio i = new Intermedio("Intermedio");
		
		
		List<Livello> lista = new ArrayList<>();
		
		 lista.add(p);
		//lista.add(i);
		 //lista.add(e);
		
		
		for(Livello l: lista) {
			model.creaGrafo(l);
			//System.out.println(model.stampaPiste());
		}
		
		List<Impianto> sauze =  model.getImpiantiIniziali("Sestriere");
		List<Impianto> sestriere =  model.getImpiantiIniziali("Sestriere");
		System.out.println("sau:"+sauze.size()+" ses: "+sestriere.size());
		/*
		for(int ij =0; ij<sauze.size();ij++) {
			for(int j =0; j<sestriere.size();j++) {
				String ris = model.camminiMinimo(sauze.get(ij), sestriere.get(j));
				System.out.println("---------------------------------\n"+ris);
				
			}
			
		}
		
		*/
		Map<String, Stazione> idMap = model.getIdMap();
		
		List<Tratta> best = model.trovaMassimoPercorso(e, 4, 3600, sestriere.get(0), sestriere.get(1));
		System.out.println("PARTO: "+sestriere.get(0).getIdValle()+" "+ sestriere.get(0).getNome() +"  ARRIVO "+ sestriere.get(1).getIdValle()+ "  "+sestriere.get(1).getNome());
		
		double tempo = 0.0;
		for(Tratta t: best) {
			
			if(t.getTipo().equals("Pista")) {
				System.out.println("PISTA: "+((Pista)t).getId() + "  "+ ((Pista)t).getNome()+" T: "+ ((Pista)t).getTempoPercorrenza());
				tempo +=((Pista)t).getTempoPercorrenza();
			}
			else {
				System.out.println("Impianto: "+((Impianto)t).getId()+"   "+ ((Impianto)t).getNome() +" T: "+ (((Impianto)t).getTempoRisalita())*60) ;
				tempo+=((Impianto)t).getTempoRisalita()*60;
			}
		}
			
		System.out.println("TEMPO: "+ tempo +" PT: "+model.punteggioMax);
		/*
		for(Impianto imp: model.getImpiantiIniziali("Sauze D'Oulx")) {
			System.out.println(""+imp.getNome()+"  "+imp.getLocalita());
		}
		
		
		for(String s: model.getLocalita()) {
			
			System.out.println("\n "+s);
		}
		
		
		// System.out.println("-----------------------------\n"+model.stampa());
		//System.out.println(model.stampaPiste());
		 * 
		 */
	}

}
