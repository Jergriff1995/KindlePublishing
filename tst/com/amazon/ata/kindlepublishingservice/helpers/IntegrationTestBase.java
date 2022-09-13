package com.amazon.ata.kindlepublishingservice.helpers;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.AfterEach;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;

import java.util.Optional;

/**
 * Provides a ATACurriculumKindlePublishingServiceClient that does not directly depend on the
 * ATACurriculumKindlePublishingServiceClientConfig. The endpoint is determined by either the provided endpointOverride
 * or determined by querying CloudFront using the provided stackName and outputName.
 */
public abstract class IntegrationTestBase {
    // singleton so we set up once and keep the instance across test runs
    private static final KindlePublishingServiceTctTestDao KINDLE_PUBLISHING_SERVICE_TCT_TEST_DAO =
        new KindlePublishingServiceTctTestDao(
                new DynamoDBMapper(AmazonDynamoDBClientBuilder.standard().withRegion(Regions.US_WEST_2).build()));
    private static String endpointOverride;
    private static String stackName;
    private static String loadBalancerId;

//    protected ATACurriculumKindlePublishingServiceClient kindlePublishingServiceClient;
    private final Logger log = LogManager.getLogger(getClass());

    // CHECKSTYLE:OFF:HiddenField
    /**
     * Configures a ATACurriculumKindlePublishingServiceClient for the integration tests.
     * @param endpointOverride - if this is provided this will be the endpoint
     * @param stackName        - the stack to lookup in cloudformation to lookup it's endpoint
     * @param loadBalancerId   - within a stack what is the id of the loadbalancer so the DNS value can be looked up
     */
    @Parameters({"endpointOverride", "stackName", "loadBalancerId"})
    @BeforeSuite
    public void setup(@org.testng.annotations.Optional() final String endpointOverride,
                      @org.testng.annotations.Optional() final String stackName,
                      @org.testng.annotations.Optional() final String loadBalancerId) {
        setEndpointOverride(endpointOverride);
        setStackName(stackName);
        setLoadBalancerId(loadBalancerId);
    }
    // CHECKSTYLE:ON:HiddenField

    /**
     * Creates the KindlePublishingServiceClient.
     */
    protected void setup() {
        String qualifier = "Base.Everywhere";
        final String configString = getClientConfig(qualifier, getEndpoint());
//        ClientBuilder clientBuilder = new ClientBuilder(configString);
//        kindlePublishingServiceClient = clientBuilder
//            .remoteOf(ATACurriculumKindlePublishingServiceClient.class)
//            .withConfiguration(qualifier).newClient();
    }

    /**
     * Clean up after all tests have run.
     */
    @AfterEach
    public void teardown() {
        // this is only safe to run in the aftersuite when all tests have executed
        // otherwise we risk deleting parallel tests' data
        KINDLE_PUBLISHING_SERVICE_TCT_TEST_DAO.cleanup();
    }

    // Configuring client manually so that we can override the endpoint.
    private String getClientConfig(String qualifier, String endpoint) {
        final StringBuilder configBuilder = new StringBuilder();

        configBuilder.append("ATACurriculumKindlePublishingService" + "#" + qualifier + " : { ");
        configBuilder.append("  \"protocols\" : [\"rpc\"], ");
        configBuilder.append("  \"rpc\" : { ");
        configBuilder.append("    \"encoding\" : \"text\"");
        configBuilder.append("  }, ");
        configBuilder.append("  \"httpClient\" : { ");
        configBuilder.append("    \"clientType\" : \"netty4\" ");
        configBuilder.append("  }, ");
        configBuilder.append("  \"httpEndpoint\" : { ");
        configBuilder.append("    \"url\" : \"" + endpoint + "\" ");
        configBuilder.append("  } ");
        configBuilder.append("} ");

        return configBuilder.toString();
    }

    private String getEndpoint() {
        if (StringUtils.isNotBlank(getEndpointOverride())) {
            log.warn("Endpoint override set. Configuring endpoint to be: " + getEndpointOverride());
            return getEndpointOverride();
        } else {
            if (StringUtils.isEmpty(getStackName())) {
                throw new IllegalArgumentException("Unable to configure endpoint: stackName is empty");
            }
            if (StringUtils.isEmpty(getLoadBalancerId())) {
                throw new IllegalArgumentException("Unable to configure endpoint: loadBalancerId is empty");
            }

            try {
//                final AmazonCloudFormation cloudFormation = AmazonCloudFormationClientBuilder.defaultClient();
//                DescribeStacksResult describeStacks = cloudFormation.describeStacks();
//                final Optional<Stack> serviceStack = describeStacks.getStacks().stream()
//                    .filter(stack -> stack.getStackName().equals(getStackName()))
//                    .findFirst();
//                serviceStack.orElseThrow(() -> new RuntimeException("Cannot find stack with name: " + getStackName()));
//                final Optional<Output> op = serviceStack.get().getOutputs().stream()
//                    .filter(output -> output.getOutputKey().equals(getLoadBalancerId()))
//                    .findFirst();
//                op.orElseThrow(() -> new RuntimeException("Cannot find stack output with name: " +
//                    getLoadBalancerId()));
//                log.warn("Configuring endpoint to be: http://" + op.get().getOutputValue());
//                return "http://" + op.get().getOutputValue();
            } catch (Exception e) {
                throw new IllegalStateException("Unable to determine your service's internal dns name.", e);
            }
        }
        return "";
    }

    protected KindlePublishingServiceTctTestDao getTestDao() {
        return KINDLE_PUBLISHING_SERVICE_TCT_TEST_DAO;
    }

    private static void setEndpointOverride(String endpointOverride) {
        IntegrationTestBase.endpointOverride = endpointOverride;
    }

    private static String getEndpointOverride() {
        return endpointOverride;
    }

    private static void setStackName(String stackName) {
        IntegrationTestBase.stackName = stackName;
    }

    private static String getStackName() {
        return stackName;
    }

    private static void setLoadBalancerId(String loadBalancerId) {
        IntegrationTestBase.loadBalancerId = loadBalancerId;
    }

    private static String getLoadBalancerId() {
        return loadBalancerId;
    }
}
