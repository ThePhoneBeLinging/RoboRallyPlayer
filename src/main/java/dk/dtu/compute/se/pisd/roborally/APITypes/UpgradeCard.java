package dk.dtu.compute.se.pisd.roborally.APITypes;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This class represents an upgrade card that a player can buy in the game.
 * The upgrade card has a name and a price.
 * The playerID is null if the card is not bought.
 * If a player buys the card, the playerID will be set to the player that bought the card.
 * @Author Mustafa
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpgradeCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long gameID;
    private Long playerID;
    private String cardName;
    private int price;
}
