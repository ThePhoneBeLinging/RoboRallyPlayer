package dk.dtu.compute.se.pisd.roborally.model.BoardElements;

import dk.dtu.compute.se.pisd.roborally.controller.SoundController;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import javafx.scene.image.Image;

public class Galaxy extends Pit {

    public Galaxy(Space space)
    {
        super(space);
        this.setImage(new Image("file:src/main/Resources/Images/galaxy.png"));
    }

}