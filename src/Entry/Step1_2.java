import java.io.IOException;

/**
 * 根据输入的文档结果列表，得到对应的各个文档的分词信息
 * @author Lee
 *
 */
public class Step1_2 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			new CBSum_To_WordList().GetList(2);
			CBSum_To_WordList.WriteKeyWordFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
