package pizzeriaPoggiantiCerretini;

import java.util.Scanner;

public class Pizza {
	Scanner input=new Scanner(System.in);
	
	private String gusto;		//Tipo di pizza da ordinare
	
	//costruttore
	public Pizza() {
		gusto=input.nextLine();
	}
	
		
	//metodo che fornisce il tipo di pizza ordinata dal cliente, sfruttando la proprietà dell'incapsulamento
	public String getGusto() {
		return gusto;
	}
	
	//stampa le informazioni di Pizza
	public void visualizzaPizza() {
		System.out.println("Pizza "+gusto);
	}
	
	public String toString() {
		return "pizza "+ gusto;
	}
	
}
