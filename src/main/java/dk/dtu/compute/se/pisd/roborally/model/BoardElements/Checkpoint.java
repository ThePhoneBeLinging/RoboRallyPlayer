package dk.dtu.compute.se.pisd.roborally.model.BoardElements;

import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import javafx.scene.image.Image;

/**
 * @author Elias & Adel
 */
public class Checkpoint extends NullBoardElement
{
    public Checkpoint(Space space)
    {
        super(space);
        space.board.addBoardElement(Board.CHECKPOINTS_INDEX, this);
        int index = space.board.getIndexOfCheckPoint(this);
        setImage(new Image("file:src/main/resources/images/" + (index + 1) + ".png"));
    }

    /**
     * @author Elias
     */

}
