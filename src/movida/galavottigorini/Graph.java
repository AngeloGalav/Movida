package movida.galavottigorini;
import java.lang.reflect.Array;
import java.util.AbstractQueue;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import movida.exceptions.KeyNotFoundException;
import movida.galavottigorini.Map.Elem;
import movida.galavottigorini.MovidaCore.MovidaDebug;
import movida.commons.Collaboration;
import movida.commons.Person;
import movida.exceptions.GraphLinkNotFoundException;
import movida.exceptions.GraphNodeNotFoundException;

//TODO: risolvi il problema con i nodi source!!!
//TODO: add grafo non orientato functions!!!
//TODO: decide what to do with links!!!

public class Graph<K extends Comparable<K>, E extends Object> extends Map<K, E> {
	
	private class Node
	{
		Elem content;
		
		int index;
		
		ArrayList<Node> linkedNodes;
		
		Mark mark;
		
		int distance; //used for bfs and other stuff
		
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
				if (linked_node == toLookFor) 
				{
					return true;
				}
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
	
	public static enum GraphType
	{
		Orientato,
		NonOrientato,
		MovidaGraph;
	}
	
	
	/** Link is similar to the collaboration data type (it's the exact same thing in fact), though its more generic
	 * 
	 * @author Angelo
	 *
	 */
	private class Link
	{
		Node A;
		Node B;
		
		Mark mark;
		
		double weight;
		
		private Link(Node A, Node B) {
			this.A = A;
			this.B = B;
			weight = 0;
			mark = Mark.Unmarked;
		}
		
		private Link(Node A, Node B, double w) {
			this.A = A;
			this.B = B;
			weight = w;
			mark = Mark.Unmarked;
		}
	}
	
	private Node source;	//nodo dal quale far partire eventuali visite
	private ArrayList<Node> nodes;	//lista di tutti i nodi presenti nel grafo
	private ArrayList<Link> links;	//lista di tutti i collegamenti tra nodi presenti nel grafo
	
	private ArrayList<Collaboration> collaborations; //lista di tutte le collaborazioni presenti 
	private GraphType graphType;	//defisisce il tipod i grafo (orientato, non orientato e GrafoMovida) 

	//constructor di un grafo del tipo gt dato in input
	public Graph(GraphType gt) {
		nodes = new ArrayList<Node>();
		links = new ArrayList<Link>();
		collaborations = new ArrayList<Collaboration>();
		graphType = gt;
		source = null;
	}
	
	//constructor di un grafo del tipo NonOrientato vuoto
	public Graph() {	//crea un grafo vuoto
		nodes = new ArrayList<Node>();
		links = new ArrayList<Link>();
		collaborations = new ArrayList<Collaboration>();
		source = null;
		graphType = GraphType.NonOrientato;
	}
	
	//imposta la sorgente del grafo come il nodo con chiave "key" e valore "value" passati in input
	public void setSource(K key, E value) 
	{
		source = new Node(key, value);
	}
	
	//crea un collegamento tra i due nodi dati in input
	private void makeLink(Node A, Node B) {
		A.linkedNodes.add(B);
		links.add(new Link(A, B));
		
		if (graphType == GraphType.NonOrientato || graphType == GraphType.MovidaGraph) 
		{
			B.linkedNodes.add(A);
			links.add(new Link(B, A));
		}
	}
	
	//TODO: Decide if it's right to make a makelink method using the source node.
	//crea un collegamento tra i due nodi (gi� presenti nel grafo) con chiavi "key1" e "key2" passate in input
	public void makeLink(K key1, K key2) {
		Node A = findNodeInArrayList(key1);
		Node B = findNodeInArrayList(key2);
				
		makeLink(A, B);
	}
	
	//crea una collaborazione tra i due nodi (gi� nel grafo) con chiave "key1" e "key2"
	public Collaboration makeCollaboration(K key1, K key2) 
	{
		Node A = findNodeInArrayList(key1);
		Node B = findNodeInArrayList(key2);
		
		Collaboration collab = new Collaboration((Person) A.content.getValue(),(Person) B.content.getValue());
		collaborations.add(collab);
		
		makeLink(A, B);
		
		return collab;
	}
	
	//trova il nodo con chiave "key" nel mio grafo e lo restituisce
	private Node findNodeInArrayList(K key)
	{
		for (Node n : nodes) 
		{
			if (key.compareTo(n.content.getKey()) == 0) 
			{
				return n;
			}
		}
		
		return null;
	}
	
	//retituisce true se trova il nodo con chiave "key" , false altrimenti
	public boolean checkNodePresence(K key)
	{
		for (Node n : nodes) 
		{
			if (key.compareTo(n.content.getKey()) == 0) 
			{
				return true;
			}
		}
		
		return false;
	}
	
	//data una chiave in input trovo e restituisco un vettore di tutti i nodi adiacenti a quello con chiave "key"
	public Object[] getAllValuesOfAdjiacentNodes(K key) throws KeyNotFoundException
	{
			Node node = findNodeInArrayList(key);
					
			if (node == null) 
			{
				throw new KeyNotFoundException();
			}
			
			//TODO: make in new way gay
			
			Object[] toReturn = new Object[node.linkedNodes.size()];
			
			int i = 0;
			for (Node nd : node.linkedNodes) 
			{
				toReturn[i] = nd.content.getValue();
				i++;
			}
			
			return toReturn;
	}
	
	
	/**Elimina un arco che va da A a B
	 * 
	 * @param A primo nodo
	 * @param B secondo nodo
	 * @throws GraphNodeNotFoundException arco non trovato
	 */
	public void destroyLink(Node A, Node B) throws GraphLinkNotFoundException //TODO: Create implementation for Graph.NonOrientato
	{	
		Link toLookFor = new Link(A, B);
		if (!links.remove(toLookFor)) 
		{
			throw new GraphLinkNotFoundException();
		}
		
		A.linkedNodes.remove(B);
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
		if (nodes.size() == 0) 
		{
			source = node;
		}
		nodes.add(node);		
	}
	
	
	//TODO: see if you still need this
	/**Aggiunge un nodo al grafo, inserendo il nodo senza
	 * crearne uno nuovo con gli stessi valori (come fa invece insert()).
	 * 
	 * E' usato nella funzione GenericBFS
	 * 
	 * Questo nodo per� � un nodo LIBERO, dunque il metodo non si occupa di fare
	 * il link. Ci� spetta all'utente usando la funzione makeLink
	 * 
	 * @param key chiave dell'elemento.
	 * @param value valore dell'elemento.
	 */
	private void insert(Node toInsert) 
	{
		if (nodes.size() == 0) 
		{
			source = toInsert;
		}
		nodes.add(toInsert);
	}
	
	//ritorna il numero di collegamenti complessivi presenti nel grafo
	public int countLinks() 
	{
		//TODO: Insert grafo orientato option
		return links.size();
	}
	
	/** Inserisce un nodo creandone uno nuovo con una chiave e un valore
	 * 	e collegandolo alla source. Se la source non c'�, allora fa di esso quel nodo.
	 * 
	 * 
	 * 	@params: K , chiave , E valore
	 */
	public void insert_toSource(K k, E e) //inserisce un nodo collegato alla source (se esiste)
	{ 
		Node nd = new Node(k,e);
		nd.index = nodes.size();
		
		if (source != null && nodes.size() != 0) 
		{
			makeLink(source, nd);
		} else 
		{
			source = nd;
		}
		nodes.add(nd);
	}

	@Override
	//elimina il nodo con chiave "k"
	public void delete(K k) {
		Node node = BFS(k);
		
		ArrayList<Node> arr = node.linkedNodes;
		
		for (Node v : arr) {
			
		}
		
		node = null; //TODO: Search if this is correct (cerca java null garbage collecting)
	}
	
	//TODO: fai ez delete.
	public void delete() {
	/*
		Node node = new Node();
		
		ArrayList<Node> arr = node.linkedNodes;
		
		for (Node v : arr) {
			//TODO: leti fai questo
		}
		
		node = null; //TODO: Search if this is correct (cerca java null garbage collecting)
	*/
	}
	
	
	//TODO: fai binary search
	
	@Override
	public void clear() {
		nodes.clear();
		links.clear();
		source = null;
	}
	
	//TODO: Decide to keep GenericBFS or not
	public Graph<K, E> BasicBFS(Node source) {
		for (Node n : nodes) {
			n.mark = Mark.Unvisited;
			n.distance = 0;
		}
		
		/*
		 * java.util.LinkedList implements queue, thus can be used as a queue.
		 * Tutto ci� � possibile grazie ai suoi metodi removeFirst, addLast
		 */
		LinkedList<Node> F = new LinkedList<Node>(); 

		Graph<K, E> tree = new Graph<K, E>(); //Essenzialmente uso un grafo come un albero, dato che non voglio implementare una nuova struttura dati :)
		
		//source insertion
		source.mark = Mark.Visited;
		F.addLast(source);
		tree.insert(source);
		
		while(!F.isEmpty()) 
		{
			Node u = F.removeFirst();
			u.mark = Mark.Visited; //nodo visitato
			
			for (Node n : u.linkedNodes) 
			{
				if (n.mark == Mark.Unvisited) {
					n.mark = Mark.Visited;
					n.distance = u.distance + 1;
					F.addLast(n);
					
					//inserimento nell'albero BFS
					tree.insert(n);
					tree.makeLink(u, n);
				}
			}
		}
		return tree;
	}

	//faccio partire una BFS dal nodo contenente l'Elem dato in input ( che identifica la persona dalla quale far partire la BFS) 
	public Node BFS(Elem toLookFor) {
		for (Node n : nodes) {
			n.mark = Mark.Unvisited;
		}
		
		/*
		 * java.util.LinkedList implements queue, thus can be used as a queue.
		 * Tutto ci� � possibile grazie ai suoi metodi removeFirst, addLast
		 */
		LinkedList<Node> F = new LinkedList<Node>(); 
		
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
					if (toLookFor == n.content) {
						return n;
					}
					
					n.mark = Mark.Visited;
					F.addLast(n);
				}
			}
		}
		return null;
	}
		
	//faccio partire una BFS dal nodo con chiave "keyToLookFor" data in input 
	public Node BFS(K keyToLookFor) {
		for (Node n : nodes) {
			n.mark = Mark.Unvisited;
		}
		
		/*
		 * java.util.LinkedList implements queue, thus can be used as a queue.
		 * Tutto ci� � possibile grazie ai suoi metodi removeFirst, addLast
		 */
		LinkedList<Node> F = new LinkedList<Node>(); 
		
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
		/*
		 * java.util.LinkedList implements queue, thus can be used as a queue.
		 * Tutto ci� � possibile grazie ai suoi metodi removeFirst, addLast
		 */
		LinkedList<Node> F = new LinkedList<Node>(); 
		ArrayList<Person> team =new ArrayList();
		
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
					//se la distanza � maggiore non proseguo la visita da quel nodo
					if (  n.distance < max_step )
						F.addLast(n);
					
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

//end of BFS related functions
	
	
	//DFS usata per implementare CC , per trovare le componenti connesse
	public void DFS_CC (Node u , int[] components , int n) {
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
		int[] components= new int [nodes.size()]; //array in cui salviamo chi � di quale componente
		int n_comp=0 ;
		
		for (int i=0 ; i<nodes.size() ; i++) {	components[i]=0;}	//inizializzo tutte le componenti a 0
		for (int i=0 ; i<nodes.size() ; i++) {
			//per ogni nodo controllo se appartiene gi� ad una componente connessa o no
			if ( components[nodes.get(i).index] == 0 ) {
				n_comp++;
				this.DFS_CC( nodes.get(i) , components , n_comp);
			}
		}
		return components;
	} 
	
	//ritorna la collaborazione tra due attori
	public Collaboration findCollaboration(Person A, Person B) 
	{
		for (Collaboration collaboration : collaborations) 
		{
			//TODO: Why equals without the string type is not working?
			if (collaboration.getActorA().getName().equals(A.getName()) && collaboration.getActorB().getName().equals(B.getName())
				|| 	collaboration.getActorA().getName().equals(B.getName()) && collaboration.getActorB().getName().equals(A.getName()))
			{
				return collaboration;
			}
		}
		return null;
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
				if (graphType == GraphType.NonOrientato || graphType == GraphType.MovidaGraph)
				{
					return linked_nodes.hasInLinkedNodes(toLookFor);
				} 
				
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
		for (Node node : nodes) {
			System.out.println(node.content.getKey());
		}
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
	public void print() 
	{		
		for (Node n : nodes) {
			n.mark = Mark.Unvisited;
		}
		
		LinkedList<Node> F = new LinkedList<Node>(); 
		
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
	
	public void printAllLinks() {
		for (Link link : links) {
			System.out.print(link.A.content.getKey() + " - " + link.B.content.getKey() + "\n");
		}
	}
	public void printCollaborations() {
		for (Collaboration collab : collaborations) {
			System.out.print(collab.toString());
		}
	}

	@Override
	public String toString() {
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

}
