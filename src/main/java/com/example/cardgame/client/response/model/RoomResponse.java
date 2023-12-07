package com.example.cardgame.client.response.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;
import java.util.List;

@Getter
@Setter
@Builder
public class RoomResponse {
    private String name;
    private UUID uuid;
    private int playersCount;
    private int maxPlayersCount;
//    private List<String> players;

    @Override
    public String toString() {
        return "%s\t(%s)\t%s/%s".formatted(name, uuid.toString(), playersCount, maxPlayersCount);
    }

    public static RoomResponse parse(String text) {
        String[] parsed = text.split("\t");
        String[] count = parsed[2].split("/");
        return RoomResponse.builder()
                .name(parsed[0])
                .uuid(UUID.fromString(parsed[1].substring(1, parsed[1].length() - 1)))
                .playersCount(Integer.parseInt(count[0]))
                .maxPlayersCount(Integer.parseInt(count[1]))
                .build();
    }
}
