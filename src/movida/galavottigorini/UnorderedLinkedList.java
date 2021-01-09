package movida.galavottigorini;

import java.util.ArrayList;

import java.lang.reflect.Array;

public class UnorderedLinkedList<K extends Comparable<K>, E extends Object> extends Map<K,E>
{
	private class ListElem extends Elem{
		
		private ListElem next;
		private ListElem prev; 
		
		//constructors
		public ListElem (K key, E value)
		{
			super(key, value);
			next = null;
			prev = null;
		}
		
		public void setNext(ListElem next) {
			this.next = next;
		}
		
		public void setPrev(ListElem prev) {
			this.prev = prev;
		}
		
	}
	
	private ListElem root;
	private int size;
	
	//class constructor
	public UnorderedLinkedList() {
		root = null;
	}
	
	/** Ritorna la radice della lista.
	 * 
	 * 	@return: root, la radice della lista
	 */
	public ListElem getRoot() 
	{
		return root;
	}
	
	/** Setter della radice della lista.
	 * 
	 *  @param root
	 */
	public void setRoot(Elem root) 
	{
		ListElem rt = new ListElem(root.getKey(), root.getValue()); 
		this.root = rt;
	}
	
	@Override
	public void insert(K k, E e)
	{
		ListElem insListElement = new ListElem(k,e);
		
		if (root == null) 
		{
			root = insListElement;
		} 
		else 
		{ 
			insListElement.next = root;
			root.prev = insListElement;
			root = insListElement;
			root.prev = null;
		}
		
		size++;
	}
	
	
	
	//TODO: Vedere se lasciare questa funzione o no
	/* Prende un elemento e lo inserisce alla lista
	 * Questa metodo è stato aggiunto per dare eleganza al codice
	 * 
	 * param: Elem el
	 * 
	 */
	public void insert(Elem el)
	{
		ListElem insListElement = new ListElem(el.getKey(), el.getValue());
		
		if (root == null) 
		{
			root = insListElement;
		} 
		else 
		{ 
			insListElement.next = root;
			root.prev = insListElement;
			root = insListElement;
			root.prev = null;
		}
		
		size++;
	}
	
	/* Permette di inserire gli elementi in coda
	 * 
	 * params: chiave k dell'elemento, valore e dell'elemento
	 * 
	 */
	public void tailInsert(K k, E e)
	{
		ListElem insListElement = new ListElem(k,e);
				
		if (root == null) 
		{
			root = insListElement;
		}
		else 
		{
			ListElem pointer = root;
			
			while (pointer.next != null) 
			{
				pointer = pointer.next;
			}
			
			//pointer is now tail 
			
			pointer.next = insListElement;
			insListElement.prev = pointer;
		}
		size++;
	}
	
	@Override
	public void delete (K k) 
	{
		ListElem ListElem = (ListElem) search(k);
		if (ListElem != null) 
		{
			ListElem nextListElem = ListElem.next;
			ListElem prevListElem = ListElem.prev;
			
			prevListElem.next = nextListElem;
			nextListElem.prev = prevListElem;
			size--;
		}
		
	}
	
	@Override
	public Elem search(K k) 
	{	
		if (root == null) return null;
		else {
			ListElem pointer = root;
			while (pointer != null) 
			{
				if (pointer.getKey() == k) 
				{
					break;
				}
				
				pointer = pointer.next;
				
			}
			return pointer;
		}
	}
	
	/**	Svuota la lista completamente.
	 * 
	 */
	@Override
	public void clear()
	{
		root = null;
		size = 0;
	}
	
	@Override
	public int getSize() {
		return size;
	}
	
	@Override
	public Elem[] toArray()
	{
		Elem[] arr = (Elem[]) Array.newInstance(Elem.class , size);
		ListElem cursor = root; 
		
		int i = 0;
		while (cursor != null) {
			arr[i] = cursor;
			
			i++;
			cursor = cursor.next;
		}

		return arr;
	}
	
	
	/**	Appende elementi di una lista a questa
	 *
	 * 	@param: UnorderedLinkedList<K, E> list, ovvero la lista da appendere a quella principale
	 */	
	public void append(UnorderedLinkedList<K, E> list) {
		
		ListElem cursor = root;
		
		while (cursor.next != null) {
			cursor = cursor.next;
		}
		
		cursor.next = list.getRoot();
		(list.getRoot()).setPrev(cursor);
	}
	
	
	/*	Aggiunge elementi da un array preservando il loro ordine nell'array
	 *
	 * 	param: Elem[] arr
	 */
	public void AddElementsFromArray(Elem[] arr) 
	{
		for (Elem elem : arr) {
			insert(elem.getKey(), elem.getValue());
		}
	}
	
	
	@Override
	public Object[] valuesToArray() {
		Object[] arr = new Object[size];
		ListElem cursor = root; 
		
		int i = 0;
		while (cursor != null) {
			arr[i] = cursor.getValue();
			
			i++;
			cursor = cursor.next;
		}

		return arr;
	}
	
	@Override
	public Comparable[] keysToArray() {
		Comparable[] arr = new Comparable[size];
		ListElem cursor = root; 
		
		int i = 0;
		while (cursor != null) {
			arr[i] = cursor.getKey();
			
			i++;
			cursor = cursor.next;
		}

		return arr;
		
	}
	
	
	//DEBUG FUNCTIONS
	
	public void listKeyPrint()
	{
		ListElem cursor = root;
		
		while (cursor != null) 
		{
			System.out.print(cursor.getKey() + " ");
			cursor = cursor.next;
		}
	}
	
	public void print()
	{
		ListElem cursor = root;
		
		while (cursor != null) 
		{
			System.out.print(cursor + " > ");
			cursor = cursor.next;
		}
		
		System.out.print("\n");
	}
	
	public void reverseKeyListPrint()
	{
		ListElem cursor = root;
		
		while (cursor.next != null) 
		{
			cursor = cursor.next;
		}
		
		//tail extraction
		while (cursor != null) 
		{
			System.out.print(cursor.getKey()+ " ");
			cursor = cursor.prev;
		}
	}
	
}
