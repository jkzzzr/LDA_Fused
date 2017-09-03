

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;

import core.algorithm.lda.LDA;
import searchDoc.AllPath;

public class PM2 {
	//???qid
	private int qid;
	public int tao;// S�������ޣ������Ž����С
	public double lanta = 0.5;// ϵ��
	//????
	public  int subQueryNum = 10;// subtopic�����趨Ϊ10
	public static int Qid = 101;
	public static int FirstQ=101;
	public static int DOC_Length=0;;
	
	private  HashMap<String, Double> initScore = new HashMap<String, Double>();// ��ʼ�����id,score��
	private  ArrayList<HashMap<String, Double>> subScore = new ArrayList<HashMap<String, Double>>();// subtopic��ѯ���[subq1   // d								// p,subq2   // d  // p,...]

    //?
	public double[][] theta = null;//�ĵ��ţ��������
	public double[][] fCombSum = null;//��һ��������ʾ�ĵ���,�ڶ�����ʾ��ѯ
	int COUNT = 50;//����fCombSum�ڶ�����������ʾQ������
    public double[] u =null;
    public double[] o = null;
    public double[] v= null;
	
    public String basePath =AllPath.PM2inputPath+"/"+Qid+"/model-final";
    public String thetaPath =basePath+".theta";
    public String uPath =basePath+".u";
    public String oPath =basePath+".o";
    public String vPath = basePath+".v";
	public static String fCombSumPath = AllPath.CBSPath;
	public static String outputPath = AllPath.PM2out2Path;
    
	private ArrayList<String> divResList; // ���շ��ؽ��

	public PM2(int qid, double lanta) {
		super();
		this.qid = qid;

		this.lanta = lanta;
	}

	public int getQid() {
		return qid;
	}

	public void setQid(int qid) {
		this.qid = qid;
	}

	public int getTao() {
		return tao;
	}

	public void setTao(int tao) {
		this.tao = tao;
	}

	public double getLanta() {
		return lanta;
	}

	public void setLanta(double lanta) {
		this.lanta = lanta;
	}

	public int getSubQueryNum() {
		return subQueryNum;
	}

	public void setSubQueryNum(int subQueryNum) {
		this.subQueryNum = subQueryNum;
	}

	public HashMap<String, Double> getInitScore() {
		return initScore;
	}

	public void setInitScore(HashMap<String, Double> initScore) {
		this.initScore = initScore;
	}

	public ArrayList<HashMap<String, Double>> getSubScore() {
		return subScore;
	}

	public void setSubScore(ArrayList<HashMap<String, Double>> subScore) {
		this.subScore = subScore;
	}

	public void setDivResList(ArrayList<String> divResList) {
		this.divResList = divResList;
	}

	public ArrayList<String> getDivResList() {
		return divResList;
	}

	/**
	 * ��ĳ��docID��Ӧ��o,u,theta,fCombSum���ĵ�������Ϊint
	 * @param docID
	 * @return
	 */
	public int docID_int(String docID){
		String[] tempStr = docID.split("[+]");
		return Integer.parseInt(tempStr[0]);
	}
	/**
	 *  �ĵ�d�ڼ������Ľ���г��ֵĸ���
	 * @param docID �ĵ���
	 * @param i ����z
	 * @return
	 */
	public double p_Query_DOC(String docID, int i) {
		int id = docID_int(docID);
//		System.out.println("****"+docID+" "+i+"=="+thetaPath.length());
//		System.out.println("----->"+id+"\t"+i+"\t"+theta[id][i]);
		double temp = theta[id][i]*fCombSum[Qid-FirstQ][id]/Math.exp(u[i]+0.5*Math.pow(o[i], 2));
//		System.out.println(temp);
//		System.out.println( theta[id][i]+" --"+fCombSum[id][Qid-FirstQ]+"--"+docID+"--"+i+"--"+temp);
	    return temp;
	}
    /**
     * ���أ�����array�����ֵ��Ӧ���±�
     * @param array
     * @return
     */
	private int getMaxIndex(double[] array) {
		int index = 0;
		double max = 0;
		for (int i = 0; i < array.length; i++) {
			if (max < array[i]) {
				max = array[i];
				index = i;
			}
		}
		return index;
	}

	/**
	 * 
	 * @param initRes ����ĵ��ļ���
	 * @param votes
	 * @return
	 */
	public double[] diversification_PM2(ArrayList<String> initRes,
			double[] votes) {
		divResList = new ArrayList<String>(); // ��ʼ�����ձ�����
		// ����tao��
		double[] seats = new double[this.subQueryNum];// ÿ��subtopic��ռϯλ
		double[] docscore = new double[this.tao];
		int si = 0;
		while (divResList.size() < this.tao) {//����tao��
			// ȷ����ѡ����subtopic
//			System.out.println("������"+divResList.size()+"��"+this.tao);
			double[] quotion = new double[this.subQueryNum];// ÿ��subtopic��ǰ��ӵ�е���ϯ���
		//	System.out.println("quotion[]");
			for (int i = 0; i < this.subQueryNum; i++) {
				quotion[i] = votes[i] / (2 * seats[i] + 1);
				
		//		System.out.print(" q"+i+": "+quotion[i]);
				
			}//System.out.println();
			
			int maxqt_i = getMaxIndex(quotion);
			int size = initRes.size();// R\S
			double res[] = new double[size];// ����R/S�ĵ��ļ���÷�
			for (int j = 0; j < size-1; j++) { // �ӵ�ǰ��R��ѡ��÷���ߵ��ĵ����ŵ�S��
//				System.out.println(size);
//				System.out.println("size:->"+size+" j "+j);
				double part_1 = lanta * quotion[maxqt_i]
						* p_Query_DOC(initRes.get(j), maxqt_i);//initRes.get(j) �õ������ĵ���
				double part_2 = 0;
				for (int i = 0; i < this.subQueryNum; i++) {
					if (i != maxqt_i) {
						part_2 += (quotion[i] * p_Query_DOC(initRes.get(j), i));
					}
				}
				part_2 *= (1-lanta);
				res[j] = part_1 + part_2;
			}
			int maxdoc_i = getMaxIndex(res);
//			System.out.println(res[maxdoc_i]+" "+maxdoc_i+" "+res.length);
			docscore[si] = res[maxdoc_i];
			si++;
			String select_doc = initRes.get(maxdoc_i);
			divResList.add(select_doc);//����d*��С��������¼�����б�
			initRes.remove(maxdoc_i);
		//	initRes.forEach((o1)->System.out.print(o1+"%%"));System.out.println();

			double ts = 0;
			for (int j = 0; j < this.subQueryNum; j++) {
				ts += p_Query_DOC(select_doc, j);
//				System.out.println(ts+"  "+j);
			}
			if (ts > 0) {
				for (int i = 0; i < this.subQueryNum; i++) {
					seats[i] += p_Query_DOC(select_doc, i) / ts;
				}

			}

		}
//		System.out.println("����pm2��������");
		return docscore;
	}

	public static void main(String[] args) throws Exception {
		 FirstQ =101;
	 for(int QID = 101;QID<=150; QID++){
		 Qid = QID;
		 DOC_Length=100;
		int tao = 540;//��ʾһ���ж����ļ������������ٴ�
		//for(int k=3;k<=3;k++){
		int k =5;
		  DecimalFormat df = new DecimalFormat(".00");//?
		  double lanta= Double.parseDouble(df.format(k*0.1));
		  BufferedWriter writer_result = new BufferedWriter(new FileWriter(outputPath+"/2011.PM2",true));
				ArrayList<String> initRes = new ArrayList<String>();//��ͬ��ѯ�漰�����ĵ�����
				//rank + docid
				setInit(initRes,DOC_Length);
				//System.out.println(initRes.size()+"-----------------");
				int qid = 10;
				PM2 d = new PM2(qid, lanta);//��static void main �е��÷�static��Ա
				tao = tao > initRes.size() ? initRes.size() : tao;
				d.setTao(tao);
                System.out.println("tao\t"+tao);
				d.u_o_thetaRead(d.oPath,d.uPath,d.thetaPath,d.vPath,d.fCombSumPath,d.tao);
			
				double[] votes = d.v;
				double[] docscore = d.diversification_PM2(initRes, votes);//�洢d*
				for (int i = 0; i < d.getDivResList().size(); i++) {
					String[] tempStr = d.getDivResList().get(i).split("[+]");
/*					String RankResult = d.getQid() + " Q0 "+ d.getDivResList().get(i) 
							+ "\t" + (i + 1) + "\t"+ docscore[i] + " team\n";*/
		//			String RankResult = tempStr[1] + " Q0 "+ (i+1) 
		//					+ "\t" + tempStr[0] + "\t"+ docscore[i] + " team\n";
					String RankResult = QID+"\t"+"Q0\t"+ tempStr[1] +"\t"+ (i) 
							+ "\t" + docscore[i] + "\tteam\n";
					
					writer_result.write(RankResult);
				}
			writer_result.close();
		//}
			System.out.println("end--->"+QID+"@");
	 }
		System.out.println("end");
	}
    
	/**
	 * ��ȡ��ѯ��ص��ĵ��Ŵ洢������initRes��,ÿ����ѯһ���ĵ���
	 * @param initRes
	 */
	private static void setInit(ArrayList<String> initRes,int N) {
		// TODO Auto-generated method stub
		try {
			BufferedReader br = new BufferedReader( new FileReader(fCombSumPath));
			String temp = null;
			int tempQ=0;
			int countQ=0;
			while((temp = br.readLine())!=null){
			    String[] tempStr = temp.split("\t");
			    if(tempQ!=Integer.parseInt(tempStr[0])){
			    	tempQ=Integer.parseInt(tempStr[0]);
			    	countQ=0;
			    }
//			    System.out.println(tempStr[3]+"  "+tempStr[2]);
			    if(Integer.parseInt(tempStr[0]) == Qid){
			    	if(countQ>=N)continue;
			    	countQ++;
			    initRes.add(tempStr[3]+"+"+tempStr[2]);
			    }
			    //rank + docid
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			System.out.println("PM2 - setInit - new BufferedReader");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("PM2 - setInit - readLine");
			e.printStackTrace();
		}
		
	}

	/**
	 * ��u��o��theta����fCombSum��ֵ����
	 * @param oPath
	 * @param uPath
	 * @param thetaPath
	 * @param vPath
	 * @param fCombSumPath
	 * @param docLength�ĵ����ȣ�����theta����ĵ�һ�����ȣ��ڶ�������Ϊ������������this.subQuerNum��
	 * @throws IOException
	 */
	private void u_o_thetaRead(String oPath,String uPath, String thetaPath,String vPath,String fCombSumPath,int docLength) throws IOException {
		// TODO Auto-generated method stub
		o = new double[this.subQueryNum];
		BufferedReader bd1 = new BufferedReader( new FileReader(oPath));
		
		u = new double[this.subQueryNum];
		BufferedReader bd2 = new BufferedReader( new FileReader(uPath));
		
		theta = new double[docLength][this.subQueryNum];
		fCombSum = new double[COUNT][docLength];
		BufferedReader bd3 = new BufferedReader( new FileReader(thetaPath));
		
		v = new double[this.subQueryNum];
		BufferedReader bd4 = new BufferedReader( new FileReader(vPath));
		
		BufferedReader bd5 = new BufferedReader( new FileReader(fCombSumPath));
		
		String temp = "";
		
		int i = 0;
		while (((temp = bd1.readLine())!=null)&&i<this.subQueryNum){
			o[i] = Double.parseDouble(temp);
			i++;
		}bd1.close();
		
		i = 0;
		while (((temp = bd2.readLine())!=null)&&i<this.subQueryNum){
			u[i] = Double.parseDouble(temp);
			i++;
		}bd2.close();
		
		i = 0;
		while(((temp = bd3.readLine())!=null)&&i<docLength){
			theta[i] = new double [this.subQueryNum];
			String [] stemp = temp.split(" ");//?�Ƿ���Էָ����ո���������
			for (int j = 0; (j<stemp.length)&&(j<this.subQueryNum);j++){
				theta[i][j] = Double.parseDouble(stemp[j]);
//				System.out.print(theta[i][j]+"\t");
			}
//			System.out.println();
			i++;
		}bd3.close();
		
		i = 0;
		while(((temp = bd4.readLine())!=null)&&i<docLength){
			v[i] = Double.parseDouble(temp);
            i++;
		}bd4.close();System.out.println();
		
		i = 0;
		while(((temp = bd5.readLine())!=null)){
			String[] tempStr = temp.split("\t");
			if(Integer.parseInt(tempStr[0])!=Qid)continue;
			if(i>=docLength) break;
			//System.out.println("lenth of FCombSum temp :"+tempStr.length+" "+tempStr.toString());
			int qn = Integer.parseInt(tempStr[0])-FirstQ;
			fCombSum[qn][i] = Double.parseDouble(tempStr[4]);
			i++;
		}
		bd5.close();
	}


}
