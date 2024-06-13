package dk.dtu.compute.se.pisd.roborally;

import dk.dtu.compute.se.pisd.roborally.APITypes.CompleteGame;
import dk.dtu.compute.se.pisd.roborally.APITypes.Player.Card;
import dk.dtu.compute.se.pisd.roborally.APITypes.Player.Player;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.fileaccess.LoadBoard;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Command;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Phase;

public class ConversionUtil
{
    public static Board fromServerBoardToGameBoard(CompleteGame serverBoard)
    {
        Board gameBoard = LoadBoard.loadBoard(serverBoard.getBoard().getBoardname());
        GameController gameController = new GameController(gameBoard);
        gameBoard.setStep(serverBoard.getBoard().getStep());
        gameBoard.setPhase(Phase.valueOf(serverBoard.getBoard().getPhase()));

        for (Player player : serverBoard.getPlayerList())
        {
            dk.dtu.compute.se.pisd.roborally.model.Player gameBoardPlayer =
                    new dk.dtu.compute.se.pisd.roborally.model.Player(gameBoard, "player.getName()",
                            gameController.moveController);
            gameBoard.addPlayer(gameBoardPlayer);
            gameBoardPlayer.setSpace(gameBoard.getSpace(player.getX(), player.getY()));
            gameBoardPlayer.setLastVisitedCheckPoint(player.getLastVisitedCheckpoint());
            gameBoardPlayer.setHeading(Heading.valueOf(player.getHeading()));
            gameBoardPlayer.setMovedByConveyorThisTurn(player.isMovedByConveyorThisTurn());
            gameBoardPlayer.setEnergyCubes(player.getEnergyCubes());
            gameBoardPlayer.setThisPlayerTurn(player.isPlayersTurn());
            gameBoardPlayer.setPlayerID(player.getPlayerID());
        }

        for (Card card : serverBoard.getCards())
        {
            dk.dtu.compute.se.pisd.roborally.model.Card cardToAdd =
                    new dk.dtu.compute.se.pisd.roborally.model.Card(Command.valueOf(card.getCommand()));
            for (int i = 0; i < gameBoard.getPlayersNumber(); i++)
            {
                dk.dtu.compute.se.pisd.roborally.model.Player gamePlayer = gameBoard.getPlayer(i);
                switch (card.getLocation())
                {
                    case "REGISTER":
                        int k = 0;
                        while (gamePlayer.getProgramField(k) == null)
                        {
                            k++;
                        }
                        gamePlayer.getProgramField(k).setCard(cardToAdd);
                        break;
                    case "HAND":
                        int j = 0;
                        while (gamePlayer.getCardField(j) == null)
                        {
                            j++;
                        }
                        gamePlayer.getCardField(j).setCard(cardToAdd);
                        break;
                }
            }
        }
        return gameBoard;
    }
}
