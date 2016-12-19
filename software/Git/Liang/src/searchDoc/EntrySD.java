package searchDoc;

public class EntrySD {
    /**
     * ����docid���õ�HTMLԴ����
     * @param docid ���������ĵ�id
     * @return Դ�����String����
     */
	public static String run(String docid){
	    docid = docid.trim();
		Content.setContent("");
		new ExtractEachDoc().run(docid);
		String con = Content.getContent();
		if(con!=null)System.out.println("---------------------YES!");
		else {
			System.out.println("----NNNNNNNNNNNNo!");
		}
		return con;
	}
}
