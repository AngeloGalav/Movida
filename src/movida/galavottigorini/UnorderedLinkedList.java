package movida.galavottigorini;

import movida.galavottigorini.MovidaCore;
import movida.exceptions.KeyNotFoundException;
import java.lang.reflect.Array;

//TODO: See if you need to add any other useful methods
//TODO: toarray...
//TODO: binarysearch.

public class UnorderedLinkedList<K extends Comparable<K>, E extends Object> extends Map<K,E>
{
	//TODO: CLEAN GETTERS AND SETTERS
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
		ListElem rtNx = this.root.next;
		this.root = rt;
		this.root.next = rtNx;
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
	
	/** Permette di inserire gli elementi in coda adta la loro chiave ed il loro valore
	 * 
	 *  @param k chiave dell'elemento inserito
	 *  @param e valore e dell'elemento inserito
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
	public void delete (K k) throws KeyNotFoundException
	{
		ListElem list_elem = (ListElem) search(k);
		if (list_elem != null) 
		{
			ListElem next_list_elem = list_elem.next;
			ListElem prev_list_elem = list_elem.prev;
			
			prev_list_elem.next = next_list_elem;
			next_list_elem.prev = prev_list_elem;
			size--;
		} else {
			throw new KeyNotFoundException();
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
				if (k.compareTo(pointer.getKey()) == 0) 
				{
					break;
				}
				
				pointer = pointer.next;
				
			}
			return pointer;
		}
	}

	/** Funzione che data una chiave ritorna l'elemento che la contiene, prima ordinando il vettore poi chiamando la BinarySearchRec
	 *  
	 *  @param k chiave da cercare
	 *  @return l'elemento avente come chiave k
	 * */
	public Elem binarySearch(K k, MovidaCore movida) {
		return binarySearchRec(toArray(), 0, size, k); 
	}
	
	/** Funzione che implementa la vera ricerca binaria ricorsiva, chiamata da binarySearch
	 * 
	 * @param arr array di Elem su cui cercare 
	 * @param low indice di inizio sottoarray
	 * @param up indice di fine sottoarry
	 * @param k chiave da cercare
	 * @return elemento con chiave k cercata
	 * */
	public Elem binarySearchRec(Elem[] arr, int low, int up, K k) 
	{
		if (up >= low ) { 
			 int m = low + (up - low) / 2; 
		  
		     //se l' elemento con la chiave cercata e' quella centrale ritorno quel determinato Elem
			if ( arr[m].getKey().compareTo(k) == 0 )
				return arr [m]; 
			
			Elem tmp = binarySearchRec(arr, low, m - 1, k);
			if (tmp != null) 
				return tmp; 
			
			tmp = binarySearchRec(arr, m + 1, up, k);
			if (tmp != null) 
				return tmp; 
		} 
		  
		// se l'elemento non è presente ritorniamo null
		return null; 
	}
	
	/**	Svuota la lista completamente.
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
		
		cursor.next = list.root;
		list.root.prev = cursor;
	}

	
	/**	Aggiunge elementi da un array preservando il loro ordine nell'array
	 *
	 * 	@param arr array di Elem
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
			System.out.print(cursor.getKey() + " ");
			cursor = cursor.prev;
		}
	}
	
}
