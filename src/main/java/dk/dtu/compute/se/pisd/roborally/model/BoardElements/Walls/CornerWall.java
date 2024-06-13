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

    /**
     * @param heading
     * @return boolean that states if the player can walk out of the space that the corner wall is on
     * @author Frederik
     */
    @Override
    public boolean getCanWalkOutOf(Heading heading)
    {
        return getIsWalkable() && (getHeading() != heading && this.heading2 != heading);
    }

    /**
     * @param heading
     * @return boolean that states if the player can walk into the space
     * @author Frederik & Adel
     */
    @Override
    public boolean getCanWalkInto(Heading heading)
    {
        Heading headingToCheck = heading.next().next();

        return getIsWalkable() && (getHeading() != headingToCheck && this.heading2 != headingToCheck);
    }
}
