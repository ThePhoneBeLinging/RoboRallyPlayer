package dk.dtu.compute.se.pisd.roborally.APITypes;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This class represents a board in the game.
 * TurnID is the ID of the turn that the board is in.
 * @Author Elias
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Board
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long gameID;
    private int turnID;
    private Long playerID;
    private String boardname;
    private int step;
    private String phase;


}
