package movida.galavottigorini;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map.Entry;

import javax.crypto.Mac;

import java.util.PriorityQueue;

import movida.exceptions.KeyNotFoundException;
import movida.galavottigorini.MovidaCore.MovidaDebug;
import movida.commons.Collaboration;
import movida.commons.Movie;
import movida.commons.Person;
import movida.exceptions.GraphLinkNotFoundException;
import movida.exceptions.GraphNodeNotFoundException;

//TODO: Ottimizza uso dei dati.
//TODO: fare djikstra.
//TODO: CHECK ALL EQUALS IN OTHER STRUCTURES!!!!

public class MovidaGraph<K extends Comparable<K>, E extends Object> extends Map<K, E> {
	
	private class Node
	{
		Elem content;
		ArrayList<Node> linkedNodes;
		Mark mark;
		int distance; //used for bfs and other stuff
		int index;
		
		private Node(K key, E value) {
			content = new Elem(key, value);
			linkedNodes = new ArrayList<Node>();
			mark = Mark.Unmarked;
			index = 0;
			distance = 0;
		}
		
		private boolean hasInLinkedNodes(Node toLookFor) 
		{
			for (Node linked_node : linkedNodes) 
			{
				if (linked_node == toLookFor) return true;
			}
			
			return false;
		}
	} 
	
	private static enum Mark
	{
		Unmarked,
		Visited,
		Unvisited,
		isBeingVisited;
	}
	
	
	private HashMap<Person, ArrayList<Collaboration>> _nodes; //TODO: this is the REAL graph.
	
	private ArrayList<Node> nodes;	//lista di tutti i nodi presenti nel grafo
	private ArrayList<Collaboration> collaborations; //lista di tutte le collaborazioni presenti 
	
	//constructor di un grafo 
	public MovidaGraph() 
	{
		_nodes = new HashMap<>();
		nodes = new ArrayList<Node>();
		collaborations = new ArrayList<Collaboration>();
	}
	
	//crea un collegamento tra i due nodi dati in input
	private void makeLink(Node A, Node B) {
		A.linkedNodes.add(B);
		B.linkedNodes.add(A);
	}
	
	//crea una collaborazione tra i due nodi (già nel grafo) con chiave Persona A e Persona B
	public Collaboration makeCollaboration(Person A, Person B) //TODO: Set to void
	{	
		Collaboration collab = new Collaboration(A, B);
		(_nodes.get(A)).add(collab);
		(_nodes.get(B)).add(collab);
		
		return collab;
	}
	
	
	//retituisce true se trova il nodo con chiave A , false altrimenti
	public boolean checkNodePresence(Person A)
	{
		return _nodes.containsKey(A);
	}
	
	//data una chiave in input trovo e restituisco un vettore di tutti i nodi adiacenti a quello con chiave "key"
	public Person[] getValuesOfAdjiacentNodes(Person A)
	{
		ArrayList<Collaboration> collabs = _nodes.get(A);
			
		Person[] toReturn = new Person[collabs.size()];
			
		int i = 0;
		for (Collaboration cb : collabs) 
		{
			if (cb.getActorA().equals(A)) toReturn[i] = cb.getActorB();				
			else toReturn[i] = cb.getActorA();
			
			i++;
		}
			
		return toReturn;
	}
	
	
	/**Elimina la collaborazione fra A e B
	 * 
	 * @param A primo nodo
	 * @param B secondo nodo
	 * @throws GraphNodeNotFoundException Collaborazione non trovata
	 */
	public void destroyCollab(Person A, Person B) throws GraphLinkNotFoundException //TODO: Create implementation for Graph.NonOrientato
	{	
		Collaboration collab = new Collaboration(A, B);
		
		if (!(_nodes.get(A)).remove(collab) || !(_nodes.get(B)).remove(collab)) 
			throw new GraphLinkNotFoundException();	
	}
	
	/**Crea un nodo avendo una key e un valore.
	 * 
	 * @param key chiave dell'elemento.
	 * @param value valore dell'elemento.
	 */
	@Override
	public void insert(K key, E value)
	{
		Node node = new Node(key, value);
		node.index = nodes.size();
		nodes.add(node);		
	}
	
	/**
	 * Inserisce un "nodo libero" nel grafo, quindi senza collaborazioni.
	 * 
	 * @param: Person A, il nodo da inserire.
	 * 
	 */
	public void insert(Person A)
	{
		_nodes.put(A, new ArrayList<Collaboration>());
	}
	
	
	//TODO: see if you still need this
	/**Aggiunge un nodo al grafo, inserendo il nodo senza
	 * crearne uno nuovo con gli stessi valori (come fa invece insert()).
	 * 
	 * E' usato nella funzione GenericBFS
	 * 
	 * Questo nodo però è un nodo LIBERO, dunque il metodo non si occupa di fare
	 * il link. Ciò spetta all'utente usando la funzione makeLink
	 * 
	 * @param key chiave dell'elemento.
	 * @param value valore dell'elemento.
	 */
	private void insert(Node toInsert) //TODO: remove this.
	{
		nodes.add(toInsert);
	}
	
	/** Inserisce un nodo creandone uno nuovo con una chiave e un valore
	 * 	e collegandolo alla source. Se la source non c'è, allora fa di esso quel nodo.
	 * 
	 * 
	 * 	@params: K , chiave , E valore
	 */
	public void insert_toSource(K k, E e) //inserisce un nodo collegato alla source (se esiste)
											//TODO: remove this.
	{ 
		Node nd = new Node(k,e);
		nd.index = nodes.size();
		
		Node source = nodes.get(0);
		if (source != null && nodes.size() != 0) 
		{
			makeLink(source, nd);
		}
		
		nodes.add(nd);
	}

	@Override
	//elimina il nodo con chiave "k"
	public void delete(K k) {	//TODO: remove this.
		Node node = BFS(k);
		
		ArrayList<Node> arr = node.linkedNodes;
		
		for (Node v : arr) {
			
		}
		
		node = null; //TODO: Search if this is correct (cerca java null garbage collecting)
	}
	
	public void delete(Person A) 
	{
		_nodes.remove(A);
	}
	
	
	@Override
	public void clear() {
		nodes.clear();
		collaborations.clear();
		_nodes.clear();
	}
		
	//faccio partire una BFS dal nodo con chiave "keyToLookFor" data in input 
	public Node BFS(K keyToLookFor) {
		for (Node n : nodes) {
			n.mark = Mark.Unvisited;
		}
		
		/*
		 * java.util.LinkedList implements queue, thus can be used as a queue.
		 * Tutto ciò è possibile grazie ai suoi metodi removeFirst, addLast
		 */
		LinkedList<Node> F = new LinkedList<Node>(); 
		
		Node source = nodes.get(0);
		
		//source insertion
		source.mark = Mark.Visited;
		F.addLast(source);
		
		while(!F.isEmpty()) 
		{
			Node u = F.removeFirst();
			u.mark = Mark.Visited; //nodo visitato
			
			for (Node n : u.linkedNodes) 
			{
				if (n.mark == Mark.Unvisited) {
					if (keyToLookFor.compareTo(n.content.getKey()) == 0) {
						return n;
					}
					
					n.mark = Mark.Visited;
					F.addLast(n);
				}
			}
		}
		
		return null;
	}
	
	/**	Data una chiave ed un intero, calcola la BFS fino ad ampiezza "max_step" 
	 *  e ritorna il vettore di persone raggiunte dalla visita
	 */
	public Person[] BFS_forSteps(K keySource , int max_step) {
		
		Node source_BFSsteps=this.getNodeThatContainsKey(keySource);
		
		for (Node n : nodes) {
			n.mark = Mark.Unvisited;
			n.distance = 0;
		}
		
		LinkedList<Node> F = new LinkedList<Node>(); 
		ArrayList<Person> team = new ArrayList();
		
		//source insertion
		source_BFSsteps.mark = Mark.Visited;
		F.addLast(source_BFSsteps);
		
		while(!F.isEmpty()) 
		{
			Node u = F.removeFirst();
			u.mark = Mark.Visited; //nodo visitato
			
			for (Node n : u.linkedNodes) 
			{
				if (n.mark == Mark.Unvisited ) {
					n.mark = Mark.Visited;
					n.distance = u.distance + 1;
					//se la distanza è maggiore non proseguo la visita da quel nodo
					if (n.distance < max_step)	F.addLast(n);
					
					//inserimento nel vettore di persone
					team.add( (Person) n.content.getValue() );
				}
			}
		}
		//se ho qualcuno nel mio team ritorno il vettore di persone
		if ( ! team.isEmpty() )
			return (Person[]) team.toArray();
		
		//altrimenti null->errore
		return null;
	}
	
	public Person[] getIndirectCollaboratorsOf(K actor) {
		Node source_BFS = getNodeThatContainsKey(actor);
		
		for (Node n : nodes) {
			n.mark = Mark.Unvisited;
			n.distance = 0;
		}
		/*
		 * java.util.LinkedList implements queue, thus can be used as a queue.
		 * Tutto ciò è possibile grazie ai suoi metodi removeFirst, addLast
		 */
		LinkedList<Node> F = new LinkedList<Node>(); 
		ArrayList<Person> team = new ArrayList();
		
		//source insertion
		source_BFS.mark = Mark.Visited;
		F.addLast(source_BFS);
		
		while(!F.isEmpty()) 
		{
			Node u = F.removeFirst();
			u.mark = Mark.Visited; //nodo visitato
			
			for (Node n : u.linkedNodes) 
			{
				if (n.mark == Mark.Unvisited ) {
					n.mark = Mark.Visited;
					n.distance = u.distance + 1;
					//se la distanza è maggiore non proseguo la visita da quel nodo
					F.addLast(n);
					
					//inserimento nel vettore di persone
					if (n.distance>1)
						team.add( (Person) n.content.getValue() );
				}
			}
		}
		//se ho qualcuno nel mio team ritorno il vettore di persone
		if ( ! team.isEmpty() )
			return (Person[]) team.toArray();
		
		//altrimenti null->errore
		return null;
	}
	
	
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

//end of BFS related functions
	
	//DFS usata per implementare CC , per trovare le componenti connesse
	public void DFS_CC (Node u , int[] components , int n) { //TODO: remove this... sorry leti...
		//TODO testare sta roba
		try {
			components[u.index]=n ;
			for ( int i=0 ; i<u.linkedNodes.size() ;i++ ) {
				Node v= u.linkedNodes.get(i);
				if ( components [ v.index ] == 0 ) {
							DFS_CC(v, components, n);
				} 
				
			}
		}catch (Exception e) {
			System.out.println(e.getMessage());
		}
		
	}
	
	/*  trova le componenti connesse e ritorna il vettore che le identifica
	 * 	in sostanza: sia i il mio nodo so che 
	 * 			components[i.index] = numero componente connessa
	 * 
	 * 		!!	se due nodi u e v hanno components[u.index]=components[v.index]
	 * 			 fanno parte quindi della stessa componente connessa	!!
	 * */
	public int[] CC () {
		//TODO testare sta roba
		int[] components = new int [nodes.size()]; //array in cui salviamo chi è di quale componente
		int n_comp = 0 ;
		
		for (int i = 0; i < nodes.size(); i++) components[i]=0;	//inizializzo tutte le componenti a 0
		
		for (int i = 0; i < nodes.size(); i++) {
			//per ogni nodo controllo se appartiene già ad una componente connessa o no
			if ( components[nodes.get(i).index] == 0 ) {
				n_comp++;
				DFS_CC(nodes.get(i), components, n_comp);
			}
		}
		
		return components;
	} 
	
	public Collaboration[] MovidaPrim(Person source) //CONTROLLARE OLLARE OLLARE
	{												//TODO: da testare.
		HashMap<Person, Collaboration> tree = new HashMap<Person, Collaboration>();
		HashMap<Person, Double> distance = new HashMap<Person, Double>();
		
		for (Person pers : tree.keySet()) 
		{
			tree.put(pers, null);
			distance.put(pers, Double.NEGATIVE_INFINITY);
		}
		
		distance.replace(source, 0.0);
		
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
					tree.replace(n, collab);
				}
				else if (collab.getScore() > distance.get(n)) 
				{
					Q.remove(new PrimDistElem(n, distance.get(n)));
					Q.add(new PrimDistElem(n, collab.getScore()));
					distance.replace(n, collab.getScore());
					tree.replace(n, collab);
				}
			}
		}
		return tree.values().toArray(new Collaboration[tree.values().size()]);
	}
	
	/** Ritorn la collaboration fra due attori A e B
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
			if (temp.equals(collaboration))
			{
				return collaboration;
			}
		}
		
		for (Collaboration collaboration : _nodes.get(B)) 
		{
			if (temp.equals(collaboration))
			{
				return collaboration;
			}
		}
		
		return null;
	}
	
	public boolean collaborationExists(Person A, Person B) //TODO: check if you need this.
	{
		for (Collaboration collaboration : collaborations) 
		{
			if ((collaboration.getActorA().equals(A) && collaboration.getActorB().equals(B))
				|| 	(collaboration.getActorA().equals(B) && collaboration.getActorB().equals(A)))
			{
				return true;
			}
		}
		return false;
	}
	
	//ritorna true in caso di presenza di una collaborazione tra i nodi con chiave A e B, false altrimenti
	public boolean checkLinkFromKey(K A, K B) 
	{
		Node toLookFor = getNodeThatContainsKey(A);
		
		if (toLookFor == null) 
		{
			return false;
		}
				
		for (Node linked_nodes : toLookFor.linkedNodes) 
		{
			if (B.compareTo(linked_nodes.content.getKey()) == 0) 
			{
				
				return true;
			}
		}
		
		return false;
	}
	
	//ritorna il nodo con chiave "toLookFor" data in input
	private Node getNodeThatContainsKey(K toLookFor) 
	{
		for (Node node : nodes) 
		{
			if (toLookFor.compareTo(node.content.getKey()) == 0) 
			{
				return node;
			}
		}
		
		return null;
	}
	
	//stampa tutti i nodi del grafo
	public void printNodes() 
	{
		for (Person node : _nodes.keySet()) 
		{
			System.out.println(node.getName());
		}
	}
	
	public Person[] nodesToArray() 
	{
		Person[] arr = new Person[_nodes.size()];
		
		int i = 0;
		for (Object person : _nodes.keySet().toArray()) {
			arr[i] = (Person) person;
			i++;
		}
		
		return arr;
	}
	
	//ritorna l'Elem del nodo avente per chiave "k"
	@Override
	public Elem search(K k) throws KeyNotFoundException{
		if (BFS(k) == null) {
			throw new KeyNotFoundException();
		} else {
			return BFS(k).content;
		}
	}

	@Override
	public void print() //STAMPA USANDO BFS
	{		
		for (Node n : nodes) {
			n.mark = Mark.Unvisited;
		}
		
		LinkedList<Node> F = new LinkedList<Node>(); 
		
		Node source = nodes.get(0);
		
		//source insertion
		source.mark = Mark.Visited;
		F.addLast(source);
		
		
		while(!F.isEmpty()) 
		{
			Node u = F.removeFirst();
			
			for (Node n : u.linkedNodes) 
			{
				if (n.mark == Mark.Unvisited) 
				{			
					System.out.print("\nNode " + n.index + "\n");
					System.out.print("Of key " + n.content + "\n");
					System.out.print("Of Value " + n.content.getKey() + "\n");
					System.out.print("Son of " + u.content.getKey() + ", which is Node " + u.index + "\n");
					
					n.mark = Mark.Visited;
					F.addLast(n);
				}
			}
		}
		
	}
	
	@Override
	//ritorna il numero di nodi totali
	public int getSize() {
		return nodes.size();
	}
	
	public int size() {
		return _nodes.size();
	}
	
	
	@Override
	public Elem[] toArray() {
		Elem[] arr = (Elem[]) Array.newInstance(Elem.class , nodes.size());
		
		for (int i = 0; i < nodes.size(); i++) 
		{
			arr[i] = (nodes.get(i)).content;
		}
		
		return arr;
	}

	@Override
	public Object[] valuesToArray() {
		Object[] arr = (Object[]) Array.newInstance(Object.class , nodes.size());
		
		for (int i = 0; i < nodes.size(); i++) 
		{
			arr[i] = (nodes.get(i)).content.getValue();
		}
		
		return arr;
	}

	@Override
	public Comparable[] keysToArray() {
		Comparable[] arr = (Comparable[]) Array.newInstance(Comparable.class , nodes.size());
		
		for (int i = 0; i < nodes.size(); i++) 
		{
			arr[i] = (nodes.get(i)).content.getKey();
		}
		
		return arr;
	}
	
	
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

	@Override
	public String toString() {	//TODO: delete this
		//TODO: Change to a BFS (PER LETI: avvisami quando arrivi a questa...)
		String toPrint = new String();
		
		toPrint = "\n";
		
		for (Node n : nodes)
		{
			toPrint += "| ";
			toPrint += n.index + " : ";
			
			for (Node k : n.linkedNodes) {
				if (k != null) {
					toPrint += k.index + " ";
				}
			}
			
			toPrint += " |\n";
		}
		return toPrint;
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
			if (!(o instanceof MovidaGraph.PrimDistElem) || o == null) return false;
			MovidaGraph.PrimDistElem toCheck = (MovidaGraph.PrimDistElem) o;
			
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
}
