package searchDoc;

public class EntrySD2 {
    /**
     * 输入：文档编号，返回：对应文档的文本文档。中间处理：对应网页的网页源码，转换为文本文档
     * @param docid
     * @return
     */
	public static String run(String docid){
	   docid = docid.trim();
		Content.setContent("");
		new ExtractEachDoc().run(docid);
		String con = Content.getContent();
/*		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter("F:/Fusiondata/ResultBox"+"/string."+docid));
			bw.write(con);
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		return con;
	}
}
