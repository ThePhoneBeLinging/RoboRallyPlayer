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

import dk.dtu.compute.se.pisd.roborally.controller.AppController;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class RoboRallyMenuBar extends MenuBar {

    private AppController appController;

    private Menu controlMenu;

    private MenuItem saveGame;

    private MenuItem newGame;

    private MenuItem loadGame;

    private MenuItem stopGame;

    private MenuItem exitApp;

    /**
     * @param appController
     * @author
     */
    public RoboRallyMenuBar(AppController appController) {
        this.appController = appController;

        int width = 120;
        int height = 40;

        controlMenu = new Menu("File");
        this.getMenus().add(controlMenu);


        newGame = new MenuItem();
        ImageView newGameView = new ImageView(new Image("file:src/main/resources/Images/newGamet.png"));
        newGameView.setFitWidth(width);
        newGameView.setFitHeight(height);
        newGame.setGraphic(newGameView);
        newGame.setOnAction(e -> this.appController.newGame());
        controlMenu.getItems().add(newGame);

        stopGame = new MenuItem();
        ImageView stopGameView = new ImageView(new Image("file:src/main/resources/Images/stopGamet.png"));
        stopGameView.setFitWidth(width);
        stopGameView.setFitHeight(height);
        stopGame.setGraphic(stopGameView);
        stopGame.setOnAction(e -> this.appController.stopGame());
        controlMenu.getItems().add(stopGame);

        saveGame = new MenuItem();
        ImageView saveGameView = new ImageView(new Image("file:src/main/resources/Images/saveGamet.png"));
        saveGameView.setFitWidth(width);
        saveGameView.setFitHeight(height);
        saveGame.setGraphic(saveGameView);
        saveGame.setOnAction(e -> this.appController.saveGame());
        controlMenu.getItems().add(saveGame);

        loadGame = new MenuItem();
        ImageView loadGameView = new ImageView(new Image("file:src/main/resources/Images/loadGamet.png"));
        loadGameView.setFitWidth(width);
        loadGameView.setFitHeight(height);
        loadGame.setGraphic(loadGameView);
        loadGame.setOnAction(e -> this.appController.loadGame());
        controlMenu.getItems().add(loadGame);

        exitApp = new MenuItem();
        ImageView exitAppView = new ImageView(new Image("file:src/main/resources/Images/exitAppt.png"));
        exitAppView.setFitWidth(width);
        exitAppView.setFitHeight(height);
        exitApp.setGraphic(exitAppView);
        exitApp.setOnAction(e -> this.appController.exit());
        controlMenu.getItems().add(exitApp);

        controlMenu.setOnShowing(e -> update());
        controlMenu.setOnShown(e -> this.updateBounds());
        update();
    }

    /**
     * @author
     */
    public void update() {
        if (appController.isGameRunning()) {
            newGame.setVisible(false);
            stopGame.setVisible(true);
            saveGame.setVisible(true);
            loadGame.setVisible(false);
        } else {
            newGame.setVisible(true);
            stopGame.setVisible(false);
            saveGame.setVisible(false);
            loadGame.setVisible(true);
        }
    }

}
