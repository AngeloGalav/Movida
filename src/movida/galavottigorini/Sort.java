package movida.galavottigorini;

import java.util.Comparator;

import movida.commons.Movie;
import movida.commons.Person;
import movida.galavottigorini.Map.Elem;
import movida.galavottigorini.MovidaCore.MovidaDebug;



public class Sort<T>{
	
	public T struttura;
	
	private static boolean isReversed = false;
	
	
	public void setReversed(boolean condition) 
	{
		isReversed = condition;
	}
	
	/**	Algoritmo di ordinamento che dato un array e un Comparator adeguato lo ordina
	 * 	non ritorna nulla perchè l'array viene ordinato in loco
	 * 	@param A array da ordinare
	 * 	@param comp comparatore da usare
	 * */
	public void insertionSort(T[] A, Comparator<T> comp) 
	{
		for (int i = 1; i < A.length; i++) 
		{
			T temp = A[i];
			int j = i - 1;
			
			/* partendo dalla fine scorro tutto il mio array e sposto tutti i valori di uno, così facendo quando 
			 * arriverò al primo valore minore di temp  in posizione j avrò già A[j+1] libero per temp
			*/
			while ( (j >= 0) && (comp.compare(A[j], temp) > 0) ) 
			{
				A[j + 1] = A[j];
				j--;
			}
			
			A[j + 1] = temp;
		}
	}
	
	
	/**	Algoritmo di ordinamento che dato un array e un Comparator adeguato lo ordina
	 * 	non ritorna nulla perchè l'array viene ordinato in loco
	 * 	E' la funzione chiamata dal programma che a sua volta richiama la prima quickSortRec() sull'intero vettore dato in input
	 * 	@param A array da ordinare
	 *  @param comp comparatore da usare
	 * */
	public void quickSort(T[] A, Comparator<T> comp) 	//oppure "public void QuickSort(Comparable <T>[] A)" ??
	{	 
		quickSortRec(A, 0, A.length - 1, comp);
	}
	
	/**	Funzione che fa il vero e proprio ordinamento, che ordina i due sottoarray (separati dal pivot) 
	 * 	con rispettivamente i numeri minori e maggiori del pivot
	 * 	@param A array da ordinare
	 *  @param i indice di inizio sottoarray
	 *  @param f indice di fine sottoarray
	 *  @param comp comparatore da usare
	 *  @return up indice del pivot scelto in modo randomico
	 * */
	private int partition(T A[], Integer i, Integer f, Comparator<T> comp) 
	{ 	
		int low = i, up = f + 1;
		T temp; //creo variabile temporanea per scambiare gli elementi dell'array
		
		//scegliamo il pivot in modo randomico, che è più efficiente
		int pos = i + (int) Math.floor((f-i+1) * Math.random()); 
		T x = A[pos];    
		A[pos] = A[i]; 
		A[i] = x; 
		
		
		while (true)
		{	
			//finchè non troverò un elemento dell'array prima del pivot maggiore di esso lo scorrerò normalmente 
			do low++; 
			while (low <= f && comp.compare(A[low], x) <= 0); 
			
			//finchè non troverò un elemento dell'array dopo il pivot minore di esso lo scorrerò normalmente 
			do up--; 
			while (comp.compare(A[up], x) > 0 ); 
			
			//quando sono "bloccato" scambio i miei low & up e ricomincio
			if (low < up) 
			{ 	temp = A[low]; 
				A[low] = A[up]; 
				A[up] = temp;
			} 
			else break;
		} 
		
		temp = A[i]; 
		A[i] = A[up]; 
		A[up] = temp; 
		return up; 
	}


	/**	Funzione che chiama ricorsivamente se stessa sulle due parti dell'array divise dal pivot
	 * 	@param A array da ordinare
	 *  @param i indice di inizio sottoarray
	 *  @param f indice di fine sottoarray
	 *  @param comp comparatore da usare
	 * */
	private void quickSortRec(T[] A, Integer i, Integer f, Comparator<T> comp) 	//oppure "public void quickSortRec(Comparable <T>[] A, Integer i, Integer f) " ??
	{	if (i >= f) { return; }
		Integer m = partition(A, i, f, comp); 
		quickSortRec(A, i, m - 1, comp); 
		quickSortRec(A, m + 1, f, comp); 
	}

	
	/**	Comparatore di Elem che ordina in base alla chiave degli Elem passati in input
	 * */
	public static class sortByKey implements Comparator<Elem>{
		
		@Override
		public int compare(Elem x1, Elem x2)
		{
			if (isReversed) {
				return -1 * ( x1.getKey()).compareTo(x2.getKey() );
			} else {
				return ( x1.getKey()).compareTo(x2.getKey() );
			}
		}
		
	}
	
	/**	Comparatore di Elem che ordina in base al nome dei film contenuti nel loro campo "Value" 
	 * */
	public static class sortByMovieName implements Comparator<Elem>{
		
		@Override
		public int compare(Elem x1, Elem x2)
		{
			Movie x_t = (Movie) x1.getValue(); 
			Movie x_t2 = (Movie) x2.getValue(); 
			if (isReversed) {
				return -1 * ( x_t.getTitle().compareTo(x_t2.getTitle()) );
			} else {
				return ( x_t.getTitle().compareTo(x_t2.getTitle()) );
			}
		}
		
	}
	
	/**	Comparatore di Elem che ordina in base all' anno dei film contenuti nel loro campo "Value" 
	 * */
	public static class sortByMovieYear implements Comparator<Elem>{
		
		@Override
		public int compare(Elem x1, Elem x2)
		{
			Movie x_t = (Movie) x1.getValue(); 
			Movie x_t2 = (Movie) x2.getValue(); 
			if (isReversed) {
				return -1 * ( x_t.getYear().compareTo(x_t2.getYear()) );
			} else {
				return ( x_t.getYear().compareTo(x_t2.getYear()) );
			}
		}
		
	}
	
	/**	Comparatore di Elem che ordina in base all voto dei film contenuti nel loro campo "Value" 
	 * */
	public static class sortByMovieVotes implements Comparator<Elem>{
		
		@Override
		public int compare(Elem x1, Elem x2)
		{
			Movie x_t = (Movie) x1.getValue(); 
			Movie x_t2 = (Movie) x2.getValue(); 
			if (isReversed) {
				return -1 * ( x_t.getVotes().compareTo(x_t2.getVotes()) );
			} else {
				return ( x_t.getVotes().compareTo(x_t2.getVotes()) );
			}
		}
		
	}

	
	public static class sortByDebugName implements Comparator<Person>{
		
		@Override
		public int compare(Person x1, Person x2)
		{
			if (isReversed) {
				return -1 * ( x1.getName().compareTo(x2.getName()) );
			} else {
				return ( x1.getName().compareTo(x2.getName()) );
			}
		}
		
	}
	
}
