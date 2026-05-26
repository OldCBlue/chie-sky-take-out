package com.sky.test;

import com.sky.controller.AiService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = com.sky.SkyApplication.class, properties = {"spring.main.web-application-type=servlet"})
class AiServiceTest {

    @Autowired
    private AiService aiService;

    @Test
    void testAiChat() {
        String answer = aiService.getManagementAdvice("目前店里 20 元以下的蔬菜有哪些？", "admin-001");
        System.out.println("AI 回复内容：" + answer);

        Assertions.assertNotNull(answer);
        Assertions.assertTrue(answer.contains("18"));
    }
}
