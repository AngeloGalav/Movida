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
import movida.galavottigorini.Sort.sortByDebugName;

/** COSE DA FARE PER LETI:
 * 
 * 	
 *  
 *  4) darmi tanti baci
 *  
 *  5) controllare se ci sono metodi che posso mettere a private (es. se non vengono mai usate fuori dalla classe stessa...) 
 *  (per fare veloce ti consiglio di premere il tasto destro del mouse su una funzione e vedere se ha riferimenti fuori dalla sua 
 *  classe)
 *  
 *  6) controllare se ci sono getter e setter che non vengono mai usati.. (un esempio credo sia getSource)
 *  
 *  7) nel grafo in particolare ci sono molte funzioni che non vengono mai usate o che sono duplicate: controlla e proponi se 
 *  alcune sonon utili o no..
 *  
 *  8) guarda se ci sono possibili compareTo che possono mettere in alcuni casi
 *  
 *  9) aggiungi commenti simili a come li ho aggiunti io nelle funzioni precedenti.
 *  
 *  10) leggere il codice, se hai miglioramenti proponili etc...
 *    
 *  Se hai bisogno d'aiuto o hai domande, avvisami subito, senza pensarci due volte.
 * 
 * @author il tuo Angelo <3
 *
 */

//TODO: redo to array... (see if you can do better)
//TODO: exceptions...
//TODO: Add case insensitive name for movies (and people)... (done)(da testare)
//TODO: Test movida file exception
//TODO: NULL Test con i vari search (!!)
//TODO: Riguardare TUTTE le funzioni di movida core


//TODO: Elimina sela ward da air force one


/**TODO PER LETI: Cose da testare:
 * 
 * 	- deleteAllMovies, con tutte e due le strutture dati
 * */

public class Main {

	public static MovidaCore m_movidaCore;
		
	public static void main(String[] args) {
		// TODO: FIND METHODS THAT CAN BE SET TO PRIVATE
		// TODO: CLEAN GETTER AND SETTERS FROM CLASSES IF YOU DONT NEED THEM
				
		try {
			m_movidaCore = new MovidaCore(MapImplementation.ListaNonOrdinata, SortingAlgorithm.QuickSort);

			File r = new File("./src/movida/galavottigorini/esempio-formato-dati-real.txt");
			File s = new File("./src/movida/galavottigorini/output.txt");
			
			m_movidaCore.loadFromFile(r);	
			m_movidaCore.setMap(MapImplementation.HashIndirizzamentoAperto);
			m_movidaCore.reload();
			m_movidaCore.m_movies.print();
			/*				
			m_movidaCore.setMap(MapImplementation.HashIndirizzamentoAperto);
			m_movidaCore.clear();
			m_movidaCore.reload();
//			m_movidaCore.m_movies.print();
			
			m_movidaCore.setMap(MapImplementation.ListaNonOrdinata);
			m_movidaCore.reload();
//			m_movidaCore.m_movies.print();
		
			System.out.println(" ------- ");

			
			Elem[]  movs = m_movidaCore.m_movies.toArray();
			Person[] pers = (Person []) m_movidaCore.m_person.valuesToArray();
			MovidaDebug.printArray(movs);
			MovidaDebug.printArray(pers);
			
			//svuoto m_movies
			System.out.println(" -------svuoto m_movies ");
//			for (int i=0 ; i<movs.length ; i++) { System.out.println(((Movie) movs[i].getValue()).getTitle() + "--->" + m_movidaCore.deleteMovieByTitle(( ( (Movie) movs[i].getValue()).getTitle()).toString()) );}
			System.out.println(" ------- ");
			
			
			
			System.out.println(" ------- ");
			
		Person hFord = new Person("harrison ford", "actor");
			
			m_movidaCore.deleteMovieByTitle("cape fear");
			MovidaDebug.printArray(m_movidaCore.getAllPeople());
			
			System.out.println(" ------- ");
			MovidaDebug.Log("" + m_movidaCore.getPersonByName("harrison ford"));
			MovidaDebug.Log("\n" + m_movidaCore.getMovieByTitle("cape fear"));
			
			
				
PROVA ORDINAMENTO
			m_movidaCore.sorting_algorithms.setReversed(true);
			m_movidaCore.sort(arr2, new Sort.sortByMovieVotes());
			MovidaDebug.printArray(arr2);
*/			
			
/* PROVA GRAFI
			//System.out.print("\n" + m_movidaCore.m_movies.getClass());
				
			Person[] arr = m_movidaCore.getAllPeople();
			Elem[] peeps = m_movidaCore.m_persons.toArray();
			
			m_movidaCore.processCollaborations();
			m_movidaCore.m_collaboration.printCollaborations();
			m_movidaCore.m_collaboration.printNodes();
			*/
			
/*PROVA SORT con InsertionSort
			Elem[] arr2= m_movidaCore.m_movies.toArray();
			m_movidaCore.sorting_algorithms.setReversed(true);
			m_movidaCore.sort(arr2, new Sort.sortByMovieYear());
		
			MovidaDebug.printArray(arr2);
*/
			/*
			StructureTest st = new StructureTest(90, HashingFunction.HashCodeJava);
			
			st.DemoStringHashFill(10);
			
			MovidaDebug.printArray(st.hashString.keysToArray());*/
			
			
			
			
			//st.DemoGraphFill(0);
			/*
			st.DemoListFill(1);
			
			st2.DemoListFill(1);
			
			st.ULLTest.delete(2);

			
			Elem[] arr = st.ULLTest.toArray();
			
			st.ULLTest.append(st2.ULLTest);
			
			st.ULLTest.print();
			st.ULLTest.reverseKeyListPrint();
			
			//MovidaDebug.printArray(arr);
			
			
			//System.out.print(st.graphTest);
			*/
			
			
				
		} catch (Exception e) {
				e.getMessage();
				e.printStackTrace();
		}
		
	}


}
