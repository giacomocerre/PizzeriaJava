package pizzeriaPoggiantiCerretini;

public class OrdinaPizza {
	private String cliente; 	//Nome del cliente che ordina
	private String indirizzo;	//Indirizzo del cliente, utile se consegna a domicilio
	private String gusto;		//Tipo di pizza da ordinare
	
	//costruttore
	public OrdinaPizza(String cliente, String indirizzo, String gusto) {
		this.cliente=cliente;
		this.indirizzo=indirizzo;
		this.gusto=gusto;
	}
	
	//metodo che fornisce il nome del cliente, sfruttando la proprietà dell'incapsulamento
	public String getCliente() {
		return cliente;
	}
	
	//metodo che fornisce l'indirizzo del cliente, sfruttando la proprietà dell'incapsulamento
	public String getIndirizzo() {
		return indirizzo;
	}
	
	//metodo che fornisce il tipo di pizza ordinata dal cliente, sfruttando la proprietà dell'incapsulamento
	public String getGusto() {
		return gusto;
	}
}
