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

	public static void main(String[] theArgs) {

		// Create a FHIR client
		FhirContext fhirContext = FhirContext.forR4();
		IGenericClient client = fhirContext.newRestfulGenericClient("http://hapi.fhir.org/baseR4");
		client.registerInterceptor(new LoggingInterceptor(false));
		client.registerInterceptor(new IClientInterceptor());

		int task = 2;

		if (task == 1) // task 1
		{
			String lastName = "SMITH";
			doTaskOne(client, lastName, false);
		} 
		else if (task == 2) // task 2
		{
			List<Long> times = new ArrayList<>();

			// run #1
			times.add(doTaskTwo(client, false));

			// run #2
			times.add(doTaskTwo(client, false));

			// run #3
			times.add(doTaskTwo(client, true));

			System.out.println("===================================================================================");
			System.out.println("  Task 2 execution times for the 3 runs : " + times + " in milliseconds");
			System.out.println("===================================================================================");
		} 
		else {
			System.out.println("No task found for task numbr : " + task);
		}
	}

	public static void doTaskOne(IGenericClient client, String lastName, boolean noCache) {

		Bundle response = client.search().forResource("Patient").where(Patient.FAMILY.matches().value(lastName))
				.returnBundle(Bundle.class).cacheControl(new CacheControlDirective().setNoCache(noCache)).execute();

		if (!response.isEmpty()) 
		{
			List<Patient> pList = new ArrayList<>();

			response.getEntry().forEach(entry -> {
				if (entry.getResource() instanceof Patient) {
					pList.add((Patient) entry.getResource());
				}
			});

			Collections.sort(pList, (p1, p2) -> (p1.getNameFirstRep().getGivenAsSingleString()
					.compareTo(p2.getNameFirstRep().getGivenAsSingleString())));

			System.out.println("\nNAME & DOB OF PATIENT WITH LAST NAME : " + lastName);
			pList.forEach(p -> System.out
					.println(p.getNameFirstRep().getGivenAsSingleString() + " " + p.getNameFirstRep().getFamily()
							+ " | " + (p.getBirthDate() != null ? p.getBirthDate() : "Not available")));
		}
	}

	public static long doTaskTwo(IGenericClient client, boolean noCache) {

		long startTime = 0L;
		long endTime = 0L;

		try {
			List<String> lastNames = getLastNames();
			startTime = System.currentTimeMillis();
			lastNames.forEach(lastName -> doTaskOne(client, lastName, noCache));
			endTime = System.currentTimeMillis();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return (endTime - startTime);
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
