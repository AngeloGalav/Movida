package movida.galavottigorini;

public class MovidaParser 
{
	public static class Parser
	{
		public static String rmvWhiteSpaces(String temp) 
		{
			String[] str_tmp = temp.split(" ");
			String formatted_string = new String();
			
			for (int k = 0; k < str_tmp.length; k++) {
				str_tmp[k] = str_tmp[k].replaceAll("\\s+", "");
				str_tmp[k] = str_tmp[k].replaceAll("\n", "");
				str_tmp[k] = str_tmp[k].replaceAll("\t", "");
				if (str_tmp[k].compareTo("") != 0) {
					formatted_string += str_tmp[k] + " ";
				}
			}
			
			formatted_string = formatted_string.substring(0, formatted_string.length() - 1);
			
			return formatted_string;
		}
	}
}
