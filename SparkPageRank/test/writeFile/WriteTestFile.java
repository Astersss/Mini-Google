package test.writeFile;

import java.io.*;

/**
 * Created by marcusma on 5/3/17.
 */
public class WriteTestFile{

	public static void main(String[] args){
		File file = new File("/Users/marcusma/IdeaProjects/CIS555_Final_Project/new_page_test.txt");
		try{
			BufferedWriter bw = new BufferedWriter(new FileWriter(file));

			for(int i = 0; i < 1000000; i++){
				bw.write(i + " "+ (int)(Math.random()*(1000000)) + " "+ (int)(Math.random()*(1000000)) + " "+ (int)(Math.random()*(1000000)) + " "+ (int)(Math.random()*(1000000)) + " "+ (int)(Math.random()*(1000000)) + " "+ (int)(Math.random()*(1000000)) + " "+ (int)(Math.random()*(1000000)) + " "+ (int)(Math.random()*(1000000)) + " "+ (int)(Math.random()*(1000000)) + " "+ (int)(Math.random()*(1000000)) + "\n");
				bw.flush();
			}
		}
		catch(IOException e){
			e.printStackTrace();
		}


	}
}
