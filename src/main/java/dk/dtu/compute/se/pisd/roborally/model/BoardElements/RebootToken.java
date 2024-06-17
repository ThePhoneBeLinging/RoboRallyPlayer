package dk.dtu.compute.se.pisd.roborally.model.BoardElements;

import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import javafx.scene.image.Image;

/**
 * @author Elias, Adel & Frederik
 */
public class RebootToken extends BoardElement
{

    public RebootToken(Heading heading, Space space)
    {
        super(heading, true, space);
        space.board.addBoardElement(Board.NOT_ACTIVATE_ABLE_INDEX, this);
        setImage(new Image("file:src/main/resources/images/respawn.png"));
    }

    public void reboot(Player player)
    {
    }
}
