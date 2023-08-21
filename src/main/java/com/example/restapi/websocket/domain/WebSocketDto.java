package com.example.restapi.websocket.domain;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
public class WebSocketDto {
    private String name;
    private String language;
    private String cmd;

}
