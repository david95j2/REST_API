package com.example.restapi.websocket;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@Component
@Log4j2
public class MyWebSocketHandler extends TextWebSocketHandler {
    private static List<WebSocketSession> list = new ArrayList<>();

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
//        log.info("payload : " + payload);
//        String[] cmd = { "C:\\Users\\jylee\\Desktop\\code\\intellij\\websocket_test\\bin\\test_1.exe" };
//
//        try {
//            ProcessBuilder process_builder = new ProcessBuilder(cmd);
//            Process process = process_builder.start();
//            //process.waitFor();
//            try (InputStream psout = process.getInputStream()) {
//                byte[] buffer = new byte[1024];
//                int n = 0;
//                while((n = psout.read(buffer)) != -1) {
//                    System.out.write(buffer, 0, n);
//                }
//            }
//        }catch(IOException e1) {
//            log.error(e1);
//            //}catch(InterruptedException e2) {
//            //     log.error(e2);
//        }

        //if(session.getUri().equals("ws://localhost:8080/broadcast")) {
        for (WebSocketSession sess : list) {
            sess.sendMessage(message);
        }
        //}
        //else
        //   session.sendMessage(new TextMessage("reply: " + payload));
        log.info(session.getUri());
    }

    /* Client가 접속 시 호출되는 메서드 */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {

        list.add(session);

        log.info(session + " 클라이언트 접속");
    }

    /* Client가 접속 해제 시 호출되는 메서드드 */

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

        log.info(session + " 클라이언트 접속 해제");
        list.remove(session);
    }
}
