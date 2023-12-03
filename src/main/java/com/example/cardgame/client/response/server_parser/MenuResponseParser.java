package com.example.cardgame.client.response.server_parser;

import com.example.cardgame.client.response.model.RoomResponse;
import com.example.cardgame.properties.ServerProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MenuResponseParser {
    public static List<RoomResponse> parseGetAvailableRooms(String message) {
        List<String> split = List.of(message.split(ServerProperties.getMainDelimiter()));
        List<RoomResponse> ans = new ArrayList<>();
        for (String entity : split.subList(1, split.size())) {
            String[] singleEntity = entity.split(ServerProperties.getSideDelimiter());
            ans.add(
                    RoomResponse.builder()
                            .uuid(UUID.fromString(singleEntity[0]))
                            .name(singleEntity[1])
                            .playersCount(Integer.parseInt(singleEntity[2]))
                            .maxPlayersCount(Integer.parseInt(singleEntity[3]))
                            .build()
            );
        }
        return ans;
    }
}
