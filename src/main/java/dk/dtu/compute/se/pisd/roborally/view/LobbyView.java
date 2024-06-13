package dk.dtu.compute.se.pisd.roborally.view;

import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import javafx.application.Platform;
import javafx.scene.control.*;
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

    RestTemplate restTemplate = new RestTemplate();
    String url = "http://localhost:8080/lobby/getAll";

    //List response = restTemplate.getForObject(url, List.class);

    public LobbyView() {

        searchBar = new TextField();
        searchBar.setPromptText("Search lobbies...");
        searchBar.setMinSize(300,20);
        lobbyList = new VBox();
        lobbyList.setMinSize(300, 500);
        lobbyList.setTranslateY(searchBar.getTranslateY() + 20);
        chatArea = new TextArea();
        chatArea.setMinSize(300, 700);
        chatArea.setTranslateX(400);
        chatArea.setTranslateY(0);
        chatArea.setEditable(true);
        this.getChildren().addAll(searchBar, chatArea, lobbyList);
        fetchLobbies();
    }

    public TextField getSearchBar() {
        return searchBar;
    }

    public TextArea getChatArea() {
        return chatArea;
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

    private void populateVBox(List<Long> lobbies) {
        lobbyList.getChildren().clear();

        for(Long lobbyID: lobbies) {
            Button label = new Button("Lobby ID: " + lobbyID);
            lobbyList.getChildren().add(label);
        }
    }
}
