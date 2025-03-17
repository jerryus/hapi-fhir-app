import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;

public class SampleClient {

	public static void main(String[] theArgs) {

		// Create a FHIR client
		FhirContext fhirContext = FhirContext.forR4();
		IGenericClient client = fhirContext.newRestfulGenericClient("http://hapi.fhir.org/baseR4");
		client.registerInterceptor(new LoggingInterceptor(false));
		
		int taskNum = 2;
		
		// task 1
		if(taskNum == 1) 
		{					
			String lastName = "SMITH";
			doTaskOne(client, lastName);
		}
		// task 2
		else if(taskNum == 2) 
		{				
			doTaskTwo(client);
		}
		else 
		{
			System.out.println("No task found for task numbr : " + taskNum);
		}		
	}
	
	public static void doTaskOne(IGenericClient client, String lastName) {

		// Search for Patient resources
		Bundle response = client.search().forResource("Patient").where(Patient.FAMILY.matches().value(lastName)).returnBundle(Bundle.class).execute();

		if (!response.isEmpty()) 
		{
			List<Patient> pList = new ArrayList<>();

			response.getEntry().forEach(entry -> {
				if (entry.getResource() instanceof Patient) {
					pList.add((Patient) entry.getResource());
				}
			});
			
			Collections.sort(pList, (p1, p2) -> (p1.getNameFirstRep().getGivenAsSingleString().compareTo(p2.getNameFirstRep().getGivenAsSingleString())));

			System.out.println("\nName & DOB for patient with last name : " + lastName + "\n");
			pList.forEach(p -> System.out.println(p.getNameFirstRep().getGivenAsSingleString() + " " + p.getNameFirstRep().getFamily() + " | " + (p.getBirthDate() != null ? p.getBirthDate() : "Not available")));
		}
	}
	
	public static void doTaskTwo(IGenericClient client) {
		
		List<String> lastNames = null;
		
		try 
		{
			lastNames = getLastNames();			
			lastNames.forEach(lastName -> doTaskOne(client, lastName));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.out.println(lastNames);
		
	}
	
	public static List<String> getLastNames() throws IOException {
		
		List<String> lastNameList = new ArrayList<>();
		
		try (InputStream is = SampleClient.class.getClassLoader().getResourceAsStream("lastnames.txt");
			 InputStreamReader streamReader = new InputStreamReader(is);
			 BufferedReader reader = new BufferedReader(streamReader)) {
			String line;
            while ((line = reader.readLine()) != null) {
                lastNameList.add(line);
            }
		}
		
		return lastNameList;		
	}
	
}
