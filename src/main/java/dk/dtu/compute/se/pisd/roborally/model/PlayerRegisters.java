package dk.dtu.compute.se.pisd.roborally.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;

/**
 * This class is used to store the registers of a player
 * It contains the cards that the player has chosen to register
 * The playerID and the gameID
 * @Author Frederik
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PlayerRegisters {
    private ArrayList<Integer> registerCards;
    private Long playerID;
    private Long gameID;
}

