package it.polito.tdp.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import it.polito.tdp.dao.EsameDAO;

public class Model {
	//esami letti dal database
	private List<Esame> esami;
	
	//variabili per la gestione della ricorsione
	private List<Esame> best;
	private double media_best;
	
	public Model() {
		//lettura dei dati dal database
		EsameDAO dao = new EsameDAO();
		this.esami = dao.getTuttiEsami();
	}
	

	/**
	 * Trova la combinazione di corsi avente la somma dei crediti richiesta che abbia 
	 * la media dei voti massima 
	 * @param numeroCrediti
	 * @return l'elenco dei corsi ottimale, oppure {@code null} se non esiste alcuna 
	 * combinazione di corsi che assomma al numero esatto di crediti.
	 */
	
	public List<Esame> calcolaSottoinsiemeEsami(int numeroCrediti) {
		
		best = null;
		media_best = 0.0;
		Set<Esame> parziale = new HashSet<>();
		cerca(parziale, 0, numeroCrediti);
		
		return best;
	}
	
	private void cerca(Set<Esame> parziale, int L, int m) {
		
		//siamo in un caso terminale
		int crediti = sommacrediti(parziale);
		if(crediti>m)
			return;
		if(crediti==m) {
			double media = calcolaMedia(parziale);
			
			if(media>media_best) {
				//ho trovato una soluzione migliore della media
				best = new ArrayList<Esame>(parziale);
				media_best = media;
				return;
			} else {
				return;
			}
		}
		
		//sicuramente crediti<m
		//devo vedere se ho ancora degli esami che 
		//si potrebbero aggiungere
		if(L==esami.size())
			return;
		
		//generiamo sotto-problemi
		//esami[L] è da aggiungere o no?
		//provo a non aggiungerlo 
		cerca(parziale, L+1, m);
		//provo ad aggiumgerlo 
		parziale.add(esami.get(L));
		cerca(parziale, L+1, m);
		parziale.remove(esami.get(L));
	}


	private double calcolaMedia(Set<Esame> parziale) {
		double media = 0.0;
		int crediti = 0;
		for(Esame e: parziale) {
			media += e.getVoto()*e.getCrediti();
			crediti += e.getCrediti();
		}
		return media/crediti;
	}


	private int sommacrediti(Set<Esame> parziale) {
		int somma = 0;
		for(Esame e: parziale) {
			somma += e.getCrediti();
		}
		return somma;
	}

}
