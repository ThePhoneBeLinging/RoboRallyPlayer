package dk.dtu.compute.se.pisd.roborally.model.BoardElements;

import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import javafx.scene.image.Image;

/**
 * @author Emil
 */
public class PushPanel extends BoardElement
{
    public PushPanel(Heading heading, Space space)
    {
        super(heading, true, space);
        space.board.addBoardElement(Board.PUSH_PANELS_INDEX, this);
        setImage(new Image("file:src/main/resources/images/push135.png"));
    }


}
