package dk.dtu.compute.se.pisd.roborally.model.BoardElements;

import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import javafx.scene.image.Image;


/**
 * @Author Emil
 */
public class Pit extends NullBoardElement
{
    public Pit(Space space)
    {
        super(space);
        space.board.addBoardElement(Board.NOT_ACTIVATE_ABLE_INDEX, this);
        this.setImage(new Image("file:src/main/Resources/Images/pit.png"));
    }


}
