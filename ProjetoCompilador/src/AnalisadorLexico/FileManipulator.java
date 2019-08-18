package AnalisadorLexico;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class FileManipulator {
	
	
	public String fileRead(String path) throws IOException {
		FileReader arq = new FileReader(path);
		BufferedReader reader = new BufferedReader(arq);
		
		String line = reader.readLine();
		String text = null;
		
		while(line != null) {
			if(text == null)
				text = line;
			else
				text = text + "\n" + line;
			line = reader.readLine();
		}
		return text;
	}
	

}
