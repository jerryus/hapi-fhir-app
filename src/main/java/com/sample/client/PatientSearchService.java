package com.sample.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.uhn.fhir.rest.api.CacheControlDirective;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.gclient.IQuery;

@Service
public class PatientSearchService {
	
	@Autowired
	private IGenericClient appClient;
	
	public List<Patient> getPatientsByLastName(String lastName, boolean noCache) {
		
		List<Patient> pList = findPatientsWithLastName(lastName, false);	
		displayPatientSearchInfo(pList, lastName);
		
		return pList;
	}

	public List<List<Patient>> getPatientsByLastNameList(List<String> lastNameList, boolean noCache) {
				
		List<List<Patient>> pListList = new ArrayList<>();			
		lastNameList.forEach( lastName -> pListList.add(getPatientsByLastName(lastName, noCache)));
		
		return pListList;
	}	
	
	public List<Patient> findPatientsWithLastName(String lastName, boolean noCache) {
		
		List<Patient> pList = new ArrayList<>();
		
		Bundle response = appClient.search().forResource("Patient").where(Patient.FAMILY.matches().value(lastName))
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
	
	public void displayPatientSearchInfo(List<Patient> pList, String lastName) {	
		System.out.println("\n *** NAME & DOB FOR PATIENT : " + lastName);
		pList.forEach(p -> System.out
				.println(p.getNameFirstRep().getGivenAsSingleString() + " " + p.getNameFirstRep().getFamily() + " | "
						+ (p.getBirthDate() != null ? p.getBirthDate() : "Not available")));		
	}
}
