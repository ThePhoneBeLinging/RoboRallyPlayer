package dk.dtu.compute.se.pisd.roborally.controller;

import dk.dtu.compute.se.pisd.roborally.model.*;
import org.jetbrains.annotations.NotNull;

public class MoveController
{
    GameController gameController;

    /**
     * @param gameController
     * @author Elias
     */
    public MoveController(GameController gameController)
    {
        this.gameController = gameController;
    }

    /**
     * @param player
     * @param command
     * @author Elias, Emil, Frederik, Mustafa & Mads
     */
    public void executeCommand(@NotNull Player player, Command command)
    {
        if (player.board == gameController.board && command != null)
        {
            // XXX This is a very simplistic way of dealing with some basic cards and
            //     their execution. This should eventually be done in a more elegant way
            //     (this concerns the way cards are modelled as well as the way they are executed).

            switch (command)
            {
                case FORWARD:
                    this.moveForward(player);
                    break;
                case RIGHT:
                    this.turnRight(player);
                    break;
                case LEFT:
                    this.turnLeft(player);
                    break;
                case FAST_FORWARD:
                    this.fastForward(player);
                    break;
                case MOVE_THREE:
                    this.moveThree(player);
                    break;
                case U_TURN:
                    this.uTurn(player);
                    break;
                case MOVE_BACK:
                    this.moveBack(player);
                    break;
                case AGAIN:
                    this.again(player);
                    break;
                case POWER_UP:
                    this.powerUp(player);
                    break;
                case OPTION_LEFT_RIGHT:
                    this.optionLeftOrRight(player, command);
                    break;
                case SPAM:
                    this.useSpamCard(player);
                    break;
                case TROJAN_HORSE:
                    this.useTrojanHorse(player);
                    break;
                case WORM:
                    this.useWormCard(player);
                    break;
                case VIRUS:
                    this.useVirusCard(player);
                    break;
                case DEATH:
                    break;
                case DONT_MOVE:
                    break;
                default:
                    throw new RuntimeException("Something went wrong");
            }
        }
    }

    /**
     * Moves the player one step forward in the direction of the player's heading.
     *
     * @param player the player to be moved
     * @author Adel, Refactored by Elias
     */
    public void moveForward(@NotNull Player player)
    {
        movePlayerAmountOfTimesWithHeading(player, player.getHeading(), 1);
    }

    /**
     * Turns the player to the right by changing the player's heading to the next heading in the enumeration.
     *
     * @param player the player to be turned
     * @author Mustafa
     */
    public void turnRight(@NotNull Player player)
    {
        Heading heading = player.getHeading();
        player.setHeading(heading.next());

    }

    /**
     * Turns the player to the left by changing the player's heading to the previous heading in the enumeration.
     *
     * @param player the player to be turned
     * @author Mustafa
     */
    public void turnLeft(@NotNull Player player)
    {
        Heading heading = player.getHeading();
        player.setHeading(heading.prev());
    }

    /**
     * Moves the player two steps forward in the direction of the player's heading.
     *
     * @param player the player to be moved
     * @author Adel, Refactored by Elias
     */
    public void fastForward(@NotNull Player player)
    {
        movePlayerAmountOfTimesWithHeading(player, player.getHeading(), 2);
    }

    /**
     * Moves the player three steps forward in the direction of the player's heading.
     *
     * @param player the player to be moved
     * @Author Emil
     */
    public void moveThree(@NotNull Player player)
    {
        movePlayerAmountOfTimesWithHeading(player, player.getHeading(), 3);
    }

    /**
     * Turns the player 180 degrees.
     *
     * @param player the player to be turned
     * @Author Emil
     */
    public void uTurn(@NotNull Player player)
    {
        turnRight(player);
        turnRight(player);
    }

    /**
     * Moves the player one step back in the direction of the player's heading.
     *
     * @param player the player to be moved
     * @Author Emil
     */
    public void moveBack(@NotNull Player player)
    {
        movePlayerAmountOfTimesWithHeading(player, player.getHeading().next().next(), 1);
    }

    /**
     * Executes the command card in the previous register. If the player is on the first register, no command is not
     * executed.
     *
     * @param player the player to be moved
     * @Author Emil
     */
    public void again(@NotNull Player player)
    {
        if (player.getSpace().board.getStep() > 0)
        {
            int index = player.getSpace().board.getStep() - 1;
            Command command = player.getProgramField(index).getCard().command;
            while (command == Command.AGAIN)
            {
                index--;
                if (index < 0)
                {
                    return;
                }
                command = player.getProgramField(index).getCard().command;
            }
            executeCommand(player, command);
        }
    }

    /**
     * Gives the player an energy cube.
     *
     * @param player the player to be powered up
     * @author Elias
     */
    public void powerUp(@NotNull Player player)
    {
        player.pickUpEnergyCube();
    }

    /**
     * Turns the player to the left or right depending on the command.
     *
     * @param player  the player to be turned
     * @param command the command to be executed
     * @author Emil
     */
    public void optionLeftOrRight(Player player, Command command)
    {
        if (command == Command.LEFT)
        {
            turnLeft(player);
        }
        else if (command == Command.RIGHT)
        {
            turnRight(player);
        }
    }

    /**
     * Adds a spam card to the player's discard pile.
     *
     * @param player the player to be affected by the spam card
     * @author Mustafa
     */
    public void useSpamCard(@NotNull Player player)
    {
        Card card = player.drawTopCard();
        executeCommand(player, card.command);
    }

    /**
     * Adds 2 spam cards to the player's discard pile.
     *
     * @param player the player to be affected by the Trojan Horse card
     * @author Mustafa
     */
    public void useTrojanHorse(@NotNull Player player)
    {
        player.addDamageCardToPile(Command.SPAM, 2);
    }

    /**
     * Reboots the player by setting the player's heading and space to the values stored in the reboot token.
     *
     * @param player the player to be rebooted
     * @author Mustafa
     */
    public void useWormCard(@NotNull Player player)
    {
        player.die();
    }

    /**
     * The virus card affects all players within a 6x6 square around the player who played the card.
     *
     * @param currentplayer the player who played the card
     * @author Mustafa
     */
    public void useVirusCard(@NotNull Player currentplayer)
    {
        int numberOfPlayers = gameController.board.getPlayersNumber();
        for (int i = 0; i < numberOfPlayers; i++)
        {
            Player player = gameController.board.getPlayer(i);
            if (player != currentplayer)
            {
                int xDist = Math.abs(currentplayer.getSpace().x - player.getSpace().x);
                int yDist = Math.abs(currentplayer.getSpace().y - player.getSpace().y);
                if (xDist <= 6 && yDist <= 6)
                {
                    player.addDamageCardToPile(Command.SPAM, 1);
                }
            }
        }
        executeCommand(currentplayer, currentplayer.drawTopCard().command);
    }

    /**
     * Moves the player a given amount of times in the direction of the player's heading.
     *
     * @param player              the player to be moved
     * @param heading             the heading of the player
     * @param amountOfTimesToMove the amount of times the player should be moved
     * @author Elias og Emil
     */
    public void movePlayerAmountOfTimesWithHeading(Player player, Heading heading, int amountOfTimesToMove)
    {
        for (int i = 0; i < amountOfTimesToMove; i++)
        {
            Space currentSpace = player.getSpace();
            Space newSpace = gameController.board.getNeighbour(currentSpace, heading);
            if (newSpace == null)
            {
                return;
            }
            if (currentSpace.getBoardElement().getCanWalkOutOf(heading) && newSpace.getBoardElement().getCanWalkInto(heading))
            {
                //Logic for moving to a space should be put here:
                if (newSpace.getPlayer() != null)
                {
                    movePlayerAmountOfTimesWithHeading(newSpace.getPlayer(), heading, 1);
                }
                if (newSpace.getPlayer() != null)
                {
                    newSpace.getPlayer().die();
                }
                player.setSpace(newSpace);
                newSpace.getBoardElement().onWalkOver(player);
            }
            if (player.getProgramField(0).getCard().command == Command.DEATH)
            {
                break;
            }
        }
    }

    /**
     * @param space
     * @author Frederik
     */
    public void moveCurrentPlayerToSpace(Space space)
    {
        Player currentPlayer = gameController.board.getCurrentPlayer();
        currentPlayer.setSpace(space);
    }

    /**
     * @author Elias
     */
    class ImpossibleMoveException extends Exception
    {
        private final Player player;
        private final Space space;
        private final Heading heading;

        /**
         * @param player
         * @param space
         * @param heading
         * @author Frederik
         */
        public ImpossibleMoveException(Player player, Space space, Heading heading)
        {
            super("Move impossible");
            this.player = player;
            this.space = space;
            this.heading = heading;
        }
    }
}
