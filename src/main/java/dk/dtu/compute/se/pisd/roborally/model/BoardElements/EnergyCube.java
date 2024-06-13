package dk.dtu.compute.se.pisd.roborally.model.BoardElements;

import dk.dtu.compute.se.pisd.roborally.controller.SoundController;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import javafx.scene.image.Image;

/**
 * This class represents an energy cube on the board.
 * When a player lands on a space with an energy cube, the player picks up the energy cube.
 * The energy cube should then be removed from the space.
 *
 * @author Emil
 */

public class EnergyCube extends NullBoardElement
{
    public EnergyCube(Space space)
    {
        super(space);
        space.board.addBoardElement(Board.ENERGY_SPACE_INDEX, this);
        setImage(new Image("file:src/main/resources/images/energyCube.png"));
    }

    /**
     * @author Elias & Emil
     */
    @Override
    public void activate()
    {
        Player playerOnSquare = this.getSpace().getPlayer();
        if (playerOnSquare != null)
        {
            this.getSpace().getPlayer().pickUpEnergyCube();
            this.getSpace().setBoardElement(new NullBoardElement(this.getSpace()));
            SoundController sc = SoundController.getInstance();
            sc.playSound("pickup");
        }

    }
}
