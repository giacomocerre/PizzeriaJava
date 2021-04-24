package ordinazioni;

import java.time.LocalTime;
//importazione librerie e classi
import java.util.ArrayList;
import java.util.Map.Entry;

import pizzeria.Pizzeria;

//Sottoclasse che estende Ordinazione. Inizializza un'ordinazione a domicilio aggiungendo l'indirizzo del cliente e fa overriding del metodo calcolaSpesa
public class OrdinazioneDomicilio extends Ordinazione{
	
	private static final long serialVersionUID = -4799851707759257083L;
	
	private String indirizzo; //Indirizzo di consegna del cliente
	
	//Costruttore che inizializza una nuova ordinazione a domicilio
	public OrdinazioneDomicilio(String nome, String cognome, String indirizzo, int num_pizze) {
		super(nome, cognome, num_pizze); //Richiamo il costruttore della superclasse Ordinazione
		this.indirizzo = indirizzo;
	}
	
	// Costruttore per la GUI di ordinazione da domicilio
	public OrdinazioneDomicilio(String nome, String cognome, String indirizzo, LocalTime orario, ArrayList<String> pizze) {
		super(nome, cognome, orario, pizze); //Richiamo il costruttore della superclasse Ordinazione
		this.indirizzo = indirizzo;
	}

	//Metodo che restituisce l'indirizzo del cliente, sfruttando la proprietà dell'incapsulamento
	public String getIndirizzo() {
		return indirizzo;
	}
	
	//Overriding del metodo presente nella superclasse
	//prende in input la lista delle pizze ordinate dal cliente e calcola il costo totale dell'ordine effettuato, aggiungendo alla fine il costo del servizio a domicilio
	public void calcolaSpesa(ArrayList<String> pizze) {
		double spesa = 0.00;
		for (String pizza : pizze) { 
        	for (Entry<String, Double> entry : Pizzeria.getMenu().entrySet()) {
				if(pizza.toLowerCase().equals(entry.getKey().toLowerCase())) {
					spesa = spesa + entry.getValue();
				}
			}
        }
		//Aggiunta per il servizio a domicilio
		this.spesa = spesa + 2;
	}

}
