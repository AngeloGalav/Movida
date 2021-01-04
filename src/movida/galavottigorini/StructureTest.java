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
	
	public UnorderedLinkedList<Integer, Float> ULLTest;
	
	public StructureTest(int hashSize, HashingFunction fHash) 
	{	
		ULLTest = new UnorderedLinkedList<Integer, Float>();
		hashTest = new Hash<Integer, Float>(hashSize, fHash); 
		
	}
	
	
	public void DemoListFill(int a) 
	{
		ULLTest.clearList();
		
		if (a == 1) 
		{
			for (int i = 0; i<10; i++) 
			{
				ULLTest.insert(i, i/2f);
			}
			
		}
		else if (a == 2) 
		{
			for (int i=0; i<10; i++) 
			{
				ULLTest.tailInsert(i,i/2f);
			}
			
		}
		else  
		{			
			for (int j=0; j<10; j++)
			{
				Random rand = new Random();
				int i = rand.nextInt(100);
				ULLTest.tailInsert(i,i/2f);
			}
		
		}
		ULLTest.print();
	}
	
	public void DemoHashFill(int elements) throws HashTableOverflowException{
		
		if (hashTest.getHashSize() < elements) 
		{
			throw new HashTableOverflowException();
		} else {
			
			for (int i=0; i < elements; i++) 
			{
				
				hashTest.insert(i, (float) Math.sqrt(i));
			}
			
			hashTest.print();
		}
	}
	
	public void toArrayTest() 
	{
		Elem[] arr = hashTest.toArray();
	}

}
