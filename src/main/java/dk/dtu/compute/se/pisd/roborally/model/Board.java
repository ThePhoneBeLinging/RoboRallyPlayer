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
package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import dk.dtu.compute.se.pisd.roborally.APITypes.CompleteGame;
import dk.dtu.compute.se.pisd.roborally.APITypes.Player.Card;
import dk.dtu.compute.se.pisd.roborally.model.BoardElements.BoardElement;
import dk.dtu.compute.se.pisd.roborally.model.BoardElements.Checkpoint;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static dk.dtu.compute.se.pisd.roborally.model.Phase.*;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class Board extends Subject
{
    public final static int GREEN_CONVEYOR_INDEX = 0;
    public final static int BLUE_CONVEYOR_INDEX = 1;
    public final static int PUSH_PANELS_INDEX = 2;
    public final static int GEARS_INDEX = 3;
    public final static int BOARD_LASER_INDEX = 4;
    public final static int ROBOT_LASER_INDEX = 5;
    public final static int ENERGY_SPACE_INDEX = 6;
    public final static int CHECKPOINTS_INDEX = 7;
    public final static int ANTENNA_INDEX = 8;
    public final static int NOT_ACTIVATE_ABLE_INDEX = 9;
    public final int width;
    public final int height;
    public final String boardName;
    private final ArrayList<BoardElement>[] boardElements = new ArrayList[10];
    private final Space[][] spaces;
    private final List<Player> players = new ArrayList<>();
    private final RestTemplate restTemplate = new RestTemplate();
    private final boolean stepMode;
    private final Thread updateBoard;
    public boolean keepUpdatingBoard = true;
    private Phase phase = INITIALISATION;
    private int step = 0;
    private ArrayList<UpgradeCard> upgradeCards = new ArrayList<>();
    private int turnID;
    private Long playerID;
    private Long gameID;

    private String URL;
    private boolean hasSubmittedCards = false;
    private List<String> options;

    /**
     * @param width  the width of the board
     * @param height the height of the board
     * @author Elias
     */
    public Board(int width, int height)
    {
        this(width, height, "default");
    }

    /**
     * @param width     the width of the board
     * @param height    the height of the board
     * @param boardName the name of the board
     *                  (this is used for the name of the file in which the board is stored)
     * @author Elias, Adel & Mads
     */

    //In the files given to us by the teacher spaces are stored in an arraylist instead of an array, maybe we should
    // do that is well.
    public Board(int width, int height, @NotNull String boardName)
    {
        this.boardName = boardName;
        this.width = width;
        this.height = height;
        spaces = new Space[width][height];
        for (int x = 0; x < width; x++)
        {
            for (int y = 0; y < height; y++)
            {
                Space space = new Space(this, x, y);
                spaces[x][y] = space;
            }
        }
        this.stepMode = false;
        for (int i = 0; i < boardElements.length; i++)
        {
            boardElements[i] = new ArrayList<BoardElement>();
        }
        this.updateURL();
        this.upgradeCards = new ArrayList<>();
        this.updateBoard = new Thread(() -> {
            try
            {
                this.updateGameBoard();
            }
            catch (Exception e)
            {
                //Platform.runLater(() -> chatArea.setText("Failed to fetch lobbies: " + e.getMessage()));
            }

        });
        this.updateBoard.start();
        this.options = new ArrayList<>();
    }

    /**
     * @author Elias
     */


    private void updateURL()
    {
        String gameID = "gameID=" + this.getGameID();
        String playerID = "&playerID=" + this.getPlayerID();
        String turnID = "&TurnID=" + this.getTurnID();
        this.URL = "http://localhost:8080/get/boards/single?" + gameID + turnID + playerID;
    }

    public void updateGameBoard()
    {

        while (keepUpdatingBoard)
        {
            updateURL();
            if (this.URL.contains("null"))
            {
                continue;
            }
            try
            {
                ResponseEntity<CompleteGame> response = restTemplate.exchange(this.URL, HttpMethod.GET, null,
                        new ParameterizedTypeReference<CompleteGame>()
                {
                });
                CompleteGame serverBoard = response.getBody();
                fromServerBoardToGameBoard(serverBoard);
            }
            catch (Exception e)
            {
                //Platform.runLater(() -> chatArea.setText("Failed to fetch lobbies: " + e.getMessage()));
            }
            try {
                Thread.sleep(2500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }
    }

    public Long getGameID()
    {
        return gameID;
    }

    public void setGameID(Long gameID)
    {
        this.gameID = gameID;
    }

    public Long getPlayerID()
    {
        return playerID;
    }

    public int getTurnID()
    {
        return turnID;
    }

    public void setTurnID(int turnID)
    {
        if (this.turnID != turnID)
        {
            if (turnID == Player.NO_REGISTERS * this.getPlayersNumber())
            {
                turnID = 0;
            }
            if (turnID == 0)
            {
                hasSubmittedCards = false;
            }
            this.turnID = turnID;
        }
    }

    public void fromServerBoardToGameBoard(CompleteGame serverBoard)
    {
        if (serverBoard == null)
        {
            return;
        }
        this.setPhase(Phase.valueOf(serverBoard.getBoard().getPhase()));
        this.setStep(serverBoard.getBoard().getStep());
        if (phase != PROGRAMMING && phase != PLAYER_INTERACTION)
        {
            this.setTurnID(this.getTurnID() + 1);
        }
        for (dk.dtu.compute.se.pisd.roborally.APITypes.Player.Player player : serverBoard.getPlayerList())
        {
            for (Player gameBoardPlayer : this.players)
            {
                if (!Objects.equals(player.getPlayerID(), gameBoardPlayer.getPlayerID()))
                {
                    continue;
                }
                gameBoardPlayer.setSpace(this.getSpace(player.getX(), player.getY()));
                gameBoardPlayer.setLastVisitedCheckPoint(player.getLastVisitedCheckpoint());
                gameBoardPlayer.setHeading(Heading.valueOf(player.getHeading()));
                gameBoardPlayer.setEnergyCubes(player.getEnergyCubes());

                gameBoardPlayer.setPlayerID(player.getPlayerID());
                if(this.getPhase() == PLAYER_INTERACTION)
                {
                    for (Card card : serverBoard.getCards())
                    {
                        if (card.getLocation().equals("OPTION") && card.getPlayerID() == player.getPlayerID())
                        {
                            this.options.add(card.getCommand());
                        }
                    }
                    if (this.options.isEmpty())
                    {
                        this.turnID++;
                    }
                    gameBoardPlayer.notify();
                }
            }
        }
        boolean toLoadNewCards = false;
        for (int i = 0; i < this.getPlayersNumber(); i++)
        {
            Player player = this.getPlayer(i);
            if (this.playerID.equals(player.getPlayerID()))
            {
                int j = 0;
                while (player.getCardField(j).getCard() == null)
                {
                    j++;
                    if (j == Player.NO_CARDS)
                    {
                        toLoadNewCards = true;
                        break;
                    }
                }
            }
        }

        this.upgradeCards.clear();
        for (dk.dtu.compute.se.pisd.roborally.APITypes.UpgradeCard upgradeCard : serverBoard.getUpgradeCards())
        {
            UpgradeCard upgrade = new UpgradeCard(upgradeCard.getCardName(), upgradeCard.getPrice());
            this.upgradeCards.add(upgrade);
        }


        if (toLoadNewCards)
        {
            for (Card card : serverBoard.getCards())
            {
                dk.dtu.compute.se.pisd.roborally.model.Card cardToAdd =
                        new dk.dtu.compute.se.pisd.roborally.model.Card(Command.valueOf(card.getCommand()));
                for (int i = 0; i < this.getPlayersNumber(); i++)
                {
                    if (!Objects.equals(this.getPlayerID(), this.getPlayer(i).getPlayerID()))
                    {
                        continue;
                    }
                    dk.dtu.compute.se.pisd.roborally.model.Player gamePlayer = this.getPlayer(i);
                    switch (card.getLocation())
                    {
                        case "REGISTER":
                            int k = 0;
                            while (gamePlayer.getProgramField(k).getCard() != null)
                            {
                                k++;
                            }
                            cardToAdd.setCardNumber(k);
                            gamePlayer.getProgramField(k).setCard(cardToAdd);
                            break;
                        case "HAND":
                            int j = 0;
                            while (gamePlayer.getCardField(j).getCard() != null)
                            {
                                j++;
                            }
                            cardToAdd.setCardNumber(j);
                            gamePlayer.getCardField(j).setCard(cardToAdd);
                            break;
                    }
                }
            }
        }
        notifyChange();
    }

    /**
     * @param x the x-coordinate of the space
     * @param y the y-coordinate of the space
     * @return the space at the given coordinates; null if the coordinates are out of bounds
     * @author Elias
     */
    public Space getSpace(int x, int y)
    {
        if (x >= 0 && x < width && y >= 0 && y < height)
        {
            return spaces[x][y];
        }
        else
        {
            return null;
        }
    }

    /**
     * @return the number of players on the board
     * @author Elias
     */
    public int getPlayersNumber()
    {
        return players.size();
    }

    /**
     * @param i the index of the player to be returned
     * @return the player with the given index
     * @author Elias
     */
    public Player getPlayer(int i)
    {
        i %= players.size();
        if (i >= 0 && i < players.size())
        {
            return players.get(i);
        }
        else
        {
            return null;
        }
    }

    public void setPlayerID(Long playerID)
    {
        this.playerID = playerID;
    }

    public boolean isHasSubmittedCards()
    {
        return hasSubmittedCards;
    }

    public void setHasSubmittedCards(boolean hasSubmittedCards)
    {
        this.hasSubmittedCards = hasSubmittedCards;
    }

    public ArrayList<UpgradeCard> getUpgradeCards()
    {
        return upgradeCards;
    }

    public void setUpgradeCards(ArrayList<UpgradeCard> upgradeCards)
    {
        this.upgradeCards = upgradeCards;
    }

    public void addBoardElement(int index, BoardElement boardElement)
    {
        this.boardElements[index].add(boardElement);
    }


    /**
     * @param player the player to be added to the board
     * @author Elias
     */
    public void addPlayer(@NotNull Player player)
    {
        if (player.board == this && !players.contains(player))
        {
            players.add(player);
            notifyChange();
        }
    }


    /**
     * @return the list of players on the board
     * @author Elias
     */
    public String getStatusMessage()
    {
        // This is actually a view aspect, but for making the first task easy for
        // the students, this method gives a string representation of the current
        // status of the game (specifically, it shows the phase, the player and the step)

        return "Phase: " + getPhase().name() + " Step: " + getStep() + " TurnID: " + getTurnID() + " PlayerID: " + getPlayerID() + "GameID: " + getGameID();

    }

    /**
     * @return the current phase of the board
     * @author Elias
     */
    public Phase getPhase()
    {
        return phase;
    }

    /**
     * @return the list of players on the board
     * @author Elias
     */
    public int getStep()
    {
        return step;
    }

    /**
     * @param step the step to be set
     * @author Elias
     */
    public void setStep(int step)
    {
        if (step != this.step)
        {
            this.step = step;
            notifyChange();
        }
    }

    /**
     * @param phase the phase to be set
     * @author Elias
     */
    public void setPhase(Phase phase)
    {
        if (phase != this.phase)
        {
            if (this.phase == ACTIVATION && phase == PROGRAMMING && !this.hasSubmittedCards)
            {
                for (int i = 0; i < this.getPlayersNumber(); i++)
                {
                    if (Objects.equals(players.get(i).getPlayerID(), this.playerID))
                    {
                        for (int x = 0; x < Player.NO_CARDS; x++)
                        {
                            players.get(i).getCardField(x).setCard(null);
                        }
                        for (int y = 0; y < Player.NO_REGISTERS; y++)
                        {
                            players.get(i).getProgramField(y).setCard(null);
                        }
                    }
                }
            }
            if (this.phase == ACTIVATION && phase == PROGRAMMING && this.hasSubmittedCards)
            {
                return;
            }
            this.phase = phase;
            notifyChange();
        }
    }

    public ArrayList<BoardElement> getBoardElementsWithIndex(int index)
    {
        return boardElements[index];
    }

    public int getIndexOfCheckPoint(Checkpoint checkpoint)
    {
        List<BoardElement> checkpoints = boardElements[Board.CHECKPOINTS_INDEX];
        return checkpoints.indexOf(checkpoint);

    }

    public List<String> getOptions()
    {
        return options;
    }

    public void setOptions(List<String> options)
    {
        this.options = options;
    }
}