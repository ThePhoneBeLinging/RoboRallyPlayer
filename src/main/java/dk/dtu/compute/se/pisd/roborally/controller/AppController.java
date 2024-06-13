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
import dk.dtu.compute.se.pisd.roborally.RoboRally;
import dk.dtu.compute.se.pisd.roborally.fileaccess.LoadBoard;
import dk.dtu.compute.se.pisd.roborally.fileaccess.LoadSaveGameState;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.BoardElements.SpawnPoint;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import dk.dtu.compute.se.pisd.roborally.view.LobbyView;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import javafx.scene.control.Dialog;
import javafx.scene.control.ButtonBar;
import javafx.scene.layout.HBox;
import javafx.scene.image.ImageView;
import java.util.Map;
import java.util.HashMap;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class AppController implements Observer
{
    final private List<Integer> PLAYER_NUMBER_OPTIONS = Arrays.asList(2, 3, 4, 5, 6);
    final private List<String> PLAYER_COLORS = Arrays.asList("red", "green", "blue", "orange", "grey", "magenta");
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
        this.gameController = LoadSaveGameState.loadGameState("default");
        roboRally.createBoardView(gameController);
    }

    /**
     * Start a new game. The user is asked to select the number of players.
     *
     * @author Elias & Emil
     */
    public void newGame()
    {
        Stage primStage = roboRally.getStage();

        ChoiceDialog<Integer> dialog = new ChoiceDialog<>(PLAYER_NUMBER_OPTIONS.get(0), PLAYER_NUMBER_OPTIONS);
        dialog.setTitle("Player number");
        dialog.setHeaderText("Select number of players");
        dialog.initOwner(primStage);
        Optional<Integer> result = dialog.showAndWait();

        if (result.isPresent())
        {
            if (gameController != null)
            {
                // The UI should not allow this, but in case this happens anyway.
                // give the user the option to save the game or abort this operation!
                if (!stopGame())
                {
                    return;
                }
            }
            Map<String, Image> mapImages = new HashMap<>();
            mapImages.put("dizzyHighway", new Image("file:src/main/resources/Images/dizzyHighway.png"));
            mapImages.put("mallfunctionMayhem", new Image("file:src/main/resources/Images/mallfunctionMayhem.png"));
            mapImages.put("riskyCrossing", new Image("file:src/main/resources/Images/riskyCrossing.png"));
            mapImages.put("chopShopChallenge", new Image("file:src/main/resources/Images/chopShopChallenge.png"));


            Dialog<String> mapDialog = new Dialog<>();
            mapDialog.initOwner(primStage);
            mapDialog.setTitle("Map Selection");
            mapDialog.setHeaderText("Select a map");

            ButtonType buttonTypeOk = new ButtonType("Okay", ButtonBar.ButtonData.OK_DONE);
            mapDialog.getDialogPane().getButtonTypes().add(buttonTypeOk);

            HBox box = new HBox();
            box.setSpacing(10);

            for (Map.Entry<String, Image> entry : mapImages.entrySet()) {
                ImageView imageView = new ImageView(entry.getValue());
                imageView.setFitHeight(200);
                imageView.setPreserveRatio(true);
                imageView.setOnMouseClicked(e -> mapDialog.setResult(entry.getKey()));
                box.getChildren().add(imageView);
            }

            mapDialog.getDialogPane().setContent(box);

            Optional<String> mapResult = mapDialog.showAndWait();
            Board board = null;
            if (mapResult.isPresent()) {
                board = LoadBoard.loadBoard(mapResult.get());
                gameController = new GameController(board, roboRally);

            }

            int no = result.get();
            List<String> namesChosen = Arrays.asList(new String[no]);
            for (int i = 0; i < no; i++)
            {
                Player player = new Player(board, PLAYER_COLORS.get(i), "Player " + (i + 1),
                        gameController.moveController);
                board.addPlayer(player);
                player.setSpace(board.getAvailableSpawnPoint());

                TextInputDialog chooseName = new TextInputDialog("Player " + (i + 1));
                chooseName.initOwner(primStage);
                chooseName.setTitle("Player name");
                chooseName.setHeaderText("Choose a name for player " + (i + 1));
                Optional<String> name = chooseName.showAndWait();
                if (name.isPresent())
                {
                    if (name.get().isEmpty() || name.get().isBlank() || namesChosen.contains(name.get()))
                    {
                        player.setName("Player " + (i + 1));
                    }
                    else
                    {
                        player.setName(name.get());
                    }
                    namesChosen.set(i, player.getName());
                    player.setEnergyCubes(10);
                }
            }
            board.setTabNumbersOnPlayers();
            // XXX: the line below is commented out in the current version
            gameController.startProgrammingPhase();
            roboRally.createBoardView(gameController);
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
            // here we save the game (without asking the user).
            saveGame();
            gameController = null;
            roboRally.createBoardView(null);
            roboRally.createMainMenuView(this);
            return true;
        }
        return false;
    }

    public void joinGame() {
        System.out.println("Join Pressed");
        LobbyView lobbyView = new LobbyView();
        roboRally.createLobbyView(lobbyView);
    }


    /**
     * Saves the game
     *
     * @author Elias
     */
    public void saveGame()
    {
        // XXX needs to be implemented eventually
        LoadSaveGameState.saveGameState(gameController, "default");
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
        }

        // If the user did not cancel, the RoboRally application will exit
        // after the option to save the game
        if (gameController == null || stopGame())
        {
            Platform.exit();
        }
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
