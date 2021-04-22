package com.example.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.websocket.Session;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.HashSet;

@SpringBootApplication
@EnableScheduling
public class WebsocketApplication {

    public static void main(String[] args) {
        SpringApplication.run(WebsocketApplication.class, args);
    }
    @Bean
    public HashMap<String, HashMap<String, WebSocketSession >>sessionMap(){
        HashMap<String, HashMap<String, WebSocketSession >> room = new HashMap<>();
        return room;
    }
//    @Autowired
//    HashSet<WebSocketSession>sessions;
//    @Scheduled(fixedRate = 2000)
//    public void sendMessage(){
//        System.out.println("连接人数："+sessions.size());
//        int cnt=0;
//        for (WebSocketSession session : sessions) {
//            TextMessage textMessage = new TextMessage("say hello" + cnt + " at " + System.currentTimeMillis());
//            try {
//                session.sendMessage(textMessage);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            cnt++;
//        }
//    }

}
