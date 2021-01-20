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

/** COSE DA FARE PER LETI:
 * 
 * 	1) finire il resto delle funzioni (tranne il saveToFile, che voglio provare a farlo io con un modo figo)  
 * 		(in realtà puoi provare anche tu ma se proprio vuoi avvisami che ti dico come voglio farlo...)
 * 		(Tra l'altro alcune delle funzioni sono sui grafi e sono abbastanza difficilotte)
 * 	
 * 	2) abbellire il quickSort (esattamente come hai fatto col resto)
 * 
 *  3) finire tutti il resto dei TODO (messaggiami se hai domande)
 *  	(trovare i TODO è semplice: basta cliccare sui quadrati blu che ci sono vicino alla barra di scorrimento)
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
 *  
 *  Se hai bisogno d'aiuto o hai domande, avvisami subito, senza pensarci due volte.
 * 
 * @author il tuo Angelo <3
 *
 */




public class Main {

	public static MovidaCore m_movidaCore;
		
	public static void main(String[] args) {
		// TODO: FIND METHODS THAT CAN BE SET TO PRIVATE
		// TODO: CLEAN GETTER AND SETTERS FROM CLASSES IF YOU DONT NEED THEM
				
		try {
			m_movidaCore = new MovidaCore(MapImplementation.ListaNonOrdinata, SortingAlgorithm.InsertionSort);

			File m = new File("./src/movida/galavottigorini/esempio-formato-dati.txt");
			
			m_movidaCore.loadFromFile(m);	
			
			//m_movidaCore.setMap(MapImplementation.HashIndirizzamentoAperto);
			
			//m_movidaCore.m_persons.print();			
			
			//System.out.print("\n" + m_movidaCore.m_movies.getClass());
				
			Person[] arr = m_movidaCore.getAllPeople();
			
			Elem[] peeps = m_movidaCore.m_persons.toArray();
			
			
			m_movidaCore.processCollaborations();
			m_movidaCore.m_collaboration.printCollaborations();
			m_movidaCore.m_collaboration.printNodes();
			
			/*
			//prova sort con InsertionSort
			Elem[] arr2= m_movidaCore.m_movies.toArray();
			m_movidaCore.sorting_algorithms.setReversed(true);
			m_movidaCore.sort(arr2, new Sort.sortByMovieYear());
		
			MovidaDebug.printArray(arr2);*/
		
			
			
			StructureTest st = new StructureTest(5, HashingFunction.IspezioneLineare);
			StructureTest st2 = new StructureTest(5, HashingFunction.IspezioneLineare);
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
