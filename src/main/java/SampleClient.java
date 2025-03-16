import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.LoggingInterceptor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.Patient;

public class SampleClient {

	public static void main(String[] theArgs) {

		// Create a FHIR client
		FhirContext fhirContext = FhirContext.forR4();
		IGenericClient client = fhirContext.newRestfulGenericClient("http://hapi.fhir.org/baseR4");
		client.registerInterceptor(new LoggingInterceptor(false));

		// Search for Patient resources
		Bundle response = client.search().forResource("Patient").where(Patient.FAMILY.matches().value("SMITH"))
				.returnBundle(Bundle.class).execute();

		if (!response.isEmpty()) 
		{
			List<Patient> pList = new ArrayList<>();

			response.getEntry().forEach(entry -> {
				if (entry.getResource() instanceof Patient) {
					pList.add((Patient) entry.getResource());
				}
			});
			
			Collections.sort(pList, (p1, p2) -> (p1.getNameFirstRep().getGivenAsSingleString().compareTo(p2.getNameFirstRep().getGivenAsSingleString())));

			System.out.println("\nPatient Name & DOB\n");
			pList.forEach(p -> System.out.println(p.getNameFirstRep().getGivenAsSingleString() + " " + p.getNameFirstRep().getFamily() + " | " + (p.getBirthDate() != null ? p.getBirthDate() : "Not available")));
		}
	}

}
