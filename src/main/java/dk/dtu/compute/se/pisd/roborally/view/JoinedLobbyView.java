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

/**
 * This class is responsible for the view of the lobby that the player has joined.
 * It displays the players that are in the lobby, the map that is selected and the option to start the game.
 * It also allows the host to change the map and the host to start the game.
 * It also allows the player to leave the lobby.
 * @Author Emil, Elias, Mustafa & Adel
 */
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
    Button leaveButton;
    Button dizzyHighWay;
    Button chopShopChallenge;
    Button mallfunctionMayhem;
    Button riskyCrossing;
    HBox mapSelection;


    public JoinedLobbyView(AppController appController, Lobby lobby)
    {
        listOfPlayers.add(lobby.getPlayerID());
        this.appController = appController;
        this.lobby = lobby;
        executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(this::updateLobbyState, 0, 1, TimeUnit.SECONDS);
        lobbyContent = new TextArea();
        lobbyContent.setEditable(false);
        lobbyContent.setMinWidth(500);
        lobbyContent.setMinHeight(100);
        lobbyContent.appendText("Joined lobby with gameID: " + lobby.getGameID() + "\n");
        lobbyContent.appendText("Joined as player: " + lobby.getPlayerID() + "\n");
        this.getChildren().add(lobbyContent);
        startButton = new Button("Start Game");
        startButton.setOnAction(e -> this.startGame());
        startButton.setMinSize(500, 100);
        dizzyHighWay = createButton("Images/dizzyHighway.PNG");
        dizzyHighWay.setOnAction(e -> changeBoard("dizzyHighway", dizzyHighWay));
        changeBoard("dizzyHighway", dizzyHighWay);
        chopShopChallenge = createButton("Images/chopShopChallenge.PNG");
        chopShopChallenge.setOnAction(e -> changeBoard("chopShopChallenge", chopShopChallenge));
        mallfunctionMayhem = createButton("Images/mallfunctionMayhem.PNG");
        mallfunctionMayhem.setOnAction(e -> changeBoard("mallfunctionMayhem", mallfunctionMayhem));
        riskyCrossing = createButton("Images/riskyCrossing.PNG");
        riskyCrossing.setOnAction(e -> changeBoard("riskyCrossing", riskyCrossing));


        mapSelection = new HBox();
        mapSelection.getChildren().addAll(dizzyHighWay, chopShopChallenge, mallfunctionMayhem, riskyCrossing);
        mapSelection.setSpacing(10);
        mapSelection.setAlignment(Pos.CENTER);
        this.getChildren().addAll(startButton, mapSelection);
        leaveButton = new Button("Leave Lobby");
        leaveButton.setOnAction(e->{
            appController.joinGame();
            leaveLobby();
        });
        leaveButton.setMinSize(500, 100);
        this.getChildren().add(leaveButton);
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

    /**
     * Sends a request to the server to update the lobby state.
     * @Author Emil & Adel
     */
    private void updateLobbyState()
    {
        String URL = "http://localhost:8080/get/boards/single?gameID=" + lobby.getGameID() + "&turnID=0" + "&playerID"
            + "=" + lobby.getPlayerID();
        try
        {
            ResponseEntity<CompleteGame> response = restTemplate.exchange(URL, HttpMethod.GET, null,
                new ParameterizedTypeReference<CompleteGame>()
            {
        });
        CompleteGame serverBoard = response.getBody();
        this.listOfPlayers.clear();
        if (serverBoard.getBoard().getBoardname() != null) {
            this.boardName = serverBoard.getBoard().getBoardname();
        }
        for (dk.dtu.compute.se.pisd.roborally.APITypes.Player.Player player : serverBoard.getPlayerList())
        {
            listOfPlayers.add(player.getPlayerID());
        }
            for(Button button: Arrays.asList(dizzyHighWay, chopShopChallenge, mallfunctionMayhem, riskyCrossing))
            {
                button.setBorder(new Border(new BorderStroke(Color.TRANSPARENT, BorderStrokeStyle.NONE, null, new BorderWidths(3))));
            }
            switch (serverBoard.getBoard().getBoardname()){
                case("dizzyHighway"):
                    dizzyHighWay.setBorder(new Border(new BorderStroke(Color.GREEN, BorderStrokeStyle.SOLID, null, new BorderWidths(3))));
                    for(Button button: Arrays.asList(chopShopChallenge, mallfunctionMayhem, riskyCrossing))
                    {
                        button.setBorder(new Border(new BorderStroke(Color.TRANSPARENT, BorderStrokeStyle.NONE, null, new BorderWidths(3))));
                    }
                    break;
                case("chopShopChallenge"):
                    chopShopChallenge.setBorder(new Border(new BorderStroke(Color.GREEN, BorderStrokeStyle.SOLID, null, new BorderWidths(3))));
                    break;
                case("mallfunctionMayhem"):
                    mallfunctionMayhem.setBorder(new Border(new BorderStroke(Color.GREEN, BorderStrokeStyle.SOLID, null, new BorderWidths(3))));
                    break;
                case("riskyCrossing"):
                    riskyCrossing.setBorder(new Border(new BorderStroke(Color.GREEN, BorderStrokeStyle.SOLID, null, new BorderWidths(3))));
                    break;
                default:
                    return;
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

    /**
     * Sends a request to the server to start the game.
     * @Author Mustafa, Emil & Adel
     */
    private void startGame()
    {
        Collections.sort(listOfPlayers);
        if(listOfPlayers.get(0) != lobby.getPlayerID())
        {
            lobbyContent.appendText("Only the host can start the game\n");
        }else {
            String lobbyUrl = "http://localhost:8080/lobby/startGame?gameID=" + this.lobby.getGameID();

            new Thread(() -> {
                try {
                    var response = restTemplate.exchange(lobbyUrl, HttpMethod.GET, null,
                            new ParameterizedTypeReference<Boolean>() {
                            });

                    if (Boolean.TRUE.equals(response.getBody())) {
                        Platform.runLater(this::switchToBoardView);
                    }

                } catch (Exception e) {
                    Platform.runLater(() -> lobbyContent.appendText("Failed to get other players info: " + e.getMessage() + "\n"));
                }
            }).start();
        }
    }

    /**
     * Sends a request to the server to change the board.
     * @param boardName The name of the board to change to.
     * @param button The button that was clicked.
     * @Author Emil & Adel
     */
    private void changeBoard(String boardName, Button button)
    {
        Collections.sort(listOfPlayers);
        if(listOfPlayers.get(0) != lobby.getPlayerID())
        {
            lobbyContent.appendText("Only the host can change the board\n");
        }else {
            String urlToSendTo = "http://localhost:8080/lobby/changeBoard?gameID=" + this.lobby.getGameID() + "&boardName"
                    + "=" + boardName;

            new Thread(() -> {
                try {
                    var response = restTemplate.exchange(urlToSendTo, HttpMethod.GET, null,
                            new ParameterizedTypeReference<Boolean>() {

                            });

                    button.setBorder(new Border(new BorderStroke(Color.GREEN, BorderStrokeStyle.SOLID, null, new BorderWidths(3))));

                    if (selectedMap != null) {
                        selectedMap.setBorder(new Border(new BorderStroke(Color.TRANSPARENT, BorderStrokeStyle.NONE, null, new BorderWidths(3))));
                    }
                    this.boardName = boardName;
                    selectedMap = button;

                } catch (Exception e) {
                    Platform.runLater(() -> lobbyContent.appendText("Failed to change Board" + e.getMessage() + "\n"));
                }
            }).start();
        }
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

    private void leaveLobby(){
        String urlToSend1 = "http://localhost:8080/lobby/leave?gameID=" + this.lobby.getGameID() + "&playerID=" + this.lobby.getPlayerID();
        String urlToSend2 = "http://localhost:8080/players/leave?gameID=" + this.lobby.getGameID() + "&playerID=" + this.lobby.getPlayerID();
        new Thread(() -> {
            try {
                var response = restTemplate.exchange(urlToSend1, HttpMethod.GET, null,
                        new ParameterizedTypeReference<Boolean>() {
                        });
                var response2 = restTemplate.exchange(urlToSend2, HttpMethod.GET, null,
                        new ParameterizedTypeReference<Boolean>() {
                        });
                if (Boolean.TRUE.equals(response.getBody()) && Boolean.TRUE.equals(response2.getBody())) {
                    Platform.runLater(() -> appController.joinGame());
                }
            } catch (Exception e) {
                Platform.runLater(() -> lobbyContent.appendText("Failed to leave lobby" + e.getMessage() + "\n"));
            }
        }).start();
    }
}
