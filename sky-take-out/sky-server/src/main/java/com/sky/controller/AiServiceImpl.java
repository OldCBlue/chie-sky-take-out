package com.sky.controller;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class AiServiceImpl implements AiService {

    // 在 application.yml 中配置
    private String apiKey = "app-";

    private final String DIFY_API_URL = "https://api.dify.ai/v1/chat-messages";

    public String getManagementAdvice(String query, String userId) {
        RestTemplate restTemplate = new RestTemplate();

        // 1. 设置请求头
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        // 2. 构建请求体 (根据 Dify 官方文档)
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("inputs", new HashMap<>()); // 这里的 inputs 对应 Dify 里的变量
        requestBody.put("query", query);
        requestBody.put("response_mode", "blocking"); // 阻塞模式直接获取结果
        requestBody.put("user", userId);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        // 3. 发送请求
        try {
            ResponseEntity<DifyResponseDTO> response = restTemplate.postForEntity(DIFY_API_URL, entity, DifyResponseDTO.class);
            return response.getBody().getAnswer();
        } catch (Exception e) {
            e.printStackTrace(); // 这一行极其重要，它会在 IDEA 控制台输出真正的报错原因
            return "错误详情：" + e.getMessage();
        }
    }
}