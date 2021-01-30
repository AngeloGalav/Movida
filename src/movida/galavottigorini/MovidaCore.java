package movida.galavottigorini;

import movida.exceptions.*;
import movida.galavottigorini.Hash.HashingFunction;
import movida.galavottigorini.Map.Elem;
import movida.commons.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*; //scanner is here

public class MovidaCore implements IMovidaDB, IMovidaSearch, IMovidaConfig, IMovidaCollaborations{
	
	/*
	 * Si usano due strutture dati diverse per i film e per gli attori, sostituendo i due generic con una stringa che indica il nome 
	 * e l'oggetto (che puï¿½ essere un film o una persona) rispettivamente.
	 */
	Map<String, Movie> m_movies;
	Map<String, Person> m_person;

	MovidaGraph m_collaboration;
	
	MapImplementation chosen_map;
	SortingAlgorithm chosen_algo;
	 
	
	private File data_source;
	
	int default_hash_size = 40;
	HashingFunction default_hash_function = HashingFunction.HashCodeJava;
	
	Sort<Elem> sorting_algorithms;
	
	
	public MovidaCore(MapImplementation map, SortingAlgorithm sortAlgo) throws UnknownMapException, UnknownSortException
	{	
		//default settings
		chosen_algo = SortingAlgorithm.QuickSort;
		chosen_map = MapImplementation.ListaNonOrdinata;
		
		sorting_algorithms = new Sort<Elem>();	
		m_collaboration = new MovidaGraph();
		
		setMap(map);
		setSort(sortAlgo);
		
		switch (chosen_map) {
			case HashIndirizzamentoAperto:	//TODO: SET RISAZABLE HASH!!
				m_movies = new Hash<String, Movie>(default_hash_function);
				m_person = new Hash<String, Person>(default_hash_function);	//TODO: REDO!!!!!!!!
				break;
			case ListaNonOrdinata:
				m_movies = new UnorderedLinkedList<String, Movie>();
				m_person = new UnorderedLinkedList<String, Person>();
				break;
	
			default:
				throw new UnknownMapException();
		}
		
		if (chosen_algo != SortingAlgorithm.InsertionSort && chosen_algo != SortingAlgorithm.QuickSort) 
		{
			throw new UnknownSortException();
		}
		
	}
	
	@Override
	public Person[] getDirectCollaboratorsOf(Person actor)
	{
		if (actor.getRole().equals("Actor")) 
		{
			return m_collaboration.getValuesOfAdjiacentNodes(actor);	//TODO: LOWER CASE!!
		}
		
		return null;
	}


	@Override
	public Person[] getTeamOf(Person actor)
	{
		if (actor.getRole().equals("Actor")) 
		{
			return m_collaboration.MovidaBFS(actor);	//TODO: LOWER CASE!!
		}
		
		return null;
	}


	@Override
	public Collaboration[] maximizeCollaborationsInTheTeamOf(Person actor) 
	{
		if (actor.getRole().equals("Actor")) 
		{
			return m_collaboration.MovidaPrim(actor);	//TODO: LOWER CASE!!
		}
		return null;
	}


	@Override
	public boolean setSort(SortingAlgorithm a) {
		if (a == chosen_algo || (a != SortingAlgorithm.QuickSort && a != SortingAlgorithm.InsertionSort)) 
		{
			return false;
		} else {
			chosen_algo = a;
			return true;
		}
	}

	@Override
	public boolean setMap(MapImplementation m) { //TODO: Da testare!!
		
		if (m == chosen_map || (m != MapImplementation.ListaNonOrdinata && m != MapImplementation.HashIndirizzamentoAperto)) 
		{
			return false;
		} else {
			chosen_map = m;
			return true;
		}
	}

	
	@Override
	public Movie[] searchMoviesByTitle(String title)	//TODO: TESTA!! 
	{	
		ArrayList<Movie> list = new ArrayList<Movie>();
		
		String formatted_title = rmvWhiteSpaces(title).toLowerCase();
		
		for (Movie mov : getAllMovies()) 
		{
			if (mov.getTitle().toLowerCase().contains(formatted_title)) list.add(mov);
		}
		
		return list.toArray(new Movie[list.size()]);
	}

	
	@Override
	public Movie[] searchMoviesInYear(Integer year) {//TODO: TESTA!! 
														//TODO: CHANGE VALUES TOARRAY!! (usa un copy of)
		ArrayList<Movie> list = new ArrayList<Movie>();
		
		for (Movie mov : getAllMovies()) 
		{
			if (mov.getYear().equals(year)) list.add(mov);
		}
		
		return list.toArray(new Movie[list.size()]);
	}

	
	@Override
	public Movie[] searchMoviesDirectedBy(String name) //TODO: TESTA!! 
													//TODO: CHANGE VALUES TOARRAY!! (usa un copy of)
	{
		ArrayList<Movie> list = new ArrayList<Movie>();
		
		String formatted_name = rmvWhiteSpaces(name).toLowerCase();
		
		for (Movie mov : getAllMovies()) 
		{
			if ((mov.getDirector().getName().toLowerCase()).equals(formatted_name)) list.add(mov);
		}
		
		return list.toArray(new Movie[list.size()]);
	}

	
	@Override
	public Movie[] searchMoviesStarredBy(String name) //TODO: TESTA!! 
	//TODO: CHANGE VALUES TOARRAY!! (usa un copy of)
	{
		ArrayList<Movie> list = new ArrayList<Movie>();
		
		String formatted_name = rmvWhiteSpaces(name).toLowerCase();
		
		for (Object movie : m_movies.valuesToArray()) 
		{
			Movie mov = ((Movie) movie);
			
			for (Person act : mov.getCast()) 
				if (act.getName().toLowerCase().equals(formatted_name)) list.add(mov);
		}
		
		return list.toArray(new Movie[list.size()]);
	}

	
	@Override
	public Movie[] searchMostVotedMovies(Integer N) //TODO: TESTA!! 
	//TODO: CHANGE VALUES TOARRAY!! (usa un copy of)
	{
		Elem[] allmovies = m_movies.toArray();
		ArrayList<Movie> list = new ArrayList<Movie>();
		
		sorting_algorithms.setReversed(true);
		sort(allmovies, new Sort.sortByMovieVotes());
		
		for (int i = 0; i < allmovies.length && i < N; i++) 
		{
			list.add( (Movie) allmovies[i].getValue() );
		}
		
		return list.toArray(new Movie[list.size()]);
	}

	
	@Override
	public Movie[] searchMostRecentMovies(Integer N) //TODO: TESTA!! 
	//TODO: CHANGE VALUES TOARRAY!! (usa un copy of)
	{
		Elem[] allmovies = m_movies.toArray();
		ArrayList<Movie> list = new ArrayList<Movie>();
		
		sorting_algorithms.setReversed(true);
		sort(allmovies, new Sort.sortByMovieYear());
		
		for (int i = 0; i < allmovies.length && i < N; i++) 
		{
			list.add( (Movie) allmovies[i].getValue() );
		}
		
		return list.toArray(new Movie[list.size()]);
	}

	@Override
	public Person[] searchMostActiveActors(Integer N) //TODO: TESTA!! 
	//TODO: CHANGE VALUES TOARRAY!! (usa un copy of)
	{
		Person[] actors = getAllPeople();
		PriorityQueue<MovieCount> Q = new PriorityQueue<MovieCount>(new MovieCountComp());
		ArrayList<Person> tmp = new ArrayList<Person>();
		
		for (int i = 0; i < actors.length; i++) 
		{ 
			if (actors[i].getRole().equals("Actor"))
			{
				Q.add(new MovieCount(actors[i], searchMoviesStarredBy(actors[i].getName()).length));
			}
		}
		
		for (int i = 0; i < N; i++) tmp.add( Q.poll().per );
		
		return tmp.toArray(new Person[tmp.size()]);
	}



	@Override
	public void loadFromFile(File f) throws MovidaFileException	//TODO: TESTA!! FILE EXCEPTION
	{ 
		data_source = f;
		
		String title_temp = new String();
		Integer i = 0, year_temp, votes_temp, num_actor_temp;
		Person director_temp;
		ArrayList<Movie> temp_movies = new ArrayList<>();
		Integer c = 0;	//contatore numero di attori per film
		
		try {
			Scanner in = new Scanner(f);
			while (in.hasNextLine())
			{
				in.next();
				title_temp = in.nextLine();
				
				title_temp = rmvWhiteSpaces(title_temp);
			
				in.next();
				year_temp = in.nextInt();
	
				in.next();
				director_temp = new Person(rmvWhiteSpaces(in.nextLine()), "Director");
				
				if (m_person.search(director_temp.getName() ) == null) 
				{
					try {
						m_person.insert(director_temp.getName().toLowerCase(), director_temp);
					} catch (Exception e) {
						e.getMessage();
						e.printStackTrace();
					}
				}
	
				c = 0; //inizializzo contatore di attori a 0
				in.next();
				String s = in.nextLine();
				String[] str = s.split(",");
				Person[] cast_temp = new Person[str.length];
				for (int j = 0; j < str.length; j++)
				{	
					cast_temp[j] = new Person(rmvWhiteSpaces(str[j]), "Actor");	
					
					if (m_person.search(cast_temp[j].getName().toLowerCase() ) == null) 
					{
						try {
							m_person.insert(cast_temp[j].getName().toLowerCase(), cast_temp[j]);
						} catch (Exception e) {
							e.getMessage();
							e.printStackTrace();
						}
					}
					
					c++;//incremento contatore numero di persone
				}
				num_actor_temp = c;
				
				in.next();
				votes_temp = in.nextInt();
				
				i++;//incremento numero di movie
				temp_movies.add(new Movie(title_temp, year_temp, votes_temp, cast_temp, director_temp));
			}
			in.close();
			
			for (int j = 0; j < i; j++) {
				m_movies.insert(temp_movies.get(j).getTitle().toLowerCase(), temp_movies.get(j));
			}
			
			processCollaborations();
				
		} catch (Exception e)
		{
			e.getMessage();
			e.printStackTrace();
			throw new MovidaFileException();
		}
	}


	@Override
	public void saveToFile(File f) throws MovidaFileException//TODO: TESTA!! FILE EXCEPTION
	{
		try 
		{
			FileWriter writer = new FileWriter(f);
			Movie[] movie = getAllMovies();
			String s = new String();
			
			for (int i = 0; i < movie.length; i++) 
			{
				s = "Title: " + movie[i].getTitle();
				s += "\nYear: " + movie[i].getYear();
				s += "\nDirector: " + movie[i].getDirector().getName();
				s += "\nCast: ";
				
				for (Person act : movie[i].getCast()) 
				{
					s += act.getName() + ", ";
				}   		
				s = s.substring(0, s.length() - 2); //Tolgo la virgola e lo spazio alla fine, per formattare meglio.
				s += "\nVotes: " + movie[i].getVotes() + "\n\n";
				
				writer.append(s);
				writer.flush();
			}
			
			writer.close();
			
		} catch (IOException ex) 
		{
			ex.getMessage();
			throw new MovidaFileException();
		}
	}


	@Override
	public void clear() {
		m_movies.clear();
		m_person.clear();
		m_collaboration.clear();
	}


	@Override
	public int countMovies() {
		return m_movies.getSize();
	}

	
	@Override
	public int countPeople() {
		return m_person.getSize();
	}

	@Override
	public boolean deleteMovieByTitle (String title)
	{
		// TODO testare
		//try {
		Movie movieToRemove = getMovieByTitle(title);
			
		if (movieToRemove != null) {
			if (searchMoviesDirectedBy( movieToRemove.getDirector().getName() ).length == 1 ) //ha diretto solo 1 film
			{				
				m_person.delete( movieToRemove.getDirector().getName().toLowerCase() );
			}
							
			for( Person actor : movieToRemove.getCast()) 
			{
				if ( searchMoviesStarredBy(actor.getName()).length <= 1 ) //ha partecipato a solo 1 film
				{ 
					m_person.delete(actor.getName().toLowerCase());
				}
			}
				
			m_movies.delete(movieToRemove.getTitle().toLowerCase());	//tolgo il film da m_movies
			m_collaboration.deleteMovieFromCollaborations(movieToRemove);

			return true;
		}
		return false;
	}
		
		/*
		catch ( Exception e ) 
		{	e.getMessage();	
			return false;
		}*/


	@Override
	public Movie getMovieByTitle(String title) 
	{	
		String formatted_title = rmvWhiteSpaces(title).toLowerCase();
		Elem mov = null;
		try {
			mov = m_movies.search(formatted_title);
			
			if (mov == null) return null;
			else return (Movie) mov.getValue();

		} catch (Exception e) {
			e.getMessage();
			e.printStackTrace();
			return null;
		}		
	}

	
	@Override
	public Person getPersonByName(String name) 
	{
		String formatted_name = rmvWhiteSpaces(name).toLowerCase();
		Elem pers = null;
		try {
			pers = m_person.search(formatted_name);
			
			if (pers == null) return null;
			else return (Person) pers.getValue();

		} catch (Exception e) {
			e.getMessage();
			e.printStackTrace();
		}
		return null;
	}


	@Override
	public Movie[] getAllMovies() {
		return Arrays.copyOf(m_movies.valuesToArray(), m_movies.getSize(), Movie[].class);
	}


	@Override
	public Person[] getAllPeople() {
		return Arrays.copyOf(m_person.valuesToArray(), m_person.getSize(), Person[].class);
	}

	/**		Inizializza tutte le strutture dati necessarie a seconda del parametro "chosen_map"
	 *		che può variare tra le strutture ListaNonOrdinata e HashTable
	 * */
	public void reload() 
	{
		switch (chosen_map) 
		{
			case HashIndirizzamentoAperto:
				m_movies = new Hash<String, Movie>(default_hash_function);
				m_person = new Hash<String, Person>(default_hash_function);
				break;
			case ListaNonOrdinata:
				m_movies = new UnorderedLinkedList<String, Movie>();
				m_person = new UnorderedLinkedList<String, Person>();
				break;
		}
		
		loadFromFile(data_source);
	}
	
	/**		Ordina un array di Elem passato in input con l'algoritmo di ordinamento scelto 
	 * 		e usando il Comparator dato anch'esso in input
	 * 		non ritorna nulla perchè l'array viene ordinato in loco
	 * 		@param arr array da ordinare
	 * 		@param sort_filter comparatore da usare
	 * */
	public void sort(Elem[] arr, Comparator<Elem> sort_filter ) 
	{
		switch (chosen_algo) {
			case QuickSort: 
			{
				sorting_algorithms.quickSort(arr, sort_filter);
				break;
			}
			case InsertionSort:
			{
				sorting_algorithms.insertionSort(arr, sort_filter);
				break;
			}
		}
	}
	
	
	/**		Funzione che formatta la stringa passatagli in input eliminando tutti gli spazi inutili al suo interno
	 * 		@param  temp stringa da formattare
	 * 		@return stringa formattata 
	 * */	
	public String rmvWhiteSpaces(String temp) {
		String[] str_tmp = temp.split(" ");
		String formatted_string = new String();
		
		for (int k = 0; k < str_tmp.length; k++) {
			str_tmp[k] = str_tmp[k].replaceAll("\\s+", "");
			str_tmp[k] = str_tmp[k].replaceAll("\n", "");
			str_tmp[k] = str_tmp[k].replaceAll("\t", "");
			if (str_tmp[k].compareTo("") != 0) {
				formatted_string += str_tmp[k] + " ";
			}
		}
		
		formatted_string = formatted_string.substring(0, formatted_string.length() - 1);
		
		return formatted_string;
	}
	
	//TODO: Check errors
	public void processCollaborations() 
	{
		Movie[] toProcess = getAllMovies();
				
		for (Movie mov : toProcess) 
		{
			Person[] cast = mov.getCast();
			
			for (Person act : cast) 
			{
				if (!(m_collaboration.checkNodePresence(act))) m_collaboration.insert(act);
			}
	
			for (Person act : cast) 
			{			
				for (int i = 0; i < cast.length; i++) 
				{
					//se il nome dell'attore è diverso dal suo e il link non esiste ancora...
					if (cast[i] != null && act != cast[i]) //TODO: why null check?
					{
						Collaboration collab = m_collaboration.findCollaboration(act, cast[i]);
						
						if (collab == null) 
						{
							collab = m_collaboration.makeCollaboration(act, cast[i]);
							collab.addMovieCollaboration(mov);
						}
						else if ( !(collab.madeMovie(mov)) )
						{
							collab.addMovieCollaboration(mov);
						}
					} 
				}
			}
		}
	}
	
	
	
	private class MovieCount
	{
		Person per;
		Integer n;
		
		private MovieCount(Person per, Integer n) 
		{
			this.per = per;
			this.n = n;
		}
		
		@Override
		public int hashCode() 
		{
			return per.hashCode();
		}
		
		@Override
		public boolean equals(Object o) 
		{
			if (o == this) return true;
			if (!(o instanceof MovieCount) || o == null) return false;
			MovieCount toCheck = (MovieCount) o;
			
			return per.equals(per);
		}
	}
	
	
	private class MovieCountComp implements Comparator<MovieCount>
	{	
		public int compare(MovieCount e1, MovieCount e2)
		{
			if (e1.n.compareTo(e2.n) != 0) return (e1.n).compareTo(e2.n) * -1;
			else return (e1.per.getName()).compareTo(e2.per.getName());
		}
	}
	
	private void name() //TODO: Set to private...
	{		
		
	}
	
	
	
	///DEBUG FUNCTIONS
	
	
	public static class MovidaDebug{
		
		public static void Log (String s) 
		{
			System.out.print(s);
		}
		
		public static void printArray(Object[] arr) {
			
			for (int i = 0; i < arr.length; i++) 
			{
				if (arr[i] != null)	System.out.println(arr[i].toString());
				else System.out.println(i + " IS NULL");
			}
		}
		
	}
	
}
