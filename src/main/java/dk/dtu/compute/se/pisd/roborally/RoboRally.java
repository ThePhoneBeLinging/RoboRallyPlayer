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
package dk.dtu.compute.se.pisd.roborally;

import dk.dtu.compute.se.pisd.roborally.controller.AppController;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.controller.SoundController;
import dk.dtu.compute.se.pisd.roborally.view.BoardView;
import dk.dtu.compute.se.pisd.roborally.view.LobbyView;
import dk.dtu.compute.se.pisd.roborally.view.MainMenuView;
import dk.dtu.compute.se.pisd.roborally.view.RoboRallyMenuBar;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.awt.*;
import java.util.Objects;

import javafx.scene.image.Image;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class RoboRally extends Application
{

    private static final int MIN_APP_WIDTH = 600;
    private static final int MIN_APP_HEIGHT = 800;

    private Stage stage;
    private BorderPane boardRoot;
    // private RoboRallyMenuBar menuBar;

    // private AppController appController;

    /**
     * @param args the command line arguments
     *             Start the application by launching the JavaFX application
     */
    public static void main(String[] args)
    {
        launch(args);
    }

    /**
     * @throws Exception in case of an exception during the initialization
     * @author Initialize the application
     */
    @Override
    public void init() throws Exception
    {
        super.init();
    }

    /**
     * @param primaryStage the primary stage for the application
     * @author Start the application
     */
    @Override
    public void start(Stage primaryStage)
    {
        stage = primaryStage;

        AppController appController = new AppController(this);

        // create the primary scene with the a menu bar and a pane for
        // the board view (which initially is empty); it will be filled
        // when the user creates a new game or loads a game
        RoboRallyMenuBar menuBar = new RoboRallyMenuBar(appController);

        boardRoot = new BorderPane();
        VBox vbox = new VBox(menuBar, boardRoot);
        vbox.setMinWidth(MIN_APP_WIDTH);
        vbox.setMinHeight(MIN_APP_HEIGHT);
        boardRoot.setMinSize(MIN_APP_WIDTH, MIN_APP_HEIGHT);
        Scene primaryScene = new Scene(vbox);
        primaryScene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if(event.getCode() == KeyCode.F11) {
                stage.setFullScreen(!primaryStage.isFullScreen());
            }
        });
        //
        // CREATE Main Menu
        //
        createMainMenuView(appController);
        //
        //
        //
        stage.setScene(primaryScene);
        stage.setTitle("RoboRally");
        stage.setOnCloseRequest(e -> {
            e.consume();
            appController.exit();
        });
        stage.setResizable(true);
        stage.setMaximized(true);
        stage.setFullScreen(true);
        stage.setX((double) (Toolkit.getDefaultToolkit().getScreenSize().width / 2) - (double) MIN_APP_WIDTH / 2);
        stage.setY(0.0);
        stage.getIcons().add(new Image("file:src/main/Resources/Images/roborally.png"));

        stage.show();

        SoundController sc = SoundController.getInstance();
        sc.loopSounds(new String[]{"OST", "OST2", "OST3", "OST4", "OST5", "OST6"});
    }

    /**
     * @throws Exception in case of an exception during the stop
     * @author Stop the application
     */
    @Override
    public void stop() throws Exception
    {
        super.stop();

        // XXX just in case we need to do something here eventually;
        //     but right now the only way for the user to exit the app
        //     is delegated to the exit() method in the AppController,
        //     so that the AppController can take care of that.
    }

    /**
     * @param appController the controller for the application
     * @author Create the main menu view
     */
    public void createMainMenuView(AppController appController)
    {
        boardRoot.getChildren().clear();
        if (appController != null)
        {
            MainMenuView mainMenu = new MainMenuView(appController);
            mainMenu.setAlignment(Pos.CENTER);
            boardRoot.setCenter(mainMenu);


        }
    }

    /**
     * @param gameController the controller for the game
     * @author Create the board view
     */
    public void createBoardView(GameController gameController)
    {
        // if present, remove old BoardView
        boardRoot.getChildren().clear();

        if (gameController != null)
        {
            // create and add view for new board
            BoardView boardView = new BoardView(gameController);
            boardRoot.setCenter(boardView);
        }
    }

    public void createLobbyView(LobbyView lobbyView) {
        boardRoot.getChildren().clear();
        boardRoot.getChildren().add(lobbyView);
    }

    public Stage getStage() {
        return this.stage;
    }

}