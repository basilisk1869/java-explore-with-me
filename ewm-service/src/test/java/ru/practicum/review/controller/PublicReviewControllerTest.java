package ru.practicum.review.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase
public class PublicReviewControllerTest extends BaseReviewControllerTest {

    public PublicReviewControllerTest(@Autowired ObjectMapper objectMapper, @Autowired MockMvc mockMvc) {
        super(objectMapper, mockMvc);
    }

}
