package core.algorithm.lda;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import searchDoc.AllPath;

public class LDA implements Runnable{
	public static int qid = 101;
//	public static String dirPath = AllPath.outPath;"F:/Fusiondata/ResultBox";
	public void run() {
		LDAOption option = new LDAOption();
		//String dirPath =  "F:/Fusiondata/ResultBox/WordList";
		String dirPath = AllPath.wordListPath;
		String dfilePath = null;
		try {
			dfilePath = getDfile(dirPath,qid);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		option.dir = AllPath.LDAoutPath+"/"+qid;//������������ļ�·��
		mkDir(option.dir);
		option.dfile = AllPath.wordListPath+"/"+qid+".out1";//���ĵ�����ȡ�� ����ļ�
		option.est = true;  /////
		///option.estc = true;
		option.inf = false;
		option.modelName = "model-final";
		
		option.niters = 1000;
		Estimator estimator = new Estimator();
		System.out.println(estimator.init(option));
		
		estimator.estimate();//��Ҫ���еĳ���
	}
	/**
	 * ������ȡ�ؼ��ֺ�������ȡ�����ĵ��ĸͼ��Σ���ȡ���ļ�qid+".out1"�������������������ʵ����ʡ��
	 * @param dirPath 
	 * @param qid 
	 * @return
	 * @throws IOException
	 */
	private String getDfile(String dirPath, int qid) throws IOException {
		/*		String tempPath = dirPath +"/"+qid+".out1";
		BufferedWriter bw = new BufferedWriter (new FileWriter(tempPath));*/
		//bw.write();
		return qid+".out1";
	}
	public String[] getKeyWords(String docString){//?????????
		return null;
	}
	public static void mkDir(String folderName){
        if (folderName == null || folderName.isEmpty()) {
            return ;
        }

        File folder = new File(folderName);
        if(!(folder.exists() && folder.isDirectory())) 
        	folder.mkdirs();
	}
	
	public void run1(){
		long ttt=System.currentTimeMillis();
		for(int i =101 ;i<=150 ;i++){
			qid =i;
			long t=System.currentTimeMillis();
			System.out.println("QID: "+i +"\t\t-------------------------");
			new LDA().run();
			System.out.println("Finish: "+i+"\t\t"+(System.currentTimeMillis()-t));
		}
		System.out.print("total time:\t"+(System.currentTimeMillis()-ttt));
	}
	
	
	// TODO Auto-generated method stub
	public static void main(String[] args) {
	//	new LDA().run();
		new LDA().run1();
		System.out.println("end\t(LAD - main)");
	}
}
