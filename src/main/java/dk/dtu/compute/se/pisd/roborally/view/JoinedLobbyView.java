package dk.dtu.compute.se.pisd.roborally.view;

import dk.dtu.compute.se.pisd.roborally.controller.AppController;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;

public class JoinedLobbyView extends HBox {
    private Button startButton;
    private AppController appController;

    public JoinedLobbyView(AppController appController) {
        this.appController = appController;
        startButton = new Button("Start Game");
        startButton.setMinSize(100,100);
        this.getChildren().add(startButton);
    }
}
