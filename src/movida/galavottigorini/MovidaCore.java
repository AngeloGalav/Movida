package movida.galavottigorini;

import movida.exceptions.*;
import movida.galavottigorini.Hash.HashingFunction;
import movida.galavottigorini.Map.Elem;
import movida.commons.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.*; //scanner is here

//TODO: Self-explanatory...


public class MovidaCore implements IMovidaDB, IMovidaSearch, IMovidaConfig, IMovidaCollaborations{
	
	
	/*
	 * Si usano due strutture dati diverse per i film e per gli attori, sostituendo i due generic con una stringa che indica il nome 
	 * e l'oggetto (che pu� essere un film o una persona) rispettivamente.
	 */
	Map<String, Movie> m_movies;
	Map<String, Person> m_persons;
	
	MapImplementation chosen_map;
	SortingAlgorithm chosen_algo;
	
	public File data_source; //TODO: Change to private
	
	int default_hash_size = 300;
	HashingFunction default_hash_function = HashingFunction.HashCodeJava;
	
	Sort<Elem> sorting_algorithms;
	 
	public MovidaCore(MapImplementation map, SortingAlgorithm sortAlgo) throws UnknownMapException, UnknownSortException{
		
		sorting_algorithms = new Sort<Elem>();	
		
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
	public Person[] getDirectCollaboratorsOf(Person actor) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Person[] getTeamOf(Person actor) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Collaboration[] maximizeCollaborationsInTheTeamOf(Person actor) {
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
	public Movie[] searchMoviesByTitle(String title) {
		
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Movie[] searchMoviesInYear(Integer year) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Movie[] searchMoviesDirectedBy(String name) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Movie[] searchMoviesStarredBy(String name) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Movie[] searchMostVotedMovies(Integer N) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Movie[] searchMostRecentMovies(Integer N) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Person[] searchMostActiveActors(Integer N) {
		// TODO Auto-generated method stub
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
				for (int j = 0; j < str.length; j++){	
					cast_temp[j] = new Person(rmvWhiteSpaces(str[j]), "Actor");	
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
		// TODO Auto-generated method stub
		
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
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public Movie getMovieByTitle(String title) {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public Person getPersonByName(String name) {
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
		
		switch (chosen_map) {
			case HashIndirizzamentoAperto:
				m_movies = new Hash<String, Movie>(default_hash_size, default_hash_function);
				m_persons = new Hash<String, Person>(default_hash_size, default_hash_function);
				break;
			case ListaNonOrdinata:
				m_movies = new UnorderedLinkedList<String, Movie>();
				m_persons = new UnorderedLinkedList<String, Person>();
				break;
		}
		
		reloadFromCached();
		
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
	
	public void reloadFromCached() {
		loadFromFile(data_source);
	}
	
	public void changeHashDefaults(int hash_size, HashingFunction hash_function) {
		default_hash_size = hash_size;
		default_hash_function = hash_function;
		
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
