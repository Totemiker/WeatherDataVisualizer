package test;



import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import control.Data_Provider;
import reste.Weather_Data_Parser;

public class Test 
{
	//static String einstieg;		
	
	
	public static void main(String[] args)
	{
		Data_Provider conn = new Data_Provider();
		Weather_Data_Parser parser = new Weather_Data_Parser();
		
	
		//parser.validate(conn.getDataFromStationInText(10410, LocalDateTime.of(LocalDate.of(2019,01,24), LocalTime.of(00, 00))),"EUR");
		
		//System.out.println(parser.getData());

	}

}
