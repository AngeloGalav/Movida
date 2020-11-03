package movida.galavottigorini;

import movida.commons.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*; //scanner is here


public class MovidaCore {

	public String test = new String();
	
	
	public void readFile(String s) {
		
		try 
		{
			File myObj = new File(s);
			Scanner scan = new Scanner(myObj);
			
			while (scan.hasNextLine()) 
			{
				System.out.print(scan.nextLine());
			}
			scan.close();
			
		} catch (FileNotFoundException e) 
		{
		      System.out.println("An error occurred.");
		      e.printStackTrace(); 
		}
	}
	
	
}
