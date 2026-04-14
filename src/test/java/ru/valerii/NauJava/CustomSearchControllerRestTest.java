package ru.valerii.NauJava;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.valerii.NauJava.dto.TransactionDto;
import ru.valerii.NauJava.service.SearchService;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
class CustomSearchControllerRestTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SearchService searchService;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.mockMvc(mockMvc);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldReturnTransactionsWhenAuthorized() {
        TransactionDto dto = new TransactionDto();
        dto.setId(1L);
        dto.setAmount(new BigDecimal("500.00"));
        dto.setDescription("Зарплата");

        when(searchService.findTransactionsByEmail(anyString())).thenReturn(List.of(dto));

        RestAssuredMockMvc.given()
                .param("email", "test@test.com")
                .when()
                .get("/api/custom/transactions/by-email")
                .then()
                .statusCode(200)
                .body("[0].id", equalTo(1))
                .body("[0].amount", equalTo(500.00f))
                .body("[0].description", equalTo("Зарплата"));
    }

    @Test
    void shouldRedirectToLoginWhenUnauthorized() {
        RestAssuredMockMvc.given()
                .param("email", "test@test.com")
                .when()
                .get("/api/custom/transactions/by-email")
                .then()
                .statusCode(302);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldReturnBadRequestWhenEmailParameterIsMissing() {
        RestAssuredMockMvc.given()
                .when()
                .get("/api/custom/transactions/by-email")
                .then()
                .statusCode(400);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void shouldReturnEmptyListWhenNoTransactionsFoundForEmail() {

        when(searchService.findTransactionsByEmail(anyString()))
                .thenReturn(Collections.emptyList());

        RestAssuredMockMvc.given()
                .param("email", "ghost@test.com")
                .when()
                .get("/api/custom/transactions/by-email")
                .then()
                .statusCode(200)
                .body("size()", equalTo(0));
    }
}