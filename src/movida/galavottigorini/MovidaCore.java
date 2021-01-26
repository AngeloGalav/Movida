package movida.galavottigorini;

import movida.exceptions.*;
import movida.galavottigorini.Hash.HashingFunction;
import movida.galavottigorini.Map.Elem;
import movida.commons.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.*; //scanner is here

public class MovidaCore implements IMovidaDB, IMovidaSearch, IMovidaConfig, IMovidaCollaborations{
	
	/*
	 * Si usano due strutture dati diverse per i film e per gli attori, sostituendo i due generic con una stringa che indica il nome 
	 * e l'oggetto (che puï¿½ essere un film o una persona) rispettivamente.
	 */
	Map<String, Movie> m_movies;
	Map<String, Person> m_persons;

	MovidaGraph<String, Person> m_collaboration;
	
	MapImplementation chosen_map;
	SortingAlgorithm chosen_algo;
	 
	
	private File data_source;
	
	int default_hash_size = 300;
	HashingFunction default_hash_function = HashingFunction.HashCodeJava;
	
	Sort<Elem> sorting_algorithms;
	
	
	public MovidaCore(MapImplementation map, SortingAlgorithm sortAlgo) throws UnknownMapException, UnknownSortException
	{	
		sorting_algorithms = new Sort<Elem>();	
		m_collaboration = new MovidaGraph<String, Person>();
		
		setMap(map);
		setSort(sortAlgo);
		
		switch (chosen_map) {
			case HashIndirizzamentoAperto:
				m_movies = new Hash<String, Movie>(default_hash_size, default_hash_function);
				m_persons = new Hash<String, Person>(default_hash_size, default_hash_function);
				break;
			case ListaNonOrdinata:
				m_movies = new UnorderedLinkedList<String, Movie>();
				m_persons = new UnorderedLinkedList<String, Person>();
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
	public Person[] getDirectCollaboratorsOf(Person actor) //TODO: Da testare.
	{
		return m_collaboration.getValuesOfAdjiacentNodes(actor);
	}


	@Override
	public Person[] getTeamOf(Person actor) //TODO: Da testare.
	{
		return m_collaboration.MovidaBFS(actor);
	}


	@Override
	public Collaboration[] maximizeCollaborationsInTheTeamOf(Person actor) {//TODO: fare con djikstra.
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public boolean setSort(SortingAlgorithm a) {
		if (a == chosen_algo) 
		{
			return false;
		} else {
			chosen_algo = a;
			return true;
		}
	}

	@Override
	public boolean setMap(MapImplementation m) {
		
		if (m == chosen_map) 
		{
			return false;
		} else {
			chosen_map = m;
			reload();
			return true;
		}
	}

	
	@Override
	public Movie[] searchMoviesByTitle(String title)
	{	
		ArrayList<Movie> list = new ArrayList<Movie>();
		
		String formatted_title = rmvWhiteSpaces(title);
		
		for (Object movie : m_movies.valuesToArray()) 
		{
			Movie mov = ((Movie) movie);
			if (mov.getTitle().contains(formatted_title)) list.add(mov);
		}
		
		return list.toArray(new Movie[list.size()]);
	}

	
	@Override
	public Movie[] searchMoviesInYear(Integer year) {
		ArrayList<Movie> list = new ArrayList<Movie>();
		
		for (Object movie : m_movies.valuesToArray()) 
		{
			Movie mov = ((Movie) movie);
			if (mov.getYear().equals(year)) list.add(mov);
		}
		
		return list.toArray(new Movie[list.size()]);
	}

	
	@Override
	public Movie[] searchMoviesDirectedBy(String name) 
	{
		ArrayList<Movie> list = new ArrayList<Movie>();
		
		String formatted_name = rmvWhiteSpaces(name);
		
		for (Object movie : m_movies.valuesToArray()) 
		{
			Movie mov = ((Movie) movie);
			if (mov.getDirector().getName().equals(formatted_name)) list.add(mov);
		}
		
		return list.toArray(new Movie[list.size()]);
	}

	
	@Override
	public Movie[] searchMoviesStarredBy(String name) {
		ArrayList<Movie> list = new ArrayList<Movie>();
		
		String formatted_name = rmvWhiteSpaces(name);
		
		for (Object movie : m_movies.valuesToArray()) 
		{
			Movie mov = ((Movie) movie);
			
			for (Person act : mov.getCast()) 
			{
				if (act.getName().equals(formatted_name)) list.add(mov);
			}
		}
		
		return list.toArray(new Movie[list.size()]);
	}

	
	@Override
	public Movie[] searchMostVotedMovies(Integer N) 
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
	public Movie[] searchMostRecentMovies(Integer N) {

		Elem[] films= m_movies.toArray();
		Movie[] arrM;
		
		//ordino il mio vettore
		sorting_algorithms.setReversed(true);
		sort(films, new Sort.sortByMovieYear());
		
		if (N >= films.length) {
			//restituisco l'intero vettore con tutti i film
			arrM= new Movie[films.length];
			for (int i=0 ; i<films.length ; i++) {
				arrM[i] = (Movie) films[i].getValue();
			}
		}
		else { //restituisco solo i primi N film
			arrM= new Movie[N];
			for (int i=0 ; i<N ; i++) {
				arrM[i] = (Movie) films[i].getValue();
			}
		}
		
		return arrM;
	}

	@Override
	public Person[] searchMostActiveActors(Integer N) {
		// TODO Auto-generated method stub 				GRAFI??
		return null;
	}


	@Override
	public void loadFromFile(File f) { //TODO: DA RIVEDERE INTEGRAZIONE CON PERSONS ARRAY
		
		data_source = f;
		
		String title_temp = new String();
		Integer i = 0, year_temp, votes_temp, num_actor_temp;
		Person director_temp;
		ArrayList<Movie> temp_movies = new ArrayList<>();
		Integer c = 0;//contatore numero di attori per film
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
	
				c = 0; //inizializzo contatore di attori a 0
				in.next();
				String s = in.nextLine();
				String[] str = s.split(",");
				Person[] cast_temp = new Person[str.length];
				for (int j = 0; j < str.length; j++)
				{	
					cast_temp[j] = new Person(rmvWhiteSpaces(str[j]), "Actor");	
					
					if ( m_persons.search(cast_temp[j].getName() ) == null) 
					{
						try {
							m_persons.insert(cast_temp[j].getName(), cast_temp[j]);
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
			
			for (int j=0; j<i; j++) {
				m_movies.insert(temp_movies.get(j).getTitle(), temp_movies.get(j));
			}
				
		} catch (Exception e){	e.getMessage();}
		
	}


	@Override
	public void saveToFile(File f) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void clear() {
		m_movies.clear();
		m_persons.clear();
		m_collaboration.clear();
	}


	@Override
	public int countMovies() {
		return m_movies.getSize();
	}

	
	@Override
	public int countPeople() {
		return m_persons.getSize();
	}


	@Override
	public boolean deleteMovieByTitle(String title) {
		// TODO testare
		try {
			Movie movieToRemove= this.getMovieByTitle(title);

			//dovrò anche gestire m_persons, perchè con un film in meno potremmo avere meno persone
			
			//mi occupo prima del direttore
			Person DirectorToRemove= movieToRemove.getDirector();
			if ( (this.searchMoviesDirectedBy(DirectorToRemove.getName())).length == 1 ) {
				//questo direttore ha fatto solo un film, ovvero quello che volgio togliere, quindi potrò eliminarlo da m_persons
				m_persons.delete(DirectorToRemove.getName());
			}
			
			//ora mi occupo degli attori
			Person[] castToRemove= movieToRemove.getCast();
			
			for( int i = 0 ; i<castToRemove.length ; i++) {
				if ( (this.searchMoviesStarredBy(castToRemove[i].getName())).length == 1 ) {
					//questo attore ha fatto solo un film, ovvero quello che volgio togliere, quindi potrò eliminarlo da m_persons
					m_persons.delete(castToRemove[i].getName());
				}
			}
				
			//infine tolgo il mio film da m_movies
			m_movies.delete(movieToRemove.getTitle());
			
		}
		catch ( Exception e ) {		System.out.println(e.getMessage());		}
		
		if(this.getMovieByTitle(title) == null ) {
			return true; }

		return false;
	}


	@Override
	public Movie getMovieByTitle(String title) {
		// TODO testare
		Elem[] films = m_movies.toArray();
		int posM=-1;
		
		//formatto la stringa passata così in caso dovesse avere spazi in più inutili trovo lo stesso il match 
		String FormattedTitle = this.rmvWhiteSpaces(title); 
	
		for ( int i=0 ; i<films.length ; i++) {
			if ( FormattedTitle.compareTo( ( (Movie) films[i].getValue() ).getTitle() ) == 0 ) {
				posM = i;
				break;
			}
		}
		
		if (posM != -1) //l'ho effettivamente trovato
			return (Movie) films[posM].getValue();
		
		return null;
	}

	
	@Override
	public Person getPersonByName(String name) {
		// TODO testare
		Elem[] films = m_persons.toArray();
		int posM=-1;
		
		//formatto la stringa passata così in caso dovesse avere spazi in più inutili trovo lo stesso il match 
		String FormattedName = this.rmvWhiteSpaces(name); 	
				
		for ( int i=0 ; i<films.length ; i++) {
			if ( FormattedName.compareTo( ( (Person) films[i].getValue() ).getName() ) == 0 ) {
				posM = i;
				break;
			}
		}
		
		if (posM != -1) //l'ho effettivamente trovato
			return (Person) films[posM].getValue();
		
		return null;
		
	}


	@Override
	public Movie[] getAllMovies() {
		return Arrays.copyOf(m_movies.valuesToArray(), m_movies.getSize(), Movie[].class);
	}


	@Override
	public Person[] getAllPeople() {
		return Arrays.copyOf(m_persons.valuesToArray(), m_persons.getSize(), Person[].class);

	}
	
	public void reload() 
	{
		switch (chosen_map) 
		{
			case HashIndirizzamentoAperto:
				m_movies = new Hash<String, Movie>(default_hash_size, default_hash_function);
				m_persons = new Hash<String, Person>(default_hash_size, default_hash_function);
				break;
			case ListaNonOrdinata:
				m_movies = new UnorderedLinkedList<String, Movie>();
				m_persons = new UnorderedLinkedList<String, Person>();
				break;
		}
		
		loadFromFile(data_source);
	}
	
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
	
	//data una stringa ritorno la lista delle posizioni degli elementi (nell'array passato) che contengono tale chiave
	public ArrayList<Integer> Find_string(String[] arr, String strn_wanted) {
		ArrayList<Integer> positions = new ArrayList(arr.length);
		String temp_title;
		String [] temp_word;
		
		for (int i=0 ; i<arr.length ; i++) {	
			temp_title=arr[i];
			
			if(temp_title.compareTo(strn_wanted) == 0) {
				positions.add(i);
				break;
				
			}else {
				temp_word= temp_title.split(" ");
				for(int j=0 ; j<temp_word.length ; j++){
					if( (temp_word[j]) . contains(strn_wanted)) {
						positions.add(i);
						break;
					}
				}
			}
		}
		return positions;
	}
	
	//dato un numero ritorno la lista delle posizioni degli elementi (nell'array passato) che contengono tale chiave
		public ArrayList<Integer> Find_int(int[] arr, int value_wanted) {
			ArrayList<Integer> positions = new ArrayList(arr.length);
			
			for(int i=0 ; i<arr.length ; i++){
				if( arr[i]==value_wanted) {
					positions.add(i);
				}
			}
			return positions;
		}
	
	
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
	
	public void changeHashDefaults(int hash_size, HashingFunction hash_function) {
		default_hash_size = hash_size;
		default_hash_function = hash_function;
		
	}
	
	
	//TODO: Check stuff with error
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
	
	
	///DEBUG FUNCTIONS
	
	
	public static class MovidaDebug{
		
		public static void Log (String s) 
		{
			System.out.print(s);
		}
		
		public static void printArray(Object[] arr) {
			
			for (int i = 0; i < arr.length; i++) {
				System.out.println(arr[i].toString());
			}
		}
		
	}
	
}
