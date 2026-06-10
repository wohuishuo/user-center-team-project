package com.usercenter;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * 接口集成测试:走完整的注册 → 登录 → 取当前用户链路(连真实 MySQL)。
 * 对应网页书:理论篇 / 软件测试(集成测试)、实战篇 / 测试。
 */
@SpringBootTest
@AutoConfigureMockMvc
class UserApiIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper om;

    @Test
    void registerLoginCurrent_happyPath() throws Exception {
        String suffix = String.valueOf(System.nanoTime() % 100000000L);
        String account = "it_" + suffix;
        String planet = "pl" + suffix;

        // 1. 注册
        String regBody = om.writeValueAsString(Map.of(
                "userAccount", account, "userPassword", "password123",
                "checkPassword", "password123", "planetCode", planet));
        mvc.perform(post("/user/register").contentType(MediaType.APPLICATION_JSON).content(regBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data").isNumber());

        // 2. 登录,拿到 token
        String loginBody = om.writeValueAsString(Map.of("userAccount", account, "userPassword", "password123"));
        String resp = mvc.perform(post("/user/login").contentType(MediaType.APPLICATION_JSON).content(loginBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.token").isNotEmpty())
                .andReturn().getResponse().getContentAsString();
        String token = om.readTree(resp).at("/data/token").asText();
        assertThat(token).isNotBlank();

        // 3. 带 token 取当前用户,且响应已脱敏(不含 userPassword)
        mvc.perform(get("/user/current").header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(0))
                .andExpect(jsonPath("$.data.userAccount").value(account))
                .andExpect(jsonPath("$.data.userPassword").doesNotExist());
    }

    @Test
    void current_withoutToken_shouldBeForbidden() throws Exception {
        mvc.perform(get("/user/current"))
                .andExpect(status().isForbidden());
    }

    @Test
    void register_withMismatchedPasswords_shouldReturnParamError() throws Exception {
        String body = om.writeValueAsString(Map.of(
                "userAccount", "it_mismatch", "userPassword", "password123",
                "checkPassword", "password999", "planetCode", "plmis"));
        mvc.perform(post("/user/register").contentType(MediaType.APPLICATION_JSON).content(body))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(40000)); // PARAMS_ERROR
    }
}
