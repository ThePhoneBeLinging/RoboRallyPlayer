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
package dk.dtu.compute.se.pisd.roborally.view;

import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import org.jetbrains.annotations.NotNull;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class SpaceView extends StackPane implements ViewObserver
{

    final public static int SPACE_HEIGHT = 60; // 60; // 75;
    final public static int SPACE_WIDTH = 60;  // 60; // 75;

    public final Space space;

    private final ImageView imageView;
    private final ImageView playerImageView;


    /**
     * @param space
     * @author Elias
     */
    public SpaceView(@NotNull Space space)
    {
        this.space = space;
        this.imageView = new ImageView();
        this.imageView.setFitWidth(SPACE_WIDTH);
        this.imageView.setFitHeight(SPACE_HEIGHT);
        this.playerImageView = new ImageView();
        this.playerImageView.setFitWidth(SPACE_WIDTH);
        this.playerImageView.setFitHeight(SPACE_HEIGHT);
        // XXX the following styling should better be done with styles
        this.setPrefWidth(SPACE_WIDTH);
        this.setMinWidth(SPACE_WIDTH);
        this.setMaxWidth(SPACE_WIDTH);

        this.setPrefHeight(SPACE_HEIGHT);
        this.setMinHeight(SPACE_HEIGHT);
        this.setMaxHeight(SPACE_HEIGHT);

        if ((space.x + space.y) % 2 == 0)
        {
            this.setStyle("-fx-background-color: white;");
        }
        else
        {
            this.setStyle("-fx-background-color: black;");
        }

        // updatePlayer();

        // This space view should listen to changes of the space
        space.attach(this);
        update(space);
    }

    /**
     * @param subject
     * @author Elias
     */
    @Override
    public void updateView(Subject subject)
    {
        if (subject == this.space)
        {
            this.getChildren().clear();
            updateBoardElement();
            updatePlayer();
        }
    }

    /**
     * @author Adel
     */
    private void updateBoardElement()
    {
        this.getChildren().add(imageView);
        imageView.setImage(this.space.getBoardElement().getImage());
        if(this.space.getBoardElement().getHeading()==Heading.NORTH){
            imageView.setRotate(90);
        }if(this.space.getBoardElement().getHeading()==Heading.EAST){
            imageView.setRotate(180);
        }if(this.space.getBoardElement().getHeading()==Heading.SOUTH){
            imageView.setRotate(270);
        }
    }

    /**
     * @author Elias
     */
    private void updatePlayer()
    {

        Player player = space.getPlayer();
        if (player != null)
        {
            int tabNumber = player.getTabNumber() + 1;
            playerImageView.setImage(new Image("file:src/main/Resources/Images/r" + tabNumber + ".png"));
            playerImageView.setRotate(90 * player.getHeading().ordinal() % 360);
            this.getChildren().add(playerImageView);
        }
    }

}
