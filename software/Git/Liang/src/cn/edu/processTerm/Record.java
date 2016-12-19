package cn.edu.processTerm;

public interface Record<T> extends Comparable<T> {

	public void  addFrequent();
	public void setTerm(String term);
	public String getTerm();
	public int getFrequent();

}
