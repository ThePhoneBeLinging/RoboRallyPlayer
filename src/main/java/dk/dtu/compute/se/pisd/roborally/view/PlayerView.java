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

import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.CardField;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.UpgradeCard;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class PlayerView extends Tab implements ViewObserver
{

    private final Player player;

    private final VBox top;
    private final HBox horizontal;
    private final VBox rightPanel;
    private final Label upgradeCardsLabel;
    private final Label energyCubesLabel;

    private final Label programLabel;
    private final GridPane programPane;
    private final Label cardsLabel;
    private final GridPane cardsPane;

    private final CardFieldView[] programCardViews;
    private final CardFieldView[] cardViews;


    private final VBox playerInteractionPanel;

    private final GameController gameController;

    /**
     * @param gameController
     * @param player
     * @author Elias, Frederik, Emil, Adel & Mads
     */
    public PlayerView(@NotNull GameController gameController, @NotNull Player player)
    {
        super(player.getName());

        top = new VBox();
        top.setSpacing(3.0);

        horizontal = new HBox();

        rightPanel = new VBox();
        rightPanel.setAlignment(Pos.TOP_LEFT);
        rightPanel.setSpacing(3.0);

        upgradeCardsLabel = new Label("Active Upgrade Cards");

        energyCubesLabel = new Label("Energy Cubes: " + player.getEnergyCubes());
        horizontal.getChildren().addAll(top, rightPanel);

        this.setContent(horizontal);

        this.gameController = gameController;
        this.player = player;
        Button submitCards = new Button("Submit Cards");
        Button upgradeShopButton = new Button("Open Upgrade Shop");

        submitCards.setOnAction(e -> gameController.submitCards(this.player));
        upgradeShopButton.setOnAction(e -> gameController.openShop(player));

        rightPanel.getChildren().addAll(energyCubesLabel, submitCards, upgradeCardsLabel, upgradeShopButton);


        programLabel = new Label("Program");

        programPane = new GridPane();
        programPane.setVgap(2.0);
        programPane.setHgap(2.0);
        programCardViews = new CardFieldView[Player.NO_REGISTERS];
        for (int i = 0; i < Player.NO_REGISTERS; i++)
        {
            CardField cardField = player.getProgramField(i);
            if (cardField != null)
            {
                programCardViews[i] = new CardFieldView(gameController, cardField);
                programPane.add(programCardViews[i], i, 0);
            }
        }
        playerInteractionPanel = new VBox();
        playerInteractionPanel.setAlignment(Pos.CENTER_LEFT);
        playerInteractionPanel.setSpacing(3.0);


        cardsLabel = new Label("Command Cards");
        cardsPane = new GridPane();
        cardsPane.setVgap(2.0);
        cardsPane.setHgap(2.0);
        cardViews = new CardFieldView[Player.NO_CARDS];
        for (int i = 0; i < Player.NO_CARDS; i++)
        {
            CardField cardField = player.getCardField(i);
            if (cardField != null)
            {
                cardViews[i] = new CardFieldView(gameController, cardField);
                cardsPane.add(cardViews[i], i, 0);
            }
        }

        top.getChildren().add(programLabel);
        top.getChildren().add(programPane);
        top.getChildren().add(cardsLabel);
        top.getChildren().add(cardsPane);

        if (player.board != null)
        {
            player.board.attach(this);
            update(player.board);
        }
    }


    private void updateUpgradeCardsLabel()
    {
        StringBuilder upgrades = new StringBuilder("Active Upgrade Cards:\n");

        for (UpgradeCard upgrade : player.getUpgradeCards())
        {
            upgrades.append(upgrade.getName()).append("\n");
        }
        upgradeCardsLabel.setText(upgrades.toString());
    }

    private void updateEnergyCubesLabel()
    {
        energyCubesLabel.setText("Energy Cubes: " + player.getEnergyCubes());
    }

    @Override
    public void updateView(Subject subject)
    {
        playerInteractionPanel.getChildren().clear();
        for (String option : player.board.getOptions())
        {
            Button optionButton = new Button(option);
            optionButton.setOnAction(e -> sendInteractiveChoice(player.board.getOptions().indexOf(option)));
            playerInteractionPanel.getChildren().add(optionButton);
            optionButton.setDisable(false);
        }
        rightPanel.getChildren().removeAll(playerInteractionPanel);
        rightPanel.getChildren().add(playerInteractionPanel);
    }

    @Override
    public void update(Subject subject)
    {
        ViewObserver.super.update(subject);
    }

    private void sendInteractiveChoice(int choice)
    {
        new Thread(() -> {
            String URL = "http://localhost:8080/set/interactive/choice" + "?gameID=" + player.board.getGameID() +
                    "&turnID=" + player.board.getTurnID() + "&playerID=" + player.getPlayerID() + "&choice=" + choice;
            try
            {
                RestTemplate restTemplate = new RestTemplate();
                ResponseEntity<Boolean> response = restTemplate.exchange(URL, HttpMethod.GET, null,
                        new ParameterizedTypeReference<Boolean>()
                {
                });
                Boolean bool = response.getBody();
                Platform.runLater(() -> {
                    playerInteractionPanel.getChildren().clear();
                });
            }
            catch (Exception e)
            {
                System.out.println("Exception occurred: " + e.getMessage());
            }
        }).start();
    }

    @Override
    public Node getStyleableNode()
    {
        return super.getStyleableNode();
    }
}
