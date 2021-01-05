package movida.galavottigorini;

import java.util.ArrayList;

import javax.security.auth.Destroyable;

import com.sun.org.apache.bcel.internal.classfile.Node;

import movida.exceptions.HashKeyNotFoundException;
import movida.exceptions.HashTableOverflowException;
import movida.exceptions.KeyNotFoundException;

public class Graph<K extends Comparable<K>, E extends Object> extends Map<K, E> {

	public class Node
	{
		Elem content;
		
		ArrayList<Node> linkedNodes;
		
		public Node(K key, E value, ArrayList<Node> links) {
			content = new Elem(key, value);
			links = linkedNodes;
		}
		
		public Node(K key, E value) {
			content = new Elem(key, value);
			linkedNodes = new ArrayList<Node>();
		}
		
	} 
	
	public Node source;
	public int nodesInGraph;
	
	//constructor
	public Graph(K key, E value) {
		source = new Node(key, value);
	}
	
	public void makeLink(Node A, Node B) {
		A.linkedNodes.add(B);
	}
		
	public void destroyLink(Node A, Node B) throws KeyNotFoundException
	{
		if (!(A.linkedNodes.remove(B))) {
			throw new KeyNotFoundException();
		}
		
	}
	
	public void BFS() {
		
	}
	
	public int nodeCount() 
	{
		return 0;
	}
	
	@Override
	public void insert(K k, E e) {
		Node nd = new Node(k,e);
		makeLink(source, nd);
	}

	@Override
	public void delete(K k) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object search(K k) {
		//BFS implemented here
		return null;
	}

	@Override
	public void print() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getSize() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Map<K, E>.Elem[] toArray() {
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
