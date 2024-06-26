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
import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import javafx.scene.control.TabPane;

import java.util.Objects;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class PlayersView extends TabPane implements ViewObserver
{
    private final Board board;
    private final PlayerView[] playerViews;

    /**
     * @param gameController
     * @author Elias
     */
    public PlayersView(GameController gameController)
    {
        board = gameController.board;

        this.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);

        playerViews = new PlayerView[1];
        for (int k = 0; k < board.getPlayersNumber(); k++)
        {
            if (Objects.equals(board.getPlayer(k).getPlayerID(), board.getPlayerID()))
            {
                playerViews[0] = new PlayerView(gameController, board.getPlayer(k));
                this.getTabs().add(playerViews[0]);
                break;
            }
        }
        board.attach(this);
        update(board);
    }

    /**
     * @param subject
     * @author Elias
     */
    @Override
    public void updateView(Subject subject)
    {
        if (subject == board)
        {

        }
    }
}
