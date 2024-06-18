package dk.dtu.compute.se.pisd.roborally.view;

import dk.dtu.compute.se.pisd.roborally.APITypes.CompleteGame;
import dk.dtu.compute.se.pisd.roborally.APITypes.Lobby;
import dk.dtu.compute.se.pisd.roborally.controller.AppController;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.fileaccess.LoadBoard;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class JoinedLobbyView extends VBox
{
    private final ScheduledExecutorService executor;
    public List<Long> listOfPlayers = new ArrayList<>();
    private Button selectedMap;
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
        executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(this::updateLobbyState, 0, 1, TimeUnit.SECONDS);
        startButton = new Button("Start Game");
        startButton.setOnAction(e -> this.startGame());
        startButton.setMinSize(500, 100);
        lobbyContent = new TextArea();
        lobbyContent.setEditable(false);
        lobbyContent.setMinWidth(500);
        lobbyContent.setMinHeight(100);
        lobbyContent.appendText("Joined lobby with gameID: " + lobby.getGameID() + "\n");
        lobbyContent.appendText("Joined as player: " + lobby.getPlayerID() + "\n");
        Button dizzyHighWay = createButton("Images/dizzyHighway.PNG");
        dizzyHighWay.setOnAction(e -> changeBoard("dizzyHighway", dizzyHighWay));
        Button chopShopChallenge = createButton("Images/chopShopChallenge.PNG");
        chopShopChallenge.setOnAction(e -> changeBoard("chopShopChallenge", chopShopChallenge));
        Button mallfunctionMayhem = createButton("Images/mallfunctionMayhem.PNG");
        mallfunctionMayhem.setOnAction(e -> changeBoard("mallfunctionMayhem", mallfunctionMayhem));
        Button riskyCrossing = createButton("Images/riskyCrossing.PNG");
        riskyCrossing.setOnAction(e -> changeBoard("riskyCrossing", riskyCrossing));
        HBox mapSelection = new HBox();
        mapSelection.getChildren().addAll(dizzyHighWay, chopShopChallenge, mallfunctionMayhem, riskyCrossing);
        mapSelection.setSpacing(10);
        mapSelection.setAlignment(Pos.CENTER);
        this.getChildren().addAll(startButton, lobbyContent, mapSelection);
    }

    private Button createButton(String imagePath)
    {
        Button button = new Button();
        Image image = new Image(imagePath);
        ImageView imageView = new ImageView(image);
        imageView.setFitHeight(100);
        imageView.setFitWidth(350);
        imageView.setPreserveRatio(true);
        button.setGraphic(imageView);
        button.setStyle("-fx-background-color: transparent;");
        return button;
    }

    private void updateLobbyState()
    {
        String URL = "http://localhost:8080/get/boards/single?gameID=" + lobby.getGameID() + "&TurnID=0" + "&playerID"
                + "=" + lobby.getPlayerID();
        try
        {
            ResponseEntity<CompleteGame> response = restTemplate.exchange(URL, HttpMethod.GET, null,
                    new ParameterizedTypeReference<CompleteGame>()
            {
            });
            CompleteGame serverBoard = response.getBody();
            this.listOfPlayers.clear();
            this.boardName = serverBoard.getBoard().getBoardname();
            for (dk.dtu.compute.se.pisd.roborally.APITypes.Player.Player player : serverBoard.getPlayerList())
            {
                listOfPlayers.add(player.getPlayerID());
            }
            if (Objects.equals(serverBoard.getBoard().getPhase(), "PROGRAMMING"))
            {
                Platform.runLater(this::switchToBoardView);
            }
        }
        catch (Exception e)
        {
            //Platform.runLater(() -> chatArea.setText("Failed to fetch lobbies: " + e.getMessage()));
        }
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

                if (Boolean.TRUE.equals(response.getBody()))
                {
                    Platform.runLater(this::switchToBoardView);
                }

            }
            catch (Exception e)
            {
                Platform.runLater(() -> lobbyContent.appendText("Failed to get other players info: " + e.getMessage() + "\n"));
            }
        }).start();
    }

    private void changeBoard(String boardName, Button button)
    {
        String urlToSendTo = "http://localhost:8080/lobby/changeBoard?gameID=" + this.lobby.getGameID() + "&boardName"
                + "=" + boardName;

        new Thread(() -> {
            try
            {
                var response = restTemplate.exchange(urlToSendTo, HttpMethod.GET, null,
                        new ParameterizedTypeReference<Boolean>()
                {

                });

                button.setBorder(new Border(new BorderStroke(Color.GREEN, BorderStrokeStyle.SOLID, null, new BorderWidths(3))));

                if (selectedMap != null) {
                    selectedMap.setBorder(new Border(new BorderStroke(Color.TRANSPARENT, BorderStrokeStyle.NONE, null, new BorderWidths(3))));
                }
                this.boardName = boardName;
                selectedMap = button;

            }
            catch (Exception e)
            {
                Platform.runLater(() -> lobbyContent.appendText("Failed to change Board" + e.getMessage() + "\n"));
            }
        }).start();
    }

    private void switchToBoardView()
    {
        executor.shutdown();
        Board board = LoadBoard.loadBoard(this.boardName);
        board.setGameID(this.lobby.getGameID());
        board.setTurnID(0);
        board.setPlayerID(lobby.getPlayerID());
        GameController gameController = new GameController(board);
        gameController.setRoboRally(appController.getRoboRally());
        for (int i = 0; i < this.listOfPlayers.size(); i++)
        {
            board.addPlayer(new Player(board, "Player"));
            board.getPlayer(i).setPlayerID(this.listOfPlayers.get(i));
            board.getPlayer(i).setSpace(board.getSpace(i, i));
        }
        this.appController.startGameFromBoard(gameController);
    }
}
