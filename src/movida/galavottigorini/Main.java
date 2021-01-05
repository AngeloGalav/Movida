package movida.galavottigorini;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

import movida.commons.MapImplementation;
import movida.commons.Movie;
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
				
		try {
			m_movidaCore = new MovidaCore(MapImplementation.ListaNonOrdinata, SortingAlgorithm.QuickSort);

			File m = new File("./src/movida/galavottigorini/esempio-formato-dati.txt");
			
			m_movidaCore.loadFromFile(m);	
			
			m_movidaCore.setMap(MapImplementation.HashIndirizzamentoAperto);
			
			m_movidaCore.m_movies.print();			
			
			//System.out.print("\n" + m_movidaCore.m_movies.getClass());
				
			Movie[] arr = m_movidaCore.getAllMovies();
								
			m_movidaCore.printArray(arr);
				
		} catch (Exception e) {
				e.getMessage();
				e.printStackTrace();
		}
		
	}

}
