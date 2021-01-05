package movida.galavottigorini;

import java.util.ArrayList;
import java.lang.reflect.Array;

//TODO: TEST FUNCTIONS: ClearList, tailinsert, reverseKeyListPrint, ListPrint, list append...
//TODO: Append function

public class UnorderedLinkedList<K extends Comparable<K>, E extends Object> extends Map<K,E>
{
	private class ListElem {
		
		Elem record;
		
		public ListElem next;
		public ListElem prev; 
		
		//constructors
		public ListElem (K key, E value)
		{
			record = new Elem(key, value);
			next = null;
		}
	}
	
	public ListElem root;
	private int size;
	
	//class constructor
	public UnorderedLinkedList() {
		root = null;
	}
	
	public int size() {
		return size;
	}
	
	//methods
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
	
	public ListElem search(K k) 
	{	
		if (root == null) return null;
		else {
			ListElem pointer = root;
			while (pointer != null) 
			{
				if (pointer.record.getKey() == k) 
				{
					break;
				}
				
				pointer = pointer.next;
				
			}
			return pointer;
		}
	}
	
	public void clearList()
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
			arr[i] = cursor.record;
			
			i++;
			cursor = cursor.next;
		}

		return arr;
	}
	
	@Override
	public Object[] valuesToArray() {
		Object[] arr = new Object[size];
		ListElem cursor = root; 
		
		int i = 0;
		while (cursor != null) {
			arr[i] = cursor.record.getValue();
			
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
			arr[i] = cursor.record.getKey();
			
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
			System.out.print(cursor.record.getKey() + " ");
			cursor = cursor.next;
		}
	}
	
	public void print()
	{
		ListElem cursor = root;
		
		while (cursor != null) 
		{
			System.out.print(cursor.record + " > ");
			cursor = cursor.next;
		}
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
			System.out.print(cursor.record.getKey()+ " ");
			cursor = cursor.prev;
		}
	}
	
}
