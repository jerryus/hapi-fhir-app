package com.sample.client;

import static org.junit.Assert.assertNotNull;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import ca.uhn.fhir.rest.client.api.IGenericClient;

@SpringBootTest
@Import(AppConfig.class) 
public class AppConfigTest {

    @Autowired
    private IGenericClient appClient; // Inject the bean

    @Test
    void appClientBeCreated() {
        assertNotNull(appClient);        
        assertNotNull(appClient.getFhirContext());
        assertNotNull(appClient.getInterceptorService());        
    }
}
