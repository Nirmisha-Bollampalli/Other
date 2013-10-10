import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class FetchURL{
	
	//Read a list of names from a file and fetch URL's from linkedIn
	
	public static ArrayList<String> FetchURLs() {
		 
		ArrayList<String> URLS = new ArrayList();
		BufferedReader br = null;
		try {
 
			String sCurrentLine;
			br = new BufferedReader(new FileReader("Links.txt"));
 
			while ((sCurrentLine = br.readLine()) != null) {
				sCurrentLine = sCurrentLine.substring(0,sCurrentLine.indexOf('|'));
				System.out.println(sCurrentLine);
				URLS.add(sCurrentLine);
			}
 
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null)br.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		
		return URLS;
 
	}
} 