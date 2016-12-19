package searchDoc;

public class AllPath {
/*	*//**
	 * path of html code which getted from the page of warc
	 * (WARC018Collection - getDocument)
	 *//*
	public static String warc_Html_P= "/home/huang/Documents/LLFile/Pro_Union/warc_Html";*/
	
	public static String Log = "/home/Lee/视频/errlist.log";
	/**
	 *  (CBSum_To_WordList - CBSPath)
	 */
	/**
	 * 输入文件：文档排序列表
	 */
	public static String CBSPath = "/home/Lee/模板/data/xucl/log/2009/6.n/ICTNETADRun4";
	/**
	 * 所有文档docid排序结果
	 */
	public static String cBSListPath="/home/Lee/模板/data/xucl/log/2009/6.n/ICTNETADRun4---1";
	public static String WordList_perDoc_Path="/home/Lee/模板/data/perdoc";
	/**
	 * file list of result
	 * (CBSum_To_Wirdlist - outPath)
	 */
	public static String wordListPath = "/home/Lee/模板/data/keyword-list";

	public static String stopwordPath = "/home/Lee/模板/data/xucl/stop_words_eng.txt";

	public static String stopwordPath2 = stopwordPath;

//STEP 2---------------------------------
	/**
	 * 数据集的存放路径
	 */
	public static String sourcePath = "/home/Lee/音乐/result/";
	public static String LDAoutPath = "/home/mapred/Documents/LLFile/Pro_Union/LDAResult";
	
	
	public static String PM2inputPath = LDAoutPath;
	public static String PM2out2Path = "/home/mapred/Documents/LLFile/Pro_Union/PM2Result";
}
