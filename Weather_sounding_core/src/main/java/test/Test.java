package test;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class Test 
{
	public static void main(String[] args) {
		Document doc = null;

		try {
			doc = Jsoup.connect("http://meteocentre.com/radiosonde/").get().set
		} catch (IOException e) {
			e.printStackTrace();
			
		}
	}
}
