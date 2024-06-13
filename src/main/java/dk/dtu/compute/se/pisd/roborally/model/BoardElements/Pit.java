package dk.dtu.compute.se.pisd.roborally.model.BoardElements;

import dk.dtu.compute.se.pisd.roborally.controller.SoundController;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import javafx.scene.image.Image;


/**
 * @Author Emil
 */
public class Pit extends NullBoardElement
{
    public Pit(Space space)
    {
        super(space);
        space.board.addBoardElement(Board.NOT_ACTIVATE_ABLE_INDEX, this);
        this.setImage(new Image("file:src/main/Resources/Images/pit.png"));
    }

    @Override
    public void activate()
    {
        Player player = this.getSpace().getPlayer();
        if (player != null)
        {
            player.die();
        }
    }


    @Override
    public void onWalkOver(Player player)
    {
        if (player == null)
        {
            return;
        }
        if (!player.checkIfOwnsUpgradeCard("HOVER UNIT"))
        {
            SoundController.getInstance().playSound("falling");
            player.die();
        }
    }
}
