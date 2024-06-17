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
package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.APITypes.CompleteGame;
import dk.dtu.compute.se.pisd.roborally.RoboRally;
import dk.dtu.compute.se.pisd.roborally.model.*;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

/**
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class GameController
{
    final public Board board;
    private final RestTemplate restTemplate = new RestTemplate();
    private RoboRally roboRally;


    /**
     * @param board
     * @author Elias
     */
    public GameController(@NotNull Board board)
    {
        this.board = board;
    }

    public GameController(@NotNull Board board, RoboRally roboRally)
    {
        this.board = board;
        this.roboRally = roboRally;
    }

    /**
     * Opens the upgrade shop for the current player. This method should be called when the player has pressed the
     * upgrade button.
     *
     * @Author Emil
     */
    // XXX: implemented in the current version
    public void openShop()
    {
        Stage primStage = roboRally.getStage();

    }

    /**
     * Method to finish the programming phase, used after the players have used programming cards.
     *
     * @author Elias
     */
    public void finishProgrammingPhase()
    {
        makeProgramFieldsInvisible();
        makeProgramFieldsVisible(0);
        board.setPhase(Phase.ACTIVATION);
        board.setStep(0);
    }

    /**
     * Makes program fields invisible, used for the finishProgrammingPhase
     *
     * @author Elias & Frederik
     */
    // XXX: implemented in the current version
    private void makeProgramFieldsInvisible()
    {
        for (int i = 0; i < board.getPlayersNumber(); i++)
        {
            Player player = board.getPlayer(i);
            for (int j = 0; j < Player.NO_REGISTERS; j++)
            {
                CardField field = player.getProgramField(j);
                field.setVisible(false);
            }
        }
    }

    /**
     * Makes program fields visible, used to revert makeProgramFieldsInvisble. Usage under
     * programming and executing next step
     *
     * @param register
     * @author Elias & Frederik
     */
    // XXX: implemented in the current version
    private void makeProgramFieldsVisible(int register)
    {
        if (register >= 0 && register < Player.NO_REGISTERS)
        {
            for (int i = 0; i < board.getPlayersNumber(); i++)
            {
                Player player = board.getPlayer(i);
                CardField field = player.getProgramField(register);
                field.setVisible(true);
            }
        }
    }

    /**
     * @param source
     * @param target
     * @return true if sourceCard is not null and targetCard is null, false otherwise
     * @author Frederik & Elias
     */
    public boolean moveCards(@NotNull CardField source, @NotNull CardField target)
    {
        Card sourceCard = source.getCard();
        Card targetCard = target.getCard();
        if (sourceCard != null && targetCard == null)
        {
            target.setCard(sourceCard);
            source.setCard(null);
            return true;
        }
        else
        {
            return false;
        }
    }


    public void submitCards(Player player)
    {
        PlayerRegisters registers = new PlayerRegisters();
        ArrayList<Integer> selectedCardsNumbers = new ArrayList<>();
        for (int i = 0; i < Player.NO_REGISTERS; i++)
        {
            Card card = player.getProgramField(i).getCard();
            if (card == null)
            {
                return;
            }
            selectedCardsNumbers.add(card.getCardNumber());
        }
        registers.setRegisterCards(selectedCardsNumbers);
        registers.setPlayerID(player.getPlayerID());
        registers.setGameID(player.board.getGameID());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<PlayerRegisters> entity = new HttpEntity<>(registers, headers);

        String url = constructURL();
        if (url.contains("null"))
        {
            return;
        }

        try
        {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<CompleteGame> response = restTemplate.exchange(url, HttpMethod.POST, entity,
                    new ParameterizedTypeReference<>()
            {
            });

            // TODO Handle the gameBoard received as response at some point

        }
        catch (Exception e)
        {
            // TODO Handle exception at some point
        }

        board.setHasSubmittedCards(true);
        board.setPhase(Phase.ACTIVATION);
    }

    private String constructURL()
    {
        String gameID = String.valueOf(board.getGameID());
        String playerID = String.valueOf(board.getPlayerID());
        String turnID = String.valueOf(board.getTurnID());

        if (gameID == null || playerID == null || turnID == null)
        {
            return "null";
        }

        return "http://localhost:8080/get/boards/single?gameID=" + gameID + "&playerID=" + playerID + "&TurnID=" + turnID;
    }
}
