package pizzeria;

//importazione librerie e classi
import java.io.*;
import java.time.LocalTime;
import java.util.ConcurrentModificationException;
import java.util.Map.Entry;
import java.util.TreeMap;

import ordinazioni.Ordinazione;
import ordinazioni.OrdinazioneDomicilio;
import standard_UI.usaServizio_standard;

//classe che gestisce l'organizzazione della Pizzeria. Contiene metodi per l'implementazione delle operazioni richieste e l'accesso in lettura a ordini e menu.
public class Pizzeria implements Serializable{

	private static final long serialVersionUID = 1;
	
	//Struttura dati per memorizzare il menù nella forma <key, value> dove key=nomePizza e value=prezzoPizza
	static TreeMap<String, Double> menu = new TreeMap<String, Double>();
	
	//Struttura dati per memorizzare tutte le ordinazioni effettuate dal cliente nella forma <key, value> dove key=orarioOrdinazione e value=oggetto contenente i dati dell'ordinazione
	public static TreeMap<LocalTime, Ordinazione> ordinazioni = new TreeMap<LocalTime, Ordinazione>();
	
	public static boolean GUI = false;
	
	//Costruttore per l'area pizzaiolo --> overloading
	public Pizzeria(int operazione) {
		//In base all'input numerico dell'utente, si accede a funzionalità diverse
		switch(operazione) {
			case 1: visualizzaOrdinazioni(); break;
			case 2: prossimoOrdine(ordinazioni.firstEntry()); break;
			case 3: cercaOrario(); break;
			case 4: cercaCliente(); break;
			case 5: salvaOrdini(getOrdinazioni()); break;
			case 6: importaOrdini(); break;
		}
	}
	
	//Costruttore per il servizio clienti --> overloading
	public Pizzeria(Ordinazione ordine) {
		addOrdine(ordine);
	}
	
	
	//Metodo per l'aggiunta di una nuova ordinazione effettuata dal cliente al TreeMap delle ordinazioni
	public void addOrdine(Ordinazione ordine) {
		//Inserisco il nuovo ordine nel TreeMamp--> <key=orarioPizza, value=oggettoOrdinazione)
		ordinazioni.put(ordine.getOrario(), ordine);
		
		char riepilogo = usaServizio_standard.setYesNo("Vuoi visualizzare il riepilogo dell'ordine? (SI/NO)").toLowerCase().charAt(0);
    	if(riepilogo == 's') {
    		//Visualizzazione del riepilogo dell'ordine
    		visualizzaOrdine(ordine);
    	}else if(riepilogo == 'n') {
    		return; 
    	}
	}
	
	//Metodo che scorre il TreeMap delle ordinazioni con un ciclo for-each e, per ogni ordine, visualizza il riepilogo
	public void visualizzaOrdinazioni() {
		for (Entry<LocalTime, Ordinazione> entry : ordinazioni.entrySet()) { //entrySet() restituisce una collezione iterabile di tutti gli oggetti contenuti nel TreeMap
			visualizzaOrdine(entry.getValue());
		}
	}
	
	//Metodo che stampa l'ordine di un cliente
	public void visualizzaOrdine(Ordinazione ordine) {
		usaServizio_standard.scrivi("------ ORIDNE ID "+ ordine.getID()+ " || ORE:" + ordine.getOrario() + "-------------------");
		usaServizio_standard.scrivi(" - Pizze Ordinate :");
    	
		//Ciclo l'arraylist contenente le pizze
		for (String pizza : ordine.getPizze()) { 
        	usaServizio_standard.scrivi("\t> "+pizza);
        }
		
		//Gestione eccezione dell'indirizzo di consegna
		try {
			usaServizio_standard.scrivi(" - Indirizzo      : " + ((OrdinazioneDomicilio) ordine).getIndirizzo());
		}catch(ClassCastException e) {
			usaServizio_standard.scrivi(" - Indirizzo      : ritiro in loco");
		}
		
		usaServizio_standard.scrivi(" - Prezzo         : " + ordine.getSpesa()+"€");
        usaServizio_standard.scrivi(" - Cliente        : " + ordine.getNome() + " " + ordine.getCognome());
        usaServizio_standard.scrivi("-------------------------------------------------------");
	}
	
	//Metodo che stampa il prossimo ordine 
	//non è necessario effettuare un controllo sull'orario perchè il TreeMap ordina automaticamente i suoi oggetti in base alla chiave
	public void prossimoOrdine(Entry<LocalTime, Ordinazione> ordine) {
		visualizzaOrdine(ordine.getValue());
	}
	
	//Metodo per la ricerca dell'ordine in base all'orario
	//prende in input un orario e, dopo aver ciclato il TreeMap delle ordinazioni, controlla che la chiave del TreeMap sia uguale all'input
	public void cercaOrario() {
		LocalTime orario = usaServizio_standard.setTime("Inserisci l'orario che vuoi ricercare:");
		for (Entry<LocalTime, Ordinazione> entry : ordinazioni.entrySet()) {
			if(entry.getKey().equals(orario)) {
				visualizzaOrdine(entry.getValue());
			}else {
				usaServizio_standard.scrivi("Non ci sono ordini a questo orario!");
				char ritenta = usaServizio_standard.setYesNo("Vuoi provare con un altro orario? (SI/NO)").toLowerCase().charAt(0);
				if(ritenta == 's') {
				    cercaOrario();
				}else if(ritenta == 'n') {
				    return; 
				}
			}
		}
	}
	
	//Metodo per la ricerca dell'ordine in base al nome del cliente
	//prende in input una stringa e, dopo aver ciclato il TreeMap delle ordinazioni, controlla che la stringa sia contenuta nel nome o cognome del cliente
	public void cercaCliente() {
		String cliente = usaServizio_standard.setStr("Inserisci il nome (o una porzione del nome) del cliente da ricercare:");
		for (Entry<LocalTime, Ordinazione> entry : ordinazioni.entrySet()) {
			if(entry.getValue().getNome().toLowerCase().contains(cliente.toLowerCase()) || entry.getValue().getCognome().toLowerCase().contains(cliente.toLowerCase())) {
				visualizzaOrdine(entry.getValue());
			}else {
				usaServizio_standard.scrivi("Non ci sono ordini a questo nome!");
				char ritenta = usaServizio_standard.setYesNo("Vuoi provare con un altro nome? (SI/NO)").toLowerCase().charAt(0);
		    	if(ritenta == 's') {
		    		cercaCliente();
		    	}else if(ritenta == 'n') {
		    		return; 
		    	}
			}
		}
	}

	//Metodo che effettua la serializzazione del TreeMap contenente la lista di tutti gli ordini
	public static void salvaOrdini(TreeMap<LocalTime, Ordinazione> o) {
		try {
			FileOutputStream f = new FileOutputStream("Lista_Ordinazioni"); //File di output su cui scrivere
			ObjectOutputStream obj = new ObjectOutputStream(f); //Definizione dello stream su cui scrivere oggetti
			obj.writeObject(o); //Scrittura degli ordini nel file
			obj.close(); //Chiusura dello stream
			f.close(); //Chiusura del file di output
			if(!GUI) {
				usaServizio_standard.scrivi("Tutti i tuoi ordini sono stati salvati nel file 'Lista_Ordinazioni'");
			}
		}catch(IOException e) {
			System.out.println(e);
		}
	}
		 
	//Metodo che effettua la deserializzazione del TreeMap contenente la lista di tutti gli ordini
	@SuppressWarnings("unchecked")
	public static void importaOrdini() {
		File file = new File("Lista_Ordinazioni");
		if (file.isFile() && file.canRead()) {
			try {
				FileInputStream f = new FileInputStream(file); //File di input dal quale leggere
				ObjectInputStream obj = new ObjectInputStream(f); //Definizione dello stream su cui leggere oggetti
				ordinazioni = (TreeMap<LocalTime, Ordinazione>) obj.readObject(); //Lettura degli ordini nel file
				obj.close(); //Chiusura dello stream
				f.close();  //Chiusura del file di input
				if(!GUI) {
					usaServizio_standard.scrivi("Lista ordinazioni inserita...:", "Le ordinazioni sono state importate correttamente!" );
				}
				rimuoviOrdine(LocalTime.now());
			}catch(IOException | ClassNotFoundException e) {
				usaServizio_standard.inputError("File non trovato!");
			}catch(ConcurrentModificationException e) {
				return;
			}
		}
	}
	
	//Metodo che genera e inserisce le pizze nel TreeMap del menù
		public static void generaMenu() {
			//<key=nomePizza, value=prezzoPizza)
			menu.put("Margherita", 5.00);
			menu.put("Capricciosa", 7.50);
			menu.put("Diavola", 6.50);
			menu.put("Bufalina", 7.00);
			menu.put("Rucolina", 6.50);
			menu.put("Napoli", 5.00);
			if(!GUI) {
				visualizzaMenu();
			}
		}

	//Metodo che stampa il menu
	public static void visualizzaMenu() {
		usaServizio_standard.scrivi("\n***************MENU**************","**");
		int i = 0;
		//ciclo for-each per scandire gli elementi nel TreeMap del menù
		for (Entry<String, Double> entry : getMenu().entrySet()) {
			i++;
			//stampa nella forma < 1 - nomePizza €prezzoPizza>
			System.out.println("**    "+ i + " - " + entry.getKey() + " €" + entry.getValue());
		}
		usaServizio_standard.scrivi("**","*********************************\n");
	}
	 
	//Metodo per la rimozione degli ordini precedenti all'orario attuale
	public static void rimuoviOrdine(LocalTime now) {
		//Ciclio for-each che scandisce il TreeMap degli ordini
		for (Entry<LocalTime, Ordinazione> entry : ordinazioni.entrySet()) {
			if(entry.getKey().isBefore(now)){
				//Se l'orario dell'ordine è precedente all'orario attuale, l'ordine viene eliminato dal TreeMap
				ordinazioni.remove(entry.getKey());
			}
		}
	}
	
	//Metodo che fornisce il menu, sfruttando la proprietà dell'incapsulamento
	public static TreeMap<String, Double> getMenu() {
		return menu;
	}
	
	//Metodo che fornisce le ordinazioni, sfruttando la proprietà dell'incapsulamento
	public static TreeMap<LocalTime, Ordinazione> getOrdinazioni(){
		return ordinazioni;
	}


}
