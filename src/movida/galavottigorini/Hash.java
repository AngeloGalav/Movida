package movida.galavottigorini;

import java.lang.reflect.Array;

import com.sun.source.tree.Tree;

import movida.exceptions.*;
import movida.galavottigorini.MovidaCore.MovidaDebug;

public class Hash<K extends Comparable<K>, E extends Object> extends Map<K,E>{
	
	public enum HashingFunction{
		Divisione,	//everything else is used for integers
		Moltiplicazione,
		HashCodeJava,
		IspezioneLineare,
		IspezioneQuadratica,
		DoppioHashing;
	}
	
	//parameters
	private Elem[] HashTable;
	
	private boolean autoResize;
	
	private HashingFunction fHash;
	
	private int m; //size of the HashTable Array
	
	private int elementsInHash; //elements in the HashTable
	
	private final Elem DELETED; //deleted element type
	
	//Constructor
	public Hash(int m, HashingFunction fHash)
	{	
		this.m = m;
		HashTable = (Elem[]) Array.newInstance(Elem.class , m);
		this.fHash = fHash;
		
		autoResize = false;
		
		DELETED = new Elem(null, null); //DELETED is just an element with empty parameters
		elementsInHash = 0;	
	}
	
	public Hash(HashingFunction fHash)
	{	
		m = 1;
		HashTable = (Elem[]) Array.newInstance(Elem.class , m);
		this.fHash = fHash;
		
		autoResize = true;
		
		DELETED = new Elem(null, null); //DELETED is just an element with empty parameters
		elementsInHash = 0;
	}
	
	public int h (K k, int i) {
		
		switch (fHash) 
		{
			case HashCodeJava : 
			{
				return Math.abs((k.hashCode() + i) % m );
			}
			case Divisione : 
			{
				return h1(k); 
			}
			case Moltiplicazione : 
			{
				return h3(k);
			}
			case IspezioneLineare:
			{
				return (h1(k) + i) % m ;
			}
			case IspezioneQuadratica:
			{
				return (h1(k) + i + 2*i*i) % m ;
			}
			case DoppioHashing:
			{
				return (h1(k) + i*h2(k)) % m; 
			}
			default:
				throw new IllegalArgumentException("Unexpected value: " + fHash);
		}
	}
	
	/**Hashing Functions for Inspection**/
	
	public int h1(K k) {
		return (Integer) k % m;
	}	
	
	public Integer h2(K k) {
		return (Integer) k % m; //TODO: Change this to a real hash function
	}	
	
	public int h3(K k) 
	{	
		float A = 0.5f;
		int value;
		value = (int) (m * ((int) k*A - (float) k*A));
		return value;
	}	
	
	
	/**Returns numbers of elements in hashTable
	 * 
	 * @returns: elementsInHash
	 */
	@Override
	public int getSize() {
		return elementsInHash;
	}
	
	public int getHashTableSize() {
		return m;
	}
	
	@Override
	public void insert(K k, E e) throws HashTableOverflowException 
	{
		int i = 0;
		int j = 0;
		while (i != m) 
		{
			j = h(k, i);
			
			if (HashTable[j] == null || HashTable[j] == DELETED) 
			{
				HashTable[j] =  new Elem(k,e);
				elementsInHash++;
				
				if (elementsInHash == m && autoResize) 
				{
					Elem[] toReinsert = toArray();
					m *= 2;
					
					HashTable =  (Elem[]) Array.newInstance(Elem.class , m);
					elementsInHash = 0;
					
					for (Elem elem : toReinsert) 
					{
						insert(elem.getKey(), elem.getValue());
					}
				}
				
				return;
			}
				
			i++;
		}

		throw new HashTableOverflowException();
	}
	
	@Override
	public void delete(K k)
	{			
		int i = 0;
		int j = 0;		

		do
		{
			j = h(k, i);
						
			if (HashTable[j] != null && HashTable[j].getKey() != null && k.compareTo(HashTable[j].getKey()) == 0)
			{
				HashTable[j] = DELETED;
				elementsInHash--;
			}
				
			i++;
		} while(HashTable[j] != null && i != m) ;
		
		
		if (elementsInHash == m/4 && autoResize) 
		{
			Elem[] toReinsert = toArray();
			m /= 2;
			HashTable = (Elem[]) Array.newInstance(Elem.class , m);
			elementsInHash = 0;
			
			for (Elem elem : toReinsert) 
			{
				try {
					insert(elem.getKey(), elem.getValue());
				} catch (Exception e) {
					e.getMessage();
				}
			}
		}
		
	}
	
	
	
	@Override
	public void clear() 
	{
		for (int i = 0; i < m; i++) 
		{
			HashTable[i] = null;
		}
		
		if (autoResize)
		{
			m = 0;
			HashTable = (Elem[]) Array.newInstance(Elem.class , 1);
		}else 
		{
			HashTable = (Elem[]) Array.newInstance(Elem.class , m);
		}
		
		elementsInHash = 0;
	}
	
	@Override
	public Elem search(K k) 
	{
		int i = 0;
		int j = 0;		
		
		do
		{
			j = h(k, i);
						
			if (HashTable[j] != null && HashTable[j].getKey() != null && k.compareTo(HashTable[j].getKey()) == 0)
			{
				return HashTable[j];
			}
				
			i++;
		} while(HashTable[j] != null && i != m) ;
		
		return null;
	}

	
	/* Fa diventare l'hashtable in un array continuo, senza parti discontinue
	 * In questo modo i key non hanno alcuna funzione nell'inserimento delle key (che invece diventano gli indici veri e propri)
	 */
	
	@SuppressWarnings("unchecked")
	@Override
	public Elem[] toArray()
	{
		Elem[] arr = (Elem[]) Array.newInstance(Elem.class , elementsInHash);

		//MovidaDebug.Log("\n" + elementsInHash);

		int i = 0, j = 0;
		while (j < m && i < elementsInHash) 
		{
			if (HashTable[j] != null) {
				if ((HashTable[j].getKey() != DELETED.getKey()) && (HashTable[j].getKey() != null)) {
					arr[i] = HashTable[j];
					i++;
				}
			}
			
			j++;
		}
		
		return arr;
	}
	
	@Override
	public Object[] valuesToArray()
	{
		Object[] arr = new Object[elementsInHash];
		
		int i = 0, j = 0;
		while (j < m && i < elementsInHash) 
		{
			if (HashTable[j] != null) {
				if ((HashTable[j].getKey() != DELETED.getKey()) && (HashTable[j].getKey() != null)) {
					arr[i] = HashTable[j].getValue();
					i++;
				}
			}
			
			j++;
		}
		
		return arr;
	}
	
	@Override
	public Comparable[] keysToArray()
	{
		Comparable[] arr = new Comparable[elementsInHash];
		
		int i = 0, j = 0;
		while (j < m) 
		{
			if (HashTable[j] != null) {
				if ((HashTable[j].getKey() != DELETED.getKey()) && (HashTable[j].getKey() != null)) {
					arr[i] = HashTable[j].getKey();
					i++;
				}
			}
			
			j++;
		}
		
		return arr;
	}
	
	
	//Debug Functions

	
	public void print()
	{
		for (int i = 0; i < m; i++) 
		{
			if (HashTable[i] != null) {
				if (HashTable[i].getValue() == null) 
				{
					System.out.print("DEL");
				} else 
				{
					System.out.print(HashTable[i]);
				}
			} else {
				System.out.print("NULL");
			}
			System.out.print(" | ");
		}
		
		System.out.print("\n");
	}

	public void printDataStructureInfo(){
		MovidaDebug.Log("This is an HashTable of type " + fHash + "\n");
		MovidaDebug.Log("It has a capacity of " + m + " elements\n");
		MovidaDebug.Log("Right now it contains " + elementsInHash + " elements\n");
	}
	
}
