package com.sample.client;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.HumanName;
import org.hl7.fhir.r4.model.Patient;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import ca.uhn.fhir.rest.api.CacheControlDirective;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.gclient.IQuery;
import ca.uhn.fhir.rest.gclient.IUntypedQuery;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.Silent.class)
public class PatientSearchServiceTest {
	
	@Test
	public void testGetPatientsByLastName() {
		
		// Arrange
		PatientSearchService mockService = mock(PatientSearchService.class);		
				
		List<Patient> pList = new ArrayList<>();
		Patient p = new Patient();
		p.setLanguage("Spanish");
		pList.add(p);	
		
		// Stub the inner method
		when(mockService.findPatientsWithLastName("Martinez", false)).thenReturn(pList);		
		// Call the real outer method
        when(mockService.getPatientsByLastName("Martinez", false)).thenCallRealMethod();
        
		// Act
		List<Patient> result = mockService.getPatientsByLastName("Martinez", false);			
		
		// Assert
		verify(mockService).getPatientsByLastName("Martinez", false);
		verify(mockService).displayPatientSearchInfo(pList, "Martinez");
		assertEquals(1, result.size());
		assertEquals("Spanish", result.get(0).getLanguage());
	}
	
	@Test
	public void testGetPatientsByLastNameList() {
		
		// Arrange
		PatientSearchService mockService = mock(PatientSearchService.class);		
		
		List<Patient> pList1 = new ArrayList<>();
		Patient p1 = new Patient();
		p1.setLanguage("Spanish");
		pList1.add(p1);
		
		List<Patient> pList2 = new ArrayList<>();
		Patient p2 = new Patient();
		p2.setLanguage("English");
		pList2.add(p2);
		
		List<List<Patient>> pListList = new ArrayList<>();
		pListList.add(pList1);
		pListList.add(pList2);
		
		List<String> lastNameList = Arrays.asList("Martinez", "Wilson");
		
		// Stub the inner method
		when(mockService.getPatientsByLastName("Martinez", false)).thenReturn(pList1);	
		when(mockService.getPatientsByLastName("Wilson", false)).thenReturn(pList2);	
		// Call the real outer method
        when(mockService.getPatientsByLastNameList(lastNameList, false)).thenCallRealMethod();
        
        // Act
     	List<List<Patient>> result = mockService.getPatientsByLastNameList(lastNameList, false);	
     	
     	// Assert     	
     	verify(mockService).getPatientsByLastNameList(lastNameList, false);
     	verify(mockService).getPatientsByLastName("Martinez", false);
     	verify(mockService).getPatientsByLastName("Wilson", false);
     	assertEquals(2, result.size());	
     	assertEquals("Spanish", result.get(0).get(0).getLanguage());	
     	assertEquals("English", result.get(1).get(0).getLanguage());	
	}
	
	@Test
	public void testFindPatientsWithLastName() {
		
		// Arrange
		PatientSearchService mockService = mock(PatientSearchService.class);		
		IGenericClient mockClient = mock(IGenericClient.class);
		
		// mock the chained client search method
		IUntypedQuery<IBaseBundle> mockIUntypedQuery = mock(IUntypedQuery.class);		
		when(mockClient.search()).thenReturn(mockIUntypedQuery);
		
		IQuery<IBaseBundle> mockIBaseBundleIQuery = mock(IQuery.class);
		when(mockIUntypedQuery.forResource("Patient")).thenReturn(mockIBaseBundleIQuery);
		when(mockIBaseBundleIQuery.where(Patient.FAMILY.matches().value("Martinez"))).thenReturn(mockIBaseBundleIQuery);
		
		IQuery<Bundle> mockBundleIQuery = mock(IQuery.class);
		when(mockIBaseBundleIQuery.returnBundle(Bundle.class)).thenReturn(mockBundleIQuery);
		
		when(mockBundleIQuery.cacheControl(new CacheControlDirective().setNoCache(false))).thenReturn(mockBundleIQuery);
		
		Bundle responseBundle = createResponseBundle();
		when(mockBundleIQuery.execute()).thenReturn(responseBundle);
		
		// Act
		List<Patient> result = mockService.findPatientsWithLastName("Smith", false);
		
		// Assert 
		assertNotNull(result);		
	}
	
	private Bundle createResponseBundle() {
		
		Bundle bundle = new Bundle();	
		
		List<BundleEntryComponent> entries = new ArrayList<>();
		
		BundleEntryComponent entry1 = new BundleEntryComponent();		
		Patient p1 = new Patient();	
		p1.setLanguage("English");
		
		List<HumanName> nameList1 = new ArrayList<>();		
		HumanName n1 = new HumanName();
		n1.setFamily("Smith");
		n1.addGiven("Brian");
		nameList1.add(n1);
		
		p1.setName(nameList1);
		entry1.setResource(p1);
		
		BundleEntryComponent entry2 = new BundleEntryComponent();
		Patient p2 = new Patient();	
		p2.setLanguage("German");
		
		List<HumanName> nameList2 = new ArrayList<>();		
		HumanName n2 = new HumanName();
		n2.setFamily("Smith");
		n2.addGiven("John");
		nameList2.add(n2);
		
		p2.setName(nameList2);
		entry2.setResource(p2);
		
		BundleEntryComponent entry3 = new BundleEntryComponent();
		Patient p3 = new Patient();	
		p3.setLanguage("Spanish");
		
		List<HumanName> nameList3 = new ArrayList<>();		
		HumanName n3 = new HumanName();
		n3.setFamily("Smith");
		n3.addGiven("Alan");
		nameList3.add(n3);
		
		p3.setName(nameList3);
		entry3.setResource(p3);
		
		entries.add(entry1);
		entries.add(entry2);
		entries.add(entry3);
				
		bundle.setEntry(entries);
		
		return bundle;	
	}
}
