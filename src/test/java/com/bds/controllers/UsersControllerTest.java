package com.bds.controllers;

import com.bds.dto.UsersRegistrationRequest;
import com.bds.models.BloodType;
import com.bds.models.Role;
import com.bds.models.Users;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class UsersControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    private static final String usersURI = "api/v1/admin";


    @Test
    void canRegisterNewUser() {
        // create registration request
        Faker faker = new Faker();
        Name fakerName = faker.name();

        String firstName = fakerName.firstName();
        String lastName = fakerName.lastName();
        String email = fakerName.lastName() + UUID.randomUUID() + "@integrationTest.com";
        String role = "DONOR";
        String bloodType = "APos";

        UsersRegistrationRequest request = new UsersRegistrationRequest(
                firstName,
                lastName,
                email,
                role,
                bloodType
        );

        // send a post request
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.put("grant_type", Collections.singletonList("password"));
        formData.put("client_id", Collections.singletonList("blood-donor-system-rest-api"));
        formData.put("username", Collections.singletonList("milos"));
        formData.put("password", Collections.singletonList("milos"));

        String resultString = webTestClient.post()
                .uri("http://localhost:8080/realms/Milos/protocol/openid-connect/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formData))
                .exchange()
                .expectBody(new ParameterizedTypeReference<String>() {
                })
                .returnResult()
                .getResponseBody();

        JacksonJsonParser jsonParser = new JacksonJsonParser();
        String jwt = jsonParser.parseMap(resultString).get("access_token").toString();

        webTestClient.post()
                .uri(usersURI + "/register_user")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(h -> h.setBearerAuth(jwt))
                .body(Mono.just(request), UsersRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isCreated();

        // get all users
        List<Users> allUsers = webTestClient.get()
                .uri(usersURI + "/donor")
                .accept(MediaType.APPLICATION_JSON)
                .headers(h -> h.setBearerAuth(jwt))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Users>() {
                })
                .returnResult()
                .getResponseBody();

        // make sure that user is present
        Users expectedUser = new Users(
                firstName,
                lastName,
                email,
                Role.DONOR,
                BloodType.APos
        );

        assertThat(allUsers)
                .usingRecursiveFieldByFieldElementComparatorIgnoringFields("Id")
                .contains(expectedUser);
    }
}
