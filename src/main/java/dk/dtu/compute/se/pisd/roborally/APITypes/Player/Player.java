package dk.dtu.compute.se.pisd.roborally.APITypes.Player;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This class represents a player in the game.
 * The player also has an ID that is unique.
 * The player is an entity and is stored in the database.
 *
 * @Author Elias
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Player
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long playerID;
    private long gameID;
    private int turnID;
    private int x;
    private int y;
    public int lastVisitedCheckpoint;
    public String name;
    public String color;
    public String heading;
    public int tabNumber;
    public boolean movedByConveyorThisTurn;
    public int energyCubes;
    public boolean playersTurn;
    public boolean hasWon;
}
