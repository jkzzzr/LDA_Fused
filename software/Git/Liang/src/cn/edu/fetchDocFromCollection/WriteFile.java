package cn.edu.fetchDocFromCollection;

import java.io.*;
public class WriteFile {
	FileWriter fw=null;
	 BufferedWriter bw=null;
	public WriteFile(String path){
		try {
			fw=new FileWriter(path);
			bw=new BufferedWriter(fw);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
	}
	public void write(String text){
		try {
			bw.append(text);
			bw.newLine();
			bw.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void close() {
		try {
			bw.close();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
		//	System.out.println("close the writeStream sucess!");
		}
	}
		
}
