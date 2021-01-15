package movida.commons;

import java.util.ArrayList;
public class Collaboration {

	Person actorA;
	Person actorB;
	ArrayList<Movie> movies;
	
	public Collaboration(Person actorA, Person actorB) {
		this.actorA = actorA;
		this.actorB = actorB;
		this.movies = new ArrayList<Movie>();
	}

	public Person getActorA() {
		return actorA;
	}

	public Person getActorB() {
		return actorB;
	}

	public Double getScore(){
		
		Double score = 0.0;
		
		for (Movie m : movies)
			score += m.getVotes();
		
		return score / movies.size();
	}
	
	public ArrayList<Movie> getCollaborationMovies() {
		return movies;
	}
	
	public void addMovieCollaboration(Movie e) {
		movies.add(e);
	}
	
	public boolean madeMovie(Movie e) 
	{
		for (Movie movie : movies) 
		{
			if (movie == e) {
				return true;
			}
		}
		
		return false;
	}
	
	public String toString() {
		String toPrint = new String();
		
		toPrint += actorA.getName() + " - " + actorB.getName();
		
		toPrint += "\nhave made these movies: \n";
		
		for (Movie movie : movies) {
			toPrint += movie.getTitle() + "\n";
		}
		
		return toPrint + "\n";
	}
}
