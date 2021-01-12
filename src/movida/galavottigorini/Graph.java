package movida.galavottigorini;

import java.lang.reflect.Array;
import java.util.AbstractQueue;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import movida.exceptions.KeyNotFoundException;
import movida.galavottigorini.Map.Elem;
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
		
		public Node(K key, E value, ArrayList<Node> links) {
			content = new Elem(key, value);
			links = linkedNodes;
			mark = Mark.Unmarked;
			index = 0;
			distance = 0;
		}
		
		public Node(K key, E value) {
			content = new Elem(key, value);
			linkedNodes = new ArrayList<Node>();
			mark = Mark.Unmarked;
			index = 0;
			distance = 0;
		}
		
		public void removeNodeLink(Node A) throws GraphNodeNotFoundException {
			if (!linkedNodes.remove(A)) 
			{
				throw new GraphNodeNotFoundException();
			}
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
		NonOrientato;
	}
	
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
	
	private Node source;
	private ArrayList<Node> nodes;
	private ArrayList<Link> links;
	private GraphType graphType;

	//constructor
	public Graph(K key, E value, GraphType gt) {
		source = new Node(key, value);
		nodes = new ArrayList<Node>();
		links = new ArrayList<Link>();
		nodes.add(source);
		graphType = gt;
	}
	
	public Graph() {	//crea un grafo vuoto
		nodes = new ArrayList<Node>();
		links = new ArrayList<Link>();
		nodes.add(source);
		graphType = GraphType.NonOrientato;
	}
	
	public void setSource(K key, E value) {
		source = new Node(key, value);
	}
	
	public void makeLink(Node A, Node B) {
		A.linkedNodes.add(B);
		links.add(new Link(A, B));
		
		if (graphType == GraphType.NonOrientato) 
		{
			B.linkedNodes.add(A);
			links.add(new Link(B, A));
		}
	}
	
	public void makeLinkWithKey(K key1, K key2) {
		Node A = findNodeInArrayList(key1);
		Node B = findNodeInArrayList(key2);
		
		makeLink(A, B);
	}
	
	private Node findNodeInArrayList(K key) 
	{
		for (Node n : nodes) 
		{
			if (n.content.getKey() == key) 
			{
				return n;
			}
		}
		
		return null;
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
	
	public void createNode(K key, E value)
	{
		Node node = new Node(key, value);
		node.index = nodes.size();
		nodes.add(node);		
	}
	
	
	//TODO: see if you still need this
	/**Aggiunge un nodo al grafo, inserendo il nodo senza
	 * crearne uno nuovo con gli stessi valori (come fa invece insert()).
	 * 
	 * Questo nodo però è un nodo LIBERO, dunque il metodo non si occupa di fare
	 * il link. Ciò spetta all'utente usando la funzione makeLink
	 * 
	 * @param key chiave dell'elemento.
	 * @param value valore dell'elemento.
	 */
	public void insertNode(Node toInsert) {
		nodes.add(toInsert);
	}
	
	public int countLinks() 
	{
		return links.size();
	}
	
	public Object[] getNodes() 
	{
		return nodes.toArray();
	}
	
	/** Inserisce un nodo creandone uno nuovo con una chiave e un valore.
	 * 
	 * 	@params: K , chiave , E valore
	 */
	@Override
	public void insert(K k, E e) { //inserisce un nodo alla source
		Node nd = new Node(k,e);
		nd.index = nodes.size();
		nodes.add(nd);
		makeLink(source, nd);
	}

	@Override
	public void delete(K k) {
		Node node = BFS(k);
		
		ArrayList<Node> arr = node.linkedNodes;
		
		for (Node v : arr) {
			
		}
		
		node = null; //TODO: Search is this is correct
		
	}
	
	@Override
	public void clear() {
		nodes.clear();
		links.clear();
		source = null;
	}
	
	//BFS Functions
	
	//TODO: Decide to keep GenericBFS or not
	public Graph<K, E> BasicBFS(Node source) {
		for (Node n : nodes) {
			n.mark = Mark.Unvisited;
			n.distance = 0;
		}
		
		/*
		 * java.util.LinkedList implements queue, thus can be used as a queue.
		 * Tutto ciò è possibile grazie ai suoi metodi removeFirst, addLast
		 */
		LinkedList<Node> F = new LinkedList<Node>(); 

		Graph<K, E> tree = new Graph<K, E>(); //Essenzialmente uso un grafo come un albero, dato che non voglio implementare una nuova struttura dati :)
		
		//source insertion
		source.mark = Mark.Visited;
		F.addLast(source);
		tree.insertNode(source);
		
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
					tree.insertNode(n);
					tree.makeLink(u, n);
				}
			}
		}
		return tree;
	}

	public Node BFS(Elem toLookFor) {
		for (Node n : nodes) {
			n.mark = Mark.Unvisited;
		}
		
		/*
		 * java.util.LinkedList implements queue, thus can be used as a queue.
		 * Tutto ciò è possibile grazie ai suoi metodi removeFirst, addLast
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
		
	public Node BFS(K keyToLookFor) {
		for (Node n : nodes) {
			n.mark = Mark.Unvisited;
		}
		
		/*
		 * java.util.LinkedList implements queue, thus can be used as a queue.
		 * Tutto ciò è possibile grazie ai suoi metodi removeFirst, addLast
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
	
	//end of BFS related functions
	
	
	@Override
	public Elem search(K k) throws KeyNotFoundException{
		if (BFS(k) == null) {
			throw new KeyNotFoundException();
		} else {
			return BFS(k).content;
		}
	}

	@Override
	public void print() {
		System.out.print("Graph print start initialized.\n");
		
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
	
	
	public void DijkstraShortestPath() {
		//TODO: Implement this
	}

	@Override
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
	
	
	//TODO: Remove this after testing
	public void debugPrint() {
		for (Node node : nodes) {
			System.out.print(node.index + "\n");
		}
	}
	
	public void printAllLinks() {
		for (Link link : links) {
			System.out.print(link.A.content.getKey() + " - " + link.B.content.getKey() + "\n");
		}
	}

	@Override
	public String toString() {
		//TODO: Change to a BFS
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
