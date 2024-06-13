package dk.dtu.compute.se.pisd.roborally.Model;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.controller.MoveController;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.BoardElements.Antenna;
import dk.dtu.compute.se.pisd.roborally.model.BoardElements.BoardElement;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import javafx.application.Platform;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class AntennaTest
{
    private final int TEST_WIDTH = 8;
    private final int TEST_HEIGHT = 8;

    private GameController gameController;
    private MoveController moveController;

    @BeforeEach
    void setUp()
    {
        Platform.startup(() -> {
        });
        Board board = new Board(TEST_WIDTH, TEST_HEIGHT);
        gameController = new GameController(board);
        moveController = new MoveController(gameController);

        for (int i = 0; i < 2; i++)
        {
            Player player = new Player(board, null, "Player " + i, moveController);
            board.addPlayer(player);
            player.setSpace(board.getSpace(i, i));
            player.setHeading(Heading.values()[i % Heading.values().length]);
        }

        new Antenna(board.getSpace(5, 5));
    }

    @AfterEach
    void tearDown()
    {
        gameController = null;
    }

    @Test
    void activate()
    {
        Board board = gameController.board;
        Player player = board.getPlayer(1);
        BoardElement antenna = board.getSpace(5, 5).getBoardElement();
        antenna.activate();
        Assertions.assertEquals(player, board.getPlayer(0));
    }
}
