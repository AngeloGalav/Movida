package movida.galavottigorini;


/*
 * TODO: implement this algorithms:
 * 
	Algoritmi di ordinamento: InsertionSort,  QuickSort
	Implementazioni dizionario: ListaCollegataNonOrdinata,  HashIndirizzamentoAperto

 * 
 */


//todo:: modificare le cose in questa cagata, aggiungere un generic

public class InsertionSort<T>{
	
	public static void insertionSort(Comparable A[]) {
		for (int k = 1; k <= A.length - 1; k++) {
			int j;
			Comparable x = A[k];
				
			// cerca la posizione j in cui inserire A[k]
			
			for (j = 0; j < k; j++) if (A[j].compareTo(x) > 0) break;
			if (j < k) 
			{
				// Sposta A[j..k-1] in A[j+1..k]
				for (int t = k; t > j; t--) A[t] = A[t - 1];
				// Inserisci A[k] in posizione j
				A[j] = x;
			}
		}
	}

}
