package movida.galavottigorini;

import java.util.*;

import movida.exceptions.HashKeyNotFoundException;
import movida.exceptions.HashTableOverflowException;
import movida.exceptions.KeyNotFoundException;

abstract class Map<K extends Comparable<K>, E extends Object> {
	
	public class Elem {
		
		private K key;
		private E value;

		public Elem (K key, E value) 
		{
			this.key = key;
			this.value = value;
			
		}
		
		public K getKey() 
		{
			return key;
		}
		
		public E getValue() 
		{
			return value;
		}
		
		@Override
		public String toString() 
		{
			return getKey() + " : " + getValue();
		}
		
	}
	
	abstract public void insert(K k, E e) throws KeyNotFoundException, HashTableOverflowException, HashKeyNotFoundException;
	
	abstract public void delete(K k);
	
	abstract public Object search(K k);
	
	abstract public void print();
	
	abstract public int getSize();
	
	abstract public Elem[] toArray();
	
	abstract public Object[] valuesToArray();
	
	abstract public Comparable[] keysToArray();
}
