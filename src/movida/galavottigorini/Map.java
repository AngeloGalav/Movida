package movida.galavottigorini;

import java.util.*;

import movida.exceptions.HashTableOverflowException;

abstract class Map<K extends Comparable<K>, E extends Object>
{	
	public class Elem //Record del dizionario.
	{
		
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
		
		public void setValue(E value) 
		{
			this.value = value;
		}
		
		@Override
		public String toString() 
		{
			return getKey() + " : " + getValue();
		}
		
	}
		
	/**Inserisce l'elemento con chiave "k" e valore "e" nel dizionario.
	 * 
	 * @param k
	 * @param e
	 * @throws HashTableOverflowException
	 */
	abstract public void insert(K k, E e) throws HashTableOverflowException;
	
	/**Elimino elemento con chiave k
	 * 
	 * @param k
	 */
	abstract public void delete(K k);
	
	/**Restituisce il record di chiave k
	 * 
	 * @param k
	 * @return
	 */
	abstract public Elem search(K k);
	
	
	/**Sostituisce il valore di un elemento con un altro.
	 * 
	 * @param k
	 */
	abstract public void replace(K k, E e);
	
	/**Stampa il contenuto del dizionario.
	 * 
	 * @param k
	 * @return
	 */
	abstract public void print();
	
	/**Elimina tutti gli elementi del dizionario, svuotandolo.
	 * 
	 * @param k
	 * @return
	 */
	abstract public void clear();
	
	/**Ritorna il numero degli elementi del dizionario.
	 * 
	 * @param k
	 * @return
	 */
	abstract public int getSize();
	
	/**Ritorna il vettore con tutti gli elementi nel dizionario. 
	 * 
	 * @return array di valori associati alle chiavi
	 * */
	abstract public Elem[] toArray();

	/**Ritorna il vettore con tutti i valori degli elementi nel dizionario. 
	 * 
	 * @return array di valori associati alle chiavi
	 * */
	abstract public Object[] valuesToArray();

	/**Ritorna il vettore con tutte le chiavi degli elementi nel dizionario.
	 * 
	 * @return array di chiavi (di tipo Comparable)
	 * */
	abstract public Comparable[] keysToArray();
}
