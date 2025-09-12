package pe.edu.vallegrande.structure_microservice.infrastructure.rest;

import lombok.AllArgsConstructor;
import pe.edu.vallegrande.structure_microservice.application.services.UserService;
import pe.edu.vallegrande.structure_microservice.domain.models.User;
import pe.edu.vallegrande.structure_microservice.infrastructure.dto.ErrorMessage;
import pe.edu.vallegrande.structure_microservice.infrastructure.dto.ResponseDto;
import pe.edu.vallegrande.structure_microservice.infrastructure.dto.request.UserCreateRequest;
import pe.edu.vallegrande.structure_microservice.infrastructure.dto.response.UserResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin("*")
@AllArgsConstructor
public class UserRest {

    private final UserService userService;

    @GetMapping
    public Mono<ResponseDto<List<User>>> getAll() {
        return userService.getAll()
                .collectList()
                .map(users -> new ResponseDto<>(true, users));
    }

    @GetMapping("/active")
    public Mono<ResponseDto<List<User>>> getAllActive() {
        return userService.getAllActive()
                .collectList()
                .map(users -> new ResponseDto<>(true, users));
    }

    @GetMapping("/inactive")
    public Mono<ResponseDto<List<User>>> getAllInactive() {
        return userService.getAllInactive()
                .collectList()
                .map(users -> new ResponseDto<>(true, users));
    }

    @GetMapping("/{id}")
    public Mono<ResponseDto<User>> getById(@PathVariable String id) {
        return userService.getById(id)
                .map(user -> new ResponseDto<>(true, user))
                .onErrorResume(e -> Mono.just(
                        new ResponseDto<>(false,
                                new ErrorMessage(HttpStatus.NOT_FOUND.value(),
                                        "User not found",
                                        e.getMessage()))));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<ResponseDto<UserResponse>> create(@RequestBody UserCreateRequest request) {
        return userService.save(request)
                .map(savedUser -> new ResponseDto<>(true, savedUser))
                .onErrorResume(e -> Mono.just(
                        new ResponseDto<>(false,
                                new ErrorMessage(HttpStatus.BAD_REQUEST.value(),
                                        "Validation error",
                                        e.getMessage()))));
    }
    @PutMapping("/{id}")
    public Mono<ResponseDto<User>> update(@PathVariable String id, @RequestBody User user) {
        return userService.update(id, user)
                .map(updatedUser -> new ResponseDto<>(true, updatedUser))
                .onErrorResume(e -> Mono.just(
                        new ResponseDto<>(false,
                                new ErrorMessage(HttpStatus.BAD_REQUEST.value(),
                                        "Update failed",
                                        e.getMessage()))));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<ResponseDto<Object>> delete(@PathVariable String id) {
        return userService.delete(id)
                .then(Mono.just(new ResponseDto<>(true, null)))
                .onErrorResume(e -> Mono.just(
                        new ResponseDto<>(false,
                                new ErrorMessage(HttpStatus.BAD_REQUEST.value(),
                                        "Delete failed",
                                        e.getMessage()))));
    }

    @PatchMapping("/{id}/activate")
    public Mono<ResponseDto<User>> activate(@PathVariable String id) {
        return userService.activate(id)
                .map(user -> new ResponseDto<>(true, user))
                .onErrorResume(e -> Mono.just(
                        new ResponseDto<>(false,
                                new ErrorMessage(HttpStatus.BAD_REQUEST.value(),
                                        "Activation failed",
                                        e.getMessage()))));
    }

    @PatchMapping("/{id}/deactivate")
    public Mono<ResponseDto<User>> deactivate(@PathVariable String id) {
        return userService.deactivate(id)
                .map(user -> new ResponseDto<>(true, user))
                .onErrorResume(e -> Mono.just(
                        new ResponseDto<>(false,
                                new ErrorMessage(HttpStatus.BAD_REQUEST.value(),
                                        "Deactivation failed",
                                        e.getMessage()))));
    }


}