package dk.dtu.compute.se.pisd.roborally.model.BoardElements;

import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Space;

/**
 * @author Elias
 */
public class NullBoardElement extends BoardElement
{
    public NullBoardElement(Space space)
    {
        this(true, space);
    }

    public NullBoardElement(boolean isWalkable, Space space)
    {
        super(Heading.NORTH, isWalkable, space);
    }

    public NullBoardElement(Heading heading, Space space)
    {
        super(heading, true, space);
    }

    /**
     * @param heading
     * @return
     * @author Elias
     */
    @Override
    public boolean getCanWalkOutOf(Heading heading)
    {
        return this.getIsWalkable();
    }

    /**
     * @param heading
     * @return
     * @author Elias
     */
    @Override
    public boolean getCanWalkInto(Heading heading)
    {
        return this.getIsWalkable();
    }
}
