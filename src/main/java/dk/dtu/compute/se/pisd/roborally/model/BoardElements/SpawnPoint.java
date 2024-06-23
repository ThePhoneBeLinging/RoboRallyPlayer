package dk.dtu.compute.se.pisd.roborally.model.BoardElements;

import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import javafx.scene.image.Image;

/**
 * This class represents a spawn point on the board.
 * A spawn point is a point on the board where a player can spawn in the beginning of the game
 * The spawnpoints look like any other empty field, but is used by the logic of the game to decide
 * where a player must start the game
 *
 * @author Frederik
 */

public class SpawnPoint extends NullBoardElement {

    public SpawnPoint(Space space) {
        super(space);
        space.board.addBoardElement(Board.NOT_ACTIVATE_ABLE_INDEX, this);
    }
}
