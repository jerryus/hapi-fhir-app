package com.sample.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SampleClientApp implements CommandLineRunner {
	
	@Autowired
	private final PatientSearchService service;
	
	public SampleClientApp(PatientSearchService service) {
		this.service = service;
	}
	
	public static void main(String[] args) {
		SpringApplication.run(SampleClientApp.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception {
		
		String taskExeTime = null;
        
		taskExeTime = runTaskOne();		
		System.out.println("\n*** Task 1 execution time: " + taskExeTime + " ms ***\n");		
		
		taskExeTime = runTaskTwo();		
		System.out.println("\n*** Task 2 execution times for the 3 runs : " +  taskExeTime + " in ms ***\n");        
    }	

	public String runTaskOne() {
		
		long startTime = 0L;
		long endTime = 0L;
		
		String lastName = "SMITH";
		
		startTime = System.currentTimeMillis();
		service.getPatientsByLastName(lastName, false);		
		endTime = System.currentTimeMillis();	
		
		return "" + (endTime - startTime);		
	}
	
	public String runTaskTwo() throws InterruptedException {
		
		long startTime = 0L;
		long endTime = 0L;		
		
		List<String> lastNameList = getPatientLastNameList("lastnames.txt");
				
		startTime = System.currentTimeMillis();		
		service.getPatientsByLastNameList(lastNameList, false);		
		endTime = System.currentTimeMillis();		
		
		long run1 = endTime - startTime;
		
		Thread.sleep(3000);
		
		startTime = System.currentTimeMillis();		
		service.getPatientsByLastNameList(lastNameList, false);		
		endTime = System.currentTimeMillis();	
		
		long run2 = endTime - startTime;		
		
		Thread.sleep(3000);
		
		startTime = System.currentTimeMillis();		
		service.getPatientsByLastNameList(lastNameList, false);		
		endTime = System.currentTimeMillis();	
		
		long run3 = endTime - startTime;
		
		return "" + run1 + ", " + run2 + ", " + run3;		
	}
	
	private List<String> getPatientLastNameList(String fileName) {

		List<String> lastNameList = new ArrayList<>();

		try (InputStream is = SampleClientApp.class.getClassLoader().getResourceAsStream(fileName);
				InputStreamReader streamReader = new InputStreamReader(is);
				BufferedReader reader = new BufferedReader(streamReader)) 
		{
			String line;
			while ((line = reader.readLine()) != null) {
				lastNameList.add(line);
			}
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}

		return lastNameList;
	}
}
