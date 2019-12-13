package com.gpnk.common;

import com.codahale.metrics.MetricRegistry;
import com.google.inject.AbstractModule;
import com.palominolabs.metrics.guice.MetricsInstrumentationModule;

/**
 * Enables Guice Codahale Metric annotation implementation (https://github.com/palominolabs/metrics-guice).
 * Configures it with the given metric registry.
 */
public class MetricsModule extends AbstractModule {

    private final MetricRegistry metricRegistry;

    /**
     * The {@code metricRegistry} to use in Guice metrics.
     * @param metricRegistry - metric registry to use in Guice metrics.
     */
    public MetricsModule(final MetricRegistry metricRegistry) {
        this.metricRegistry = metricRegistry;
    }

    @Override
    protected void configure() {
        // enable Guice Codahale Metrics with the given metric registry
        install(MetricsInstrumentationModule.builder().withMetricRegistry(metricRegistry).build());
    }

}
