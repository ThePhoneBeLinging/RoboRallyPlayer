/*
 *  This file is part of the initial project provided for the
 *  course "Project in Software Development (02362)" held at
 *  DTU Compute at the Technical University of Denmark.
 *
 *  Copyright (C) 2019, 2020: Ekkart Kindler, ekki@dtu.dk
 *
 *  This software is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; version 2 of the License.
 *
 *  This project is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this project; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class Card extends Subject
{

    public Command command;
    private Image image;

    /**
     * @param command the command represented by this card
     * @author Elias & Mustafa
     */
    public Card(@NotNull Command command)
    {

        this.command = command;
           this.image = getImageBasedOnCommand(command);
    }

    /**
     * @param command
     * @return
     * @author Elias, Mustafa & Frederik
     */
    protected Image getImageBasedOnCommand(Command command)
    {
        switch (command)
        {
            case FORWARD:
                return new Image("file:src/main/Resources/Images/move1.png");
            case FAST_FORWARD:
                return new Image("file:src/main/Resources/Images/move2.png");
            case LEFT:
                return new Image("file:src/main/Resources/Images/turnL.png");
            case RIGHT:
                return new Image("file:src/main/Resources/Images/turnR.png");
            case OPTION_LEFT_RIGHT:
                return new Image("file:src/main/Resources/Images/turnLorR.png");
            case SPAM:
                return new Image("file:src/main/Resources/Images/SPAM.png");
            case TROJAN_HORSE:
                return new Image("file:src/main/Resources/Images/TROJANH.png");
            case WORM:
                return new Image("file:src/main/Resources/Images/WORM.png");
            case VIRUS:
                return new Image("file:src/main/Resources/Images/VIRUS.png");
            case AGAIN:
                return new Image("file:src/main/Resources/Images/AGAIN.png");
            case MOVE_THREE:
                return new Image("file:src/main/Resources/Images/move3.png");
            case MOVE_BACK:
                return new Image("file:src/main/Resources/Images/moveBack.png");
            case U_TURN:
                return new Image("file:src/main/Resources/Images/uTurn.png");
            case POWER_UP:
                return new Image("file:src/main/Resources/Images/POWERUP.png");
            case OPTION_FORWARD_OR_NOT:
                return new Image("file:src/main/Resources/Images/moveOrNot.png");

            default:
                return null;
        }
    }

    /**
     * @param command
     * @param image
     * @author Mustafa
     */
    public Card(@NotNull Command command, Image image)
    {
        this.command = command;
        this.image = image;
    }

    /**
     * @return the name of the command represented by this card
     * @author Elias
     */
    public String getName()
    {
        return command.displayName;
    }

    /**
     * @return
     * @author Elias
     */
    public Image getImage()
    {
        return image;
    }

    /**
     * @param image
     * @author Mustafa
     */
    public void setImage(Image image)
    {
        if (image == null)
        {
            this.image = new Image("file:src/main/Resources/Images/facedownCard.png");
        }
        else
        {
            this.image = image;
        }
        this.notifyChange();
    }
}
