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

			File r = new File("./src/movida/galavottigorini/esempio-formato-dati.txt");
			File s = new File("./src/movida/galavottigorini/output.txt");
			
			m_movidaCore.loadFromFile(r);	
			//m_movidaCore.setMap(MapImplementation.HashIndirizzamentoAperto);
			//m_movidaCore.reload();
			//m_movidaCore.m_movies.print();
			
			Movie[] movs= m_movidaCore.getAllMovies();
			Movie mov;
			Person[] pers= m_movidaCore.getAllPeople();
			Person per, per2;
			String title;
			String name;
			String name2;
			int N;
			int year;
			
			
			
			//test movida core function about movies
			MovidaDebug.Log("\n\n----MOVIES----\n");
			MovidaDebug.printArray(movs);
			int nMov=movs.length;
			for (int i = 0 ; i < nMov ; i++) 
			{
				mov= movs[i];
				title = mov.getTitle();
				N = i;
				year = 2000 - i;
				
				MovidaDebug.Log( "\n-----------------\n prova  get movie by title  '" + title + "'   :\n" );
				MovidaDebug.Log( m_movidaCore.getMovieByTitle(title).toString() );
				
				MovidaDebug.Log( "\n prova search "+ N +" most recent films  :\n" );
				MovidaDebug.printArray( m_movidaCore.searchMostRecentMovies(N) );
				
				MovidaDebug.Log( "\n prova search "+ N +" most voted films :\n" );
				MovidaDebug.printArray( m_movidaCore.searchMostVotedMovies(N) );
				
				MovidaDebug.Log( "\n prova  search movies in year '" + year + "'   :\n" );
				MovidaDebug.printArray( m_movidaCore.searchMoviesInYear(year) );
			
//				MovidaDebug.Log( "\n prova  delete movie by title '" + title + "' :\n" );
//				m_movidaCore.deleteMovieByTitle(title) ;
				
			}
			
			
			//test movida core function about persons
			MovidaDebug.Log("\n\n----PERSONS----\n");
			MovidaDebug.printArray(pers);
			int nPer=pers.length;
			for (int i = 0 ; i < nPer ; i++) 
			{
				per= pers[i];
				name = per.getName();
				N = i;
				year = 2000 - i;
				
				MovidaDebug.Log( "\n----------\n prova  get person by name  '" + name + "'   :\n" );
				MovidaDebug.Log( m_movidaCore.getPersonByName(name).toString() );

				MovidaDebug.Log( "\n\n prova  search movies directed by  '" + name + "'   :\n" );
				MovidaDebug.printArray( m_movidaCore.searchMoviesDirectedBy(name) );
				
				MovidaDebug.Log( "\n prova  search "+ N +" most active actors   :\n" );
				MovidaDebug.printArray( m_movidaCore.searchMostActiveActors(N) );

				MovidaDebug.Log( "\n prova  serach movie starred by  '" + name + "'  :\n" );
				MovidaDebug.printArray( m_movidaCore.searchMoviesStarredBy(name) );
				
				
			
			}
			
			
			
			//test movida graph function
			MovidaDebug.Log( "\n\n ->ALL COLLAB :\n" );
			m_movidaCore.m_collaboration.printCollaborations();
			MovidaDebug.Log( "\n\n ->ALL NODES :\n" );
			m_movidaCore.m_collaboration.printNodes();
				
			int nGrafo=m_movidaCore.m_collaboration.getSize();
			for (int i = 0 ; i < nGrafo ; i++) 
			{	
				per= pers[i];
				per2 = pers[ pers.length - i -1 ];
				name=per.getName();
				name2=per2.getName();
				
//				MovidaDebug.Log( "\n prova    :\n" );
//				m_movidaCore.m_collaboration.deleteMovieFromCollaborations(mov);
				
//				MovidaDebug.Log( "\n prova insert '"+ per2.getName() +"' :\n" );
//				m_movidaCore.m_collaboration.insert(per2);

				MovidaDebug.Log( "\n prova checkNodePresence of  '" + name + "'  :\n" );
				System.out.print( m_movidaCore.m_collaboration.checkNodePresence(per) );

				MovidaDebug.Log( "\n prova get direct collaboration '" + name + "' :\n" );
				MovidaDebug.printArray( m_movidaCore.getDirectCollaboratorsOf(per) );
				
				MovidaDebug.Log( "\n prova getTeamOf '" + name + "' :\n" );
				MovidaDebug.printArray( m_movidaCore.getTeamOf(per) );
				
				MovidaDebug.Log( "\n prova maximizeCollaborationsInTheTeamOf  '" + name + "' :\n" );
				MovidaDebug.printArray( m_movidaCore.maximizeCollaborationsInTheTeamOf(per) );
				
				MovidaDebug.Log( "\n prova findCollab '" +per.getName()+ "' and '" + name2 + "' :\n" );
				System.out.println( m_movidaCore.m_collaboration.findCollaboration( per , per2 ) );
				
				MovidaDebug.Log( "\n prova  adjiacentNodesOf '" + name +"'  :\n" );
				MovidaDebug.printArray( m_movidaCore.m_collaboration.getValuesOfAdjiacentNodes(per) );
				
				MovidaDebug.Log( "\n prova BFS con source= '" + name + "' :\n" );
				MovidaDebug.printArray( m_movidaCore.m_collaboration.MovidaBFS(per) );

				MovidaDebug.Log( "\n prova  PRIM con source= '" + name + "'   :\n" );
				MovidaDebug.printArray( m_movidaCore.m_collaboration.MovidaPrim(per) );

				MovidaDebug.Log( "\n prova  collabsOf '" + name + "'   :\n" );
				m_movidaCore.m_collaboration.printCollabsofNode(per);
				
			}
			
			
			
			
				
/* PROVA ORDINAMENTO
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
