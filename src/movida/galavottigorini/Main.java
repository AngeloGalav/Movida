package movida.galavottigorini;
import java.util.*;

public class Main {

	public static MovidaCore m_movidaCore = new MovidaCore();
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		Scanner in = new Scanner(System.in);
		System.out.print("File name: (files have to be in the movida.commons folder)");
        m_movidaCore.readFile("src/movida/commons/" + in.nextLine());
	}

}
