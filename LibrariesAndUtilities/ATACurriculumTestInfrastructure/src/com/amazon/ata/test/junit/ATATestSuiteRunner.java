package com.amazon.ata.test.junit;

import com.amazon.ata.test.types.ATATestSuiteId;

import com.amazon.ata.test.types.ATATestSuiteReport;

import org.junit.platform.launcher.Launcher;
import org.junit.platform.launcher.LauncherDiscoveryRequest;
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectPackage;
import static org.junit.platform.launcher.TagFilter.includeTags;

public final class ATATestSuiteRunner {

    private ATATestSuiteRunner() {
    }

    /**
     * Executes test classes tagged with the testSuiteId. A report is generated and returned, containing information
     * about each test that runs as part of the provided class.
     * @param testSuiteId the id of the test suite to be run, should be populated in the TctClassExecutionResult
     * @return a report containing test level results
     */
    public static ATATestSuiteReport execute(ATATestSuiteId testSuiteId) {
        LauncherDiscoveryRequest launcherRequest = LauncherDiscoveryRequestBuilder.request()
            // setting explicitly to false as TctClassExecutionResult is not thread safe
            .configurationParameter("junit.jupiter.execution.parallel.enabled", "false")
            .selectors(
                    selectPackage("com.amazon.ata")
            )
            .filters(
                    includeTags(testSuiteId.getTestSuiteId())
            )
            .build();

        Launcher launcher = LauncherFactory.create();

        ATATestSuiteReport ataTestSuiteReport = new ATATestSuiteReport(testSuiteId);
        ATATestExecutionListener listener = new ATATestExecutionListener(ataTestSuiteReport);
        launcher.registerTestExecutionListeners(listener);

        launcher.execute(launcherRequest);
        return listener.getResult();
    }
}
