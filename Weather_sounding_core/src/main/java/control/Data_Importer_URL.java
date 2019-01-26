package control;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import model.Sounding;

/**
 * 
 * @author Tobias Teumert
 *
 */
public class Data_Importer_URL 
{
	public void readURL() 
	{
		try {
			URL oracle = new URL(
					//"http://meteocentre.com/radiosonde/get_sounding.php?lang=en&show=0&hist=0&area=eur&stn=10410&type=txt&yyyy=2019&mm=01&dd=25&run=12");
					"http://meteocentre.com/radiosonde/get_sounding.php?lang=en&show=0&hist=0&area=eur&stn=10410&type=txt&yyyy=2019&mm=01&dd=14&run=12");
			BufferedReader in = new BufferedReader(new InputStreamReader(oracle.openStream()));

			String inputLine;
			while ((inputLine = in.readLine()) != null)
				System.out.println(inputLine);
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String readURL2()
	{
		Document doc = null;
		try {
			doc = Jsoup.connect(
					//"http://meteocentre.com/radiosonde/get_sounding.php?lang=en&show=0&hist=0&area=eur&stn=10410&type=txt&yyyy=2019&mm=01&dd=25&run=12")
					"http://meteocentre.com/radiosonde/get_sounding.php?lang=en&show=0&hist=0&area=eur&stn=10410&type=txt&yyyy=2019&mm=01&dd=14&run=12")
					.get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Element e = doc.select("pre").first();
		String s = e.text();	//System.out.println(e.text());
		return s;		
				
	}
}
