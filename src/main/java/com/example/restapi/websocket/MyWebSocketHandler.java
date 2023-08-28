package com.example.restapi.websocket;

import com.example.restapi.websocket.domain.WebSocketDto;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.session.StandardSession;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Component
@Slf4j
public class MyWebSocketHandler extends TextWebSocketHandler {
    private static List<WebSocketSession> list = new ArrayList<>();

    private Process currentProcess = null;  // 현재 실행 중인 프로세스 참조를 저장
//    @Override
//    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//        String payload = message.getPayload();
//        ObjectMapper mapper = new ObjectMapper();
//        int maxInactiveInterval = (int) session.getAttributes().get("maxInactiveInterval");
//        session.sendMessage(new TextMessage("Max Inactive Interval: " + maxInactiveInterval));
//        try {
//            WebSocketDto webSocketDto = mapper.readValue(payload, WebSocketDto.class);
//            if ("exit".equals(webSocketDto.getCmd())) {
//                if (currentProcess != null) {
//                    currentProcess.destroy();  // 현재 실행 중인 프로세스 종료
//                    currentProcess = null;  // 참조 초기화
//                    session.sendMessage(new TextMessage("Process terminated!"));
//                }
//
//            } else {
//                String file_path = Paths.get(System.getProperty("user.dir"),"bin", webSocketDto.getName()).toString();
//                try {
//                    ProcessBuilder process_builder;
//                    if (webSocketDto.getLanguage().equals("python")) {
//                        process_builder = new ProcessBuilder("python",file_path, webSocketDto.getCmd());
//                    } else {
//                        process_builder = new ProcessBuilder(file_path, webSocketDto.getCmd());
//                    }
//                    process_builder.redirectInput(ProcessBuilder.Redirect.PIPE);
//                    process_builder.redirectOutput(ProcessBuilder.Redirect.PIPE);
//                    process_builder.redirectErrorStream(true);  // 표준 오류 출력을 표준 출력으로 리다이렉트합니다.
//                    Process process = process_builder.start();
//                    currentProcess = process;  // 프로세스 시작하고 참조 저장
//
//                    new Thread(() -> {
//                        try (InputStream psout = currentProcess.getInputStream()) {
//                            byte[] buffer = new byte[1024];
//                            int n = 0;
//                            while((n = psout.read(buffer)) != -1) {
//                                session.sendMessage(new TextMessage(new String(buffer, 0, n, StandardCharsets.UTF_8)));
//                            }
//                        } catch(IOException e) {
//                            log.error(e);
//                        }
//                    }).start();
//                } catch(IOException e1) {
//                    log.error(e1);
//                }
//            }
//
////            if(session.getUri().equals("ws://localhost:8080/broadcast")) {
////                for (WebSocketSession sess : list) {
////                    sess.sendMessage(message);
////                }
////            }
////            else {
////                session.sendMessage(new TextMessage("reply: " + payload));
////                log.info(session.getUri());
////            }
//
//            /* 여기 아래는 Fix */
//        } catch (JsonParseException e) {
//                session.sendMessage(new TextMessage("JsonParseException Error : check your json : "+payload));
//        } catch (UnrecognizedPropertyException e) {
//            session.sendMessage(new TextMessage("현재 Json 형태로만 입력할 수 있으며, " +
//                    "보낼 수 있는 인자는 String name, String language, " +
//                    "String cmd 입니다."));
//        }
//    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();

        for (WebSocketSession sess : list) {
            sess.sendMessage(message);
        }

        log.info(String.valueOf(session.getUri()));
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
