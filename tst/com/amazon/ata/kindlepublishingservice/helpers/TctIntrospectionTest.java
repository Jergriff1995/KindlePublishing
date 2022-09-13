package com.amazon.ata.kindlepublishingservice.helpers;

//import com.amazon.ata.kindlepublishingservice.impl.ExecuteTctSuiteCall;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;

public abstract class TctIntrospectionTest extends IntegrationTestBase {

    /**
     * Ensure the test infra is ready for test run, including creating the client.
     */
    @BeforeClass
    public void setup() {
        super.setup();
    }

    /**
     * Makes a call to the ExecuteTctSuite to run a tct suite, which is determined by calling
     * getTctSuiteId().
     *
     * @return a 2D Object Array - each Array has one object in it, a
     * {@link com.amazon.ata.test.tct.introspection.types.TctResult} object
     */
//    @DataProvider(name = "TctResults")
//    public Object[][] getTctResults() {
//        ExecuteTctSuiteCall executeTctSuiteCall = kindlePublishingServiceClient.newExecuteTctSuiteCall();
//
//        ExecuteTctSuiteRequest request = new ExecuteTctSuiteRequest(getTctSuiteId());
//
//        ExecuteTctSuiteResponse response = executeTctSuiteCall.call(request);
//        TctSuiteReport report = response.getTctSuiteReport();
//
//        Object[][] results = new Object[report.getTctResultList().size()][1];
//        for (int i = 0; i < report.getTctResultList().size(); i++) {
//            results[i][0] = report.getTctResultList().get(i);
//        }
//
//        return results;
//    }

    /**
     * Returns the testSuiteId the test class would like to execute.
     *
     * @return the TestSuiteId for the test suite you would like to be run.
     */
    protected abstract String getTctSuiteId();
}
