package movida.galavottigorini;

import java.lang.reflect.Array;

public class UnorderedLinkedList<K extends Comparable<K>, E extends Object> extends Map<K,E>
{
	private class ListElem extends Elem{	//record della lista
		
		private ListElem next;
		private ListElem prev; 
		
		//costruttore
		public ListElem (K key, E value)
		{
			super(key, value);
			next = null;
			prev = null;
		}
	}
	
	private ListElem root;	//radice della lista
	private int size;		//elementi nella lista
	
	//costruttore UnorderedLinkedList
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
	
	@Override
	public void insert(K k, E e)
	{
		ListElem insListElement = new ListElem(k,e);
		
		if (root == null) 
		{
			root = insListElement;
			root.next = null;
			root.prev = null;
		} 
		else 
		{ 
			ListElem cursor = root;
			
			while (cursor.next != null) 
			{
				cursor = cursor.next;
			}
			
			cursor.next = insListElement;
			insListElement.prev = cursor;
		}
		
		size++;
	}
	
	@Override
	public void replace(K k, E e)
	{
		ListElem pointer = root;
						
		while (pointer != null) 
		{
			if (k.compareTo(pointer.getKey()) == 0) break;
				
			pointer = pointer.next;
		}
		
		pointer.setValue(e);
	}
	
	
	/** Permette di inserire gli elementi in coda data la loro chiave ed il loro valore
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
	public void delete(K k)
	{
		ListElem list_elem = (ListElem) search(k);
		if (list_elem != null) 
		{
			if (list_elem == root) root = list_elem.next;
			if (list_elem.next != null) list_elem.next.prev = list_elem.prev;
			if (list_elem.prev != null) list_elem.prev.next = list_elem.next;			
			
			list_elem = null;
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
				if (k.compareTo(pointer.getKey()) == 0) break;
				
				pointer = pointer.next;
			}
			return pointer;
		}
	}
	
	@Override
	public void clear()
	{
		root = null;
		size = 0;
	}
	
	@Override
	public int getSize() 
	{
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
	
	
	/**	Appende elementi di una lista alla corrente
	 *
	 * 	@param: UnorderedLinkedList<K, E> list, ovvero la lista da appendere a quella principale
	 */	
	public void append(UnorderedLinkedList<K, E> list) 
	{	
		ListElem cursor = root;
		
		while (cursor.next != null) {
			cursor = cursor.next;
		}
		
		cursor.next = list.root;
		list.root.prev = cursor;
		
		size += list.getSize();	
	}
	
	
	@Override
	public Object[] valuesToArray() 
	{
		Object[] arr = new Object[size];
		ListElem cursor = root; 
		
		int i = 0;
		while (cursor != null && i < size) {
			arr[i] = cursor.getValue();
			
			i++;
			cursor = cursor.next;
		}

		return arr;
	}
	
	@Override
	public Comparable[] keysToArray() 
	{
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
		
	@Override
	public void print()
	{
		ListElem cursor = root;
		
		while (cursor != null) 
		{
			System.out.print(cursor.getKey() + " : ");
			cursor = cursor.next;
		}
		
		System.out.print("\n");		
	}
	
	/** Stampa le chiavi degli elementi della lista in verso opposto.
	 * 
	 * 	Usato per verificare i legami bidirezioni della lista.
	 * */
	public void reverseKeyListPrint()
	{
		ListElem cursor = root;
		if (cursor != null) 
		{
			while (cursor.next != null) 
			{
				cursor = cursor.next;
			}
		}
		
		//tail extraction
		while (cursor != null) 
		{
			System.out.print(cursor.getKey() + " : ");
			cursor = cursor.prev;
		}
		
		System.out.print("\n");
	}
	
}
