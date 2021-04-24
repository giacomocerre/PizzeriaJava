
package GUI;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.Map.Entry;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ordinazioni.Ordinazione;
import ordinazioni.OrdinazioneDomicilio;
import pizzeria.Pizzeria;
 
public class Controller {
	// **** COMPONENTI LAYOUT GRAFICO **** //
	// Button per la gestione degli step dell'ordinazione cliente
	public Button step2, step3, step4;
	// Button per la gestione interna dell'ordine
	public Button btn_ordina, back, btn_menu, btn_consegna, btn_ritiro, btn_orario, btn_areaRiservata;
	// Button per la gestione interna dell'area riservata
	public Button btn_visualizza, btn_prossimo, btn_salva, btn_importa, btn_cercaNome, btn_cercaOrario;
	// Button per la gestione delle pizze
	public Button margherita, bufalina, capricciosa, diavola, napoli, rucolina;
	public Button add_margherita, add_bufalina, add_capricciosa, add_diavola, add_napoli, add_rucolina;
	public Button r_margherita, r_bufalina, r_capricciosa, r_diavola, r_napoli, r_rucolina;
	public Text tot_margherita, tot_bufalina, tot_capricciosa, tot_diavola, tot_napoli, tot_rucolina;
	// TextArea per i campi da inserire
	public TextArea txt_nome, txt_cognome, txt_indirizzo, txt_cercaNome;
	public MenuButton menu_cercaOrario;
	// Text per gestire i testi dinamicamente
	public Text totale, lbl_orario, error, esito_ser;
	// AnchorPane che contengono componenti da utilizzare
	public AnchorPane AP_indirizzo, AP_orario, AP_ser;
	public ListView<String> view_listaOrdini, view_dettaglioOrdine;
	
	// **** VARIABILI INTERNE **** //
	// Stringhe finali per il completamento di un ordine;
	private static String nome, cognome, indirizzo;
	LocalTime orario;
	// Lista delle pizze da ordinare
	private static ArrayList<String> tmp_pizze = new ArrayList<String>();
	// variabili di appoggio
	private boolean consegna = false, clicked = false;
	//ordini
	private TreeMap<LocalTime, Ordinazione> ordinazioni = Pizzeria.getOrdinazioni();
	
	public void cambiaScena(ActionEvent event) throws IOException, InterruptedException {
		Node node = (Node) event.getSource() ;
		//primo step iniza ordine --> step2
		if(node == btn_ordina) {
			apriScena(btn_ordina, node);
			//Svuota array pizze
			tmp_pizze.clear();
		}
		//secondo step inserimento informazioni personali --> step3
		if(node == step2) {
			nome = txt_nome.getText();
			cognome = txt_cognome.getText();
			indirizzo = consegna ? txt_indirizzo.getText() : "ritiro in loco";
			if(!nome.isEmpty() && !cognome.isEmpty() && !indirizzo.isEmpty()) {
				apriScena(step2, node);
			}else {
				error.setVisible(true);
			}
		}
		//terzo step scegli pizze --> step4
		if(node == step3) {
			if(tmp_pizze.size() > 0) {
				apriScena(step3, node);
			}else {
				error.setVisible(true);
			}
		}
		//quarto step scegli orario --> main
		if(node == step4) {
			if(orario != null) {
				apriScena(step4, node);
				Ordinazione ordine;
				if(indirizzo == "ritiro in loco"){
					ordine = new Ordinazione(nome, cognome, orario, tmp_pizze);
				}else {
					ordine = new OrdinazioneDomicilio(nome, cognome, indirizzo, orario, tmp_pizze);
				}
				Pizzeria.ordinazioni.put(orario, ordine);
			}else {
				error.setVisible(true);
			}
		}
		//torna alla pagina precedente
		if(node == back) {
			//se il back è verso la scena di oridina pizze pulisci l'array di pizze.
			if(node.getUserData().equals("2_ordine.fxml")) {
				tmp_pizze.clear();
			}
			apriScena(back, node);
		}
		//apre il menu in modalità consultazione
		if(node == btn_menu) {
			apriScena(btn_menu, node);
		}
		
		if(node == btn_areaRiservata) {
			apriScena(btn_areaRiservata, node);
		}
	}

	//metodo che esegue l'aperura di una scena in base al button premuto
	public void apriScena(Button button, Node node) throws IOException, InterruptedException {
		//chiude la scena corrente
		Stage stage = (Stage) button.getScene().getWindow();
		stage.close();
		//prende il nome del file fxml della scena da aprire dall'attributo userData
	    String data = (String) node.getUserData();
		Main.openScene(data, getClass());
	}
	
	//metodo per le impostazioni della consegna a domicilio
	public void setConsegna(ActionEvent event) throws IOException {
		AP_indirizzo.setDisable(false);
		AP_indirizzo.setStyle("-fx-background-color: #fff; -fx-opacity: 1; -fx-background-radius: 20; -fx-border-color: #eee; -fx-border-radius: 20");
		btn_consegna.setStyle("-fx-background-color: #E6F3FF; ");
		btn_ritiro.setStyle("-fx-background-color: #fff; ");
		consegna = true;
	}
	
	//metodo per le impostazioni del ritiro in loco
	public void setRitiro(ActionEvent event) throws IOException {
		AP_indirizzo.setDisable(true);
		AP_indirizzo.setStyle("-fx-background-color: #ccc; -fx-opacity: 0.31; -fx-background-radius: 20; -fx-border-color: #eee; -fx-border-radius: 20");
		btn_consegna.setStyle("-fx-background-color: #fff; ");
		btn_ritiro.setStyle("-fx-background-color: #E6F3FF; ");
		consegna = false;
	}
	
	//metodo per aggiungere una pizza alla lista di quelle ordinate
	public void addPizza(ActionEvent event) throws IOException {
		Node node = (Node) event.getSource();
		String pizza = (String) node.getUserData();
		// controllo che esegue il set text aggiungendo uno se la pizza viene aggiunta
		switch(pizza) {
			case "margherita": tot_margherita.setText((Integer.parseInt(tot_margherita.getText()) + 1) + ""); break;
			case "bufalina": tot_bufalina.setText((Integer.parseInt(tot_bufalina.getText()) + 1) + ""); break;
			case "diavola": tot_diavola.setText((Integer.parseInt(tot_diavola.getText()) + 1) + ""); break;
			case "capricciosa": tot_capricciosa.setText((Integer.parseInt(tot_capricciosa.getText()) + 1) + ""); break;
			case "napoli": tot_napoli.setText((Integer.parseInt(tot_napoli.getText()) + 1) + ""); break;
			case "rucolina": tot_rucolina.setText((Integer.parseInt(tot_rucolina.getText()) + 1) + ""); break;
		}
		tmp_pizze.add(pizza); // aggiunge la pizza all'array di pizze
		totale.setText(tmp_pizze.size()+""); // aggiorna il totale del carrello in base alla lunghezza dell'array di pizze
	}
	
	//metodo per rimuovere una pizza alla lista di quelle ordinate
	public void removePizza(ActionEvent event) throws IOException {
		Node node = (Node) event.getSource();
		String pizza = (String) node.getUserData();
		// controllo che esegue il set text controllando che il tot non vada mai sotto 0. Se tot è maggiore di 0 allora toglie uno se la pizza viene rimossa
		switch(pizza) {
			case "margherita": tot_margherita.setText(Integer.parseInt(tot_margherita.getText()) > 0 ? ((Integer.parseInt(tot_margherita.getText()) - 1) + "") : 0 + ""); break;
			case "bufalina": tot_bufalina.setText(Integer.parseInt(tot_bufalina.getText()) > 0 ? ((Integer.parseInt(tot_bufalina.getText()) - 1) + "") : 0 + ""); break;
			case "diavola": tot_diavola.setText(Integer.parseInt(tot_diavola.getText()) > 0 ? ((Integer.parseInt(tot_diavola.getText()) - 1) + "") : 0 + ""); break;
			case "capricciosa": tot_capricciosa.setText(Integer.parseInt(tot_capricciosa.getText()) > 0 ? ((Integer.parseInt(tot_capricciosa.getText()) - 1) + "") : 0 + ""); break;
			case "napoli": tot_napoli.setText(Integer.parseInt(tot_napoli.getText()) > 0 ? ((Integer.parseInt(tot_napoli.getText()) - 1) + "") : 0 + ""); break;
			case "rucolina":tot_rucolina.setText(Integer.parseInt(tot_rucolina.getText()) > 0 ? ((Integer.parseInt(tot_rucolina.getText()) - 1) + "") : 0 + ""); break;
		}
		// rimuove la pirima pizza nell'array che ha lo stesso nome di quella rimossa dall'utente.
		for(String p: tmp_pizze) {
			if(p.equals(pizza)) {
				tmp_pizze.remove(p);
				break;
			}
		}
		totale.setText(tmp_pizze.size()+"");// aggiorna il totale del carrello in base alla lunghezza dell'array di pizze
	}
	
	// metodo per gestire la scelta dell'utente ad un orario specifico
	public void impostaOrario() {
		// toggle dei button per l'orario
		if(AP_orario.isVisible()) {
			AP_orario.setVisible(false);
		}else {
			AP_orario.setVisible(true);
		}
		for (Node node : AP_orario.getChildren()) { // ciclo sui bottoni dell'orario
			Button nodeH = (Button)node;
			String h = nodeH.getText(); // prendi orario selezionato
			for (Entry<LocalTime, Ordinazione> entry : ordinazioni.entrySet()) {
				checkOrario(h, entry.getKey().toString(), nodeH); //controllo se orario già occupato
			}
			// set text della label con l'orario selezionato
			nodeH.setOnAction(new EventHandler<ActionEvent>() {
	            public void handle(ActionEvent event) {
	                orario = LocalTime.parse(h, DateTimeFormatter.ofPattern("H:mm"));
	            	lbl_orario.setText(h);
	            }
	        });
		}
	}
	
	// metodo che disattiva gli orari non disponibili
	public void checkOrario(String orario, String orarioOrdine, Button btn) {
		if(orario.equals(orarioOrdine)){
			// set stile e disabilitazione del button
			btn.setDisable(true);
			btn.setStyle("-fx-background-color: red;");
		}
	}
	
	// metodo che visualizza tutti gli ordini
	public void visualizzaOrdini(ActionEvent event) throws IOException  {
		view_listaOrdini.getItems().clear(); // pulisce il ListView della lista deglio ordini
		int i = 1;
		clicked = false; // abilita click
		if(!clicked) {
			view_dettaglioOrdine.getItems().clear(); //pulisce list view del dettaglio ordine
			for (Entry<LocalTime, Ordinazione> entry : ordinazioni.entrySet()) {
				// agginge alla list view un figlio (1 - ID - Orario)
				view_listaOrdini.getItems().add(i + " - " + entry.getValue().getID().toString() + " - " + entry.getValue().getOrario().toString()); 
				i++;
			}
			ciclaOrdini(ordinazioni.size() > 0); // gestisce l'evento click su ogni figlio 
			clicked = true; // disabilita click
		}

		
	}
	
	// metodo che visualizza i detagli di un singolo ordine
	public void visualizzaDettaglioOrdine(Entry<LocalTime, Ordinazione> ordine) {
		view_dettaglioOrdine.getItems().clear();// pulisce il ListView della lista deglio ordini
		view_dettaglioOrdine.getItems().add("Orario: " + ordine.getValue().getOrario()); // stampa orario
		view_dettaglioOrdine.getItems().add("Cliente: " + ordine.getValue().getNome() + " " + ordine.getValue().getCognome()); //stampa nome - cognome
		try {
			view_dettaglioOrdine.getItems().add("Indirizzo: " + ((OrdinazioneDomicilio) ordine.getValue()).getIndirizzo()); //stampa indirizzo della consegna
		}catch( ClassCastException e) {
			view_dettaglioOrdine.getItems().add("Indirizzo: ritiro in loco"); // stampa ritiro in loco
		}
		//stampa le pizze ordinate
		view_dettaglioOrdine.getItems().add("Pizze Ordinate: "); 
		for(String pizza : ordine.getValue().getPizze()) {
			view_dettaglioOrdine.getItems().add(" \t> " + pizza);
		}
		view_dettaglioOrdine.getItems().add("Spesa : "  + ordine.getValue().getSpesa() + "€"); // stampa spesa totale dell'ordine
	}
	
	// metodo per visualizzare il prossimo ordine
	public void visualizzaProssimoOrdine(ActionEvent event) throws IOException  {
		view_listaOrdini.getItems().clear(); // pulisce il ListView della lista deglio ordini
		clicked = false; // abilita click
		if(!clicked) {
			view_dettaglioOrdine.getItems().clear(); //pulisce list view del dettaglio ordine
			Entry<LocalTime, Ordinazione> ordine = ordinazioni.firstEntry(); // prendi il primo ordine della lista ordinazioni
			if(ordine != null) {
				// agginge alla list view un figlio (1 - ID - Orario)
				view_listaOrdini.getItems().add(1 + " - " + ordine.getValue().getID().toString() + " - " + ordine.getValue().getOrario().toString());
			}else {
				// agginge alla list view un figlio ("Nessun risultato")
				view_listaOrdini.getItems().add("Nessun risultato");

			}
			// gestisce il click dell'ordine visualizzato nella list view
			view_listaOrdini.setOnMouseClicked(e -> {
				visualizzaDettaglioOrdine(ordine);
		    });
			clicked = true; // disabilita click
		}
	}
	
	// metodo per salvare gli ordini in un file
	public void salvaOrdini(ActionEvent event) throws IOException  {
		AP_ser.setVisible(true);
		AP_ser.setStyle("-fx-background-color: green; -fx-background-radius: 10;");
		esito_ser.setText("Ordini salvati");
		Pizzeria.salvaOrdini(ordinazioni);
	}
	
	// metodo per importare gli ordini da un file
	public void importaOrdini(ActionEvent event) throws IOException  {
//		TreeMap<LocalTime, Ordinazione> ordinazioni = Pizzeria.getOrdinazioni();
		AP_ser.setVisible(true);
		AP_ser.setStyle("-fx-background-color: orange; -fx-background-radius: 10;	");
		esito_ser.setText("Ordini importati");
		Pizzeria.importaOrdini();
	}
	
	// metodo che esegue la ricerca per nome e cognome di un cliente tra le ordinazioni
	public void cercaNome(ActionEvent event) throws IOException  {
		view_listaOrdini.getItems().clear(); // pulisce il ListView della lista deglio ordini	
		String ricerca = txt_cercaNome.getText().toLowerCase(); // testo della ricerca dell'utente
		int i = 1;
		boolean trovato = false;
		for (Entry<LocalTime, Ordinazione> ordine : ordinazioni.entrySet()) {
			//controllo se la ricerca esiste tra gli ordini
			if(ordine.getValue().getNome().toLowerCase().contains(ricerca) || ordine.getValue().getCognome().toLowerCase().contains(ricerca) ) {
				view_listaOrdini.getItems().add(i + " - " + ordine.getValue().getID().toString() + " - " + ordine.getValue().getOrario().toString());
				i++;
				trovato = true;
			}
		}
		ciclaOrdini(trovato);
	
	}
	
	// metodo che esegue la ricerca per orario tra le ordinazioni
	public void cercaOrario(ActionEvent event) throws IOException  {
		view_listaOrdini.getItems().clear(); // pulisce il ListView della lista deglio ordini
		MenuItem item = (MenuItem) event.getSource(); //Item del menu
		String orario = (String) item.getText(); // orario selezionato
		menu_cercaOrario.setText(orario);
		boolean trovato = false;
		for (Entry<LocalTime, Ordinazione> ordine : ordinazioni.entrySet()) {
			if(ordine.getKey().toString().equals(orario)) {
				// agginge alla list view un figlio (1 - ID - Orario)
				view_listaOrdini.getItems().add(1 + " - " + ordine.getValue().getID().toString() + " - " + ordine.getValue().getOrario().toString());
				trovato = true;
			}
		}
		ciclaOrdini(trovato);
	}
	
	// metodo che gestisce i click sugli Item all'interno della ListView per stampare  poi i dettagli di un singolo oridine
	public void ciclaOrdini(boolean exist) {
		// controllo se sono stati trovati risultati
		if(exist) {
			view_listaOrdini.setOnMouseClicked(e -> { // imposta il click sugli items e gestisce quello cliccato
				String [] data = view_listaOrdini.getSelectionModel().getSelectedItem().split(" - "); // split del testo dell'item in loista di stringhe ["1", "ID", "Orario"]
				if(data[0].contains("Nessun risultato")) { // se il valore della prima stringa è "Nessun risultato" pulisi list view dettaglio ordine
					view_dettaglioOrdine.getItems().clear(); // pulisce il ListView della lista dettaglio ordini
				}else {
					for (Entry<LocalTime, Ordinazione> ordine : ordinazioni.entrySet()) {
						if(data[1].equals(ordine.getValue().getID().toString())) { // se la seconda stringa (ID) si trova tra le ordinazioni allora visualizza l'ordine cliccato
							visualizzaDettaglioOrdine(ordine);
						}
					}
				}
		    });
		}else {
			view_listaOrdini.getItems().add("Nessun risultato");
		}
	}
}