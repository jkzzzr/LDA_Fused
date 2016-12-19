package tools;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import searchDoc.AllPath;

public class WriteFile_Log {

	public static BufferedWriter bWriter = null;
	
	static {
		try {
			bWriter = new BufferedWriter(new FileWriter(AllPath.Log, true));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void writererr(String string){
		try {
			bWriter.write(string);
			bWriter.newLine();
			bWriter.flush();
			System.out.println("++++++++++++++" + string);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
