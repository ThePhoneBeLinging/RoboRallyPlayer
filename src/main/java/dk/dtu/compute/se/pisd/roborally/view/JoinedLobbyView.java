package dk.dtu.compute.se.pisd.roborally.view;

import dk.dtu.compute.se.pisd.roborally.APITypes.Lobby;
import dk.dtu.compute.se.pisd.roborally.controller.AppController;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

public class JoinedLobbyView extends HBox
{
    Button startButton;
    RestTemplate restTemplate = new RestTemplate();
    TextArea lobbyContent;
    AppController appController;
    Lobby lobby;
    String boardName;

    public JoinedLobbyView(AppController appController, Lobby lobby)
    {
        this.appController = appController;
        this.lobby = lobby;
        startButton = new Button("Start Game");
        startButton.setOnAction(e -> this.startGame());
        startButton.setMinSize(500, 100);
        lobbyContent = new TextArea();
        lobbyContent.setEditable(false);
        lobbyContent.setMinWidth(500);
        lobbyContent.setMinHeight(100);
        this.getChildren().addAll(startButton, lobbyContent);
    }

    private void startGame()
    {
        String lobbyUrl = "http://localhost:8080/lobby/startGame?gameID=" + this.lobby.getGameID();

        new Thread(() -> {
            try
            {
                var response = restTemplate.exchange(lobbyUrl, HttpMethod.GET, null,
                        new ParameterizedTypeReference<Boolean>()
                {
                });
                Platform.runLater(this::switchToBoardView);

            }
            catch (Exception e)
            {
                Platform.runLater(() -> lobbyContent.appendText("Failed to join game: " + e.getMessage() + "\n"));
            }
        }).start();
    }

    private void switchToBoardView()
    {
        this.boardName = "dizzyHighway";
        this.appController.startGameFromBoard(boardName);
    }
}
