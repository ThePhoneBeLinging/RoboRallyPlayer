package dk.dtu.compute.se.pisd.roborally.Model;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.controller.MoveController;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.BoardElements.Walls.Wall;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import javafx.application.Platform;
import org.junit.jupiter.api.*;

/**
 * Test class for the wall element.
 *
 * @Author Emil
 */
public class WallTest
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
            player.setHeading(Heading.SOUTH);
        }

        new Wall(Heading.NORTH, board.getSpace(1, 2));
        new Wall(Heading.WEST, board.getSpace(0, 1));
        new Wall(Heading.SOUTH, board.getSpace(0, 2));
        board.setCurrentPlayer(board.getPlayer(0));
    }

    @AfterEach
    void tearDown()
    {
        gameController = null;
    }

    @Test
    void walkInFromValidDirection()
    {
        Board board = gameController.board;
        Player player = board.getCurrentPlayer();
        player.moveController.moveForward(player);
        Assertions.assertEquals(board.getSpace(0, 1), player.getSpace());
    }

    @Test
    void walkOutFromValidDirection()
    {
        Board board = gameController.board;
        Player player = board.getCurrentPlayer();
        player.moveController.moveForward(player);
        player.moveController.moveForward(player);
        Assertions.assertEquals(board.getSpace(0, 2), player.getSpace());
    }

    @Test
    void walkInFromInvalidDirection()
    {
        Board board = gameController.board;
        board.setCurrentPlayer(board.getPlayer(1));
        Player player = board.getCurrentPlayer();
        player.moveController.moveForward(player);
        Assertions.assertEquals(board.getSpace(1, 1), player.getSpace());
    }

    @Test
    void walkOutFromInvalidDirection()
    {
        Board board = gameController.board;
        board.setCurrentPlayer(board.getPlayer(0));
        Player player = board.getCurrentPlayer();
        player.setSpace(board.getSpace(0, 2));
        player.moveController.moveForward(player);
        player.moveController.moveForward(player);
        Assertions.assertEquals(board.getSpace(0, 2), player.getSpace());
    }
}
