package dk.dtu.compute.se.pisd.roborally.model.BoardElements.Conveyors;

import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import javafx.scene.image.Image;

/**
 * @author Elias & Adel
 */
public class GreenConveyor extends Conveyor
{
    public GreenConveyor(Heading orientation, Space space)
    {
        super(orientation, 1, space);
        space.board.addBoardElement(Board.GREEN_CONVEYOR_INDEX, this);
        setImage(new Image("file:src/main/resources/images/green.png"));
    }
}
