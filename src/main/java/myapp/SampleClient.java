package myapp;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.CacheControlDirective;
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

	public static void main(String[] theArgs) throws InterruptedException {

		// Create a FHIR client
		FhirContext fhirContext = FhirContext.forR4();
		IGenericClient client = fhirContext.newRestfulGenericClient("http://hapi.fhir.org/baseR4");
		client.registerInterceptor(new LoggingInterceptor(false));
		client.registerInterceptor(new TimeClientInterceptor());

		// Task one
		String lastName = "SMITH";
		getPatientsByLastName(client, lastName, false);
		
		// Task Two		
		List<String> lastNames = getLastNamesFromFile("lastnames.txt");
		
		long run1 = getPatientsByLastNames(client, lastNames, false);
		Thread.sleep(3000);
		long run2 = getPatientsByLastNames(client, lastNames, false);
		Thread.sleep(3000);
		long run3 = getPatientsByLastNames(client, lastNames, true);
		
		System.out.println("===================================================================================================");
		System.out.println("  Task 2 execution times for the 3 runs : [" + run1 + ", " + run2 + ", " + run3 + "] in milliseconds");
		System.out.println("===================================================================================================");		
	}
	
	private static void getPatientsByLastName(IGenericClient client, String lastName, boolean noCache) {
		
		List<Patient> pList = findPatientsWithLastName(client, lastName, false);		
		
		System.out.println("\n *** NAME & DOB FOR PATIENT : " + lastName);

		pList.forEach(p -> System.out
				.println(p.getNameFirstRep().getGivenAsSingleString() + " " + p.getNameFirstRep().getFamily() + " | "
						+ (p.getBirthDate() != null ? p.getBirthDate() : "Not available")));		
	}
	
	private static long getPatientsByLastNames(IGenericClient client, List<String> lastNames, boolean noCache) {
		
		long startTime = 0L;
		long endTime = 0L;
		
		startTime = System.currentTimeMillis();
		lastNames.forEach( name -> getPatientsByLastName(client, name, noCache));
		endTime = System.currentTimeMillis();	
		
		return (endTime - startTime);		
	}		
	
	private static List<Patient> findPatientsWithLastName(IGenericClient client, String lastName, boolean noCache) {

		List<Patient> pList = new ArrayList<>();

		Bundle response = client.search().forResource("Patient").where(Patient.FAMILY.matches().value(lastName))
				.returnBundle(Bundle.class).cacheControl(new CacheControlDirective().setNoCache(noCache)).execute();

		if (!response.isEmpty()) {
			response.getEntry().forEach(entry -> {
				if (entry.getResource() instanceof Patient) {
					pList.add((Patient) entry.getResource());
				}
			});

			Collections.sort(pList, (p1, p2) -> (p1.getNameFirstRep().getGivenAsSingleString()
					.compareTo(p2.getNameFirstRep().getGivenAsSingleString())));
		}

		return pList;
	}

	private static List<String> getLastNamesFromFile(String fileName) {

		List<String> lastNameList = new ArrayList<>();

		try (InputStream is = SampleClient.class.getClassLoader().getResourceAsStream(fileName);
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
