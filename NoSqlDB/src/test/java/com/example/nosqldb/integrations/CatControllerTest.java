package com.example.nosqldb.integrations;

import com.example.nosqldb.configs.MongoTestConfiguration;
import com.example.nosqldb.restclients.cat.CatServiceClient;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@WireMockTest
@ContextConfiguration(classes = {MongoTestConfiguration.class})
@AutoConfigureMockMvc
@TestPropertySource(properties = { "app.remote-cat-service.url=http://localhost:3000" })
public class CatControllerTest {

    WireMockServer wireMockServer;

    @Autowired
    private CatServiceClient catServiceClient;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        wireMockServer = new WireMockServer(new WireMockConfiguration().port(3000));
        wireMockServer.start();

        WireMock.configureFor("localhost", wireMockServer.port());
        stubFor(get(urlEqualTo("/fact"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"fact\":\"Cats wire mock works fine!\"}")));
    }

    @AfterEach
    void tearDown() {
        wireMockServer.stop();
    }

    @Test
    void getCatFactTest() {
        String response = catServiceClient.getCatFact().getBody();
        assertEquals("{\"fact\":\"Cats wire mock works fine!\"}", response);
        assertEquals(HttpStatus.OK, catServiceClient.getCatFact().getStatusCode());
    }

    @Test
    void getCatFactEndpointTest() throws Exception {
        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/api/cat/fact"))
                .andExpect(status().isOk())
                .andExpect(content().string("{\"fact\":\"Cats wire mock works fine!\"}"));
    }
}
