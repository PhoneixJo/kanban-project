package com.example.prello.attachment.controller;

import com.example.prello.attachment.dto.AttachmentResponseDto;
import com.example.prello.attachment.service.AttachmentService;
import com.example.prello.config.WebConfig;
import com.example.prello.util.Mapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(
        value = AttachmentController.class,
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebConfig.class)}
)
class AttachmentControllerTest {

    @MockitoBean
    AttachmentService attachmentService;

    @Autowired
    private MockMvc mockMvc;

//    @Test
//    @DisplayName("첨부파일 등록")
//    void createSuccessTest() throws Exception {
//        Mapper<AttachmentResponseDto> mapper = new Mapper<>();
}
