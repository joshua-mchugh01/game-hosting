package com.example.demo.game.projection;

import com.example.demo.game.aggregate.event.GameCreatedEvent;
import com.example.demo.game.entity.GameType;
import com.example.demo.game.entity.service.IGameService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;
import java.util.UUID;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class GameProjectorExistsAnyTest {

    @Autowired
    private IGameProjector gameProjection;

    @Autowired
    private IGameService gameService;

    @Test
    public void whenNoEntitiesThenExistsAllReturnFalse() {

        boolean exists = gameProjection.existsAny();

        Assertions.assertFalse(exists);
    }

    @Test
    public void whenEntitiesThenExistsAllReturnTrue() {

        GameCreatedEvent event = GameCreatedEvent.builder()
                .id(UUID.randomUUID())
                .type(GameType.MINECRAFT_JAVA)
                .build();

        gameService.handleCreated(event);

        boolean exists = gameProjection.existsAny();

        Assertions.assertTrue(exists);
    }
}
