package io.pivotal.pal.tracker.zipkinsupport;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.EurekaClient;
import org.springframework.cloud.sleuth.metric.SpanMetricReporter;
import org.springframework.cloud.sleuth.zipkin.HttpZipkinSpanReporter;
import org.springframework.cloud.sleuth.zipkin.ZipkinProperties;
import org.springframework.cloud.sleuth.zipkin.ZipkinSpanReporter;
import zipkin.Span;

import java.util.Optional;


public class EurekaZipkinReporter implements ZipkinSpanReporter {

    private final Optional<EurekaClient> maybeEurekaClient;
    private final ZipkinProperties zipkinProperties;
    private final SpanMetricReporter spanMetricReporter;
    private final String zipkinEndpointConfig;
    private final String skipPattern;

    public EurekaZipkinReporter(
        Optional<EurekaClient> maybeEurekaClient,
        ZipkinProperties zipkinProperties,
        SpanMetricReporter spanMetricReporter,
        String zipkinEndpointConfig,
        String skipPattern
    ) {
        this.maybeEurekaClient = maybeEurekaClient;
        this.zipkinProperties = zipkinProperties;
        this.spanMetricReporter = spanMetricReporter;
        this.zipkinEndpointConfig = zipkinEndpointConfig;
        this.skipPattern = skipPattern;
    }


    private String baseUrl = null;
    private HttpZipkinSpanReporter delegate = null;

    @Override
    public void report(Span span) {
        String newZipkinUrl = maybeEurekaClient
            .map(eurekaClient -> {
                InstanceInfo instanceInfo = eurekaClient.getNextServerFromEureka(zipkinEndpointConfig, false);
                return instanceInfo.getHomePageUrl();
            })
            .orElse(zipkinEndpointConfig);


        if (baseUrlHasChanged(newZipkinUrl)) {
            baseUrl = newZipkinUrl;

            delegate = new HttpZipkinSpanReporter(
                baseUrl,
                zipkinProperties.getFlushInterval(),
                zipkinProperties.getCompression().isEnabled(),
                spanMetricReporter
            );
        }

        if (!span.name.matches(skipPattern)) {
            delegate.report(span);
        }
    }


    private boolean baseUrlHasChanged(String zipkinUrl) {
        return baseUrl == null || !baseUrl.equals(zipkinUrl);
    }
}
