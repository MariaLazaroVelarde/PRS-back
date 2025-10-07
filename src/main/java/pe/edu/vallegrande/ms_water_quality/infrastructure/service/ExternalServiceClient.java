package pe.edu.vallegrande.ms_water_quality.infrastructure.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import pe.edu.vallegrande.ms_water_quality.infrastructure.client.dto.ExternalOrganization;
import pe.edu.vallegrande.ms_water_quality.infrastructure.client.dto.ExternalUser;
import pe.edu.vallegrande.ms_water_quality.infrastructure.client.dto.UserApiResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ExternalServiceClient {

    private final WebClient userWebClient;
    private final WebClient organizationWebClient;

    public ExternalServiceClient(
            @Qualifier("userWebClient") WebClient userWebClient, 
            @Qualifier("organizationWebClient") WebClient organizationWebClient) {
        this.userWebClient = userWebClient;
        this.organizationWebClient = organizationWebClient;
    }

    public Flux<ExternalUser> getAdminsByOrganization(String organizationId) {
        return userWebClient.get()
                .uri("/internal/organizations/" + organizationId + "/admins")
                .retrieve()
                .bodyToMono(UserApiResponse.class)
                .flatMapMany(response -> Flux.fromIterable(response.getData()))
                .onErrorResume(e -> Flux.empty());
    }

    public Mono<ExternalOrganization> getOrganizationById(String organizationId) {
        return organizationWebClient.get()
                .uri("/api/management/organizations/" + organizationId)
                .retrieve()
                .bodyToMono(ExternalOrganization.class)
                .onErrorResume(e -> Mono.empty());
    }
}