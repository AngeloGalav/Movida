package movida.galavottigorini;

import java.util.ArrayList;

import movida.exceptions.KeyNotFoundException;
import movida.exceptions.GraphLinkNotFoundException;
import movida.exceptions.GraphNodeNotFoundException;

//TODO: Adjust functions!!

public class Graph<K extends Comparable<K>, E extends Object> extends Map<K, E> {
	
	public class Node
	{
		Elem content;
		
		int index;
		
		ArrayList<Node> linkedNodes;
		
		Mark mark;
		
		public Node(K key, E value, ArrayList<Node> links) {
			content = new Elem(key, value);
			links = linkedNodes;
			mark = Mark.Unmarked;
			index = 0;
		}
		
		public Node(K key, E value) {
			content = new Elem(key, value);
			linkedNodes = new ArrayList<Node>();
			mark = Mark.Unmarked;
			index = 0;
		}
		
		public void setMark(Mark mark) 
		{
			this.mark = mark;
		}
		
		public Mark getMark()
		{
			return mark;
		}
		
		public void setIndex(int index) 
		{
			this.index = index;
		}
		
		public int getIndex() 
		{
			return index;
		}
		
		public void addNodeLink(Node A) {
			linkedNodes.add(A);
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
		Black,
		Grey,
		White;
		
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
		
		public Mark getMark(){
			return mark;
		}
		
		public double getWeight(){
			return weight;
		}
		
		public Node getA() {
			return A;
		}
		
		public Node getB() {
			return B;
		}		
		
		public void setMark(Mark mark) 
		{
			this.mark = mark;
		}
	}
	
	private Node source;
	private ArrayList<Node> nodes;
	private ArrayList<Link> links;
	private int nodesInGraph;
	private int linksInGraph;
	
	//constructor
	public Graph(K key, E value) {
		source = new Node(key, value);
		nodes = new ArrayList<Node>();
		links = new ArrayList<Link>();
		nodes.set(0, source);
		nodesInGraph = 1;
		linksInGraph = 0;
	}
	
	public Graph() {	//crea un grafo vuoto
		nodes = new ArrayList<Node>();
		links = new ArrayList<Link>();
		nodesInGraph = 0;
		linksInGraph = 0;
	}
	
	public void makeLink(Node A, Node B) {
		A.linkedNodes.add(B);
		links.add(new Link(A, B));
		linksInGraph++;
	}
	
	
	/**Elimina un arco che va da A a B
	 * 
	 * @param A primo nodo
	 * @param B secondo nodo
	 * @throws GraphNodeNotFoundException arco non trovato
	 */
	public void destroyLink(Node A, Node B) throws GraphLinkNotFoundException
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
		nodesInGraph++;
		node.setIndex(nodesInGraph);
		nodes.add(node);		
	}
	
	public int nodeCount() 
	{
		return nodesInGraph;
	}
	
	public int linkCount() 
	{
		return linksInGraph;
	}
	
	public Object[] getNodes() 
	{
		return nodes.toArray();
	}
	
	@Override
	public void insert(K k, E e) { //inserisce un nodo alla source
		Node nd = new Node(k,e);
		nodesInGraph++;
		nd.setIndex(nodesInGraph);
		makeLink(source, nd);
	}

	@Override
	public void delete(K k) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void clear() {
		nodes.clear();
		links.clear();
		nodesInGraph = 0;
		linksInGraph = 0;
		source = null;
		
	}

	@Override
	public Elem search(K k) {
		//BFS implemented here
		return null;
	}

	@Override
	public void print() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getSize() {
		return nodeCount();
	}

	@Override
	public Elem[] toArray() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object[] valuesToArray() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Comparable[] keysToArray() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}
	
	
	

}
