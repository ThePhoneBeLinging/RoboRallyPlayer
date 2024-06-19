package dk.dtu.compute.se.pisd.roborally.model.BoardElements.Conveyors;

import dk.dtu.compute.se.pisd.roborally.model.BoardElements.NullBoardElement;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Space;

/**
 * @author Elias
 */
public abstract class Conveyor extends NullBoardElement
{
    private final int moveAmount;

    Conveyor(Heading heading, int moveAmount, Space space)
    {
        super(heading, space);
        this.moveAmount = moveAmount;
    }
}
