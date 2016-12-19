package searchDoc;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.filechooser.FileNameExtensionFilter;

import cn.edu.fetchDocFromCollection.WriteFile;

public class WARC018Collection 
{
	
	/** has the end of the current input file been reached? */
	protected boolean eof = false;
	/** the input stream of the current input file */
	public static BufferedReader bufferedReader=null;
	/** the length of the blob containing the document data */
	protected long currentDocumentBlobLength = 0;
	/** properties for the current document */
	protected Map<String,String> DocProperties = null;
	public int flag=0;

	public static String bufferString=null;
	String pageId,docid;
	String url;
    WriteFile wf;

	/**
	 * 此处构造失败，就应该抛出来
	 * @throws FileNotFoundException 
	 * */
	public WARC018Collection(String filename,String docid) throws FileNotFoundException
	{
			this.docid=docid;
			bufferedReader=new BufferedReader(new FileReader(filename));
	}


	public void run(String docid){

		this.docid = docid;
		this.nextDocument(docid);
	}
	

	private void nextDocument(String docid2) {
		String tempString =null;
		try {
			StringBuffer sBuffer=new StringBuffer("");
			//找新文档
			while(findNewFile()){
				//找到文档号
				while ((tempString = bufferedReader.readLine())!=null){
					
					if (tempString.startsWith("WARC-TREC-ID:")){
						
						String docid_in_warc = tempString.substring(tempString.indexOf("clueweb"));
						int cmpResult = docid_in_warc.compareTo(docid2);
						if (cmpResult < 0){
							continue;
						}else if (cmpResult == 0){
							while (true){
								String stemp = bufferedReader.readLine();
								if (stemp == null || stemp.startsWith("WARC/0.18")||tempString.startsWith("WARC-Type:")||tempString.startsWith("WARC-Target-URI:")){
									break;
								}else {
									sBuffer.append(stemp);
								}
							}
							Content.setContent(sBuffer.toString());
							bufferString=tempString;
							return;
						}
						else if (cmpResult > 0){
							System.err.println("出错了！要查找的文档 ：" + docid2 +"不存在，当前查找到的文档编号为：" + docid_in_warc);
							return ;
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	private boolean findNewFile() throws IOException{
		String tempString;
		while(true){
			tempString=bufferedReader.readLine();
			if (tempString == null){
				return false;
			}
			if (tempString.startsWith("WARC/0.18")||tempString.startsWith("WARC-Type:")||tempString.startsWith("WARC-Target-URI:")){
				return true;
			}
		}
	}

	/** Move the collection to the start of the next document. */
	public void nextDocument() {
		String tempString =null;
		try {
			StringBuffer sBuffer=new StringBuffer("");
			if(!FindPage()){
						return;
			}
			
			//		while((tempString=bufferedReader.readLine())!=null){
					while(true){
						tempString=bufferedReader.readLine();
						//如果文件读完了
						if(tempString==null){
							Content.setContent(sBuffer.toString());
							bufferString=tempString;
							return;
						}
						//如果读取到了新文件
						if(tempString.startsWith("WARC/0.18")){
							Content.setContent(sBuffer.toString());
							bufferString=tempString;
							return;
						}
						sBuffer.append(tempString);
					}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	private boolean FindPage() {
		return false;
	}
	
	private boolean FindPage(int x) {
		String tempString=null;
		try {
			//文件最后一个
			if(!(bufferString.startsWith("WARC/0.18"))){
					if (bufferString.trim().equals(docid)) {
						return MoveHead();
				}	
			}
			
			
			while((tempString=bufferedReader.readLine())!=null){
				if(tempString.startsWith("WARC-TREC-ID")) {
					String[] strarr=tempString.split(":");
					    if (strarr[1].trim().equals(docid)) {
							return MoveHead();
						}else if(strarr[1].trim().compareTo(docid)>0){
							bufferString=docid;
							
							BufferedWriter bwBufferedWriter=new BufferedWriter(new FileWriter(ExtractEachDoc.errPath));
							bwBufferedWriter.write(docid);
							bwBufferedWriter.close();
							
							return false;
						}
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	
	}


	private boolean MoveHead() {
		// TODO Auto-generated method stub
		String tempString=null;
		int tempF=-1;
		try {
			while((tempString=bufferedReader.readLine())!=null){
				if(tempString.startsWith("Content-Length")) {
								tempF++;
				}
				if(tempString.startsWith("WARC/0.18")) return false;
				if(tempF>0){
					return true;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;

	
	/** read a line from the currently open InputStream is */
	}
}