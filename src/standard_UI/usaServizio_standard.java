package standard_UI;

//importazioni librerie e classi
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.InputMismatchException;
import java.util.Scanner;

import ordinazioni.Ordinazione;
import ordinazioni.OrdinazioneDomicilio;
import pizzeria.Pizzeria;

//classe contenente il main dell'interfaccia testuale con il menu e i metodi per la gestione degli input
public class usaServizio_standard{
	
	static Scanner input = new Scanner(System.in);
	private static Boolean isRunning;	//variabile booleana che monitora lo stato del thread
	private static Thread myThread;		//Thread che, ogni 10 min, si attiva per rimuovere gli ordini già effettuati 
	private static boolean firstExe = true;

	public static void main(String[] args){		
		isRunning = true;
		myThread = new Thread(rimuoviOrdine);
		myThread.start();
		
		//Se il programma viene avviato per la prima volta, si importano eventuali ordini effettuati in precedenza e salvati su file per non avere sovrapposizioni di orario
		if(firstExe) {
			Pizzeria.importaOrdini();
			firstExe = false;
		}

		scrivi( "\n-----------------------------------\r",
				"| BENVENUT* NELLA NOSTRA PIZZERIA |\r",
				"-----------------------------------\n");
		
		//PRIMO STEP: l'utente si autentica. Diramazione in ruolo Cliente / ruolo Pizzaiolo
		boolean ruoloValido;
		int ruolo;
		do {
			ruoloValido = true;
			ruolo = setMenu("1 - Servizio clienti\r", "2 - Area Pizzaiolo");
			
			if(ruolo == 1) {
				//AREA CLIENTE
				scrivi("--------------------------------------------", "Benvenut*, consulta il menù:");
				
				Pizzeria.generaMenu();	//Stampo il menù per farlo consultare al cliente
					
				//Avvio procedura ordinazione
				scrivi("\nProcediamo con la tua ordinazione..."); 	//Nome cliente in input
				String nome = setStr("\nInserisci il tuo nome:");	//Cognome cliente in input
				String cognome = setStr("Inserisci il tuo cognome:");
				int consegna = setMenu("1 - Servizio a domicilio \r","2 - Ritiro in loco");	//Variabile contenente l'intero di riferimento alla scelta della tipologia di consegna
				String indirizzo;	//Indirizzo del cliente
				Ordinazione ordine;	//Oggetto contenente l'ordine del cliente
					
				//Diramazione Servizio a domicilio / Ritiro in loco
				if(consegna == 1) {
					//SERVIZIO A DOMICILIO
					indirizzo = setStr("Inserisci l'indirizzo di consegna:");	//Indirizzo del cliente in input
					int num_pizze = setNum("Quante pizze vuoi ordinare?");		//Numero delle pizze che il cliente vuole ordinare in input
						
					//Creazioe di un'ordinazione con servizio a domicilio
					ordine = new OrdinazioneDomicilio(nome, cognome, indirizzo, num_pizze);
					new Pizzeria(ordine);
						
					comeBack();	//Invocazione metodo per tornare al menù iniziale
				}else{
					//RITIRO IN LOCO
					indirizzo = "Ritiro in loco";	//Attribuzione di un valore fisso all'indirizzo
					int num_pizze = setNum("Quante pizze vuoi ordinare?");	//Numero delle pizze che il cliente vuole ordinare in input
						
					//Creazione di un'ordinazione standard
					ordine = new Ordinazione(nome, cognome, num_pizze);
					new Pizzeria(ordine);
						
					comeBack();	//Invocazione metodo per tornare al menù iniziale
				}
				
			}else {
				//AREA PIZZAIOLO
				scrivi("Benvent* in cucina...");
					
				//Il pizzaiolo sceglie in input il servizio al quale accedere fra quelli messi a disposizione
				int scelta = setMenu(
						"1 - Visualizza tutti gli ordini da preparare\r",
						"2 - Visualizza il prossimo ordine da preparare\r",
						"3 - Visualizza gli ordini previsti a un certo orario\r",
						"4 - Ricerca un ordine in base al nome cliente\r",
						"5 - Salva le ordinazini\r",
						"6 - Importa ordinazioni esterne\r");
					
				new Pizzeria(scelta);
				comeBack();	//Invocazione metodo per tornare al menù iniziale
			}
		}while(!ruoloValido);
	}
	
	//Metodo vargas che consente all'utente di inserire in input una stringa, gestendo direttamente eventuali errori. Permette eventualmente di stampare del testo prima dell'input.
	public static String setStr(String... str) {
		String s = null;
		while(true) {
			scrivi(str);	//Inserzione di testo prima dell'input utente
			s = input.nextLine();	//Input di tipo stringa da parte dell'utente
			if(!s.matches("^\\d+(\\.\\d+)?")) {
				break;
			}else {
				//Gestione dell'errore
				typeError("Si prega di inserire un valore testuale!");
				scrivi("\nRiprova: \n");
			}
		}
		return s;
	}
	
	//Metodo vargas che consente all'utente di inserire in input un intero, gestendo direttamente le eccezioni. Permette eventualmente di stampare del testo prima dell'input.
	 public static int setNum(String... str) {
		 int n = 0;
		 while(true) {
			 scrivi(str); //Inserzione di testo prima dell'input utente
			 //Gestione eccezioni
			 try {
				 n = input.nextInt(); //Input di tipo int da parte dell'utente
				 input.nextLine();  //Consumo la stringa di testo
				 if(n<=0) {
					 typeError("Si prega di inserire un valore maggiore di 0!");
				     scrivi("\nRiprova: \n");
			     }else{
			    	break;
			     }
			   }catch(InputMismatchException | NumberFormatException e) {
				    typeError("Si prega di inserire un valore numerico!");
				    scrivi("\nRiprova: \n");
				    input.nextLine();
			   }
		  }
		  return n;
	}
	
	//Metodo vargas che gestisce tutte le scelte dell'utente effettuate con input numerici, gestendo direttamente eventuali errori. Permette eventualmente di stampare del testo prima dell'input.
	public static int setMenu(String... str) {
		int scelta = 0;
		while(true) {
			scrivi("Scegli:\r");
			scrivi(str);	//Lista delle scelte alle quali può accedere l'utente, passata come parametro del metodo
			scelta = setNum();	//Invocazione di metodo per inserire un input numerico
			
			//Gestione dell'errore: se l'utente inserisce un intero non presente nella lista
			if(scelta > str.length) {
				typeError("La scelta effettuate non è presente nel menu:");
				scrivi("Riprova:");
			}else {
				break;
			}
		}
		return scelta;
	}
	
	//Metodo vargas che gestisce tutte le scelte dell'utente effettuate con input testuali di tipo SI/NO, gestendo direttamente eventuali errori. Permette eventualmente di stampare del testo prima dell'input.
	public static String setYesNo(String...str) {
		String scelta = null;
		boolean sn = true;
		while(sn) {
			scrivi(str);
			scelta = input.nextLine().toLowerCase();	//L'utente inserisce in input la sua scelta in forma testuale
			switch(scelta) {
				case "si":
				case "s":
					sn = false;
					break;
				case "no":
				case "n":
					sn = false;
					break;
				
				//Gestione dell'errore	
				default: 
					typeError("Si prega di scelgiere tra i casi SI e NO!");
					scrivi("Riprova:");
			}
		}
		return scelta;
	}
	
	//Metodo vargas che gestisce tutte le inserzioni di orari in input, gestendo direttamente eventuali eccezioni. Permette eventualmente di stampare del testo prima dell'input.
	public static LocalTime setTime(String... str) {
		LocalTime time;	//Oggetto contenente l'orario
		while(true) {
			scrivi(str);
			
			//Gestione eccezioni
			try {
				time = LocalTime.parse(input.nextLine(), DateTimeFormatter.ofPattern("H:mm"));	//Inserzione dell'orario in input con formattazione dell'ora
				break;
			}catch(DateTimeParseException e) {
				typeError("L'orario inserito non è corretto. Deve rispettare la forma \"ore:minuti\" (Esempio: 21:20).");
				scrivi("Riprova:");
			}
		}
		
		return time;
	}
	
	//Metodo vargas per stampare una o più stringhe di comunicazione, senza richiamare ogni volta Systemout.println
	public static void scrivi(String... string) {
		for(int i = 0; i < string.length; i++ ) {
			System.out.println(string[i]);
		}
	}
	
	//Metodo vargas per stampare una comunicazione di avviso di errore
	public static void inputError(String... string) {
		scrivi("\n>>   ATTENZIONE:"); 
		for(int i = 0; i < string.length; i++ ) {
			scrivi("     - " + string[i]);
		}
		scrivi("\n");
	}
	
	//Metodo per stampare informazioni, che vengono passate come parametro, circa l'errore che si è verificato
	public static void typeError(String string) {
		scrivi("\n!!   ERRORE DI TIPO:\n     - " + string);
	}
	
	//Metodo per stampare consigli, che vengono passati come parametro, per rimediare determinati errori
	public static void consiglio(String... string) {
		scrivi("\n??   CONSIGLIO:"); 
		for(int i = 0; i < string.length; i++ ) {
			scrivi("     - " + string[i]);
		}
		scrivi("\n");
	}
	
	//Metodo che consente di tornare al menù iniziale oppure di terminare l'esecuzione del programma
	public static void comeBack() {
		char back = usaServizio_standard.setYesNo("Vuoi tornare al menu iniziale? (SI/NO)").toLowerCase().charAt(0);
    	if(back == 's') {
    		main(null);
    	}else if(back == 'n') {
    		//Interruzione del thread
    		isRunning = false;
    		scrivi("Grazie per averci scelto, a presto!"); 
       		System.exit(0); // chide l'interfaccia forzando l'arresto anche del Thread.
       	}
	}
	
	//Oggetto che implementa il metodo run()
	static Runnable rimuoviOrdine = new Runnable() {
		//Metodo che attiva il thread eseguendo la rimozione dell'ordine ogni 10min
		public void run() {
			while (isRunning) {
				try {
					Thread.sleep(10*60*1000);
		            Pizzeria.rimuoviOrdine(LocalTime.now());
		        } catch (InterruptedException e) {
		            isRunning = false;
		        }
			}
		}
	};
}
