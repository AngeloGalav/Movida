package movida.galavottigorini;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import movida.commons.MapImplementation;
import movida.commons.Person;
import movida.commons.SortingAlgorithm;
import movida.galavottigorini.Hash.HashingFunction;
import movida.galavottigorini.Map.Elem;
import movida.galavottigorini.MovidaCore.MovidaDebug;
import movida.galavottigorini.Sort;

public class Main {

	public static MovidaCore m_movidaCore;
		
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		StructureTest st = new StructureTest(8, HashingFunction.IspezioneLineare);
		
		try {
			m_movidaCore = new MovidaCore(MapImplementation.ListaNonOrdinata, SortingAlgorithm.QuickSort);
		} catch (Exception e) {
			e.getMessage();
		}
		
		try {
			/*
			st.DemoListFill(3);
			MovidaDebug.Log("\n");
			Elem[] arr = st.ULLTest.toArray();
			
			Sort<Elem> sort = new Sort<Elem>();
			
			sort.quickSort(arr, new Sort.sortByKey() );
			
			for (int i = 0; i < arr.length; i++) {
				System.out.print(arr[i] + " | ");
			}*/
			try {
				File m = new File("C:\\Users\\ASUS\\eclipse-workspace\\Movida\\src\\movida\\galavottigorini\\esempio-formato-dati.txt");
				//m.createNewFile();

				m_movidaCore.loadFromFile(m);
				//m_movidaCore.m_movies.print();
				
				Elem[] arr = m_movidaCore.m_movies.toArray();
				
				Sort.setReversed(true);
				
				m_movidaCore.sort(arr,new Sort.sortByMovieYear());
				
				m_movidaCore.printArray(arr);
				
				
			} catch (Exception e) {
				e.getMessage();
			}
		} catch (Exception e) {
			e.getMessage();
		}
		
	}

}
