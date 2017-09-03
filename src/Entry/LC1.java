import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.TreeMap;
//+++++++++++++++�����䣺����׼����Ȩ��ֵ�趨�������ڴ��󣡣�����Ӧ�����ݲ�ͬ��Query����ѯ���ֿ���Ҳ����˵��ѯ�ú�docid��ͬ��������������������������������������������������������
public class LC1 {
	public static String iFilePath="F:/Fusiondata/2009/divinput.n";
	public static String oFilePath="F:/Fusiondata/ResultBox/Result.LC1";
	public static Scanner sc;
	public static HashMap<String,String> hm2 = new HashMap<String,String>();//����MAP�Ĳ��ظ����ʣ��ۼӲ�ͬ��qid��docString
	public static int standardlength=10000;
	public LC1() throws FileNotFoundException{
        //������������Ĺ��캯��������ִ��
	}

	public static double normscore(int rank){
		return 1.0/(rank+60);
	}
	
	public static void main(String[] args) throws IOException {
		//***********************��ȡ�ļ����������ļ�Ŀ¼************************
		String fileList[] = new File(iFilePath).list();
		for (String file : fileList){
			BufferedReader br = new BufferedReader( new FileReader(iFilePath+"/"+file));
			String tempLine = null;
		//********************��ȡĳ���ļ����������*******************************
			while((tempLine = br.readLine())!=null){
				StringTokenizer st = new StringTokenizer(tempLine);
				if(!st.hasMoreTokens()) break;
				double qid = Double.parseDouble(st.nextToken()); //����ѯ�����ó�double����
				st.nextToken(); //Q0��ȥ
				String docid = st.nextToken(); 
				int rank = Integer.parseInt(st.nextToken());//����rank
				
				String qid_doc = qid +"$"+ docid;//����ѯ����docid�ϲ�
				
				double score_temp =normscore(rank); //���ķ���ȡ��������ϵͳ���¸���1/rank+60
				String score = qid + "$" +score_temp;
                //ֻ�С���ѯ��+�ĵ��š� ��ͬ�ĲŻ�����ۻ���
				if(hm2.get(qid_doc)==null){
					hm2.put(qid_doc, score);
				}else{
					String[] temp = hm2.get(qid_doc).split("[$]"); 
					score_temp += Double.parseDouble(temp[1]);
					hm2.put(qid_doc, temp[0]+"$"+temp[1]);
				}			
			}	
			br.close();
			System.out.println(file);
		}
//***********************��ȡ��ȫ���ļ����õ��˽�������hm2��***********************
		BufferedWriter bw = new BufferedWriter(new FileWriter(oFilePath));
		//����TreeMap�Զ���������ʣ����ɴ洢���ݡ� ��ʽΪ��(qid+score,qid_doc)
		@SuppressWarnings("resource")
		TreeMap<String,String>  tr = new TreeMap<String,String>((o1,o2)->{//o1λqid+score
			String[] temp1 = o1.split("[$]");//System.out.println(o1+"  "+temp1[1].getClass());
			double a1 = Double.parseDouble(temp1[0]);double a2 = Double.parseDouble(temp1[1]);
			String[] temp2 = o2.split("[$]");
			double b1 = Double.parseDouble(temp2[0]); double b2 = Double.parseDouble(temp2[1]);
			//ǰ�����Ƚ�����ȷ�ģ���Ϊϣ�� С��qid����ǰ�棬�����������෴�ģ���Ϊϣ�� ���score����ǰ��
			if(a1 > b1)return 1;
			else if(a1 < b1) return -1;
			//a1 = b1
			     if(a2 > b2) {return -1;}
			     else if(a2 < b2) {return 1;}
			return 0;
		});
		System.out.println("1");
		hm2.forEach((o1,o2)->{tr.put((String)o2, (String)o1);});
		System.out.println("11");
		//int count = -1;
		int count = 1;
		int  tmpqid = 1;
		for (Object o1 : tr.keySet()){//tr ��ʽΪ��(qid+score,qid_doc)
			String[] tmp1 = ((String)o1).split("[$]");
			
            String [] tmp2 =((String)tr.get(o1)).split("[$]");
            if(tmpqid != (int) Double.parseDouble(tmp1[0])) 
            {tmpqid = (int)Double.parseDouble(tmp1[0]);
            count = 1;}
           // bw.write(++count+"\t"+(int)(Double.parseDouble(tmp1[0]))+"\t"+tmp2[1]+"\t"+tmp1[1]+"\n");
           bw.write((int)Double.parseDouble(tmp1[0])+"\t"+"Q0"+"\t"+tmp2[1]+"\t"+(count++)+"\t"+tmp1[1]+"\t"+"CombSum--");
           bw.newLine();
		}System.out.println("1111");
		bw.flush();
		bw.close();
		System.out.println("end");
	} 
    
	
}
