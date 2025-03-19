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
	private StopWatch stopWatch;	

	@Override
	public void interceptRequest(IHttpRequest theRequest) 
	{
		this.setStopWatch(new StopWatch(System.currentTimeMillis()));		
	}

	@Override
	public void interceptResponse(IHttpResponse theResponse) throws IOException 
	{
		this.setStopWatch(theResponse.getRequestStopWatch());
		
		System.out.println("------------------------------------------------------------------");
		System.out.println("  Request-Response Time : " + this.stopWatch.getMillis() + " ms   ");
		System.out.println("------------------------------------------------------------------");		
	}

	public StopWatch getStopWatch() {
		return stopWatch;
	}

	public void setStopWatch(StopWatch stopWatch) {
		this.stopWatch = stopWatch;
	}
}
