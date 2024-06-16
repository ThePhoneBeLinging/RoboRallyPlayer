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

import dk.dtu.compute.se.pisd.roborally.RoboRally;
import dk.dtu.compute.se.pisd.roborally.model.*;
import javafx.stage.Stage;
import org.jetbrains.annotations.NotNull;

/**
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class GameController
{
    final public Board board;
    final public MoveController moveController;
    private RoboRally roboRally;

    /**
     * @param board
     * @author Elias
     */
    public GameController(@NotNull Board board)
    {
        this.board = board;
        this.moveController = new MoveController(this);

    }

    public GameController(@NotNull Board board, RoboRally roboRally)
    {
        this.board = board;
        this.moveController = new MoveController(this);
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
        board.activateBoardElementsOfIndex(Board.ANTENNA_INDEX);
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
     * Executes the registers of the players. This method should be called when the players have pressed the execute
     * registers button.
     *
     * @author Elias
     */
    // XXX: implemented in the current version


    /**
     * Continues the execution of the programs of the players. This method should be called when the
     *
     * @author Elias
     */
    // XXX: implemented in the current version


    /**
     * Executes the next step in the programming deck. Used for single steps or executing the whole deck
     *
     * @author Elias, Frederik, Emil & Adel
     */
    // XXX: implemented in the current version


    /**
     * Starts the programming phase of the game. This method should be called when the game has begun
     *
     * @author Elias, Adel & Frederik
     */
    // XXX: implemented in the current version


    /**
     * Executes the command option and continues the program. This method should be called when the player has chosen
     * an option for an interactive command.
     *
     * @param commandOption the command option to be executed
     * @author Emil
     */


    /**
     * @return new Card with random commands
     * @author Elias & Frederik
     */
    // XXX: implemented in the current version
    private Card generateRandomCommandCard()
    {
        Command[] commands = Command.values();
        int random = (int) (Math.random() * commands.length);
        return new Card(commands[random]);
    }

    /**
     * Executes next step
     *
     * @author Elias
     */
    // XXX: implemented in the current version

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

    /**
     * A method called when no corresponding controller operation is implemented yet. This
     * should eventually be removed.
     */
    public void notImplemented()
    {
        // XXX just for now to indicate that the actual method is not yet implemented
        assert false;
    }

    public void submitCards(Player player)
    {
        boolean canSend = true;
        for (int i = 0; i < Player.NO_REGISTERS; i++)
        {
            Card card = player.getProgramField(i).getCard();
            if (card == null) canSend = false;
        }
        if (canSend)
        {
            //TODO add function to submit cards, should also stop the player from doing anything with them
            System.out.print("submitted cards");
        }
    }
}
