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
import dk.dtu.compute.se.pisd.roborally.model.BoardElements.Checkpoint;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

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
    private final CardField[] program;
    private final ArrayList<UpgradeCard> upgradeCards = new ArrayList<>();
    private final CardField[] cards;
    private int lastVisitedCheckPoint = 0;
    private String name;
    private Space space;
    private Heading heading = SOUTH;
    private Long playerID;
    private int energyCubes;
    public boolean partyLeader = false;



    /**
     * @param board the board to which this player belongs
     * @param name  the name of the player
     * @author Elias, Frederik & Emil
     */
    public Player(@NotNull Board board, @NotNull String name)
    {
        this.board = board;
        this.name = name;
        this.space = null;
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

    public void addUpgradeCard(@NotNull UpgradeCard upgradeCard)
    {
        this.upgradeCards.add(upgradeCard);
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
     * @return
     * @author Elias
     */
    public Long getPlayerID()
    {
        return playerID;
    }

    /**
     * @param playerID
     * @author Elias
     */
    public void setPlayerID(Long playerID)
    {
        this.playerID = playerID;
        if(this.playerID==1l){
            partyLeader = true;
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


    public int getEnergyCubes()
    {
        return energyCubes;
    }

    public void setEnergyCubes(int energyCubes)
    {
        this.energyCubes = energyCubes;
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

    /**
     * @return the space on which the player is located
     * @author Elias
     */
    public Space getSpace()
    {
        return space;
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

    public void setPartyLeader(boolean partyLeader){
        this.partyLeader=partyLeader;
    }


}
