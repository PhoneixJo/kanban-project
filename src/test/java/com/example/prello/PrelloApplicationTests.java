package com.example.prello;

import com.example.prello.notification.SlackService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PrelloApplicationTests {

    @Autowired
    private SlackService slackService;

    @Test
    void contextLoads() {
    }

}
