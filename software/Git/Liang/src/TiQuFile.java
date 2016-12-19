import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import searchDoc.AllPath;

import cn.edu.splitByWord.LexicalAnalysis;


public class TiQuFile {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
/*		String string="/home/huang/Documents/LLFile/Pro_Union/Html-Word-Change/clueweb09-en0003-55-20460";
		BufferedReader bReader=new BufferedReader(new FileReader(string));
		BufferedWriter bwBufferedWriter =new BufferedWriter(new FileWriter("/home/huang/Documents/LLFile/Pro_Union/abc"));
		String temp=null;
		while((temp = bReader.readLine())!=null ){
			bwBufferedWriter.write(temp);
			bwBufferedWriter.newLine();
		}
		bwBufferedWriter.close();
		bReader.close();
		System.out.println("end123");
*/
		BufferedReader br=new BufferedReader(new FileReader("/home/huang/Documents/LLFile/Pro_Union/stopword2.txt"));
		String stemp=null;
		String temp="";
		while((stemp=br.readLine())!=null){
			temp+=stemp+"\t";
		}
		new LexicalAnalysis("DOC", temp,"qID");
		String result = LexicalAnalysis.ResultList;
		String[] arr=result.split("\t");
		BufferedWriter bw=new BufferedWriter(new FileWriter("/home/huang/Documents/LLFile/Pro_Union/stopword.txt"));
		for(int i=0;i<arr.length;i++){
			bw.write(arr[i]);
			bw.newLine();
		}
		bw.close();
	}

}
