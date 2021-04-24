package ordinazioni;

//Importazione librerie e classi
import java.io.Serializable;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map.Entry;
import java.util.ArrayList;
import java.util.Random;

import pizzeria.Pizzeria;
import standard_UI.usaServizio_standard;

//Superclasse estesa da OrdinazioneDomicilio. Inizializza un'ordinazione mettendo a disposizione metodi per il settaggio di tutti i campi previsti dall'ordinazione
public class Ordinazione implements Serializable {
	
	private static final long serialVersionUID = 1;
	
	private String id;	//identificatore dell'ordine
	private String nome;	//nome del cliente
	private String cognome;	//cognome del cliente
	private LocalTime orario;	//orario dell'ordine
	private ArrayList<String> pizze = new ArrayList<String>();	//struttura dati per la raccolta delle pizze ordinate dal singolo cliente
	protected double spesa;	//costo totale dell'ordine del cliente
	
	//Costruttore che inizializza una nuova ordinazione
	public Ordinazione(String nome, String cognome, int num_pizze) {
		this.id = String.format("%06d", new Random().nextInt(99999)); //Genera un numero di 6 cifre e lo converte in stringa
		this.nome = nome;
		this.cognome = cognome;
		setPizze(num_pizze);
		setOrario();
	}
	
	// Costruttore per la GUI
	public Ordinazione(String nome, String cognome, LocalTime orario, ArrayList<String> pizze) {
		this.id = String.format("%06d", new Random().nextInt(99999)); //Genera un numero di 6 cifre e lo converte in stringa
		this.nome = nome;
		this.cognome = cognome;
		this.pizze = pizze;
		this.orario = orario;
		calcolaSpesa(pizze);
	}

	//Metodo che prende in input il numero di pizze che il cliente vuole ordinare, le setta e invoca il calcolo della spesa
	private void setPizze(int n) {	
		int i;
		for( i = n; i>0; i--) {
			pizzaInMenu(i);		
		}
		calcolaSpesa(pizze);
	}
	
	//Metodo che controlla che la pizza scelta dal cliente sia presente nel menu (TreeMap)
	private void pizzaInMenu (int i) {
		String pizza = usaServizio_standard.setStr("Scegli la pizza che vuoi! Puoi ordinare ancora: " + (i) + ((i == 1) ? " pizza" : " pizze")); //Pizza scelta dal cliente in input
		boolean trovata = false;
		
		//Ciclo for-each che scorre il menu
		for (Entry<String, Double> entry : Pizzeria.getMenu().entrySet()) {
			if(pizza.toLowerCase().equals(entry.getKey().toLowerCase())){
				//Se la pizza richiesta è presente nel menu, aggiungila all'ArrayList delle pizze ordinate dal singolo cliente
				this.pizze.add(pizza);
				trovata = true;
			}
		}	
		if(!trovata) {
			//Se il cliente sceglie una pizza che non è presente in menu, stampa di nuovo il menu e dà la possibilità di inserire una nuova pizza in input
			usaServizio_standard.scrivi("La pizza non è presente nel menù!\rConsultalo di nuovo:");
			Pizzeria.visualizzaMenu();
			usaServizio_standard.scrivi("Riprova:");
			pizzaInMenu(i);
		}
	}
	
	//Metodo che prende in input la lista delle pizze ordinate dal cliente e calcola il costo totale dell'ordine effettuato
	public void calcolaSpesa(ArrayList<String> pizze) {
		for (String pizza : pizze) { 
        	for (Entry<String, Double> entry : Pizzeria.getMenu().entrySet()) {
				if(pizza.toLowerCase().equals(entry.getKey().toLowerCase())) {
					this.spesa = spesa + entry.getValue();
				}
			}
        }
	}

	//Metodo che consente all'utente di inserire un orario sotto forma di stringa
	public void setOrario() {
		try {
			//L'utente inserisce in input una stringa con una formattazione precisa, che viene parsata a istanza di LocalTime
			orario = LocalTime.parse(usaServizio_standard.setStr("Per che orario vuoi ordinare?"), DateTimeFormatter.ofPattern("H:mm"));
			checkOrario(orario); //controllo dell'orario inserito
		}catch(DateTimeParseException e) {
			//Gestione dell'eccezione
			usaServizio_standard.typeError("L'orario inserito non è corretto deve rispettare la forma \"ore:minuti\" (Esempio: 21:20).");
			setOrario();
		}
		
	}
	
	//Metodo che effettua un controllo di validità sull'orario inserito dall'utente
	public void checkOrario(LocalTime orario) {
		//Per garantire ordini a intervalli di 10min, costringo l'utente a inserire un orario i cui minuti siano multipli di 10
	    if(!(orario.getMinute() % 10 == 0)) {
	    	//Se l'orario non è valido, consiglio al cliente un possibile orario valido
	    	consigliaOrario(orario, orario.getMinute());
	    	setOrario();
	    }else{
	    for (Entry<LocalTime, Ordinazione> entry : Pizzeria.getOrdinazioni().entrySet()) { //Ciclo for-each che scorre la lista degli ordini già presenti
	    	if(entry.getKey().equals(orario)) {
	    		//Se c'è già un ordine a quell'orario impedisco l'inserzione e chiedo un altro orario
	    		usaServizio_standard.inputError("Non c'è posto... prova a un altro orario!");
	    		consigliaOrario(orario, orario.getMinute());
	    		setOrario();
	    	}	
	      }
	    }
	  }
	
	//Metodo che consiglia un orario alternativo al cliente che inserisce un orario i cui minuti non sono multipli di 10
	public void consigliaOrario(LocalTime orario, int minuti) {
		if (minuti % 10 != 0) {
			//Se i minuti non sono multipli di dieci, suggerisco il multiplo di 10 immediatamente precedente e successivo
	        LocalTime beforeTime = orario.withMinute(minuti / 10 * 10);
	        LocalTime afterTime = beforeTime.plusMinutes(10);
	        usaServizio_standard.consiglio("L'orario inserito non è valido", "Prova con " + beforeTime + " oppure " + afterTime);
	    }
	}
	
	//Metodo che fornisce il nome del cliente, sfruttando la proprietà dell'incapsulamento
	public String getNome() {
		return nome;
	}
	
	//Metodo che fornisce il cognome del cliente, sfruttando la proprietà dell'incapsulamento
	public String getCognome() {
		return cognome;
	}
	
	//Metodo che fornisce la lista delle pizze settate dal cliente, sfruttando la proprietà dell'incapsulamento
	public ArrayList<String> getPizze() {
		return pizze;
	}
	
	//Metodo che fornisce l'orario settato dal cliente, sfruttando la proprietà dell'incapsulamento
	public LocalTime getOrario() {
		return orario;
	}
	
	//Metodo che fornisce l'ID dell'ordine, sfruttando la proprietà dell'incapsulamento
	public String getID() {
		return id;
	}
	
	//Metodo che fornisce la spesa del cliente, sfruttando la proprietà dell'incapsulamento
	public double getSpesa() {
		return spesa;
	}
}
