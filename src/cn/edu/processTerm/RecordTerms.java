package cn.edu.processTerm;

public class RecordTerms implements Record{

	String term;
	int frequent=1;
	public RecordTerms(String term) {
		// TODO Auto-generated constructor stub
	this.term=term;
	}
	public void  addFrequent(){
		frequent++;
	}
	public void setTerm(String term){
		this.term=term;
	}
	public String getTerm(){
		return this.term;
	}
	public int getFrequent(){
		return this.frequent;
	}
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		
		if(this.getFrequent()>((RecordTerms)o).getFrequent()){
			return -1;
		}else if(this.getFrequent()<((RecordTerms)o).getFrequent()){
			return 1;
		}
		return 0;
	}
}



