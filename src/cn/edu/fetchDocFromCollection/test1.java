package cn.edu.fetchDocFromCollection;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class test1 {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
        HtmlToText ht = new HtmlToText();
        StringBuffer sb = new StringBuffer("");
        BufferedReader br = new BufferedReader(new FileReader ("F:/Fusiondata/a2.html"));
        String intemp = "";
        while((intemp = br.readLine())!=null){
        	sb.append(intemp);
        }
        br.close();
        
        
        String text = ht.Convert(sb.toString());
        BufferedWriter bw = new BufferedWriter(new FileWriter("F:/Fusiondata/b2.txt"));
        bw.write(text);
        bw.close();
        System.out.println("end");
	}

}
