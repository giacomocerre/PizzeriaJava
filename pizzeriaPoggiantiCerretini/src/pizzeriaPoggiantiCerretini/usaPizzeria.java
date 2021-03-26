package pizzeriaPoggiantiCerretini;

import java.util.Scanner;
import java.util.Vector;

public class usaPizzeria {

	public static void main(String[] args) {
		Scanner input=new Scanner(System.in);
		
		int numPizzeOrdinate;	//Numero totale delle pizze che il singolo cliente vuole ordinare
		int pizzaCorrente=1;
		
		Vector<Pizza> Pizze= new Vector<Pizza>(); //vettore contenente tutte le pizze ordinate
		
		
		//Effettua nuovo ordine
		System.out.println("Quante pizze vuoi ordinare?");
		numPizzeOrdinate=input.nextInt();
		System.out.println("Scegli i gusti");
		
		int i=1;	//contatore delle pizze
		while(i<=numPizzeOrdinate) {
			System.out.println("Pizza "+i+":");
			Pizza p1=new Pizza();
			Pizze.add(p1);
			i++;
		}
		
		
		Cliente c1=new Cliente("Mario", "Via Grande 12");
		
		System.out.println("Riepilogo ordine:");
		System.out.println("-----------------");
		c1.visualizzaCliente();
		
		for(Pizza x:Pizze) {
			System.out.print("Pizza "+pizzaCorrente+") ");
			System.out.println(x);
			pizzaCorrente++;
		}

		input.close();
	}

}
