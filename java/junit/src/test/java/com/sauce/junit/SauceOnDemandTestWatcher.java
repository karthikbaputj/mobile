package com.sauce.junit;

import com.sauce.common.SauceOnDemandAuthentication;
import com.saucelabs.saucerest.SauceREST;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;

import java.util.HashMap;
import java.util.Map;

/**
 * {@link TestWatcher} subclass that will mark a Sauce OnDemand job as passed or failed depending on the result
 * of the test case being executed.
 *
 * @author see {@link } for original
 * @author Ross Rowe - modifications to use {@link SauceOnDemandAuthentication}
 */
public class SauceOnDemandTestWatcher extends TestWatcher {

    /**
     * The underlying {@link com.sauce.common.SauceOnDemandSessionIdProvider} instance which contains the Selenium session id.  This is typically
     * the unit test being executed.
     */
    private final com.sauce.common.SauceOnDemandSessionIdProvider sessionIdProvider;

    /**
     * The instance of the Sauce OnDemand Java REST API client.
     */
    private final SauceREST sauceREST;

    /**
     * Boolean indicating whether to print the log messages to the stdout.
     */
    private boolean verboseMode = true;


    /**
     * @param sessionIdProvider
     */
    public SauceOnDemandTestWatcher(com.sauce.common.SauceOnDemandSessionIdProvider sessionIdProvider) {
        this(sessionIdProvider, new SauceOnDemandAuthentication());
    }

    public SauceOnDemandTestWatcher(com.sauce.common.SauceOnDemandSessionIdProvider sessionIdProvider, boolean verboseMode) {
        this(sessionIdProvider, new SauceOnDemandAuthentication(), verboseMode);
    }

    /**
     * @param sessionIdProvider
     * @param authentication
     */
    public SauceOnDemandTestWatcher(com.sauce.common.SauceOnDemandSessionIdProvider sessionIdProvider, SauceOnDemandAuthentication authentication) {
        this(sessionIdProvider,
                authentication.getUsername(),
                authentication.getAccessKey(), true);
    }

    /**
     * @param sessionIdProvider
     * @param authentication
     */
    public SauceOnDemandTestWatcher(com.sauce.common.SauceOnDemandSessionIdProvider sessionIdProvider, SauceOnDemandAuthentication authentication, boolean verboseMode) {
        this(sessionIdProvider,
                authentication.getUsername(),
                authentication.getAccessKey(),
                verboseMode);
    }

    /**
     * @param sessionIdProvider
     * @param username
     * @param accessKey
     */
    public SauceOnDemandTestWatcher(com.sauce.common.SauceOnDemandSessionIdProvider sessionIdProvider, final String username, final String accessKey, boolean verboseMode) {
        this.sessionIdProvider = sessionIdProvider;
        sauceREST = new SauceREST(username, accessKey);
        this.verboseMode = verboseMode;
    }

    /**
     * Invoked if the unit test passes without error or failure.  Invokes the Sauce REST API to mark the Sauce Job
     * as 'passed'.
     *
     * @param description not used
     */
    protected void succeeded(Description description) {
        if (sessionIdProvider.getSessionId() != null) {
            //log the session id to the system out
            printSessionId(description);
            Map<String, Object> updates = new HashMap<String, Object>();
            updates.put("passed", true);
            com.sauce.common.Utils.addBuildNumberToUpdate(updates);
            sauceREST.updateJobInfo(sessionIdProvider.getSessionId(), updates);

        }
    }

    private void printSessionId(Description description) {
        if (verboseMode) {
            String message = String.format("SauceOnDemandSessionID=%1$s job-name=%2$s.%3$s", sessionIdProvider.getSessionId(), description.getClassName(), description.getMethodName());
            System.out.println(message);
        }
    }

    /**
     * Invoked if the unit test either throws an error or fails.  Invokes the Sauce REST API to mark the Sauce Job
     * as 'failed'.
     *
     * @param e           not used
     * @param description not used
     */
    protected void failed(Throwable e, Description description) {
        if (sessionIdProvider != null && sessionIdProvider.getSessionId() != null) {
            printSessionId(description);
            Map<String, Object> updates = new HashMap<String, Object>();
            updates.put("passed", false);
            com.sauce.common.Utils.addBuildNumberToUpdate(updates);
            sauceREST.updateJobInfo(sessionIdProvider.getSessionId(), updates);

            if (verboseMode) {
                // get, and print to StdOut, the link to the job
                String authLink = sauceREST.getPublicJobLink(sessionIdProvider.getSessionId());
                System.out.println("Job link: " + authLink);
            }
        }
    }


}