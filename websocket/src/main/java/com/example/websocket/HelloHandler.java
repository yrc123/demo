package com.example.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.io.IOException;
import java.net.URI;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class HelloHandler extends AbstractWebSocketHandler {
    @Autowired
    HashMap<WebSocketSession,String>sessions;
//    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss ");

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String s = sessions.get(session);
        TextMessage textMessage = new TextMessage("用户 "+s+" 说: " + message.getPayload());
        sendAll(textMessage);
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
        TextMessage join = new TextMessage(name + " 加入聊天室");
        sendAll(join);
    }

    private void sendAll(TextMessage textMessage){
        TextMessage send = new TextMessage(getTime() + textMessage.getPayload());
        for (Map.Entry<WebSocketSession, String> entry: sessions.entrySet()) {
            try {
                entry.getKey().sendMessage(send);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private String getTime(){
        return simpleDateFormat.format(System.currentTimeMillis());
    }
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
//        super.afterConnectionClosed(session, status);
        sessions.remove(session);
    }
}
