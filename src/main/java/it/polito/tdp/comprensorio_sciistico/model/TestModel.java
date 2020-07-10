package it.polito.tdp.comprensorio_sciistico.model;

import java.util.ArrayList;
import java.util.List;

public class TestModel {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Model model = new Model();
		
		//model.creaGrafo();
		
		Esperto e = new Esperto("Esperto");
		Principiante p = new Principiante("Principiante");
		Intermedio i = new Intermedio("Intermedio");
		
		
		List<Livello> lista = new ArrayList<>();
		
		 //lista.add(p);
		 //lista.add(i);
		 lista.add(e);
		
		
		for(Livello l: lista) {
			model.creaGrafo(l);
			//System.out.println(model.stampaPiste());
		}
		
		List<Impianto> sauze =  model.getImpiantiIniziali("Sansicario");
		List<Impianto> sestriere =  model.getImpiantiIniziali("Sauze D'Oulx");
		System.out.println("sau:"+sauze.size()+" ses: "+sestriere.size());
		for(int ij =0; ij<sauze.size();ij++) {
			for(int j =0; j<sestriere.size();j++) {
				String ris = model.camminiMinimo(sauze.get(ij), sestriere.get(j));
				System.out.println("---------------------------------\n"+ris);
				
			}
			
		}
		
		
		model.trovaMassimoPercorso(p, 0);
		
		/*
		for(Impianto imp: model.getImpiantiIniziali("Sauze D'Oulx")) {
			System.out.println(""+imp.getNome()+"  "+imp.getLocalita());
		}
		
		
		for(String s: model.getLocatita()) {
			
			System.out.println("\n "+s);
		}
		*/
		
		// System.out.println("-----------------------------\n"+model.stampa());
		//System.out.println(model.stampaPiste());
	}

}
