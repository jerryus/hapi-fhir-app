package com.sample.client;
import java.io.IOException;

import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.rest.client.api.IClientInterceptor;
import ca.uhn.fhir.rest.client.api.IHttpRequest;
import ca.uhn.fhir.rest.client.api.IHttpResponse;
import ca.uhn.fhir.util.StopWatch;

@Interceptor
public class TimeClientInterceptor implements IClientInterceptor 
{
	@Override
	public void interceptRequest(IHttpRequest theRequest) 
	{
		new StopWatch(System.currentTimeMillis());		
	}

	@Override
	public void interceptResponse(IHttpResponse theResponse) throws IOException 
	{
		long endTime = theResponse.getRequestStopWatch().getMillis();
		
		System.out.println("---------------------------------------------------");
		System.out.println("      Request-Response Time : " + endTime + " ms   ");
		System.out.println("---------------------------------------------------");		
	}

}
