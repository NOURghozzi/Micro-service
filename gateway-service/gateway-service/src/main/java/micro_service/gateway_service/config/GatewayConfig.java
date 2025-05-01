package micro_service.gateway_service.config;


import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("teamtrack", r -> r.path("/user-management/**").uri("http://localhost:8081"))
                .route("project-service", r -> r.path("/project-management/**").uri("http://localhost:8084"))
                .route("team-service", r -> r.path("/team-management/**").uri("http://localhost:8083"))
                .route("tasks-service", r -> r.path("/task-management/**").uri("http://localhost:8086"))
                .route("sprint-service", r -> r.path("/sprint-management/**").uri("http://localhost:8085"))
                .build();
    }
}
