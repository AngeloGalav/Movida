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
	
	public Graph<Integer, Float> graphTest;
	
	int size;
	
	public StructureTest(int size, HashingFunction fHash) 
	{	
		this.size = size;
		ULLTest = new UnorderedLinkedList<Integer, Float>();
		hashTest = new Hash<Integer, Float>(size, fHash); 
		graphTest = new Graph<>();
		
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
		ULLTest.print();
	}
	
	public void DemoHashFill(int elements) throws HashTableOverflowException{
		
		if (hashTest.getSize() < elements) 
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
	
	public void DemoGraphFill(int elements) {
		
	}

}
