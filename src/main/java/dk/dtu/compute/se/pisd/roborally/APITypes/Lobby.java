package dk.dtu.compute.se.pisd.roborally.APITypes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Lobby {
    private Long id;
    private Long gameID;
    private Long playerID;
}
