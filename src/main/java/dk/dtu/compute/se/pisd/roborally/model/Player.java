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
import dk.dtu.compute.se.pisd.roborally.controller.MoveController;
import dk.dtu.compute.se.pisd.roborally.controller.SoundController;
import dk.dtu.compute.se.pisd.roborally.model.BoardElements.Checkpoint;
import dk.dtu.compute.se.pisd.roborally.model.BoardElements.NullBoardElement;
import javafx.scene.image.Image;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import static dk.dtu.compute.se.pisd.roborally.model.Command.*;
import static dk.dtu.compute.se.pisd.roborally.model.Heading.SOUTH;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class Player extends Subject
{

    public static int NO_CARDS = 8;
    public static int NO_REGISTERS = 5;
    final public Board board;
    public final MoveController moveController;
    public final Deck activeCardsPile;
    public final Deck discardedCardsPile;
    private final CardField[] program;
    private final ArrayList<UpgradeCard> upgradeCards = new ArrayList<>();
    private CardField[] cards;
    private int lastVisitedCheckPoint = 0;
    private String name;
    private String color;
    private Space space;
    private Heading heading = SOUTH;
    private int tabNumber;
    private boolean movedByConveyorThisTurn;
    private int energyCubes;
    private boolean thisPlayerTurn = false;

    /**
     * @param board the board to which this player belongs
     * @param color the color of the player
     * @param name  the name of the player
     * @author Elias, Frederik & Emil
     */
    public Player(@NotNull Board board, String color, @NotNull String name, MoveController moveController)
    {
        this.board = board;
        this.name = name;
        this.color = color;
        this.space = null;
        this.moveController = moveController;
        activeCardsPile = new Deck();
        activeCardsPile.initializeAPlayerDeck();
        activeCardsPile.shuffleDeck();
        discardedCardsPile = new Deck();
        energyCubes = 0;
        program = new CardField[NO_REGISTERS];
        for (int i = 0; i < program.length; i++)
        {
            program[i] = new CardField(this);
        }

        cards = new CardField[NO_CARDS];
        for (int i = 0; i < cards.length; i++)
        {
            cards[i] = new CardField(this);
        }
    }

    public int getLastVisitedCheckPoint()
    {
        return lastVisitedCheckPoint;
    }

    public void setLastVisitedCheckPoint(int lastVisitedCheckPoint)
    {
        this.lastVisitedCheckPoint = lastVisitedCheckPoint;
    }

    /**
     * @author Elias
     */
    public void die()
    {
        this.board.getRebootToken().reboot(this);
    }

    /**
     * @return the name of the player
     * @author Elias
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param name the name of the player
     * @author Elias
     */
    public void setName(String name)
    {
        if (name != null && !name.equals(this.name))
        {
            this.name = name;
            notifyChange();
            if (space != null)
            {
                space.playerChanged();
            }
        }
    }

    /**
     * @return the color of the player
     * @author Elias
     */
    public String getColor()
    {
        return color;
    }

    /**
     * @param color the color of the player
     * @author Elias
     */
    public void setColor(String color)
    {
        this.color = color;
        notifyChange();
        if (space != null)
        {
            space.playerChanged();
        }
    }

    public void addUpgradeCard(@NotNull UpgradeCard upgradeCard)
    {
        this.upgradeCards.add(upgradeCard);
        if (upgradeCard.getName().equals("BRAKES"))
        {
            this.changeCardsOfCertainType(FORWARD, OPTION_FORWARD_OR_NOT);
        }
        if (upgradeCard.getName().equals("MEMORY STICK"))
        {
            NO_CARDS += 1;
            CardField[] newCardArr = new CardField[NO_CARDS];
            if (NO_CARDS - 1 >= 0)
            {
                System.arraycopy(this.cards, 0, newCardArr, 0, NO_CARDS - 1);
            }
            newCardArr[NO_CARDS - 1] = new CardField(this);
            this.cards = new CardField[NO_CARDS];
        }
    }

    private void changeCardsOfCertainType(Command cmd, Command newCommand)
    {
        Card card = new Card(newCommand);
        Image image = card.getImage();

        for (Card activeCards : this.activeCardsPile.playerCards)
        {
            if (activeCards.command == cmd)
            {
                activeCards.command = newCommand;
                activeCards.setImage(image);
            }
        }
        for (Card discardedCards : this.discardedCardsPile.playerCards)
        {
            if (discardedCards.command == cmd)
            {
                discardedCards.command = newCommand;
                discardedCards.setImage(image);
            }
        }
        for (int i = 0; i < Player.NO_CARDS; i++)
        {
            CardField cardField = this.getCardField(i);
            if (cardField != null && cardField.getCard() != null)
            {
                if (cardField.getCard().command == cmd)
                {
                    cardField.getCard().command = newCommand;
                    cardField.getCard().setImage(image);
                }
            }
        }
        for (int i = 0; i < Player.NO_REGISTERS; i++)
        {
            CardField cardField = this.getProgramField(i);
            if (cardField != null && cardField.getCard() != null)
            {
                if (cardField.getCard().command == cmd)
                {
                    cardField.getCard().command = newCommand;
                    cardField.getCard().setImage(image);
                }
            }
        }
    }

    /**
     * @param i the index of the card field to be returned
     * @return the card field with the given index
     * @author Frederik
     */
    public CardField getCardField(int i)
    {
        return cards[i];
    }

    /**
     * @param i the index of the register to be returned
     * @return the register with the given index
     * @author Frederik
     */
    public CardField getProgramField(int i)
    {
        return program[i];
    }

    public ArrayList<UpgradeCard> getUpgradeCards()
    {
        return upgradeCards;
    }

    /**
     * @param checkpoint
     * @author Elias
     */
    public void addCheckPointAsVisited(Checkpoint checkpoint)
    {
        int indexOfCheckPoint = board.getIndexOfCheckPoint(checkpoint);

        if (indexOfCheckPoint == lastVisitedCheckPoint)
        {
            lastVisitedCheckPoint++;
        }
    }

    /**
     * @return
     * @author Elias
     */
    public int getTabNumber()
    {
        return tabNumber;
    }

    /**
     * @param tabNumber
     * @author Elias
     */
    public void setTabNumber(int tabNumber)
    {
        this.tabNumber = tabNumber;
    }

    /**
     * @return
     * @author Elias
     */
    public boolean getMovedByConveyorThisTurn()
    {
        return movedByConveyorThisTurn;
    }

    /**
     * @param movedByConveyorThisTurn
     * @author Elias
     */
    public void setMovedByConveyorThisTurn(boolean movedByConveyorThisTurn)
    {
        this.movedByConveyorThisTurn = movedByConveyorThisTurn;
    }

    /**
     * @return void
     * @author Frederik
     */
    public void discardAllCardsUponReboot()
    {
        for (int i = 0; i < program.length; i++)
        {
            this.addCardToDiscardPile(program[i].getCard());
            program[i].setCard(new Card(DEATH));
        }
        this.discardedCardsPile.playerCards.add(new Card((SPAM)));
        this.discardedCardsPile.playerCards.add(new Card((SPAM)));


    }

    /**
     * @param card
     * @return void
     * @author Frederik
     */
    public void addCardToDiscardPile(Card card)
    {
        if (card != null)
        {
            if (card.command == SPAM || card.command == DEATH || card.command == TROJAN_HORSE || card.command == WORM || card.command == VIRUS)
            {
                return;
            }
            this.discardedCardsPile.playerCards.add(card);
        }
    }

    /**
     * @return void
     * @author Emil
     */
    public void pickUpEnergyCube()
    {
        energyCubes++;
    }

    public Card drawTopCard()
    {
        Card cardToReturn = activeCardsPile.drawTopCard();
        if (cardToReturn == null)
        {
            activeCardsPile.shuffleDeckIntoAnotherDeck(discardedCardsPile);
            cardToReturn = activeCardsPile.drawTopCard();
        }

        return cardToReturn;
    }

    public int getEnergyCubes()
    {
        return energyCubes;
    }

    public void setEnergyCubes(int energyCubes)
    {
        this.energyCubes = energyCubes;
    }

    public boolean isThisPlayerTurn()
    {
        return thisPlayerTurn;
    }

    public void setThisPlayerTurn(boolean thisPlayerTurn)
    {
        this.thisPlayerTurn = thisPlayerTurn;
    }

    public void shoot()
    {
        Heading headingToCheck = this.getHeading();
        Space spaceToCheck = this.getSpace();

        while (spaceToCheck != null)
        {
            if (spaceToCheck.getPlayer() != null)
            {
                if (!spaceToCheck.getPlayer().equals(this))
                {
                    if (checkIfOwnsUpgradeCard("DOUBLE BARREL LASER"))
                    {
                        spaceToCheck.getPlayer().addDamageCardToPile(SPAM, 2);
                        break;
                    }
                    else
                    {
                        spaceToCheck.getPlayer().addDamageCardToPile(SPAM, 1);
                        break;
                    }
                }
            }
            Space nextSpace = spaceToCheck.board.getNeighbour(spaceToCheck, headingToCheck);
            // We check if we were to hit a board element, and break if we do
            if (spaceToCheck.getBoardElement() == null)
            {
                spaceToCheck.setBoardElement(new NullBoardElement(spaceToCheck));
            }
            if (!spaceToCheck.getBoardElement().getCanWalkOutOf(headingToCheck) || nextSpace == null || !nextSpace.getBoardElement().getCanWalkInto(headingToCheck))
            {
                break;
            }
            spaceToCheck = nextSpace;
        }
        if (this.getSpace().board.isStepMode())
        {
            SoundController sc = SoundController.getInstance();
            sc.playSound("laser_sound");
        }
    }

    /**
     * @return the heading of the player
     * @author Elias
     */
    public Heading getHeading()
    {
        return heading;
    }

    /**
     * @return the space on which the player is located
     * @author Elias
     */
    public Space getSpace()
    {
        return space;
    }

    /**
     * @param upgradeCardName
     * @return
     * @author Elias & Mads
     */
    public boolean checkIfOwnsUpgradeCard(String upgradeCardName)
    {
        for (int i = 0; i < upgradeCards.size(); i++)
        {
            if (upgradeCards.get(i).getName().equals(upgradeCardName))
            {

                return true;
            }

        }
        return false;
    }

    /**
     * @param amount
     * @author Elias
     */
    public void addDamageCardToPile(Command command, int amount)
    {
        for (int i = 0; i < amount; i++)
        {
            this.discardedCardsPile.playerCards.add(new Card(command));
        }


    }

    /**
     * @param space the space on which the player is located
     * @author Elias
     */
    public void setSpace(Space space)
    {
        Space oldSpace = this.space;
        if (space != oldSpace && (space == null || space.board == this.board))
        {
            this.space = space;
            if (oldSpace != null)
            {
                oldSpace.setPlayer(null);
            }
            if (space != null)
            {
                space.setPlayer(this);
            }
            notifyChange();
        }
    }

    /**
     * @param heading the heading of the player
     * @author Elias
     */
    public void setHeading(@NotNull Heading heading)
    {
        if (heading != this.heading)
        {
            this.heading = heading;
            notifyChange();
            if (space != null)
            {
                space.playerChanged();
            }
        }
    }
}
