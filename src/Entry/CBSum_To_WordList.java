import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import cn.edu.fetchDocFromCollection.EntryH_W;
import searchDoc.*;
/**
 * 根据输入的文档结果列表，得到对应的各个文档的分词信息
 * @author Lee
 *
 */
public class CBSum_To_WordList {

	public static HashMap<String ,String> ResultMap ;
	/**
	 * CombSum结果列表
	 */
	public static String CBSPath = AllPath.CBSPath;
	public static String outPath = AllPath.wordListPath;
	
	public static void main(String[] args) {
		
		try {
			(new CBSum_To_WordList()).GetList(1);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			WriteKeyWordFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
        System.out.println("end\tCBSum_to_WordList - main()");
        
	}
	/**
	 * 重新生成关键词
	 * 根据docID 返回对应文档的关键词
	 * @param docID
	 * @return
	 */
	public String GetKeyWordStr(String docID){
        String htmlCode = EntrySD2.run(docID);
        String keyString = EntryH_W.run(htmlCode, docID,1);
        return keyString;
	}
	
	/**
	 * 读取已有关键词
	 * 根据docID 返回对应文档的关键词
	 * @param docID
	 * @return
	 */
	public String GetKeyWordStr2(String docID){
		String result="";
		try {
			BufferedReader bReader = new BufferedReader(new FileReader(AllPath.WordList_perDoc_Path+"/"+docID));
			String tempString=null;
			try {
				while((tempString=bReader.readLine())!=null){
					result+=tempString+"\t";
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return result;
	}
	/**
	 * @param i i>0，选取前100个元素。i绝对值为1表示，重新生成关键词
	 * @throws IOException
	 */
	public void GetList(int i) throws IOException{
		ResultMap = new HashMap<String,String>();
		BufferedReader br = new BufferedReader( new FileReader(CBSPath));
		String tempLine;
		while((tempLine = br.readLine()) != null){
			String[] tempArr = tempLine.split("\t");

			String tempDoc = tempArr[2];
			if( ResultMap.containsKey(tempDoc)){
				continue;
			}
			String tempQid = tempArr[0];
			
			
			String tempValue="";
			//如果i大于0,表示，只选取前100个元素
			if (i > 0){
				if (Integer.parseInt(tempArr[3])>99) {
					continue;
				}
			}
			if (Math.abs(i) == 1){
				tempValue = GetKeyWordStr(tempDoc);
			}else {
				tempValue = GetKeyWordStr2(tempDoc);
			}
			
			/*if(i ==1){
				tempValue = GetKeyWordStr(tempDoc);
			}else{
				if (Integer.parseInt(tempArr[3])>99) {
					continue;
				}
				tempValue=GetKeyWordStr2(tempDoc);
				System.out.println("Reading: "+tempDoc+"   "+tempArr[0]+"    "+tempArr[3]+"-------"+tempValue.length());
			}*/
			
			
			ResultMap.put(tempDoc, tempQid+"+"+tempValue);
		}
		br.close();
		System.out.println("end\tCBSum_To_WordList.java - GetList() ");
	}
	/**
	 * ��GetList�õ��˺������ݵ�ResultMap֮�󣬽��CombSum�����
	 * ���ݲ�ͬ�Ĳ�ѯ�õ�50���ļ���ÿ���ļ���ÿһ�ж�Ӧ����PM2�������ļ�
	 * @throws IOException 
	 */
	public static void WriteKeyWordFile() throws IOException{
		BufferedReader br = new BufferedReader(new FileReader(CBSPath));
		String tempLine ;
		String tmpqid = "";
		BufferedWriter bw = new BufferedWriter(new FileWriter(outPath+"/step2/out.null",true));;
		int[] count2 = new int[50];
		for(int i=0;i<50;i++){
			count2[i]=100;
		}
		int num=0;
		while(((tempLine = br.readLine())!=null)){
			//count1++;
			String[] tempArr = tempLine.split("\t");
			String tempDoc = tempArr[2];
			String tempQid = tempArr[0];
			if(Double.parseDouble(tempArr[4])<=0.0)continue;
			if(!tempQid.equals(tmpqid)){
				System.out.println("Writing QID : "+tmpqid);
				tmpqid = tempQid;
			   bw.close();
			   bw = new BufferedWriter(new FileWriter(outPath+"/"+tempQid+".out1"));
			   bw.write(count2[Integer.parseInt(tmpqid)-1]+"\n");
			   num=0;
			}
			if(num>=100)continue;
			num++;
	//		System.out.println(tempDoc);
			String[] tempValue = null;
			try{
				tempValue = (ResultMap.get(tempDoc)).split("[+]");
				System.out.println("resultmap"+ResultMap.get(tempDoc));
			}
			catch(Exception e){
				System.out.println(tempDoc+"\t"+tempLine+"@@@@@@@@@@@");
			}
		//	System.out.println("***********"+tempValue.length);
			
		//	try{
				if (tempValue.length <= 1 || tempValue[1].length()<3){
					BufferedWriter bwBufferedWriter=new BufferedWriter(new FileWriter(AllPath.Log+".CBSum_to_WordList",true));
					bwBufferedWriter.write(tempDoc);
					bwBufferedWriter.newLine();
					bwBufferedWriter.close();
					tempValue = new String[]{"","null"};
					System.out.println("123\t");
				}
		/*	}catch(Exception e){
				System.err.println(tempDoc + "\t"+tempValue.length);
			}*/
			bw.write(tempValue[1]);
			bw.newLine();
			bw.flush();
		}
		br.close();
		bw.close();
	}
	public static int[] Count(){
		int temp=-1;
		int tmpC=0;
		int[] result=new int[50];
		try {
			BufferedReader bReader=new BufferedReader(new FileReader(CBSPath));
			String templine=null;
			while((templine=bReader.readLine())!=null){
				String[] temparr=templine.split("\t");
				if(Double.parseDouble(temparr[4])==0.0)continue;
				result[Integer.parseInt(temparr[0])-1]++;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return result;
	}
	
}
