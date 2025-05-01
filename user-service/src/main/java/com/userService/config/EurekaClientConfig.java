package com.userService.config;

import com.netflix.discovery.AbstractDiscoveryClientOptionalArgs;
import com.netflix.discovery.shared.transport.jersey3.Jersey3TransportClientFactories;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EurekaClientConfig {

    @Bean
    public AbstractDiscoveryClientOptionalArgs<?> discoveryClientOptionalArgs() {
        return new AbstractDiscoveryClientOptionalArgs<Object>() {};
    }

    @Bean
    public Jersey3TransportClientFactories transportClientFactories() {
        return new Jersey3TransportClientFactories();
    }
}