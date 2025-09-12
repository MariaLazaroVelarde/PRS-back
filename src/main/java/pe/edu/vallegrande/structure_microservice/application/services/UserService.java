package pe.edu.vallegrande.structure_microservice.application.services;

import pe.edu.vallegrande.structure_microservice.domain.models.User;
import pe.edu.vallegrande.structure_microservice.infrastructure.dto.request.UserCreateRequest;
import pe.edu.vallegrande.structure_microservice.infrastructure.dto.response.UserResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {
    Flux<User> getAll();
    Flux<User> getAllActive();
    Flux<User> getAllInactive();
    Mono<User> getById(String id);
    Mono<UserResponse> save(UserCreateRequest userRequest);
    Mono<User> update(String id, User user);
    Mono<Void> delete(String id);
    Mono<User> activate(String id);
    Mono<User> deactivate(String id);
}