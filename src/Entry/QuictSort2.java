import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.TreeSet;
/**
 * 将结果列表，按照docid进行排序
 * @author lee
 *
 */
public class QuictSort2 {
	
	public static String inputPath = "/home/Lee/模板/data/xucl/log/2009/6.n/ICTNETADRun4";
	public static String outputPath = "/home/Lee/模板/data/xucl/log/2009/6.n/ICTNETADRun4---1";

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		TreeSet<String> hSet = new TreeSet<>();
		String tempString = "";
		BufferedReader bReader = new BufferedReader(new FileReader(inputPath));
		while ((tempString = bReader.readLine())!=null){
			StringTokenizer stringTokenizer = new StringTokenizer(tempString);
			stringTokenizer.nextToken();
			stringTokenizer.nextToken();
			String docid = stringTokenizer.nextToken();
			if (hSet.contains(docid)){
				
			}else {
				hSet.add(docid);
			}
		}
		bReader.close();
		
		BufferedWriter bWriter = new BufferedWriter(new FileWriter(outputPath));
		Iterator<String> iterator = hSet.iterator();
		while (iterator.hasNext()){
			bWriter.write(iterator.next());
			bWriter.newLine();
		}
		bWriter.flush();
		bWriter.close();

	}

}
