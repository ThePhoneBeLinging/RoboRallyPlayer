package dk.dtu.compute.se.pisd.roborally.model.BoardElements;

import dk.dtu.compute.se.pisd.roborally.controller.SoundController;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import javafx.scene.image.Image;

import static dk.dtu.compute.se.pisd.roborally.model.Command.SPAM;

/**
 * @author Elias & Mads
 */
public class BoardLaser extends BoardElement
{
    public BoardLaser(Space space, Heading heading)
    {
        super(heading, true, space);
        space.board.addBoardElement(Board.BOARD_LASER_INDEX, this);
        setImage(new Image("file:src/main/resources/images/laserStart.png"));
    }

    /**
     * @author Elias & Mads
     */
    @Override
    public void activate()
    {
        Heading headingToCheck = this.getHeading().next().next();
        Space spaceToCheck = this.getSpace();

        while (spaceToCheck != null)
        {
            if (spaceToCheck.getPlayer() != null)
            {
                spaceToCheck.getPlayer().addDamageCardToPile(SPAM, 1);
            }
            Space nextSpace = spaceToCheck.board.getNeighbour(spaceToCheck, headingToCheck);
            // We check if we were to hit a board element, and break if we do
            if (!spaceToCheck.getBoardElement().getCanWalkOutOf(headingToCheck) || nextSpace == null || !nextSpace.getBoardElement().getCanWalkInto(headingToCheck))
            {
                break;
            }
            spaceToCheck = nextSpace;
        }
        if (this.getSpace().board.isStepMode())
        {
            SoundController sc = SoundController.getInstance();
            sc.playSound("laser_sound");
        }
    }
}
