package movida.galavottigorini;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map.Entry;

import java.util.PriorityQueue;

import movida.galavottigorini.MovidaCore.MovidaDebug;
import movida.commons.Collaboration;
import movida.commons.Movie;
import movida.commons.Person;
import movida.exceptions.GraphNodeNotFoundException;

//TODO: Ottimizza uso dei dati.
//TODO: CHECK ALL EQUALS IN OTHER STRUCTURES!!!!

//TODO: CHECK IF IS STILL NECESSARY TO DO FIND COLLABS
public class MovidaGraph {
	
	private static enum Mark
	{
		Unmarked,
		Visited,
		Unvisited,
		isBeingVisited;
	}
	
	private HashMap<Person, ArrayList<Collaboration>> _nodes;
		
	//constructor di un grafo 
	public MovidaGraph() 
	{
		_nodes = new HashMap<Person, ArrayList<Collaboration>>();
	}
	
	/**Crea una collaborazione tra i due nodi (già nel grafo) con chiave Persona A e Persona B
	 * 
	 * @param A prima persona.
	 * @param B seconda persona.
	 * @return collaborazione tra le due persone.
	 */
	public Collaboration makeCollaboration(Person A, Person B)
	{	
		Collaboration collab = new Collaboration(A, B);
		(_nodes.get(A)).add(collab);
		(_nodes.get(B)).add(collab);
		
		return collab;
	}
	
	/**Controlla la presenza di un nodo all'interno del grafo
	 * 
	 * @param A.
	 * @return true se il nodo e' preesnte nel grafo, false altrimenti.
	 */
	public boolean checkNodePresence(Person A)
	{
		return _nodes.containsKey(A);
	}
	
	 
	/**	Restituisco il vettore di Person contenuti nei nodi adiacenti a quello contenete A.
	 * 
	 * @param A persona di cui trovare i nodii adiacenti.
	 * @return vettore di persone "adiacenti" (i loro nodi lo sono) a quella in input.
	 */
	 public Person[] getValuesOfAdjiacentNodes(Person A)
	{
		ArrayList<Collaboration> collabs = _nodes.get(A);
		Person[] toReturn = new Person[0];
		
		if (collabs != null) 
		{
			toReturn = new Person[collabs.size()];
				
			int i = 0;
			for (Collaboration cb : collabs) 
			{
				if (cb.getActorA().equals(A)) toReturn[i] = cb.getActorB();				
				else toReturn[i] = cb.getActorA();
				
				i++;
			}
		}
			
		return toReturn;
	}
	
	
	/**
	 * Inserisce un "nodo libero" nel grafo, quindi senza collaborazioni.
	 * 
	 * @param A nodo da inserire.
	 */
	public void insert(Person A)
	{
		_nodes.put(A, new ArrayList<Collaboration>());
	}
	
	public void clear() 
	{
		_nodes.clear();
	}
	
	/** Cerca il Team (collaboratori diretti ed indirett) di un attore
	 *
	 *	@param source attore di cui cerco il team.
	 *	@return vettore di tutti i sui collaboratori.
	 */
	public Person[] MovidaBFS(Person source) 
	{		
		HashMap<Person, Mark> marks = new HashMap<Person, Mark>();
				
		for (java.util.Map.Entry<Person, ArrayList<Collaboration>> entry : _nodes.entrySet()) 
		{
			marks.put(entry.getKey(), Mark.Unvisited);
		}
		
		/*
		 * java.util.LinkedList implements queue, thus can be used as a queue.
		 * Tutto ciò è possibile grazie ai suoi metodi removeFirst, addLast
		 */
		LinkedList<Person> F = new LinkedList<Person>(); 
		ArrayList<Person> team = new ArrayList<Person>();
		
		//source insertion
		marks.replace(source, Mark.Visited);
		
		F.addLast(source);
		
		while(!F.isEmpty()) 
		{
			Person u = F.removeFirst();
			marks.replace(u, Mark.Visited); //nodo visitato
			
			for (Person n : getValuesOfAdjiacentNodes(u)) 
			{
				if (marks.get(n) == Mark.Unvisited ) 
				{
					marks.replace(n, Mark.Visited);
					team.add(n);
					F.addLast(n);
				}
			}
		}

		return team.toArray(new Person[team.size()]);
	}

	/** Funzione che usando l'algoritmo di Prim per i MST ritorna il vettore di collaborazioni 
	 *	che rappresentano l'albero di copertura minima del mio grafo (per ottenere il 
	 * 	Minimum Spanning Tree l'algoritmo ha subito alcune modifiche dall'originale per i Minimun Spanning Tree)
	 *	
	 *	@param source chiave del nodo dal quale dovrà partire il MST
	 *	@return vettore di collaborazioni che costituiscono il MST
	 */
	public Collaboration[] MovidaPrim(Person source)//TODO: da testare.
	{
		HashMap<Person, Person> tree = new HashMap<Person, Person>();
		HashMap<Person, Double> distance = new HashMap<Person, Double>();
		
		for (Person pers : _nodes.keySet()) 
		{
			tree.put(pers, null);
			distance.put(pers, Double.NEGATIVE_INFINITY);
		}
		
		distance.replace(source, Double.POSITIVE_INFINITY);
		
		PriorityQueue<PrimDistElem> Q = new PriorityQueue<PrimDistElem>(new PrimComp());
		Q.add(new PrimDistElem(source, distance.get(source)));
				
		while (!Q.isEmpty()) 
		{
			Person u = Q.poll().per;			
			for (Person n : getValuesOfAdjiacentNodes(u)) 
			{
				Collaboration collab = findCollaboration(n, u);
				if (distance.get(n) == Double.NEGATIVE_INFINITY) 
				{
					Q.add(new PrimDistElem(n, collab.getScore()));
					distance.replace(n, collab.getScore());
					tree.replace(n, u);
				}
				else if (collab.getScore() > distance.get(n) && !(n.equals(tree.get(u)) )) 
				{
					Q.remove(new PrimDistElem(n, distance.get(n)));
					Q.add(new PrimDistElem(n, collab.getScore()));
					distance.replace(n, collab.getScore());
					Person replaced = tree.replace(n, u);
				}
			}
		}
		
		ArrayList<Collaboration> collabs = new ArrayList<Collaboration>();
		for (Entry<Person, Person> entry : tree.entrySet()) 
		{
			if (entry.getValue() != null) 
			{
				collabs.add(findCollaboration(entry.getKey(), entry.getValue()));
			}
		}
		
		return collabs.toArray(new Collaboration[collabs.size()]);
	}
	
	
	/** Ritorna la collaboration fra due attori A e B
	 * 
	 * @param A, primo attore.
	 * @param B, secondo attore.
	 * @return collaboration, la collaborazione.
	 */
	public Collaboration findCollaboration(Person A, Person B) 
	{
		Collaboration temp = new Collaboration(A, B);
		
		for (Collaboration collaboration : _nodes.get(A)) 
		{
			if (temp.equals(collaboration)) return collaboration;
		}
		
		for (Collaboration collaboration : _nodes.get(B)) 
		{
			if (temp.equals(collaboration)) return collaboration;
		}
		
		return null;
	}
	
	/** Elimina il film nelle collaborazioni del cast.
	 * 
	 * @param mov, il film da eliminare.
	 */
	public void deleteMovieFromCollaborations(Movie mov) 
	{	
		Person[] castArray = mov.getCast();
		
		for (int i = 0; i < castArray.length; i++) 
		{
			ArrayList<Collaboration> collabs = _nodes.get(castArray[i]);
			
			if (collabs != null) 
			{
				for (Iterator<Collaboration> iterator = collabs.iterator(); iterator.hasNext(); )//uso gli iterator per eviatare la concurrentModificationException
				{
					Collaboration temp = iterator.next();
					temp.removeMovieCollaboration(mov);
					
					if (temp.getCollaborationMovies().size() < 1) iterator.remove();
					
					Person A = temp.getActorA();
					Person B = temp.getActorB();
					
					if (_nodes.get(A) != null && _nodes.get(A).size() == 0) _nodes.remove(A);
					if (_nodes.get(B) != null && _nodes.get(B).size() == 0) _nodes.remove(B);
				}
			}
		}
	}
	
	private class PrimDistElem
	{
		Person per;
		Double maxScore;
		
		private PrimDistElem(Person per, Double maxScore) 
		{
			this.per = per;
			this.maxScore = maxScore;
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
			if (!(o instanceof PrimDistElem) || o == null) return false;
			PrimDistElem toCheck = (PrimDistElem) o;
			
			return per.equals(per);
		}
	}
	
	
	private class PrimComp implements Comparator<PrimDistElem>
	{	
		public int compare(PrimDistElem e1, PrimDistElem e2)
	    {
			return e1.maxScore.compareTo(e2.maxScore) * -1;
	    }
	}
	
	
	/**Conta nodi del mio grafo
	 *
	 * @return numero nodi tot.
	 */
	public int getSize() 
	{
		return _nodes.size();
	}
	

	///FUNZIONI DI DEBUGGING
	
	/**Stampa tutte le collaborazioni per ciascun nodo.
	 *
	 * @return numero nodi tot.
	 */
	public void printCollaborations() 
	{
		for (Entry<Person, ArrayList<Collaboration>> entry : _nodes.entrySet()) 
		{
			MovidaDebug.Log("\nCollaborations of : " + entry.getKey().getName() + "\n");
			for (Collaboration collab : entry.getValue()) 
			{
				System.out.print(collab.toString());
			}
		}
	}
	
	/**Stampa tutti i nodi del grafo
	*/
	public void printNodes() 
	{
		for (Person node : _nodes.keySet()) 
		{
			System.out.println(node.getName());
		}
	}
	
	public void printCollabsofNode(Person node) 
	{
		if (_nodes.get(node) != null) {
		MovidaDebug.printArray(_nodes.get(node).toArray(new Collaboration[_nodes.get(node).size()]));
		}else {
			MovidaDebug.Log("No collabs, node is not present in graph.\n");
		}
	}
}
