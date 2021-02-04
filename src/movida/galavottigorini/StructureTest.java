package movida.galavottigorini;

import java.util.*;

import movida.commons.Movie;
import movida.commons.Person;
import movida.exceptions.HashTableOverflowException;
import movida.galavottigorini.Hash.HashingFunction;
import movida.galavottigorini.Map.Elem;
import movida.galavottigorini.MovidaCore.MovidaDebug;

public class StructureTest {
	
	public Hash<Integer, Float> hashTest;
	public Hash<String, Float> hashString;
	public String savedKey;
	
	public UnorderedLinkedList<Integer, Float> ULLTest;
	
	public ArrayList<String> keysTest;
	
	public MovidaGraph graphTest;
	
	int size;
	
	public StructureTest(int size, HashingFunction fHash) 
	{	
		keysTest = new ArrayList<String>();
		this.size = size;
		ULLTest = new UnorderedLinkedList<Integer, Float>();
		hashTest = new Hash<Integer, Float>(fHash); 
		hashString = new Hash<String, Float>(fHash); 
		graphTest = new MovidaGraph();
	}
	
	
	public void DemoListFill(int choice) 
	{
		ULLTest.clear();
		
		if (choice == 1) //head insert
		{
			for (int i = 0; i < size ; i++) 
			{
				ULLTest.insert(i, i/2f);
			}
			
		}
		else if (choice == 2) //tail insert
		{
			for (int i = 0; i < size; i++) 
			{
				ULLTest.tailInsert(i,i/2f);
			}
			
		}
		else  
		{			
			for (int j = 0; j < size; j++)
			{
				Random rand = new Random();
				int i = rand.nextInt(100);
				ULLTest.tailInsert(i,i/2f);
			}
		
		}
		
		//ULLTest.print();
	}
	
	public void DemoHashFill(int elements) throws HashTableOverflowException
	{
		
			for (int i = 0; i < elements; i++) 
			{
				
				hashTest.insert(i, (float) Math.sqrt(i));
			}
			
			hashTest.print();
		
	}
	
	public void DemoStringHashFill(int elements) throws HashTableOverflowException
	{
			int k = (new Random()).nextInt(elements);
			for (int i = 0; i < elements; i++) 
			{
				String s = new String();
				for (int j = 0; j < 5; j++) 
				{
					s +=  (char)((new Random()).nextInt(123 - 97) + 97);
				}
				if (i == k) savedKey = s;
				
				keysTest.add(s);
				
				hashString.insert(s, (float) Math.sqrt(i));
			}
			
			hashString.print();
	}
	
	public void DemoStringHashDelete(int elementsToDelete) throws HashTableOverflowException
	{
			String[] arr = keysTest.toArray(new String[keysTest.size()]);
			
			try {
				for (int i = 0; i < elementsToDelete; i++) 
				{
					hashString.delete(arr[i]);
				}
			}
			catch (Exception e) {
					e.getMessage();
					e.printStackTrace();
			}
			hashString.print();
			MovidaDebug.Log("\n" + hashString.getHashTableSize() + "\n");
	}
	
	
	public void DemoHashGet(int k) 
	{
		System.out.print("\nString table : " + savedKey +" : " + hashString.search(savedKey).getValue() + "\n");
		System.out.print("\nInt table : " + hashTest.search(k).getValue() + "\n");
	}

}
