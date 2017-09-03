package cn.edu.processTerm;
import java.util.*;
public class ConstructTerms {
	ArrayList<RecordTerms> list = new ArrayList<RecordTerms>();
	RecordTerms term;

	/**
	 * ���ʼ���ArrayList��
	 * @param keyword
	 */
	public void process(String keyword){
		if(!this.contain(keyword)){
			this.addList(keyword);
		}
	}
	
	
	public void addList(String keyword) {
		term = new RecordTerms(keyword);
		list.add(term);
	}
	
	public boolean contain(String keyword){
		int length=list.size();
		String text;
		if(length==0){
			return false;
		}else{
			for(int i=0;i<length;i++){
				term=(RecordTerms)list.get(i);
				text=term.getTerm();
				//System.out.println(text+"   "+term.getFrequent());
				if(keyword.compareTo(text)==0){
					term.addFrequent();
					return true;
				}
			}
			return false;
		}
	}
	
	public ArrayList getList(){
		return this.list;
	}
	
	/**
	 * ���ÿ��ź��������п��ţ�������ȡ�ʸ��б���ԭ���б���ȥ�ظ���
	 */
	public void quickSort(){
/*		int len=this.list.size();
		String[] keyword=new String[len];
		for(int i=0;i<len;i++){
			term=(RecordTerms)list.get(i);
			keyword[i]=term.getTerm();
		}
		QuickSort sort=new QuickSort(keyword);
		keyword=sort.sort(0, len-1);
		ArrayList temp=new ArrayList(len);
		for(int i=0;i<len;i++){
			temp.add(this.getClass(keyword[i]));
		}
		list=temp;*/
		
		int len=this.list.size();

		RecordTerms[] temp3=new RecordTerms[len];
		for(int i=0;i<len;i++){
			temp3[i]=(RecordTerms)list.get(i);
		}
		
		
		QuickSort<RecordTerms> sort=new QuickSort(temp3);
		temp3=(RecordTerms[]) sort.sort(0, len-1);
		ArrayList temp=new ArrayList(len);
		for(int i=0;i<len;i++){
			temp.add(this.getClass(temp3[i].getTerm()));
		}
		list=temp;
	}
	/**
	 * ���keyword��list������֮��Ӧ�Ĵʸ��򷵻شʸɣ����򷵻ؿ�
	 * @param keyword
	 * @return
	 */
	public RecordTerms getClass(String keyword){
		int length=list.size();
		String text;
			for(int i=0;i<length;i++){
				term=(RecordTerms)list.get(i);
				text=term.getTerm();
				//System.out.println(text+"   "+term.getFrequent());
				if(keyword.compareTo(text)==0){
					return term;
					
				}
			}
			return null;
	}
			
}
