package com.example.cardgame.client.response.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
public class RoomResponse {
    private String name;
    private UUID uuid;
    private int playersCount;
    private int maxPlayersCount;

    @Override
    public String toString() {
        return "%s \t(%s) \t %s/%s".formatted(name, uuid.toString(), playersCount, maxPlayersCount);
    }
}
