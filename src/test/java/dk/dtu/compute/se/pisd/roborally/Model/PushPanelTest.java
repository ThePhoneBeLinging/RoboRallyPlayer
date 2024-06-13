package dk.dtu.compute.se.pisd.roborally.Model;

import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.controller.MoveController;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.BoardElements.BoardElement;
import dk.dtu.compute.se.pisd.roborally.model.BoardElements.PushPanel;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import javafx.application.Platform;
import org.junit.jupiter.api.*;

/**
 * Test class for the push panel element.
 *
 * @Author Emil
 */
public class PushPanelTest
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

        new PushPanel(Heading.WEST, board.getSpace(1, 2));
        board.setCurrentPlayer(board.getPlayer(1));
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
        BoardElement pushPanel = board.getSpace(1, 2).getBoardElement();
        Player player = board.getCurrentPlayer();
        player.moveController.moveForward(player);
        pushPanel.activate();
        Assertions.assertEquals(board.getSpace(2, 2), player.getSpace());
        Assertions.assertEquals(Heading.SOUTH, player.getHeading());
        Assertions.assertNull(board.getSpace(1, 2).getPlayer());
    }

    @Test
    void walkInFromInvalidDirection()
    {
        Board board = gameController.board;
        BoardElement pushPanel = board.getSpace(1, 2).getBoardElement();
        Player player = board.getCurrentPlayer();
        player.setSpace(board.getSpace(0, 2));
        player.setHeading(Heading.EAST);
        player.moveController.moveForward(player);
        pushPanel.activate();
        Assertions.assertEquals(board.getSpace(0, 2), player.getSpace());
        Assertions.assertEquals(Heading.EAST, player.getHeading());
    }
}
