package searchDoc;

import java.io.FileNotFoundException;

public class ExtractEachDoc2 {
	private String path= AllPath.sourcePath;
	
	
	public ExtractEachDoc2(String docid){
		System.out.println("finding: "+docid+"      (ExtractEachDoc2 - ExtractEachDoc2)");
		//?xiugai
		path+="/"+docid.substring(10,16)+"/"+docid.substring(17,19)+".warc";
		System.out.println(path);
		try {
			new WARC018Collection(path,docid);
		} catch (FileNotFoundException e) {
			tools.WriteFile_Log.writererr("Err - readfileErr - ExtractEachDoc - run():\n\tparm:\t"+docid);
			e.printStackTrace();
			return;
		}
	}
}
