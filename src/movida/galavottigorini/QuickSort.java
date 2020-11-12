package galavottigorini;
import java.io.File;
import java.lang.Math;
import commons.IMovidaConfig;
import commons.IMovidaDB;
import commons.IMovidaSearch;
import commons.MapImplementation;
import commons.Movie;
import commons.Person;
import commons.SortingAlgorithm;


//implementation of quick sort
public class QuickSort {

	public void QuickSort(Comparable <T>[] A) 	//oppure "public void QuickSort(Comparable <T>[] A)" ??
	{	 quickSortRec(A, 0, A.length - 1);}
	
	
	public void quickSortRec(Comparable <T>[] A, Integer i, Integer f) 	//oppure "public void quickSortRec(Comparable <T>[] A, Integer i, Integer f) " ??
	{	if (i >= f) { return; }
		Integer m = partition(A, i, f); 
		quickSortRec(A, i, m - 1); 
		quickSortRec(A, m+1, f); 
	}
	
	
	private int partition(Comparable A[], Integer i, Integer f) 
	{ 	Integer inf = i, sup = f + 1;
		Comparable temp; 
		
		//scelta randomica->più efficiente
		Integer pos=i + (int) Math.floor((f-i+1)* Math.random()); 
		Comparable x = A[pos];    
		A[pos] = A[i]; 
		A[i]=x; 
		
		
		while (true)
		{	do { inf++; } 
			while(inf<= f && A[inf].compareTo(x)<= 0); 
			
			do { sup--; } 
			while (A[sup].compareTo(x) > 0); 
			
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
}