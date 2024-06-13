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

    public JoinedLobbyView(AppController appController) {
        this.appController = appController;
        startButton = new Button("Start Game");
        startButton.setMinSize(100,100);
        lobbyContent = new TextArea();
        this.getChildren().addAll(startButton, lobbyContent);
    }

    private void joinGame(Long lobbyID) {
        String lobbyUrl = "http://localhost:8080/lobby/startGame?gameID=" + lobbyID;

        new Thread(() -> {
            try {
                ResponseEntity<Lobby> response = restTemplate.exchange(lobbyUrl, HttpMethod.GET, null, new ParameterizedTypeReference<Lobby>() {
                });
                Lobby lobbies = response.getBody();


            } catch (Exception e) {
                Platform.runLater(() -> lobbyContent.setText("Failed to join game: " + e.getMessage()));
            }
        }).start();
    }
}
