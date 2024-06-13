package dk.dtu.compute.se.pisd.roborally.Model;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.controller.MoveController;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.BoardElements.BoardElement;
import dk.dtu.compute.se.pisd.roborally.model.BoardElements.Checkpoint;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import javafx.application.Platform;
import org.junit.jupiter.api.*;

/**
 * Test class for the Checkpoint class.
 *
 * @Author Emil
 */
public class CheckpointTest
{
    private final int TEST_WIDTH = 8;
    private final int TEST_HEIGHT = 8;

    private GameController gameController;
    private MoveController moveController;

    @BeforeAll
    static void setUpBeforeClass()
    {
        Platform.startup(() -> {
        });
    }

    @BeforeEach
    void setUp()
    {
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

        new Checkpoint(board.getSpace(0, 1));
        new Checkpoint(board.getSpace(0, 3));
        new Checkpoint(board.getSpace(0, 2));
        board.setCurrentPlayer(board.getPlayer(0));
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
        BoardElement checkpoint = board.getSpace(0, 1).getBoardElement();
        Player player = board.getCurrentPlayer();
        int expectedLastVisitedCheckPoint = 1;
        player.moveController.moveForward(player);
        checkpoint.activate();
        Assertions.assertEquals(expectedLastVisitedCheckPoint, player.getLastVisitedCheckPoint());
    }

    @Test
    void collectInWrongOrder()
    {
        Board board = gameController.board;
        Player player = board.getCurrentPlayer();
        int expectedLastVisitedCheckPoint = 2;
        for (int i = 0; i < 3; i++)
        {
            player.moveController.moveForward(player);
            board.activateBoardElements();
        }
        Assertions.assertEquals(expectedLastVisitedCheckPoint, player.getLastVisitedCheckPoint());
    }
}
