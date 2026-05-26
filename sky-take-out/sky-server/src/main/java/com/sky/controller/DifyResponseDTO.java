package com.sky.controller;

import lombok.Data;

    @Data
    public class DifyResponseDTO {
        private String answer;
        private String conversation_id;
        private String created_at;
        // 论文加分项：保留这个字段可以展示 RAG 检索到的原始文档
        private Object metadata;
    }