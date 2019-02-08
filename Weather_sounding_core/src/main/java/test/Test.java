package test;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javafx.concurrent.Task;

public class Test 
{
	public static void main(String[] args) {
		
		Task<Integer> task = new Task<Integer>() {
	         @Override protected Integer call() throws Exception {
	             int iterations;
	             for (iterations = 0; iterations < 10000000; iterations++) {
	                 if (isCancelled()) {
	                     updateMessage("Cancelled");
	                     break;
	                 }
	                 updateMessage("Iteration " + iterations);
	                 updateProgress(iterations, 10000000);
	             }
	             return iterations;
	         }
	     };
	     
	     
		
	}
}
