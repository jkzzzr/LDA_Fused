import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import cn.edu.fetchDocFromCollection.EntryH_W;

import searchDoc.AllPath;
import searchDoc.EntrySD;
import searchDoc.EntrySD2;

/**
 * 第一步，将CombSum结果列表中的所有文档关键词提取出来
 * @author lee
 *
 */
public class Step1_1 {

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static double percent=0.0;
	public static void main(String[] args) throws IOException {
	    percent=0.7;
		BufferedReader bReader = null;
		try {
			bReader=new BufferedReader(new FileReader(AllPath.cBSListPath));
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}			
		String templine = null;
		try {
			while((templine=bReader.readLine())!=null){
				String tempString = new Step1_1().GetKeyWordStr(templine.trim());
							
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		bReader.close();
		
	}
	/**
	 * 从数据集中取出文档HTML源码，再去标签得到文本，最后得到关键词String，以\t分隔
	 * @param docID
	 * @return 输入文档id，得到文档中的所有关键词
	 */
	public String GetKeyWordStr(String docID){
        String htmlCode = EntrySD.run(docID);
        String keyString = EntryH_W.run(htmlCode, docID,percent);
    //    System.out.println(keyString.length()+"+keyString+"+keyString);
        if(keyString==null){
        	System.out.println("Step1_1...GetKeyWordStr");
				try {
					BufferedWriter bwBufferedWriter=new BufferedWriter(new FileWriter("/home/huang/Documents/LLFile/Pro_Union/emptyF",true));
					bwBufferedWriter.write(docID);
					bwBufferedWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
        }
        return keyString;
	}
}
