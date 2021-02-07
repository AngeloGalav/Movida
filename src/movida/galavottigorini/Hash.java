package movida.galavottigorini;

import java.lang.reflect.Array;

import com.sun.source.tree.Tree;

import movida.exceptions.*;

public class Hash<K extends Comparable<K>, E extends Object> extends Map<K,E>{
	
	public enum HashingFunction	//vari tipi di hashing functions
	{
		Divisione,	
		Moltiplicazione,
		HashCodeJava,	//usata in movidaCore
		IspezioneLineare,
		IspezioneQuadratica,
		DoppioHashing;
	}
	
	private Elem[] HashTable;
	
	private boolean autoResize; //indica se il ridimensionamento automatico è attivato o no.
	
	private HashingFunction fHash; //HashFunction scelta
	
	private int m; //grandezza dell' HashTable Array
	
	private int elementsInHash; //elementi presenti nell' HashTable
	
	private final Elem DELETED; //elemento "tipo" DELETED
	
	//Costruttore
	public Hash(int m, HashingFunction fHash)
	{	
		this.m = m;
		HashTable = (Elem[]) Array.newInstance(Elem.class , m);
		this.fHash = fHash;
		
		autoResize = false;
		
		DELETED = new Elem(null, null); //DELETED è semplicemente un elemento dell' HashTable con tutti i campi = null
		elementsInHash = 0;	
	}
	
	public Hash(HashingFunction fHash)	//nel caso la dimensione dell'hashTable non venga specificata, l'autoresize è attivato.
	{	
		m = 1;
		HashTable = (Elem[]) Array.newInstance(Elem.class , m);
		this.fHash = fHash;
		
		autoResize = true;
		
		DELETED = new Elem(null, null);
		elementsInHash = 0;
	}
	
	//funzione hash
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
	
	/**Funzioni di ispezione**/
	
	public int h1(K k) {
		return ((Integer) k) % m;
	}	
	
	public Integer h2(K k) {
		return (((Integer) k )* 3) % m; 
	}	
	
	public int h3(K k) 
	{	
		float A = 0.5f;
		int value;
		value = (int) (m * ((int) k*A - (float) k*A));
		return value;
	}	
	
	
	@Override
	public int getSize() 
	{
		return elementsInHash;
	}
	
	
	/**Ritorna la grandezza dell'hashtable, ovvero il numero di "caselle" della tabella hash sia piene
	 * che vuote.
	 * 
	 * @return m, la grandezza dell'hashTable.
	 */
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
				
				if (elementsInHash == m && autoResize) //parte in cui l'array viene ridimensionato
				{
					Elem[] toReinsert = toArray();
					m *= 2;	//raddoppio in caso il numero di caselle scarseggi.
					
					HashTable =  (Elem[]) Array.newInstance(Elem.class , m);
					elementsInHash = 0;
					
					for (Elem elem : toReinsert) //reinserisco tutti gli elementi nella nuova tabella hash.
					{
						insert(elem.getKey(), elem.getValue());
					}
				}
				
				return;	//se ho inserito l'elemento, posso uscire dal loop
			}
			i++;
		}

		throw new HashTableOverflowException();
	}
	
	
	@Override
	public void replace(K k, E e)
	{
		int i = 0;
		int j = 0;		

		do
		{
			j = h(k, i);
						
			if (HashTable[j] != null && HashTable[j].getKey() != null && k.compareTo(HashTable[j].getKey()) == 0)
			{
				HashTable[j].setValue(e);;
			}
				
			i++;
		} while(HashTable[j] != null && i != m);
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
		} while(HashTable[j] != null && i != m);
		
		
		if (elementsInHash == m/4 && autoResize) //se il numero di elementi è basso, faccio il resize.
		{
			Elem[] toReinsert = toArray();
			m /= 2;
			HashTable = (Elem[]) Array.newInstance(Elem.class , m); //se il numero di elementi è basso, faccio il resize.
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
			m = 1;
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
		int i = 0, j = 0;		
		
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

	
	@Override
	public Elem[] toArray()
	{
		Elem[] arr = (Elem[]) Array.newInstance(Elem.class , elementsInHash);

		int i = 0, j = 0;
		while (j < m && i < elementsInHash) 
		{
			if (HashTable[j] != null  && HashTable[j].getKey() != DELETED.getKey())
			{
				arr[i] = HashTable[j];
				i++;
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
			if (HashTable[j] != null)
			{
				if ((HashTable[j].getKey() != DELETED.getKey()) && (HashTable[j].getKey() != null))
				{
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
			if (HashTable[j] != null) 
			{
				if ((HashTable[j].getKey() != DELETED.getKey()) && (HashTable[j].getKey() != null))
				{
					arr[i] = HashTable[j].getKey();
					i++;
				}
			}
			
			j++;
		}
		
		return arr;
	}
	
	
	public void print()
	{
		for (int i = 0; i < m; i++) 
		{
			if (HashTable[i] != null) 
			{
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
	
}
