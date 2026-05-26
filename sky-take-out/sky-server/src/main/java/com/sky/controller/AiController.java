package com.sky.controller;

import com.sky.context.BaseContext;
import com.sky.result.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/admin/ai")
@Tag(name = "AI管理助手接口")
public class AiController {

    @Autowired
    private AiService aiService;

    @PostMapping("/chat")
    @Operation(summary = "商家经营咨询")
    public Result<String> chat(@RequestBody String query) {
        // 在苍穹外卖中，可以通过 BaseContext 获取当前登录商家 ID
        Long currentId = BaseContext.getCurrentId();
        String userId = (currentId != null) ? currentId.toString() : "anonymous_merchant";
        String answer = aiService.getManagementAdvice(query, userId);

        return Result.success(answer);
    }
}