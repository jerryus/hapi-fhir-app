package com.sample.client;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.hl7.fhir.r4.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.Silent.class)
class SampleClientAppTest {
	
	private SampleClientApp mockApp;
	private PatientSearchService mockService;
	
	@BeforeEach
    void setUp() {
		mockService = mock(PatientSearchService.class);
		mockApp = spy(new SampleClientApp(mockService));	
    }
	@Test
	public void testRun() throws Exception {		
		mockApp.run("test");		
		verify(mockApp, times(1)).runTaskOne();
		verify(mockApp, times(1)).runTaskTwo();		
	}
	
	@Test
	public void testRunTaskOne() {		
		// Arrange
		List<Patient> pList = new ArrayList<>();
		when(mockService.getPatientsByLastName(anyString(), anyBoolean())).thenReturn(pList);
		// Act
		String result = mockApp.runTaskOne();
		// Assert
		assertNotNull(result);
		assertTrue(Long.parseLong(result) > 0);		
	}
	
	@Test
	public void testRunTaskTwo() throws InterruptedException {		
		// Arrange		
		List<List<Patient>> pListList = new ArrayList<>();			
		when(mockService.getPatientsByLastNameList(anyList(), anyBoolean())).thenReturn(pListList);
		// Act
		String result = mockApp.runTaskTwo();
		// Assert
		assertNotNull(result);
	}

}
