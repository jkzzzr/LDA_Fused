package cn.edu.term;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashSet;

import searchDoc.AllPath;

import java.io.IOException;

//import org.apache.log4j.Logger;
//import gnu.trove.THashSet;

/**
 * ͣ�ʴ洢·��ΪStopwordsFile
 * @author john
 */



public class Stopwords implements TermPipeline
{



	/** The hashset that contains all the stop words.*/
	public static final HashSet<String> stopWords = new HashSet<String>();
	public static final HashSet<String> stopWordsNew = new HashSet<String>();
	protected final TermPipeline next;
	public static String StopwordsFile = AllPath.stopwordPath;


	
	public Stopwords(final TermPipeline _next)
	{   
		this.next=_next;
		//String StopwordsFile="F:/Fusiondata/abc/q1/stopword-list.txt";   //the path of stoplist file
		loadStopwordsList(StopwordsFile);
	}
	
	


	/**load the stoplist file,and put each stopword into the THashSet */
	public void loadStopwordsList(String stopwordsFilename)
	{	
		try {
			//open the file stoplist
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(stopwordsFilename)));	
			String word;
			while ((word = br.readLine()) != null)
			{
				word = word.trim();
				if (word.length() > 0)
				{
//					if (INTERN_STOPWORDS)
//						word = word.intern();
					stopWords.add(word);
				}
			}
			br.close();   //finish reading file,close it
			
			
			BufferedReader br2 = new BufferedReader(new InputStreamReader(new FileInputStream(AllPath.stopwordPath2)));	
			String word2;
			while ((word2 = br2.readLine()) != null)
			{
				word2 = word2.trim();
				if (word2.length() > 0)
				{
//					if (INTERN_STOPWORDS)
//						word = word.intern();
					stopWordsNew.add(word2);
				}
			}
			br2.close();  
			
			
			
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		if (stopWords.size() == 0){
			System.out.println("the stoplist is empty");
		}     
	}


	/** Clear all stopwords from this stopword list object. */
	public void clear()
	{
		stopWords.clear();	
	}

	/** Returns true is term t is a stopword */
	public static boolean isStopword(final String t,int i)
	{
		if(i==1){
			return stopWordsNew.contains(t);
		}else{
			return stopWords.contains(t);
		}
}

	
	/** 
	 * Checks to see if term t is a stopword. If so, then the TermPipeline
	 * is exited. Otherwise, the term is passed on to the next TermPipeline
	 * object. This is the TermPipeline implementation part of this object.
	 * @param t The term to be checked.
	 */
	public void processTerm(final String t)
	{
		if (stopWords.contains(t))
			return;
		next.processTerm(t);
	}
	
	
	public boolean reset() {
		return true;
	}
}
