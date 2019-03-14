package nfq.test.integration;

import nfq.test.integration.functional.BiddingTest;
import nfq.test.integration.functional.JobTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
        BiddingTest.class,
        JobTest.class
})
public class IntegrationTest {
}
