package GUI;

import java.io.IOException;
import java.time.LocalTime;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import pizzeria.Pizzeria;

public class Main extends Application{
	
	private static Boolean isRunning;	//variabile booleana che monitora lo stato del thread
	private static Thread myThread;		//Thread che, ogni 10 min, si attiva per rimuovere gli ordini già effettuati 
	private static boolean firstExe = true;

	 public static void main(String[] args) {
	        launch(Main.class); // lancia l'applicazione GUI
	 }
	 
	 public void start(Stage stage) throws Exception {
			Pizzeria.GUI = true; // setta GUI a true
			Pizzeria.generaMenu(); // genera il menu della pizzeria
			// Thread
			isRunning = true;
			myThread = new Thread(rimuoviOrdine);
			myThread.start();
			// importa gli ordini al lancio dell'applicazione
			if(firstExe) {
				Pizzeria.importaOrdini();
				firstExe = false;
			}
			//apre la scena iniziale
	    	openScene("main.fxml", getClass());
	 }
	 
	//metodo che apre una nuova scena in base alla stringa fxml
	public static void openScene(String fxml, Class<?> clss) throws IOException {
		FXMLLoader fxmlSource = new FXMLLoader(clss.getResource(fxml)); // file FXML relativo alla scena da aprire
	    Parent loader = (Parent) fxmlSource.load(); // carica il file FXML
	    Stage stage = new Stage(); // crea un nuovo stage da visualizzare
        Scene scene = new Scene(loader, 600, 800); // grandezza width e height della scena
        stage.setResizable(false); // blocca il resize dell'applicazione GUI
	    stage.setTitle("Gestionale Pizzeria"); // nome dell'app
	    stage.setScene(scene);  //imposta la scena
	    stage.show(); // mostra la scena
	    // Intercetta la chiusura dell'applicazione e forza la chiusura di tutti i Thread
	    stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
	        public void handle(WindowEvent t) {
	            Platform.exit(); // esce dall'applicazione GUI
	            System.exit(0); // forza la chiusura dei Thread
	        }
	    });
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
	