package kr.ac.hansung.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@Controller
public class VaccineController {

	private static String[] strArray= new String[4];
	
	private static final Logger logger = LoggerFactory.getLogger(VaccineController.class);
	

	@RequestMapping(value = "/vaccine", method = RequestMethod.GET)
	public String vaccine(Model model, HttpServletRequest request) throws Exception {
		
		SimpleDateFormat format1 = new SimpleDateFormat ("yyyy/MM/dd");			
		Date today = new Date();		
		String toDay = format1.format(today);	

		Calendar day = Calendar.getInstance();
		day.add(Calendar.DATE , -2);
		String beforeDate = new java.text.SimpleDateFormat("yyyy/MM/dd").format(day.getTime());

		model.addAttribute("beforeDate",beforeDate);
	
		crawler();

		String[] arr3 = strArray[2].split("\r");
		String[] str;
		String[][] arr4 = new String[18][5];
		String[][] arr5 = new String[18][5];
		
		for(int i=6; i<24; i++) {
			str = arr3[i].split(" ");
			for(int j=0; j<5; j++) {
				arr4[i-6][j] = str[j];
			}
		}	
	
		for(int i=1; i<=17; i++) {
			arr5[i][2]=arr4[i][2].replaceAll(",", "");
			arr5[i][4]=arr4[i][4].replaceAll(",", "");
		}
		
		model.addAttribute("vaccineCurrent",arr4);
		model.addAttribute("vaccineCurrent2",arr5);
		
		return "vaccine";
	}

	public static String crawler() {
		try {
		String URL = "https://ncv.kdca.go.kr/mainStatus.es?mid=a11702000000";
		
		Connection conn = Jsoup.connect(URL);
		String[] array = new String[2];
 
        Document html = conn.get(); 
  
        Elements fileblocks = html.getElementsByClass("data_table tbl_scrl_mini");
        
        String current, city="";
        
        StringBuilder sb = new StringBuilder("");
        StringBuilder sb2 = new StringBuilder("");
        for( Element fileblock : fileblocks ) {
            
            Elements files = fileblock.getElementsByTag("tr");
            Elements files2 = fileblock.getElementsByTag("tr");

			for (Element elm : files) {
				current = elm.text();
				String result = current + "\r";
				sb.append(result);
			}		
        }
        strArray[2] = sb.toString();
        return sb.toString();
  				
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}