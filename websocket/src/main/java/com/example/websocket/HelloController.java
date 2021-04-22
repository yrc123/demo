package com.example.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.WebSocketMessage;

@Controller
public class HelloController {
//    @Autowired
//    SimpMessageSendingOperations operations;
//    @MessageMapping("/hello")
//    public void hello(WebSocketMessage hello){
//        Message<Object> message = new Message<>() {
//            @Override
//            public Object getPayload() {
//                return hello.getPayload();
//            }
//
//            @Override
//            public MessageHeaders getHeaders() {
//                return null;
//            }
//        };
//        operations.send("ws_hello",message );
//    }
}
