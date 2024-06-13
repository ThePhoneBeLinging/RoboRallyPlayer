package dk.dtu.compute.se.pisd.roborally.model.BoardElements;

import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Position;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import javafx.scene.image.Image;

/**
 * @author Elias
 */
public class Antenna extends NullBoardElement
{
    private Player[] players;
    private Position[] offsetArr;

    public Antenna(Space space)
    {
        super(false, space);
        this.setImage(new Image("file:src/main/Resources/Images/antenna.png"));
        space.board.addBoardElement(Board.ANTENNA_INDEX, this);
    }

    /**
     * @author Elias
     */
    @Override
    public void activate()
    {
        Board board = this.getSpace().board;
        Position positionOfSpace = board.getIndexOfSpace(this.getSpace());
        this.players = new Player[board.getPlayersNumber()];
        this.offsetArr = new Position[board.getPlayersNumber()];
        for (int i = 0; i < board.getPlayersNumber(); i++)
        {
            this.players[i] = null;
            this.offsetArr[i] = null;
        }
        for (int i = 0; i < board.getPlayersNumber(); i++)
        {
            insertPlayerIntoTempArr(board.getPlayer(i));
        }
        board.setPlayers(this.players);
    }

    /**
     * @param playerToInsert
     * @author Elias
     */
    private void insertPlayerIntoTempArr(Player playerToInsert)
    {
        Board board = this.getSpace().board;
        Position positionOfPlayer = board.getIndexOfSpace(playerToInsert.getSpace());
        if (positionOfPlayer == null)
        {
            //This should not happen, perhaps if player has fallen into pit
            return;
        }
        Position positionOfAntenna = board.getIndexOfSpace(this.getSpace());
        Position offset = new Position(Math.abs(positionOfPlayer.x() - positionOfAntenna.x()),
                Math.abs(positionOfPlayer.y() - positionOfAntenna.y()));

        for (int i = 0; i < this.players.length; i++)
        {
            if (players[i] == null)
            {
                players[i] = playerToInsert;
                offsetArr[i] = offset;
                return;
            }
            for (Player player : players)
            {
                if (player == null)
                {
                    continue;
                }
                if (player.equals(playerToInsert))
                {
                    return;
                }
            }
            int totalOffset = offset.x() + offset.y();
            if ((offsetArr[i].x() + offsetArr[i].y()) > totalOffset)
            {
                Player tempPlayer = players[i];
                players[i] = playerToInsert;
                offsetArr[i] = offset;
                insertPlayerIntoTempArr(tempPlayer);
            }
            if ((offsetArr[i].x() + offsetArr[i].y()) == totalOffset)
            {
                if (board.getIndexOfSpace(players[i].getSpace()).x() < positionOfPlayer.x())
                {
                    Player tempPlayer = players[i];
                    players[i] = playerToInsert;
                    offsetArr[i] = offset;
                    insertPlayerIntoTempArr(tempPlayer);
                }
            }
        }
    }
}
