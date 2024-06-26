package dk.dtu.compute.se.pisd.roborally.APITypes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * This class represents a lobby.
 * The lobby is used to store the information about the players that are in the lobby.
 * @Author Elias
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Lobby {
    private Long id;
    private Long gameID;
    private Long playerID;
}
