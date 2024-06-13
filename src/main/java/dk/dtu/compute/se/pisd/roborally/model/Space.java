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
import dk.dtu.compute.se.pisd.roborally.model.BoardElements.BoardElement;
import dk.dtu.compute.se.pisd.roborally.model.BoardElements.NullBoardElement;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */

// in the code provided to us by the teacher, this class has walls and actions (see fieldAction class for explanation)
// as arraylists of Heading and FieldAction respectively. Maybe we should do that as well??
public class Space extends Subject
{

    public final Board board;

    public final int x;
    public final int y;

    private Player player;
    private BoardElement boardElement;

    /**
     * @param board the board to which this space belongs
     * @param x     the x-coordinate of this space
     * @param y     the y-coordinate of this space
     * @author Elias
     */
    public Space(Board board, int x, int y)
    {
        this.board = board;
        this.x = x;
        this.y = y;
        player = null;
        boardElement = new NullBoardElement(this);
    }

    /**
     * @return the player on this space
     * @author Frederik
     */
    public BoardElement getBoardElement()
    {
        return boardElement;
    }

    /**
     * @param boardElement
     * @author Frederik
     */
    public void setBoardElement(BoardElement boardElement)
    {
        if (this.boardElement != null)
        {
            this.board.deleteBoardElement(this.boardElement);
        }
        this.boardElement = boardElement;
        notifyChange();
    }

    /**
     * @return
     * @author Frederik
     */
    public Player getPlayer()
    {
        return player;
    }

    /**
     * @param player the player to be placed on this space
     * @author Elias & Frederik
     */
    public void setPlayer(Player player)
    {
        Player oldPlayer = this.player;
        if (player != oldPlayer && (player == null || board == player.board))
        {
            this.player = player;
            if (oldPlayer != null)
            {
                // this should actually not happen
                oldPlayer.setSpace(null);
            }
            if (player != null)
            {
                player.setSpace(this);
            }
            notifyChange();
        }
    }
/*

// This is from the file given to us by the teachers, can be used if we choose to implement walls and actions as
arraylists
public List<Heading> getWalls() {
    return walls;
}

    public List<FieldAction> getActions() {
        return actions;
    }
*/

    /**
     * @author Elias
     */
    void playerChanged()
    {
        // This is a minor hack; since some views that are registered with the space
        // also need to update when some player attributes change, the player can
        // notify the space of these changes by calling this method.
        notifyChange();
    }


}
