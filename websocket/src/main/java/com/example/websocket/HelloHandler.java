package com.example.websocket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
    HashMap<String, HashMap<String, WebSocketSession >> roomMap;
//    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss ");

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        URI uri = session.getUri();
        String url = uri.toString();
        String name = getParameter("name",url);
        String room = getParameter("room",url);
        TextMessage textMessage = new TextMessage("用户 "+name+" 说: " + message.getPayload());
        sendAll(room,textMessage);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//        super.afterConnectionEstablished(session);
        URI uri = session.getUri();
        String url = uri.toString();
        String name = getParameter("name",url);
        String room = getParameter("room",url);
        TextMessage textMessage = new TextMessage("你的昵称为：" + name);
        if(!roomMap.containsKey(room)){
            roomMap.put(room,new HashMap<>());
        }
        HashMap<String, WebSocketSession> nameMap = roomMap.get(room);
        nameMap.put(name,session);
//        sessions.put(session,name);
        session.sendMessage(textMessage);
        TextMessage join = new TextMessage(name + " 加入聊天室 "+room);
        sendAll(room,join);
    }

    private void sendAll(String room,TextMessage textMessage){
        TextMessage send = new TextMessage(getTime() + textMessage.getPayload());
        HashMap<String,WebSocketSession> sessions= roomMap.get(room);
        for (Map.Entry<String,WebSocketSession> entry: sessions.entrySet()) {
            try {
                entry.getValue().sendMessage(send);
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
        URI uri = session.getUri();
        String url = uri.toString();
        String name = getParameter("name",url);
        String room = getParameter("room",url);
        roomMap.get(room).remove(name);
    }
    private String getParameter(String key,String url){
        String pattern="(?<="+key+"=).*?(?=&|$)";
        Pattern compile = Pattern.compile(pattern);
        Matcher matcher = compile.matcher(url);
        String res = null;
        if(matcher.find()){
            res=matcher.group(0);
            try {
                res= URLDecoder.decode(res,"utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return  res;
    }

}
