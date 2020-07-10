package it.polito.tdp.comprensorio_sciistico.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import it.polito.tdp.comprensorio_sciistico.model.Impianto;
import it.polito.tdp.comprensorio_sciistico.model.Pista;
import it.polito.tdp.comprensorio_sciistico.model.Stazione;

public class ComprensorioDAO {
	
public List<Pista> caricaPiste() {
		
		String sql = "SELECT * FROM piste p ";
		List<Pista> risultato = new ArrayList<>();

		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			ResultSet rs = st.executeQuery() ;
			
			
			
			while( rs.next() ) {
				
				Pista pista = new Pista(rs.getInt("id"), rs.getString("nome"), rs.getString("stazione_monte"),
				rs.getString("stazione_valle"), rs.getString("colore"),rs.getInt("lunghezza"), rs.getString("localita"));
				
				risultato.add(pista);
				
				
			}
			
			conn.close() ;
			
			return risultato;
				
			
			
		} catch (SQLException e) {
			
			e.printStackTrace();
		}
		return null;
		
		
	}



public List<Impianto> caricaImpianti() {
	
	String sql = "SELECT * from impianti ";
	List<Impianto> risultato = new ArrayList<>();

	try {
		Connection conn = DBConnect.getConnection() ;

		PreparedStatement st = conn.prepareStatement(sql) ;
		
		ResultSet rs = st.executeQuery() ;
		
		
		
		while( rs.next() ) {
			boolean iniziale = false;
			if(rs.getInt("iniziale")==1) {
				iniziale = true;
			}
			
			Impianto impianto = new Impianto(rs.getInt("id"), rs.getString("nome"), rs.getString("stazione_monte"),
					rs.getString("stazione_valle"), rs.getString("localita"),rs.getString("tipo"), iniziale,rs.getInt("posti"),rs.getDouble("tempo_risalita"), rs.getDouble("intervallo"), rs.getTime("h_apertura").toLocalTime(), rs.getTime("h_chiusura").toLocalTime());
			
			risultato.add(impianto);
			
			
		}
		
		conn.close() ;
		
		return risultato;
			
		
		
	} catch (SQLException e) {
		
		e.printStackTrace();
	}
	return null;
	
	
}
public Set<String> listaImpiantiDiscesa(Map<String, Impianto> mappaImpianti) {
	
	String sql = "SELECT i.stazione_monte as monte, i.stazione_valle as valle " + 
			"FROM impianti i, stazioni s, stazioni s2 " + 
			"WHERE i.stazione_monte=s.id AND s2.id=i.stazione_valle AND s.quota<s2.quota ";
	Set<String> risultato = new HashSet<>();

	try {
		Connection conn = DBConnect.getConnection() ;

		PreparedStatement st = conn.prepareStatement(sql) ;
		
		ResultSet rs = st.executeQuery() ;
		
		
		
		while( rs.next() ) {
			
			String chiave= ""+ rs.getString("stazione_valle")+"-"+
					rs.getString("stazione_monte");
			if(mappaImpianti.containsKey(chiave)) {
				risultato.add(chiave);
			}
			
		}
		
		conn.close() ;
		
		return risultato;
			
		
		
	} catch (SQLException e) {
		
		e.printStackTrace();
	}
	return null;
	
	
}
	


public void caricaStazioni(Map<String, Stazione> idMap){
	String sql = "SELECT * FROM stazioni s ";
	
	try {
		Connection conn = DBConnect.getConnection() ;

		PreparedStatement st = conn.prepareStatement(sql) ;
		
		ResultSet rs = st.executeQuery() ;
			
		while( rs.next() ) {
			
			Stazione stazione = new Stazione(rs.getString("id"),rs.getInt("quota"));
			idMap.put(rs.getString("id"), stazione);
			
			
		}
		
		conn.close() ;
		
	} catch (SQLException e) {
		
		e.printStackTrace();
	}
		
}
}
