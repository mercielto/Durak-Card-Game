package com.example.cardgame.server.model.request;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Builder
@Getter
public class RoomRequest {
    private String name;
    private int maxPlayersCount;
}
