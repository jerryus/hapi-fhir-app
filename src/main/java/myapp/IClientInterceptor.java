package myapp;
import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.client.api.IHttpRequest;
import ca.uhn.fhir.rest.client.api.IHttpResponse;
import ca.uhn.fhir.rest.client.api.IRestfulClient;
import ca.uhn.fhir.util.StopWatch;

@Interceptor
public class IClientInterceptor {
	
	@Hook(Pointcut.CLIENT_REQUEST)
	public void clientRequest(IHttpRequest theHttpRequest, IRestfulClient theRestfulClient) 
	{	
		long startTime = System.currentTimeMillis();		
		@SuppressWarnings("unused")
		StopWatch stopWatch = new StopWatch(startTime);
	}

	@Hook(Pointcut.CLIENT_RESPONSE)
	public void clientResponse(IHttpRequest theHttpRequest, IHttpResponse theHttpResponse, IRestfulClient theRestfulClient) 
	{
		long endTime = theHttpResponse.getRequestStopWatch().getMillis();
		
		System.out.println("====================================");
		System.out.println("  Request-Response Time : " + endTime);
		System.out.println("====================================");
	}

}
