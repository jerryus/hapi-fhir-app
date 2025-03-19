package com.sample.client;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import ca.uhn.fhir.rest.client.api.IHttpRequest;
import ca.uhn.fhir.rest.client.api.IHttpResponse;
import ca.uhn.fhir.util.StopWatch;

@RunWith(MockitoJUnitRunner.class)
public class TimeClientInterceptorTest {
	
	@Test
	public void testInterceptRequest1() throws IOException {
		
		// Arrange
		TimeClientInterceptor interceptor = spy(new TimeClientInterceptor());		
		// Act
		interceptor.interceptRequest(any(IHttpRequest.class));		
		// Assert
		assert(interceptor.getStopWatch().getMillis() > 0 );					
	}	
	
	@Test
	public void testInterceptResponse() throws IOException {
		
		// Arrange
		TimeClientInterceptor interceptor = spy(new TimeClientInterceptor());
		IHttpResponse mockResponse = mock(IHttpResponse.class);	
		StopWatch mockStopWatch = mock(StopWatch.class);
		when(mockResponse.getRequestStopWatch()).thenReturn(mockStopWatch);
		when(mockStopWatch.getMillis()).thenReturn(1000L);		
		// Act
		interceptor.interceptResponse(mockResponse);			
		// Assert
		assertEquals(1000L, interceptor.getStopWatch().getMillis());	
				
	}	
}
