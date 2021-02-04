package movida.galavottigorini;

import java.lang.reflect.Array;

import movida.galavottigorini.MovidaCore.MovidaDebug;

public class UnorderedLinkedList<K extends Comparable<K>, E extends Object> extends Map<K,E>
{
	private class ListElem extends Elem{
		
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
	
	private ListElem root;
	private int size;
	
	//costruttore classe UnorderedLinkedList
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
	/** inserisco un nuovo elemento (con chiave e valore dato in input) nella lista
	 * 
	 * @param k chiave nuovo elemento
	 * @param e valore nuovo elemento
	 * */
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
	/**elimina nodo con una determinata chiave 
	 * 
	 * @param k chiave del nodo da eliminare
	 * */
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
	/**cerca nodo con una determinata chiave 
	 * 
	 * @param k chiave del nodo da cercare
	 * @return elemento cercato
	 * */
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
	
	/**	Svuota la lista completamente. **/
	@Override
	public void clear()
	{
		root = null;
		size = 0;
	}
	
	@Override
	/**ritorna il numero di elementi nella lista
	 * 
	 * @return dimensione della lista
	 * */
	public int getSize() {
		return size;
	}
	
	@Override
	/**trasforma la lista in un array
	 * 
	 * @return array di Elem
	 * */
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
	public void append(UnorderedLinkedList<K, E> list) {
		
		ListElem cursor = root;
		
		while (cursor.next != null) {
			cursor = cursor.next;
		}
		
		cursor.next = list.root;
		list.root.prev = cursor;
		
		size+= list.getSize();
		
	}
	
	
	@Override
	/**trasforma i valori degli elementi della lista in un array
	 * 
	 * @return array di valori (di Object)
	 * */
	public Object[] valuesToArray() {
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
	/**trasforma le chiavi degli elementi della lista in un array
	 * 
	 * @return array di chiavi (di tipo Comparable)
	 * */
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
			System.out.print(cursor.getKey() + " : ");
			cursor = cursor.next;
		}
		
		System.out.print("\n");
		
		reverseKeyListPrint();//TODO: DELETE THIS LINE
	}
	
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
