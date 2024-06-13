package dk.dtu.compute.se.pisd.roborally.view;

import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import dk.dtu.compute.se.pisd.roborally.controller.AppController;
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.Lobby;
import javafx.application.Platform;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;


public class LobbyView extends VBox {

    private TextField searchBar;
    private TextArea chatArea;
    private VBox lobbyList;
    private HBox hBox;
    private AppController appController;
    private Lobby joinedLobby;

    RestTemplate restTemplate = new RestTemplate();
    String url = "http://localhost:8080/lobby/getAll";

    //List response = restTemplate.getForObject(url, List.class);

    public LobbyView(AppController appController) {
        this.appController = appController;
        hBox = new HBox();
        searchBar = new TextField();
        searchBar.setPromptText("Search lobbies...");
        searchBar.setMinSize(300,20);
        lobbyList = new VBox();
        lobbyList.setMinSize(300, 500);
        chatArea = new TextArea();
        chatArea.setMinSize(300, 700);
        chatArea.setEditable(true);
        hBox.getChildren().addAll(lobbyList, chatArea);
        this.getChildren().addAll(searchBar, hBox);
        joinedLobby = null;
        fetchLobbies();
    }


    public void joinLobbyView() {
        System.out.println("Joined Lobby View");
        while (this.joinedLobby == null)
        {
            try
            {
                Thread.currentThread().sleep(1000);
            }
            catch (InterruptedException e)
            {
                throw new RuntimeException(e);
            }
        }
        this.appController.joinLobby(joinedLobby);
    }

    private void fetchLobbies() {
        new Thread(() -> {
            try {
                ResponseEntity<List<Long>> response = restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Long>>() {
                });
                List<Long> lobbies = response.getBody();

                Platform.runLater(() -> populateVBox(lobbies));
            } catch (Exception e) {
                Platform.runLater(() -> chatArea.setText("Failed to fetch lobbies: " + e.getMessage()));
            }

        }).start();
    }

    private void joinLobbies(Long lobbyID) {
        String lobbyUrl = "http://localhost:8080/lobby/join?gameID=" + lobbyID;

        new Thread(() -> {
            try {
                ResponseEntity<Lobby> response = restTemplate.exchange(lobbyUrl, HttpMethod.GET, null, new ParameterizedTypeReference<Lobby>() {
                });
                this.joinedLobby = response.getBody();
                chatArea.appendText("Joined Lobby: " + this.joinedLobby.getGameID() + "\n");

            } catch (Exception e) {
                Platform.runLater(() -> chatArea.setText("Failed to join lobbies: " + e.getMessage()));
            }

        }).start();
        joinLobbyView();
    }

    private void populateVBox(List<Long> lobbies) {
        lobbyList.getChildren().clear();

        for(Long lobbyID: lobbies) {
            Button label = new Button("Lobby ID: " + lobbyID);
            label.setMinSize(300,20);
            label.setOnAction(e -> {
                joinLobbies(lobbyID);
            });
            lobbyList.getChildren().add(label);
        }
    }
}
