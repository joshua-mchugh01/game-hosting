package com.example.demo.game.projection.model;

import com.example.demo.game.entity.GameType;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Value;

@Value
public class AdminGameServerGameProjection {

    String id;
    GameType type;

    @QueryProjection
    public AdminGameServerGameProjection(String id, GameType type) {

        this.id = id;
        this.type = type;
    }
}
