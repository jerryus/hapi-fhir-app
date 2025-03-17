import org.slf4j.LoggerFactory;

import ca.uhn.fhir.interceptor.api.Hook;
import ca.uhn.fhir.interceptor.api.Interceptor;
import ca.uhn.fhir.interceptor.api.Pointcut;
import ca.uhn.fhir.rest.client.api.IHttpRequest;
import ca.uhn.fhir.rest.client.api.IHttpResponse;
import ca.uhn.fhir.rest.client.api.IRestfulClient;
import ca.uhn.fhir.util.StopWatch;
import org.slf4j.Logger;

@Interceptor
public class IClientInterceptor {
	//private static final Logger logger = LoggerFactory.getLogger(IClientInterceptor.class);

	@Hook(Pointcut.CLIENT_REQUEST)
	public void clientRequest(IHttpRequest theHttpRequest, IRestfulClient theRestfulClient) {
		// logger.info("Interceptor CLIENT_REQUEST - started");
		StopWatch stopWatch = new StopWatch();
		try {

		} finally {
			// logger.info("Interceptor CLIENT_REQUEST - ended, execution took {}",
			// stopWatch.getMillis());
		}
	}

	@Hook(Pointcut.CLIENT_RESPONSE)
	public void clientResponse(IHttpRequest theHttpRequest, IHttpResponse theHttpResponse,
			IRestfulClient theRestfulClient) {
		// logger.info("Interceptor CLIENT_RESPONSE - started");
		StopWatch stopWatch = new StopWatch();
		try {
			stopWatch = theHttpResponse.getRequestStopWatch();
			System.out.println("Request-Response Time === " + stopWatch.getMillis());
		} finally {
			//logger.info("Interceptor CLIENT_RESPONSE - ended, execution took {}", stopWatch.getMillis());
		}
	}

}
