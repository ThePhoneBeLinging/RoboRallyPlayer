package dk.dtu.compute.se.pisd.roborally.model.BoardElements.Walls;

import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.BoardElements.BoardElement;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import javafx.scene.image.Image;

/**
 * @author Elias & Adel
 */
public class Wall extends BoardElement
{
    public Wall(Heading heading, Space space)
    {
        super(heading, true, space);
        space.board.addBoardElement(Board.NOT_ACTIVATE_ABLE_INDEX, this);
        setImage(new Image("file:src/main/resources/Images/wall.png"));
    }
}
