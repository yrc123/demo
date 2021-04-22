package com.example.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class HelloHandler extends AbstractWebSocketHandler {
    @Autowired
    HashMap<WebSocketSession,String>sessions;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String s = sessions.get(session);
        TextMessage textMessage = new TextMessage(s + " 说: " + message.getPayload());
        for (Map.Entry<WebSocketSession, String> entry: sessions.entrySet()) {
            entry.getKey().sendMessage(textMessage);
        }
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//        super.afterConnectionEstablished(session);
        URI uri = session.getUri();
        String pattern="(?<=name=).*?(?=&|$)";
        Pattern compile = Pattern.compile(pattern);
        Matcher matcher = compile.matcher(uri.toString());
        String name = null;
        if(matcher.find()){
            name=matcher.group(0);
            name= URLDecoder.decode(name,"utf-8");
        }
        TextMessage textMessage = new TextMessage("你的昵称为：" + name);
        sessions.put(session,name);
        session.sendMessage(textMessage);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
//        super.afterConnectionClosed(session, status);
        sessions.remove(session);
    }
}
