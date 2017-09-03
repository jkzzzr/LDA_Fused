package cn.edu.splitByWord;
//L25 Stemmer缁紮绱�
//閺�瑙勵劀閿涙艾鍤遍弫鐧硁dofword閿涘牞绱氶敍锟�
/**
 * preprocess the text,such as digit,case,and other;stopwords and stemming
 * 闁跨喐鏋婚幏閿嬬槨缁″洭鏁撴笟銉ь暜閹风兘鏁撻弬銈嗗閻滎偊鏁撻弬銈嗗闁跨喓浣唀rm闁跨喐鏋婚幏閿嬫闁跨喐鏋婚幏鐑芥晸閺傘倖瀚归柨鐔告灮閹风rrayList闁跨喎褰ㄩ敐蹇斿闁跨喐鏋婚幏椋庣埠闁跨喓瀚涙潏鐐闁跨喐鏋婚幏閿嬪闁跨喓瀚涢敓锟�
 * 閻掑爼鏁撻弬銈嗗闁跨喓笑閸忓磭銆嬮幏鐑芥晸閺傘倖瀚归柨鐔告灮閹风兘鏁撻敓锟�
 * 
 */

import cn.edu.term.*;
import cn.edu.processTerm.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.*;
public class LexicalAnalysis {
	public static String ResultList;

	TermPipeline tempPipe=null;
	Stopwords stop=new Stopwords(tempPipe);
//	String pathPar="F:/Fusiondata/resultBox/Html-Word-Change/R-";
	Stemmer stem=new PorterStemmer();//閻€劋绨懢宄板絿鐠囧秴鍏�
	final int index=50;
	String docId;
	String queryID;
    ConstructTerms cons=new ConstructTerms();//閻€劋绨�涙ê鍋嶅妤�鍩岄惃鍕槤楠烇拷
    DecimalFormat df=new DecimalFormat("0.000000");
	public LexicalAnalysis(String docId,String text,String queryID){
		//WriteFile.write("<"+docId+">");
		this.docId=docId;
		this.queryID=queryID;
		ResultList = this.findWords(text.trim());//閸欘亜骞撻梽銈呯磻婢跺绮ㄧ亸鍓ф畱缁岀儤鐗搁敍灞艰厬闂傚鈹栭弽鐓庤嫙娑撳秴骞撻梽锟�
		
	}
	
	/**spilt the string from space,then process each word */
	public void findWord(String content){
		char[] temp=content.toCharArray();
		int length=temp.length;
		String word;
		int position=0;
		//int p=0;
		int extend=1;
		char[] isWord=new char[index];
		for(int i=0;i<length;i++){
			char cur=temp[i];
			
			if(this.endOfWord(cur)){  //use to split string into words
				position=0;
				extend=1;
				word=new String(isWord).trim();
				if(word.length()>0 && !Character.isLetter(word.charAt((0)))){ //if the string is not begin from a letter,then process it
					word=this.changeToWord(word);	
				}
				
				this.clear(isWord);//闁插秵鏌婃担璺ㄦ暏isWord娑斿澧犻敍灞界殺閸樼喐娼甸惃鍕枂缁岀尨绱濋柌濠冩杹鐎涙ê鍋嶇粚娲？閿涘苯鍟�娑撹桨绠ｇ挧瀣拷鐓庡従娴犳牜娈戦崘鍛啇閿涗緤绱掗敍浣衡敄闂傛潙鍩勯悽顭掔磼閿涳拷
				isWord=new char[index];
				
				if (word.length()> 0) {
					word = this.caseLower(word);
					String getword = this.process(word);
					if (getword == null) {
					//	System.out.println("remove a stopword: "+word);
					} else {
						
						cons.process(getword);
						//WriteFile.write(getword);
						//System.out.println(getword);
					}
				}else{
				//	System.out.println("the string is invalidate");
				// 閹笛嗩攽娑撳秴鍩屾潻娆庣濮濄儻绱漺ord缂佸繗绻冩禍鍞梤im娑擄拷濮濄儻绱濇稉宥囧姧鐎涙ê婀張澶嬫櫏鐎涙顑侀敍灞惧娴狀櫜ength>0,閹碉拷娴犮儰绗夋导姘崇箹娑擄拷濮濄儻绱�
				}
				
			}
			else if(this.removeChar(cur)){
				//remove this character	
			}else{
				if (position < index*extend) {
					isWord[position++] = cur;
				} else {  //extend the char[]
					extend++;
					char[] large=new char[extend*index];
					for(int k=0;k<position-1;k++){
						large[k]=isWord[k];
					}
					large[position++] = cur;
					isWord=large;
				}
			}
		}
		cons.quickSort();
		ArrayList list=cons.getList();
		int len=list.size();
		int fre;
		double weight;
		RecordTerms term;
		String s;
		String result;
		//output the terms into text files
		for(int i=0;i<len;i++){
			term=(RecordTerms)list.get(i);
			s=term.getTerm();
		    fre=term.getFrequent();
		    weight=1+Math.log(fre);  //compute the term's weight
			result=s+" "+new Integer(fre)+" "+weight;
			//WriteFile.write(result);
			
		}
		
	}
	
	
	public String findWords(String content){
		//System.out.println(this.docId+ "  $$$$");
	//	System.out.println(content);
	//	System.out.println(this.docId+ "  $$$$");
		char[] temp=content.toCharArray();/*covert text content to array*/
		int length=temp.length;
		String word;
		int position=0;//鐠佹澘缍嶅В蹇庨嚋鐠囧秶娈戦梹鍨閿涘本鐦″▎鈥冲煂闂堢偛鐡уВ宥呯摟缁楋附妞傞敍宀冨殰閸斻劍绔婚梿璁圭礉闁洤鍩岀�涙鐦濈�涙顑侀弮璁圭礉閼奉亜濮╃槐顖氬閵嗭拷
		int extend=1;//鐞涖劎銇氶梹鍨閸婂秵鏆熼敍灞界唨绾拷index娑擄拷50閿涘苯顩ч弸婊冦亣娴滐拷50閸掓瑥鐡х粭锔芥殶缂佸嫰鏆辨惔锕�褰夋稉锟�50*2閿涘苯鍟�婢堆冨綁娑擄拷50*3閿涘矉绱濋敍宀嬬礉
		char[] isWord=new char[index];
		for(int i=0;i<length;i++){
			char cur=temp[i];
			if(!Character.isLetter(cur)){  //use to split string into words
				position=0;
				extend=1;
				//缁屾椽妫跨挧鍕爱閸掆晝鏁ら敍灞肩瘍閸欘垯浜掗惄瀛樺复 isWord = new char[index];
				word=new String(isWord).trim();
				this.clear(isWord);
				isWord=new char[index];
				
				if (word.length()> 0) {
					word = this.caseLower(word);
					String getword = this.process(word);
					if (getword == null) {
					} else {
						//System.out.println(getword);
						cons.process(getword);
					}
				}
			}
			else{
				if (position < index*extend) {//闂�鍨娑擄拷閼割剛娈戦崡鏇＄槤婢跺嫮鎮婇敍锟�
					isWord[position++] = cur;
				} else {  //extend the char[]
					extend++;
					char[] large=new char[extend*index];
					for(int k=0;k<position-1;k++){
						large[k]=isWord[k];
					}
					large[position++] = cur;
					isWord=large;
				}
			}
		}
		cons.quickSort();
		ArrayList list=cons.getList();
		int len=list.size();
		RecordTerms term;
		StringBuffer sb = new StringBuffer();
		for(int i=0; i<len;i++){
			term = (RecordTerms)list.get(i);
			sb.append(term.getTerm()+"\t");
		}
		return sb.toString();
		/*---------------output the terms into text files-----------------------*/
		
		/* FileWriter fw=null;
		 BufferedWriter bw=null;
		 
		 try {
			 File file=new File(pathPar+queryID);
			 if(!file.exists()){
				 file.mkdir();
			 }
			fw=new FileWriter(pathPar+queryID+"/"+docId);
			 bw=new BufferedWriter(fw);
			 
				for(int i=0;i<len;i++){
					term=(RecordTerms)list.get(i);
				    //bw.append(term.getTerm()+" "+term.getFrequent());
				    bw.append(term.getTerm());
					bw.newLine();
				}
				bw.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			bw.close();
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
		//	System.out.println("close the writeStream sucess!");
		}
		*/

	}
	
	
	/**
	 * 鐎甸�涚艾娑撳秵妲哥�涙鐦濋幍鎾炽仈閻ㄥ嫬宕熺拠宥忕礉鏉╂稖顢戞径鍕倞閿涘苯鐨㈢拠宥呭閻ㄥ嫰娼�涙鐦濋崗銊╁劥閸樼粯甯�閿涘苯顩ч弸婊勬殻娑擃亣鐦濋崗銊╁劥閻㈤亶娼�涙鐦濋弸鍕灇閿涘苯鍨崢鐔哥壉鏉╂柨娲�
	 * remove the top position which is not letter
	 * @param keyword
	 * @return
	 */
	public String changeToWord(String keyword){ 
		char[] word=keyword.toCharArray();
		int len=keyword.length();
		int offset=0;
		boolean tag=false;
		
		for(int i=0;i<len;i++){
			if(Character.isLetter(word[i])){
				tag=true;
				break;
			}else{
				offset++;
			}
		}
		if (tag) {
			char[] newWord=new char[len-offset];
			int j = 0;
			for (int i = 0; i < len-offset; i++) {
				newWord[j++] = word[i + offset];	
			}
			return new String(newWord).trim();
		}
		
		return new String(word).trim();
	}
//	public char[] clear(char[] chars){
//		int len=chars.length;
//		for (int i=0;i<len;i++){
//			chars[i]='\0';
//		}
//		return chars;
//	}
	
	/**
	 * 鐏忓棙鏆熺紒鍕枂缁岀尨绱濋柌濠冩杹鐎涙ê鍋嶇粚娲？
	 * @param chars
	 * @return
	 */
	public char[] clear(char[] chars){
		chars=null;
		return chars;
	}
	
	/** remove character,such as number and "<> and </> tags" */
	public boolean removeChar(char c){
		if(c<='9' && c>='0'){
			return true;
		}
		switch (c){
		case '<' : ;
		
		case '/' : ;
		case '@' : ;
		case '$' : ;
		case '"' : ;
		case '\'': ;
		case '%' : ;
		case '`' : ;
		case '(' : ;
		case ')' : ;
		case '#' : return true;
		default  : return false;
		
		}
	}
	
	public boolean endOfWord(char c){
		switch (c){
		case ' ' : ;
		case '>' : ;
		case ',' : ;
		case '!' : ;
		case '?' : ;
		case '.' : ;
		case '\b': ;
		case ':' : ;
		case '-' : ;
		case '\n': ;
		case 't' : ;//?閺�瑙勫灇娴滃棔绗呮稉锟界悰灞藉敶鐎癸拷
		case '\t': ;
		case '\f': ;
		case ';' :  return true;
		default  : return false;
		
		}
	}
	/**
	 * 鏉╂柨娲栫拠宥呭叡閿涘苯顩ч弸婊�璐熼崑婊嗙槤閸掓瑨绻戦崶鐎梪ll
	 * @param word
	 * @return
	 */
	public String process(String word){
		String stemp=stem.stem(word);
			if(Stopwords.isStopword(stemp,2))
				return null;
			return stemp;
			//return word;
	}
	
	/**
	 * 閸掆晝鏁har閻ㄥ嚈SCII閸婄》绱濇俊鍌涚亯閺勵垰鐨崘娆忕摟濮ｅ秴鍨崝鐘辩瑐32瀵版鍩屾径褍鍟撶�涙鐦濋敍灞藉従娴犳牔绗夐崑姘槱閻烇拷
	 * 閻掓儼锟藉矉绱濋惄瀛樺复娴ｈ法鏁oLowerCase娑旂喎褰叉禒銉ョ杽閻滐拷
	 * transform upper to lower
	 * @param content
	 * @return
	 */
	public String caseLower(String content){
		char tempString[]=content.toCharArray();
		int strLen=tempString.length;
		for(int i=0;i<strLen;i++){
			if (tempString[i]>=65 && tempString[i]<=90){
				tempString[i]+=32;
			}
		}
		return new String(tempString).trim();
	}
	
	
}
