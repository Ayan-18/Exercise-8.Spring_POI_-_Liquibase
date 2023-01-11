package com.example.exerciseeight.controller;

import com.example.exerciseeight.ExerciseEightApplication;
import com.example.exerciseeight.service.MeterService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(classes = ExerciseEightApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MeterControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private MeterRestController meterRestController;
    @MockBean
    private MeterService meterService;

    @Test
    void testReport() throws Exception {
        this.mockMvc.perform(
                        get("/api/v1.0/report"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
