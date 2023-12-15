package com.bds.controllers;

import com.bds.dto.BloodDonationEventRequest;
import com.bds.dto.UsersRegistrationRequest;
import com.bds.models.BloodDonationEvent;
import com.bds.models.BloodType;
import com.bds.models.Users;
import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import dasniko.testcontainers.keycloak.KeycloakContainer;
import org.assertj.core.api.recursive.comparison.RecursiveComparisonConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.*;

@Testcontainers
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class BloodDonationEventControllerTest {

    @Autowired
    WebTestClient webTestClient;

    private static final String donationEventURI = "api/v1/admin";

    @Container
    static KeycloakContainer keycloak = new KeycloakContainer("quay.io/keycloak/keycloak:22.0.1")
            .withRealmImportFile("keycloak/Milos-realm.json");

    @DynamicPropertySource
    static void registerResourceServerIssuerProperty(DynamicPropertyRegistry registry) {
        registry.add(
                "spring.security.oauth2.resourceserver.jwt.issuer-uri",
                () -> keycloak.getAuthServerUrl() + "/realms/Milos"
        );
    }

    @Test
    void canAddBloodDonationEvent() {
        // get jwt token from Keycloak
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.put("grant_type", Collections.singletonList("password"));
        formData.put("client_id", Collections.singletonList("blood-donor-system-rest-api"));
        formData.put("username", Collections.singletonList("milos"));
        formData.put("password", Collections.singletonList("milos"));

        String resultString = webTestClient.post()
                .uri(keycloak.getAuthServerUrl() + "/realms/Milos/protocol/openid-connect/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formData))
                .exchange()
                .expectBody(new ParameterizedTypeReference<String>() {
                })
                .returnResult()
                .getResponseBody();

        JacksonJsonParser jsonParser = new JacksonJsonParser();
        String jwt = jsonParser.parseMap(resultString).get("access_token").toString();

        // create faker user
        Faker faker = new Faker();
        Name fakerName = faker.name();

        String firstName = fakerName.firstName();
        String lastName = fakerName.lastName();
        String email = fakerName.lastName() + UUID.randomUUID() + "@integrationTest.com";

        // create admin registration request
        UsersRegistrationRequest adminRequest = new UsersRegistrationRequest(
                firstName,
                lastName,
                email + "---ADMIN",
                "ADMIN",
                "ABPos"
        );

        // register admin user
        webTestClient.post()
                .uri("/api/v1/admin/register_user")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(h -> h.setBearerAuth(jwt))
                .body(Mono.just(adminRequest), UsersRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isCreated();

        // get all admin users
        List<Users> allAdmins = webTestClient.get()
                .uri("/api/v1/admin")
                .accept(MediaType.APPLICATION_JSON)
                .headers(h -> h.setBearerAuth(jwt))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Users>() {
                })
                .returnResult()
                .getResponseBody();

        // get admin user from users
        Users adminUser = allAdmins.stream()
                .filter(user -> user.getEmail().equals(adminRequest.email()))
                .findFirst()
                .orElseThrow();

        // make blood donation event request
        String eventName = faker.company().name().toString();
        BloodDonationEventRequest bloodDonationEventRequest = new BloodDonationEventRequest(
                eventName,
                LocalDate.now(),
                "ONeg",
                5,
                adminUser
        );

        // send a post request for registering new event
        BloodDonationEvent savedBloodDonationEvent = webTestClient.post()
                .uri(donationEventURI + "/donation_event")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .headers(h -> h.setBearerAuth(jwt))
                .body(Mono.just(bloodDonationEventRequest), BloodDonationEventRequest.class)
                .exchange()
                .expectStatus()
                .isCreated()
                .expectBody(new ParameterizedTypeReference<BloodDonationEvent>() {
                })
                .returnResult()
                .getResponseBody();

        // make sure that event is saved

        BloodDonationEvent expectedBloodDonationEvent = new BloodDonationEvent(
                eventName,
                LocalDate.now(),
                BloodType.valueOf("ONeg"),
                5,
                adminUser
        );

        RecursiveComparisonConfiguration ignoreIdConfig = new RecursiveComparisonConfiguration();
        ignoreIdConfig.ignoreFields("Id");

        assertThat(savedBloodDonationEvent)
                .usingRecursiveComparison(ignoreIdConfig)
                .isEqualTo(expectedBloodDonationEvent);
    }

    @Test
    void badEventNameFormatEmptyEventName() {
        // get jwt token from Keycloak
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.put("grant_type", Collections.singletonList("password"));
        formData.put("client_id", Collections.singletonList("blood-donor-system-rest-api"));
        formData.put("username", Collections.singletonList("milos"));
        formData.put("password", Collections.singletonList("milos"));

        String resultString = webTestClient.post()
                .uri(keycloak.getAuthServerUrl() + "/realms/Milos/protocol/openid-connect/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formData))
                .exchange()
                .expectBody(new ParameterizedTypeReference<String>() {
                })
                .returnResult()
                .getResponseBody();

        JacksonJsonParser jsonParser = new JacksonJsonParser();
        String jwt = jsonParser.parseMap(resultString).get("access_token").toString();

        // create faker user
        Faker faker = new Faker();
        Name fakerName = faker.name();

        String firstName = fakerName.firstName();
        String lastName = fakerName.lastName();
        String email = fakerName.lastName() + UUID.randomUUID() + "@integrationTest.com";

        // create admin registration request
        UsersRegistrationRequest adminRequest = new UsersRegistrationRequest(
                firstName,
                lastName,
                email + "---ADMIN",
                "ADMIN",
                "ABPos"
        );

        // register admin user
        webTestClient.post()
                .uri("/api/v1/admin/register_user")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(h -> h.setBearerAuth(jwt))
                .body(Mono.just(adminRequest), UsersRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isCreated();

        // get all admin users
        List<Users> allAdmins = webTestClient.get()
                .uri("/api/v1/admin")
                .accept(MediaType.APPLICATION_JSON)
                .headers(h -> h.setBearerAuth(jwt))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Users>() {
                })
                .returnResult()
                .getResponseBody();

        // get admin user from users
        Users adminUser = allAdmins.stream()
                .filter(user -> user.getEmail().equals(adminRequest.email()))
                .findFirst()
                .orElseThrow();

        // make blood donation event request
        String eventName = "";
        BloodDonationEventRequest bloodDonationEventRequest = new BloodDonationEventRequest(
                eventName,
                LocalDate.now(),
                "ONeg",
                5,
                adminUser
        );

        // send a post request for registering new event
        webTestClient.post()
                .uri(donationEventURI + "/donation_event")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .headers(h -> h.setBearerAuth(jwt))
                .body(Mono.just(bloodDonationEventRequest), BloodDonationEventRequest.class)
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatusCode.valueOf(406));
    }

    @Test
    void badEventNameFormatTooShort() {
        // get jwt token from Keycloak
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.put("grant_type", Collections.singletonList("password"));
        formData.put("client_id", Collections.singletonList("blood-donor-system-rest-api"));
        formData.put("username", Collections.singletonList("milos"));
        formData.put("password", Collections.singletonList("milos"));

        String resultString = webTestClient.post()
                .uri(keycloak.getAuthServerUrl() + "/realms/Milos/protocol/openid-connect/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formData))
                .exchange()
                .expectBody(new ParameterizedTypeReference<String>() {
                })
                .returnResult()
                .getResponseBody();

        JacksonJsonParser jsonParser = new JacksonJsonParser();
        String jwt = jsonParser.parseMap(resultString).get("access_token").toString();

        // create faker user
        Faker faker = new Faker();
        Name fakerName = faker.name();

        String firstName = fakerName.firstName();
        String lastName = fakerName.lastName();
        String email = fakerName.lastName() + UUID.randomUUID() + "@integrationTest.com";

        // create admin registration request
        UsersRegistrationRequest adminRequest = new UsersRegistrationRequest(
                firstName,
                lastName,
                email + "---ADMIN",
                "ADMIN",
                "ABPos"
        );

        // register admin user
        webTestClient.post()
                .uri("/api/v1/admin/register_user")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(h -> h.setBearerAuth(jwt))
                .body(Mono.just(adminRequest), UsersRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isCreated();

        // get all admin users
        List<Users> allAdmins = webTestClient.get()
                .uri("/api/v1/admin")
                .accept(MediaType.APPLICATION_JSON)
                .headers(h -> h.setBearerAuth(jwt))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Users>() {
                })
                .returnResult()
                .getResponseBody();

        // get admin user from users
        Users adminUser = allAdmins.stream()
                .filter(user -> user.getEmail().equals(adminRequest.email()))
                .findFirst()
                .orElseThrow();

        // make blood donation event request
        String eventName = "a";
        BloodDonationEventRequest bloodDonationEventRequest = new BloodDonationEventRequest(
                eventName,
                LocalDate.now(),
                "ONeg",
                5,
                adminUser
        );

        // send a post request for registering new event
        webTestClient.post()
                .uri(donationEventURI + "/donation_event")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .headers(h -> h.setBearerAuth(jwt))
                .body(Mono.just(bloodDonationEventRequest), BloodDonationEventRequest.class)
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatusCode.valueOf(406));
    }

    @Test
    void badEventNameFormatTooLong() {
        // get jwt token from Keycloak
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.put("grant_type", Collections.singletonList("password"));
        formData.put("client_id", Collections.singletonList("blood-donor-system-rest-api"));
        formData.put("username", Collections.singletonList("milos"));
        formData.put("password", Collections.singletonList("milos"));

        String resultString = webTestClient.post()
                .uri(keycloak.getAuthServerUrl() + "/realms/Milos/protocol/openid-connect/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formData))
                .exchange()
                .expectBody(new ParameterizedTypeReference<String>() {
                })
                .returnResult()
                .getResponseBody();

        JacksonJsonParser jsonParser = new JacksonJsonParser();
        String jwt = jsonParser.parseMap(resultString).get("access_token").toString();

        // create faker user
        Faker faker = new Faker();
        Name fakerName = faker.name();

        String firstName = fakerName.firstName();
        String lastName = fakerName.lastName();
        String email = fakerName.lastName() + UUID.randomUUID() + "@integrationTest.com";

        // create admin registration request
        UsersRegistrationRequest adminRequest = new UsersRegistrationRequest(
                firstName,
                lastName,
                email + "---ADMIN",
                "ADMIN",
                "ABPos"
        );

        // register admin user
        webTestClient.post()
                .uri("/api/v1/admin/register_user")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(h -> h.setBearerAuth(jwt))
                .body(Mono.just(adminRequest), UsersRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isCreated();

        // get all admin users
        List<Users> allAdmins = webTestClient.get()
                .uri("/api/v1/admin")
                .accept(MediaType.APPLICATION_JSON)
                .headers(h -> h.setBearerAuth(jwt))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Users>() {
                })
                .returnResult()
                .getResponseBody();

        // get admin user from users
        Users adminUser = allAdmins.stream()
                .filter(user -> user.getEmail().equals(adminRequest.email()))
                .findFirst()
                .orElseThrow();

        // make blood donation event request
        String eventName = "b123456789b123456789b123456798b12345";
        BloodDonationEventRequest bloodDonationEventRequest = new BloodDonationEventRequest(
                eventName,
                LocalDate.now(),
                "ONeg",
                5,
                adminUser
        );

        // send a post request for registering new event
        webTestClient.post()
                .uri(donationEventURI + "/donation_event")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .headers(h -> h.setBearerAuth(jwt))
                .body(Mono.just(bloodDonationEventRequest), BloodDonationEventRequest.class)
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatusCode.valueOf(406));
    }

    @Test
    void badEventDateFormatDateIsNotFutureOrPresent() {
        // get jwt token from Keycloak
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.put("grant_type", Collections.singletonList("password"));
        formData.put("client_id", Collections.singletonList("blood-donor-system-rest-api"));
        formData.put("username", Collections.singletonList("milos"));
        formData.put("password", Collections.singletonList("milos"));

        String resultString = webTestClient.post()
                .uri(keycloak.getAuthServerUrl() + "/realms/Milos/protocol/openid-connect/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formData))
                .exchange()
                .expectBody(new ParameterizedTypeReference<String>() {
                })
                .returnResult()
                .getResponseBody();

        JacksonJsonParser jsonParser = new JacksonJsonParser();
        String jwt = jsonParser.parseMap(resultString).get("access_token").toString();

        // create faker user
        Faker faker = new Faker();
        Name fakerName = faker.name();

        String firstName = fakerName.firstName();
        String lastName = fakerName.lastName();
        String email = fakerName.lastName() + UUID.randomUUID() + "@integrationTest.com";

        // create admin registration request
        UsersRegistrationRequest adminRequest = new UsersRegistrationRequest(
                firstName,
                lastName,
                email + "---ADMIN",
                "ADMIN",
                "ABPos"
        );

        // register admin user
        webTestClient.post()
                .uri("/api/v1/admin/register_user")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(h -> h.setBearerAuth(jwt))
                .body(Mono.just(adminRequest), UsersRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isCreated();

        // get all admin users
        List<Users> allAdmins = webTestClient.get()
                .uri("/api/v1/admin")
                .accept(MediaType.APPLICATION_JSON)
                .headers(h -> h.setBearerAuth(jwt))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Users>() {
                })
                .returnResult()
                .getResponseBody();

        // get admin user from users
        Users adminUser = allAdmins.stream()
                .filter(user -> user.getEmail().equals(adminRequest.email()))
                .findFirst()
                .orElseThrow();

        // make blood donation event request
        String eventName = faker.company().name().toString();
        BloodDonationEventRequest bloodDonationEventRequest = new BloodDonationEventRequest(
                eventName,
                LocalDate.now().minusYears(1L),
                "ONeg",
                5,
                adminUser
        );

        // send a post request for registering new event
        webTestClient.post()
                .uri(donationEventURI + "/donation_event")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .headers(h -> h.setBearerAuth(jwt))
                .body(Mono.just(bloodDonationEventRequest), BloodDonationEventRequest.class)
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatusCode.valueOf(406));
    }

    @Test
    void badBloodTypeFormat() {
        // get jwt token from Keycloak
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.put("grant_type", Collections.singletonList("password"));
        formData.put("client_id", Collections.singletonList("blood-donor-system-rest-api"));
        formData.put("username", Collections.singletonList("milos"));
        formData.put("password", Collections.singletonList("milos"));

        String resultString = webTestClient.post()
                .uri(keycloak.getAuthServerUrl() + "/realms/Milos/protocol/openid-connect/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formData))
                .exchange()
                .expectBody(new ParameterizedTypeReference<String>() {
                })
                .returnResult()
                .getResponseBody();

        JacksonJsonParser jsonParser = new JacksonJsonParser();
        String jwt = jsonParser.parseMap(resultString).get("access_token").toString();

        // create faker user
        Faker faker = new Faker();
        Name fakerName = faker.name();

        String firstName = fakerName.firstName();
        String lastName = fakerName.lastName();
        String email = fakerName.lastName() + UUID.randomUUID() + "@integrationTest.com";

        // create admin registration request
        UsersRegistrationRequest adminRequest = new UsersRegistrationRequest(
                firstName,
                lastName,
                email + "---ADMIN",
                "ADMIN",
                "ABPos"
        );

        // register admin user
        webTestClient.post()
                .uri("/api/v1/admin/register_user")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(h -> h.setBearerAuth(jwt))
                .body(Mono.just(adminRequest), UsersRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isCreated();

        // get all admin users
        List<Users> allAdmins = webTestClient.get()
                .uri("/api/v1/admin")
                .accept(MediaType.APPLICATION_JSON)
                .headers(h -> h.setBearerAuth(jwt))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Users>() {
                })
                .returnResult()
                .getResponseBody();

        // get admin user from users
        Users adminUser = allAdmins.stream()
                .filter(user -> user.getEmail().equals(adminRequest.email()))
                .findFirst()
                .orElseThrow();

        // make blood donation event request
        String eventName = faker.company().name().toString();
        BloodDonationEventRequest bloodDonationEventRequest = new BloodDonationEventRequest(
                eventName,
                LocalDate.now().minusYears(1L),
                "O",
                5,
                adminUser
        );

        // send a post request for registering new event
        webTestClient.post()
                .uri(donationEventURI + "/donation_event")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .headers(h -> h.setBearerAuth(jwt))
                .body(Mono.just(bloodDonationEventRequest), BloodDonationEventRequest.class)
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatusCode.valueOf(406));
    }

    @Test
    void badUnitsFormatLesThanZero() {
        // get jwt token from Keycloak
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.put("grant_type", Collections.singletonList("password"));
        formData.put("client_id", Collections.singletonList("blood-donor-system-rest-api"));
        formData.put("username", Collections.singletonList("milos"));
        formData.put("password", Collections.singletonList("milos"));

        String resultString = webTestClient.post()
                .uri(keycloak.getAuthServerUrl() + "/realms/Milos/protocol/openid-connect/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formData))
                .exchange()
                .expectBody(new ParameterizedTypeReference<String>() {
                })
                .returnResult()
                .getResponseBody();

        JacksonJsonParser jsonParser = new JacksonJsonParser();
        String jwt = jsonParser.parseMap(resultString).get("access_token").toString();

        // create faker user
        Faker faker = new Faker();
        Name fakerName = faker.name();

        String firstName = fakerName.firstName();
        String lastName = fakerName.lastName();
        String email = fakerName.lastName() + UUID.randomUUID() + "@integrationTest.com";

        // create admin registration request
        UsersRegistrationRequest adminRequest = new UsersRegistrationRequest(
                firstName,
                lastName,
                email + "---ADMIN",
                "ADMIN",
                "ABPos"
        );

        // register admin user
        webTestClient.post()
                .uri("/api/v1/admin/register_user")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(h -> h.setBearerAuth(jwt))
                .body(Mono.just(adminRequest), UsersRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isCreated();

        // get all admin users
        List<Users> allAdmins = webTestClient.get()
                .uri("/api/v1/admin")
                .accept(MediaType.APPLICATION_JSON)
                .headers(h -> h.setBearerAuth(jwt))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Users>() {
                })
                .returnResult()
                .getResponseBody();

        // get admin user from users
        Users adminUser = allAdmins.stream()
                .filter(user -> user.getEmail().equals(adminRequest.email()))
                .findFirst()
                .orElseThrow();

        // make blood donation event request
        String eventName = faker.company().name().toString();
        BloodDonationEventRequest bloodDonationEventRequest = new BloodDonationEventRequest(
                eventName,
                LocalDate.now().minusYears(1L),
                "ONeg",
                -1,
                adminUser
        );

        // send a post request for registering new event
        webTestClient.post()
                .uri(donationEventURI + "/donation_event")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .headers(h -> h.setBearerAuth(jwt))
                .body(Mono.just(bloodDonationEventRequest), BloodDonationEventRequest.class)
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatusCode.valueOf(406));
    }

    @Test
    void badUnitsFormatGreaterThanFive() {
        // get jwt token from Keycloak
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        formData.put("grant_type", Collections.singletonList("password"));
        formData.put("client_id", Collections.singletonList("blood-donor-system-rest-api"));
        formData.put("username", Collections.singletonList("milos"));
        formData.put("password", Collections.singletonList("milos"));

        String resultString = webTestClient.post()
                .uri(keycloak.getAuthServerUrl() + "/realms/Milos/protocol/openid-connect/token")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData(formData))
                .exchange()
                .expectBody(new ParameterizedTypeReference<String>() {
                })
                .returnResult()
                .getResponseBody();

        JacksonJsonParser jsonParser = new JacksonJsonParser();
        String jwt = jsonParser.parseMap(resultString).get("access_token").toString();

        // create faker user
        Faker faker = new Faker();
        Name fakerName = faker.name();

        String firstName = fakerName.firstName();
        String lastName = fakerName.lastName();
        String email = fakerName.lastName() + UUID.randomUUID() + "@integrationTest.com";

        // create admin registration request
        UsersRegistrationRequest adminRequest = new UsersRegistrationRequest(
                firstName,
                lastName,
                email + "---ADMIN",
                "ADMIN",
                "ABPos"
        );

        // register admin user
        webTestClient.post()
                .uri("/api/v1/admin/register_user")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .headers(h -> h.setBearerAuth(jwt))
                .body(Mono.just(adminRequest), UsersRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isCreated();

        // get all admin users
        List<Users> allAdmins = webTestClient.get()
                .uri("/api/v1/admin")
                .accept(MediaType.APPLICATION_JSON)
                .headers(h -> h.setBearerAuth(jwt))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Users>() {
                })
                .returnResult()
                .getResponseBody();

        // get admin user from users
        Users adminUser = allAdmins.stream()
                .filter(user -> user.getEmail().equals(adminRequest.email()))
                .findFirst()
                .orElseThrow();

        // make blood donation event request
        String eventName = faker.company().name().toString();
        BloodDonationEventRequest bloodDonationEventRequest = new BloodDonationEventRequest(
                eventName,
                LocalDate.now().minusYears(1L),
                "ONeg",
                10,
                adminUser
        );

        // send a post request for registering new event
        webTestClient.post()
                .uri(donationEventURI + "/donation_event")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .headers(h -> h.setBearerAuth(jwt))
                .body(Mono.just(bloodDonationEventRequest), BloodDonationEventRequest.class)
                .exchange()
                .expectStatus()
                .isEqualTo(HttpStatusCode.valueOf(406));
    }
}
