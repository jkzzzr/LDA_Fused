package searchDoc;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class ExtractEachDoc {

	private String path=AllPath.sourcePath;

	public static String changepath = null;
	public static WARC018Collection wc=null;
	public static String tempPath=null;
	public static String errPath = "/home/huang/Documents/LLFile/errID";
	
	public static void run(String docid){
	//	String path="/HardDisk2000/Clueweb09_partA/";
		String path = AllPath.sourcePath;
		tempPath=docid.substring(10,16)+"/"+docid.substring(17,19)+".warc";
		path+=tempPath;
		
	    System.out.println("finding: "+docid);
	    //如果路径改变了，就重新创建一个路径
		if(!tempPath.equals(changepath)){
			if(wc!=null){
				if(wc.bufferedReader!=null){
					try {
						wc.bufferedReader.close();
					} catch (IOException e) {
						tools.WriteFile_Log.writererr("Err - readfileErr - ExtractEachDoc - run(1):\n\tparm:\t"+docid);
						e.printStackTrace();
					}
				}
			}
			changepath = tempPath;
			try {
				wc = new WARC018Collection(path,docid);
			} catch (FileNotFoundException e) {
				tools.WriteFile_Log.writererr("Err - readfileErr - ExtractEachDoc - run(2):\n\tparm:\t"+docid+"\t"+path);
		//		e.printStackTrace();
				return;
			}
		}
		   if(wc.bufferedReader!=null){
			wc.run(docid);
		}else{
			tools.WriteFile_Log.writererr("Err - readfileErr - ExtractEachDoc - run(3):\n\tparm:\t"+docid);
		}
	}
}
