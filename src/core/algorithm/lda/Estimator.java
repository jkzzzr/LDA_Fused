package core.algorithm.lda;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import javax.security.auth.kerberos.KerberosKey;

import searchDoc.AllPath;
//=======================��estimate��ʼ����===================
public class Estimator {
	
	// output model
	protected Model trnModel;
	//public HashMap<Integer,String> map1 = new HashMap<Integer,String>();
	public HashMap<Integer,Double> map2 = new HashMap<Integer,Double>();//xuhao ,defen
	LDAOption option;
	public static double[] Fxld=new double[0];
	
	public boolean init(LDAOption option){
		this.option = option;
		trnModel = new Model();
		
		if (option.est){//true
			if (!trnModel.initNewModel(option))//��������true
				return false;
			trnModel.data.localDict.writeWordMap(option.dir + File.separator + option.wordMapFileName);
			System.out.println(option.dir + File.separator + option.wordMapFileName);
		}
		else if (option.estc){
			if (!trnModel.initEstimatedModel(option))
				return false;
		}
		
		return true;
	}
	
	public void estimate(){
		loadMap();
//		System.exit(1);
		System.out.println("Sampling " + trnModel.niters + " iteration!");//trnModel��ʾ��ǰ����
		int lastIter = trnModel.liter;//������201�У���ȡ��201+��ǰ����
		trnModel.niters=200;
		long FTT=System.currentTimeMillis();
		for (trnModel.liter = lastIter + 1; trnModel.liter < trnModel.niters + lastIter; trnModel.liter++){
			System.out.println("Iteration " + trnModel.liter + " ...");

			
			long time=System.currentTimeMillis();
			// for all z_i
			for (int m = 0; m < trnModel.M; m++){	//M��ʾ�ĵ��������������ĵ�	
				System.out.println();
				System.out.print("QID "+LDA.qid+"\ttotal time "+(System.currentTimeMillis()-FTT)+"\tLast-Doc-Time:"+(System.currentTimeMillis()-time)+"\tInteration:"+trnModel.liter+"\tDOC: "+m+"\tWord: ");
				time=System.currentTimeMillis();
				int n = 0;
				//if(m<50)continue;
				for (; n < trnModel.data.docs[m].length; n++){ 
				int topic = sampling(m, n); 
				trnModel.z[m].set(n, topic);
				if(n%100==0)System.out.print("  "+n+",");
				}// end for each word
				System.out.print("  "+n+",");
			}// end for each documentʵֵ
			if (option.savestep > 0){
				if (trnModel.liter % option.savestep == 0){
					System.out.println("Saving the model at iteration " + trnModel.liter + " ...");
					computeTheta();
					computePhi();
					trnModel.saveModel("model-" + LDAUtils.zeroPad(trnModel.liter, 5));
				}
			}
		//	System.exit(1);
          }
		for (int i=0; i<trnModel.K; i++){
			trnModel.u[i] = udi(i);
			trnModel.o[i] = odi(i,trnModel.u[i]);
		}
		//?
		for (int top =0; top<trnModel.K; top++){
			trnModel.v[top] = computeV(top);
		}
		System.out.println("Gibbs sampling completed!\n");
		System.out.println("Saving the final model!\n");
		
		computeTheta();
		computePhi();
		trnModel.liter--;//���Ӧ����trnModel.niters+trnModel.liter-1
		trnModel.saveModel("model-final");
	}
	
	private void loadMap() {
		// TODO Auto-generated method stub
		System.out.println("++++++++++++++++++++++++++++++loadMap");
		Fxld=new double[trnModel.M];//System.out.println(trnModel.M);
		try {
			BufferedReader bd = new BufferedReader (new FileReader(AllPath.CBSPath));
			String temp = "";String[]  arr=new String [4];
			int min=5,max=5;
			while ((temp = bd.readLine())!=null){
				arr = temp.split("\t");
				int temp222=Integer.parseInt((String)arr[3]);
				if(temp222>=trnModel.M)break;
				Fxld[Integer.parseInt((String)arr[3])]=
						Math.log(Double.parseDouble((String)arr[4]));
			}
		} catch ( IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Do sampling
	 * @param m �ĵ���
	 * @param n word number �ʺ�
	 * @return topic id �����
	 */
	public int sampling(int m, int n){
		// remove z_i from the count variable
		int topic = trnModel.z[m].get(n);//�ĵ�m�ĵ�n�����飨���⣩
		int w = trnModel.data.docs[m].words[n];//�ĵ�m�ĵ�n���ʣ����⣩
		
		trnModel.nw[w][topic] -= 1;//��topic��صĴ� w ��instance����
		trnModel.nd[m][topic] -= 1;//�ĵ� m �к�topic��صĴ�����
		trnModel.nwsum[topic] -= 1;//��topic��ش� ����
		trnModel.ndsum[m] -= 1;//�ĵ� m �еĵ�������
		
		double Vbeta = trnModel.V * trnModel.beta;
	    double fxld = Fxld[m];
		//do multinominal sampling via cumulative method
		for (int k = 0; k < trnModel.K; k++){
			double ut = udi(k);
			double ot = odi(k,ut);
			double temp = (Math.exp(fxld)*ot*Math.sqrt(2*Math.PI))*
					Math.exp(
							-(Math.pow(fxld-ut,2))/
							(2*Math.pow(ot, 2)));
			trnModel.p[k] = (trnModel.nw[w][k] + trnModel.beta)/(trnModel.nwsum[k] + Vbeta) *
					(trnModel.nd[m][k] + trnModel.alpha)/temp;

		}

		trnModel.p[0]=Math.abs(trnModel.p[0]);
		for (int k = 1; k < trnModel.K; k++){
			double now=Math.abs(trnModel.p[k]);
			now+=trnModel.p[k-1];
			trnModel.p[k] = now;
		}
		
		// scaled sample because of unnormalized p[]
		double u = Math.random() * trnModel.p[trnModel.K - 1];
		for (topic = 0; topic < trnModel.K; topic++){

			if (trnModel.p[topic] > u)
				break;
		}
		trnModel.nw[w][topic] += 1;
		trnModel.nd[m][topic] += 1;
		trnModel.nwsum[topic] += 1;
		trnModel.ndsum[m] += 1;
 		return topic;
	}
	
	public void computeTheta(){
		for (int m = 0; m < trnModel.M; m++){
			for (int k = 0; k < trnModel.K; k++){
				trnModel.theta[m][k] = (trnModel.nd[m][k] + trnModel.alpha) / (trnModel.ndsum[m] + trnModel.K * trnModel.alpha);
			}
		}
	}
	
	public void computePhi(){
		for (int k = 0; k < trnModel.K; k++){
			for (int w = 0; w < trnModel.V; w++){
				trnModel.phi[k][w] = (trnModel.nw[w][k] + trnModel.beta) / (trnModel.nwsum[k] + trnModel.V * trnModel.beta);
			}
		}
	}

	
	/**
	 * @param t ��ʾĳ���ض��Ĵ���t
	 * @return //?
	 */
	public double udi(int t){
		double dtmp = 0.0;//
		int counttmp=0;
		for (int m = 0; m < trnModel.M; m++){	
			for (int n = 0; n < trnModel.data.docs[m].length; n++){ 
				if (t == trnModel.z[m].get(n)){

					
					
					dtmp += Fxld[m];//????
					counttmp++;
				}
			}
		}
		

		if(counttmp==0) return dtmp;
		return dtmp / counttmp;
	}
	
	public double odi(int t,double ut){
		double dtmp = 0.0;
		int counttmp = 0;
		for (int m = 0; m < trnModel.M; m++){		
			for (int n = 0; n < trnModel.data.docs[m].length; n++){
				if (t == trnModel.z[m].get(n)){
					dtmp += Math.pow(Fxld[m] - ut,2);
					counttmp++;
				}
			}
		}
		if(dtmp == 0.0) dtmp = 1.0;
		if(counttmp==0) return dtmp;
		return Math.sqrt(dtmp / counttmp);
	}
	public double computeV(int top){
		double tmp1 = Math.exp(trnModel.u[top] + 0.5 * Math.pow(trnModel.o[top],2));
		double tmp2 = 0.0;
		for (int i=0; i<trnModel.K; i++){
			tmp2 += Math.exp(trnModel.u[i]+0.5*Math.pow(trnModel.o[i], 2));
		}
		if (tmp2 == 0.0) tmp2 = 1.0;
		return tmp1 / tmp2;
	}
}
