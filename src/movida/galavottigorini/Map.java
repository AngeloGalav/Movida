package movida.galavottigorini;

import java.util.*;

import movida.exceptions.HashTableOverflowException;
import movida.exceptions.KeyNotFoundException;

abstract class Map<K extends Comparable<K>, E extends Object>
{	
	public class Elem {
		
		private K key;
		private E value;

		public Elem (K key, E value) 
		{
			this.key = key;
			this.value = value;
		}
		
		//ritorna chiave dell'elemento
		public K getKey() 
		{
			return key;
		}
		
		//ritorna valore dell'elemento
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
	
	//inserisco elemento con chiave k e valore e
	abstract public void insert(K k, E e) throws HashTableOverflowException;
	
	//elimino elemento con chiave k
	abstract public void delete(K k);
	
	//cerco elemento con chiave k
	abstract public Elem search(K k);
	
	abstract public void print();
	
	//svuoto la map
	abstract public void clear();
	
	//ritorna la grandezza della map
	abstract public int getSize();
	
	//ritorna il vettore con tutti gli elementi nella map 
	abstract public Elem[] toArray();

	//ritorna il vettore con tutti i valori degli elementi nella map
	abstract public Object[] valuesToArray();

	//ritorna il vettore con tutte le chiavi degli elementi nella map
	abstract public Comparable[] keysToArray();
}
