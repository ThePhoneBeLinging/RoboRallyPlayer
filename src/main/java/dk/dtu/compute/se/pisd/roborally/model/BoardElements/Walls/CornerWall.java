package dk.dtu.compute.se.pisd.roborally.model.BoardElements.Walls;

import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.BoardElements.BoardElement;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import javafx.scene.image.Image;

/**
 * @author Elias, Frederik & Adel
 */
public class CornerWall extends BoardElement
{
    public final Heading heading2;

    public CornerWall(Heading heading1, Heading heading2, Space space)
    {
        super(heading1, true, space);
        if (heading1.next().next() == heading2)
        {
            throw new IllegalArgumentException("The two headings must be adjacent");
        }
        else
        {
            this.heading2 = heading2;
            space.board.addBoardElement(Board.NOT_ACTIVATE_ABLE_INDEX, this);
            if (heading1.next() == heading2)
            {
                setImage(new Image("file:src/main/resources/Images/cornerWall.png"));
            }
            else
            {
                setImage(new Image("file:src/main/resources/Images/cornerWall2.png"));
            }
        }

    }

    public Heading getHeading2()
    {
        return heading2;
    }
    
}
