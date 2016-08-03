package test.we;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

import org.apache.poi.ss.formula.functions.Match;

import com.slic.utils.HSSFUtil;

public class sss {

	public static void main(String[] args) {
		
		double repickStr = 3.215574544;
		DecimalFormat df = new DecimalFormat("######0.0000");
	    System.out.println(df.format(Double.valueOf(repickStr)));
	    
	    double giftqty = 6.553332166;
	    double packunitqty = 2.1142323211;
	    String bulkqty = df.format( (giftqty % packunitqty));
	    String packqty = df.format((giftqty / packunitqty));
	    
	    System.out.println(bulkqty);
	    System.out.println(packqty);
	    
	    Map<String, String> pageSums = new HashMap<String, String>();
	    pageSums.put("ss", "sss");
	    System.out.println(pageSums.size());
	    
	    System.out.println((50*1.00/35.3*20*0.75)*0.7);
	    System.out.println(Math.floor(10.6/1));
	    System.out.println(0%4);
	    System.out.println(-3%4);
	    System.out.println(7%1);
	    System.out.println(2%5);
	    System.out.println(-1/3);
	    System.out.println((int)(21.0/5.3));
	    System.out.println("=====================");
	    for (int i = 0; i < 10; i++) {
	    	System.out.println("i = "+i+"; row = "+(i)/3 + "; col = "+(i)%3);
			System.out.println("--");
		}
	    
	}
}
