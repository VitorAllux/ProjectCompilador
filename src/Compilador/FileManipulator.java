package Analyser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
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

	public static void fileWrite(String path, String str) throws IOException {
		FileOutputStream arq = null;
		PrintStream ps = null;
		
		try {
		File f = new File(path);
		arq = new FileOutputStream(f);
		
			try {
				ps = new PrintStream(arq);
				ps.println(str);
			} finally {
				if(ps != null) {
					ps.close();
				}
			}
		} finally {
			if (arq != null) {
				arq.close();
			}
		}
	}
	

}
