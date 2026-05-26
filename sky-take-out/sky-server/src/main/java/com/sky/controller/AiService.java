package com.sky.controller;


public interface AiService {

    /**
     * 根据商家提问获取 AI 经营建议
     * @param query 商家输入的咨询内容
     * @param userId 商家ID（用于 Dify 会话隔离）
     * @return AI 返回的文本答案
     */
    String getManagementAdvice(String query, String userId);

}