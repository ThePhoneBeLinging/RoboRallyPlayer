package dk.dtu.compute.se.pisd.roborally.APITypes;


import dk.dtu.compute.se.pisd.roborally.APITypes.Player.Card;
import dk.dtu.compute.se.pisd.roborally.APITypes.Player.Player;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * This class represents a complete game.
 * The complete game is used to send all the information between the server and the client.
 * @Author Elias
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompleteGame
{
    private Long gameID;
    private int turnID;
    private Board board;
    private List<EnergyCube> energyCubes;
    private List<Player> playerList;
    private List<Card> cards;
    private List<UpgradeCard> upgradeCards;
    private List<String> commandsToChooseBetween;
}
