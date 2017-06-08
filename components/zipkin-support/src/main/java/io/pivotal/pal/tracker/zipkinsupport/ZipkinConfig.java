package io.pivotal.pal.tracker.zipkinsupport;

import com.netflix.discovery.EurekaClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.sleuth.metric.SpanMetricReporter;
import org.springframework.cloud.sleuth.zipkin.ZipkinProperties;
import org.springframework.cloud.sleuth.zipkin.ZipkinSpanReporter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Optional;

@Configuration
@ConditionalOnProperty(value = "spring.zipkin.enabled", matchIfMissing = true)
public class ZipkinConfig {

    @Value("${spring.sleuth.web.skipPattern:}") String skipPattern;
    @Value("${zipkin.server.endpoint:zipkin-server}") String zipkinBaseUrl;
    @Autowired(required = false) EurekaClient eurekaClient;


    @Bean
    ZipkinSpanReporter zipkinSpanReporter(ZipkinProperties zipkinProperties, SpanMetricReporter spanMetricReporter) {
        return new EurekaZipkinReporter(
            Optional.ofNullable(eurekaClient),
            zipkinProperties,
            spanMetricReporter,
            zipkinBaseUrl,
            skipPattern
        );
    }
}
