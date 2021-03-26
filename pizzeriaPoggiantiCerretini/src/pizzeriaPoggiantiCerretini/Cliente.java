package pizzeriaPoggiantiCerretini;

public class Cliente {
	private String nome; 		//nome e cognome del cliente
	private String indirizzo; 	//indirizzo del cliente per il servizio a domicilio
	
	//costruttore
	public Cliente(String nome, String indirizzo) {
		this.nome=nome;
		this.indirizzo=indirizzo;
	}
	
	//metodo che fornisce il nome del cliente, sfruttando la proprietà dell'incapsulamento
	public String getNome() {
		return nome;
	}
	
	//metodo che fornisce l'indirizzo del cliente, sfruttando la proprietà dell'incapsulamento
	public String getIndirizzo() {
		return indirizzo;
	}
	
	//stampa le informazioni di Cliente
		public void visualizzaCliente() {
			System.out.println("Nome: "+nome);
			System.out.println("Indirizzo: "+indirizzo);
			System.out.println();
		}
}
