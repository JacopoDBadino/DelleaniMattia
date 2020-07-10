package it.polito.tdp.comprensorio_sciistico.db;

import java.util.List;

import it.polito.tdp.comprensorio_sciistico.model.Impianto;
import it.polito.tdp.comprensorio_sciistico.model.Pista;

public class TestDAO {

	public static void main(String[] args) {
		
		
		ComprensorioDAO dao = new ComprensorioDAO();
		
		List<Pista> piste = dao.caricaPiste();
		
		System.out.println("NUMERO PISTE: "+ piste.size());
		List<Impianto> impianti = dao.caricaImpianti();
		for(Impianto imp: impianti) {
			
			if(imp.isIniziale()==true) {
				System.out.println(imp.toString());
			}
			
		}
		
	}

}
