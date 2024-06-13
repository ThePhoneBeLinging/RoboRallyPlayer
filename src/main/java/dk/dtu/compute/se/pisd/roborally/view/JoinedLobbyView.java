package dk.dtu.compute.se.pisd.roborally.view;

import dk.dtu.compute.se.pisd.roborally.controller.AppController;
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.Lobby;
import javafx.application.Platform;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class JoinedLobbyView extends HBox {
    Button startButton;
    RestTemplate restTemplate = new RestTemplate();
    TextArea lobbyContent;
    AppController appController;
    Lobby lobby;

    public JoinedLobbyView(AppController appController,Lobby lobby) {
        this.appController = appController;
        this.lobby = lobby;
        startButton = new Button("Start Game");
        startButton.setOnAction(e -> this.startGame());
        startButton.setMinSize(500,100);
        lobbyContent = new TextArea();
        lobbyContent.setEditable(false);
        lobbyContent.setMinWidth(500);
        lobbyContent.setMinHeight(100);
        this.getChildren().addAll(startButton, lobbyContent);
    }

    private void startGame() {
        String lobbyUrl = "http://localhost:8080/lobby/startGame?gameID=" + this.lobby.getGameID();

        new Thread(() -> {
            try {
                var response = restTemplate.exchange(lobbyUrl, HttpMethod.GET, null, new ParameterizedTypeReference<Boolean>() {
                });


            } catch (Exception e) {
                Platform.runLater(() -> lobbyContent.appendText("Failed to join game: " + e.getMessage() + "\n"));
            }
        }).start();
    }
}
