package movida.galavottigorini;

import java.util.Comparator;

import movida.commons.Movie;
import movida.galavottigorini.Map.Elem;
import movida.galavottigorini.MovidaCore.MovidaDebug;

/*
 * TODO: implement this algorithms:
 * 
	Algoritmi di ordinamento: InsertionSort,  QuickSort
	Implementazioni dizionario: ListaCollegataNonOrdinata,  HashIndirizzamentoAperto
*/

//TODO: Improve quicksort
//TODO: Allow sort by person, by year etc...


public class Sort<T>{
	
	public T struttura;
	
	private static boolean isReversed = false;
	
	
	public static void setReversed(boolean condition) 
	{
		isReversed = condition;
	}
	
	public void insertionSort(T[] A, Comparator<T> comp) {

		for (int i = 1; i < A.length; i++) 
		{
			T temp = A[i];
			int j = i-1;
			
			/* partendo dalla fine scorro tutto il mio array e sposto tutti i valori di uno, così facendo quando 
			 * arriverò al primo valore minore di temp  in posizione j avrò già A[j+1] libero per temp
			*/
			while ( (j >= 0) && (comp.compare(A[j], temp) > 0) ) 
			{
				A[j+1] = A[j];
				j--;
			}
			
			A[j+1] = temp;
			
		}
	}
	
	
	public void quickSort(T[] A, Comparator<T> comp) 	//oppure "public void QuickSort(Comparable <T>[] A)" ??
	{	 
		quickSortRec(A, 0, A.length - 1, comp);
	}
	
	
	private int partition(T A[], Integer i, Integer f, Comparator<T> comp) 
	{ 	
		Integer inf = i, sup = f + 1;
		T temp; 
		
		//scelta randomica->più efficiente
		Integer pos = i + (int) Math.floor((f-i+1)* Math.random()); 
		T x = A[pos];    
		A[pos] = A[i]; 
		A[i]=x; 
		
		
		while (true)
		{	do { inf++; } 
			while(inf <= f && comp.compare(A[inf], x) <= 0); 
			
			do { sup--; } 
			while (comp.compare(A[sup], x)> 0 ); 
			
			if (inf < sup) 
			{ 	temp = A[inf]; 
				A[inf] = A[sup]; 
				A[sup] = temp;
			} else { break; }
		} 
		temp = A[i]; 
		A[i] = A[sup]; 
		A[sup] = temp; 
		return sup; 
	}


	private void quickSortRec(T[] A, Integer i, Integer f, Comparator<T> comp) 	//oppure "public void quickSortRec(Comparable <T>[] A, Integer i, Integer f) " ??
	{	if (i >= f) { return; }
		Integer m = partition(A, i, f, comp); 
		quickSortRec(A, i, m - 1, comp); 
		quickSortRec(A, m+1, f, comp); 
	}

	//TODO: find a way to make private
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
	
	
	/*
	public static class sortByValue implements Comparator<Elem>
	{
		public int compare(Elem x1, Elem x2)
		{
			return (x1.getValue()).compareTo(x2.getValue());
		}
	}
	*/
}
