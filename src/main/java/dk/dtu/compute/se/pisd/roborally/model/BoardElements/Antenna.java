package dk.dtu.compute.se.pisd.roborally.model.BoardElements;

import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import javafx.scene.image.Image;

/**
 * @author Elias
 */
public class Antenna extends NullBoardElement
{
    private Player[] players;

    public Antenna(Space space)
    {
        super(false, space);
        this.setImage(new Image("file:src/main/Resources/Images/antenna.png"));
        space.board.addBoardElement(Board.ANTENNA_INDEX, this);
    }
}
