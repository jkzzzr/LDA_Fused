package cn.edu.fetchDocFromCollection;
/**
 * ����ҳԴ�룬ת���ɹؼ���
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import javax.imageio.IIOException;

import cn.edu.splitByWord.LexicalAnalysis;
import searchDoc.AllPath;
public class EntryH_W {
	/**
	 * 
	 * @param htmlCode 对应文档的html代码
	 * @param docID 文档id
	 * @param percent 随机取词的百分比
	 * @return 关键词
	 */
	public static String run(String htmlCode,String docID,double percent){
		String qID = "3";
		HtmlToText htt = new HtmlToText();
		String html = "";
		String temp = htt.Convert(htmlCode);
		System.out.println("\t"+temp.length()+"(Text length)");
		new LexicalAnalysis(docID, temp,qID);
		String result = LexicalAnalysis.ResultList;
		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(AllPath.WordList_perDoc_Path+"/"+docID));
			String []temp2 = result.split("\t");
			if(temp2.length<1){
				tools.WriteFile_Log.writererr("EntryH_W - run()\t 网页源码转为文档时，长度太短\t" + docID);
			}
			int lenTemp=(int)((temp2.length)*percent);
			double PWord=0.7;
			if(temp2.length<200){
				for (int i = 0; i<temp2.length;i++){
					bw.write(temp2[i]);
					bw.newLine();
				}
			}else {
				if(temp2.length<400){
					PWord=((double)temp2.length)/400.0;
					for (int i = 0; i<temp2.length;i++){
						double tempDouble=Math.random();
						if(tempDouble>PWord) continue;
						bw.write(temp2[i]);
						bw.newLine();
					}
				}else{
					PWord=0.7;
					for (int i = 0; i<lenTemp;i++){
						double tempDouble=Math.random();
						if(tempDouble>PWord) continue;
						bw.write(temp2[i]);
						bw.newLine();
					}
				}
			}
			bw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("\tend\t(EntryH_W - run)");
		return result;
	}
}
