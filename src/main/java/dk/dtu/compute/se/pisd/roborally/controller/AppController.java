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
package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.designpatterns.observer.Observer;
import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import dk.dtu.compute.se.pisd.roborally.APITypes.Lobby;
import dk.dtu.compute.se.pisd.roborally.RoboRally;
import dk.dtu.compute.se.pisd.roborally.view.JoinedLobbyView;
import dk.dtu.compute.se.pisd.roborally.view.LobbyView;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class AppController implements Observer
{
    final private RoboRally roboRally;
    private GameController gameController;

    /**
     * @param roboRally the RoboRally application
     * @author Elias
     */
    public AppController(@NotNull RoboRally roboRally)
    {
        this.roboRally = roboRally;
    }

    /**
     * @author Elias
     */
    public void loadGame()
    {
        // XXX needs to be implemented eventually
        // for now, we just create a new game
        //this.gameController = LoadSaveGameState.loadGameState("default");
        //roboRally.createBoardView(gameController);
    }

    public void startGameFromBoard(GameController gameController)
    {
        this.gameController = gameController;
        roboRally.createBoardView(gameController);
    }

    public void joinGame()
    {
        System.out.println("Join Pressed");
        LobbyView lobbyView = new LobbyView(this);
        roboRally.createLobbyView(lobbyView);
    }

    public void joinLobby(Lobby lobby)
    {
        roboRally.createJoinedLobbyView(new JoinedLobbyView(this, lobby));
    }

    /**
     * Exit the RoboRally application. If there is a game running, the user is asked whether the game should be closed
     *
     * @author Elias
     */
    public void exit()
    {
        if (gameController != null)
        {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Exit RoboRally?");
            alert.setContentText("Are you sure you want to exit RoboRally?");
            Optional<ButtonType> result = alert.showAndWait();

            if (!result.isPresent() || result.get() != ButtonType.OK)
            {
                return; // return without exiting the application
            }
            gameController.board.keepUpdatingBoard = false;
        }

        // If the user did not cancel, the RoboRally application will exit
        // after the option to save the game
        if (gameController == null || stopGame())
        {
            Platform.exit();
        }
    }

    /**
     * Stop playing the current game, giving the user the option to save
     * the game or to cancel stopping the game. The method returns true
     * if the game was successfully stopped (with or without saving the
     * game); returns false, if the current game was not stopped. In case
     * there is no current game, false is returned.
     *
     * @return true if the current game was stopped, false otherwise
     * @author Elias
     */
    public boolean stopGame()
    {
        if (gameController != null)
        {
            gameController.board.keepUpdatingBoard = false;
            // here we save the game (without asking the user).
            saveGame();
            gameController = null;
            roboRally.createBoardView(null);
            roboRally.createMainMenuView(this);
            return true;
        }
        return false;
    }

    /**
     * Saves the game
     *
     * @author Elias
     */
    public void saveGame()
    {
        // XXX needs to be implemented eventually
        //LoadSaveGameState.saveGameState(gameController, "default");
    }

    /**
     * @return true if gameController is not null, false otherwise
     * @author Elias
     */
    public boolean isGameRunning()
    {
        return gameController != null;
    }

    /**
     * @param subject
     * @author Elias
     */
    @Override
    public void update(Subject subject)
    {
        // XXX do nothing for now
    }
}
