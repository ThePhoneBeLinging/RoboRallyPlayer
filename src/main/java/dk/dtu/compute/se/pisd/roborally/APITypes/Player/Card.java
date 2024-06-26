package dk.dtu.compute.se.pisd.roborally.APITypes.Player;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This class represents a card that a player can play in the game.
 * The card has a command and a location.
 * The command is the action that the card will perform.
 * The location is describing where in the players deck the card is.
 * The card also has a playerID and a gameID.
 * The playerID is the ID of the player that has the card.
 * The gameID is the ID of the game that the card is played in.
 * The card has an ID that is unique.
 * The card is an entity and is stored in the database.
 *
 * @Author Elias
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Card
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private long gameID;
    private long playerID;
    private String command;
    private String location;
}
