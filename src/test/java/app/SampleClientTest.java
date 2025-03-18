package app;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Patient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.powermock.core.classloader.annotations.PrepareForTest;

import app.SampleClient;
import ca.uhn.fhir.rest.client.api.IGenericClient;

@RunWith(MockitoJUnitRunner.class) 
@PrepareForTest(SampleClient.class)
public class SampleClientTest {	
	
	  private Bundle createResponse() {
		
		Bundle response = new Bundle();
		
		List<BundleEntryComponent> entries = new ArrayList<>();		
		
		BundleEntryComponent entry1 = new BundleEntryComponent();
		
		List<HumanName> names = new ArrayList<>();
		HumanName n1 = new HumanName();
		n1.setFamily("Smith");
		n1.addGiven("John");		
		HumanName n2 = new HumanName();
		n1.setFamily("Wilson");
		n1.addGiven("Alan");		
		names.add(n1);
		names.add(n2);
		
		Patient p1 = new Patient();		
		p1.setName(names);
		
		entry1.setResource(p1);		
		entries.add(entry1);		
		response.setEntry(entries);
		
		return response;
		
	}

}
