package dk.dtu.compute.se.pisd.roborally.fileaccess.model;

import dk.dtu.compute.se.pisd.roborally.model.Command;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.UpgradeCard;

import java.util.ArrayList;
import java.util.List;

public class PlayerTemplate
{
    public List<UpgradeCard> upgradeCards = new ArrayList<>();
    public int lastVisitedCheckpoint;
    //Using commands allows me to not create a CardTemplate class
    public List<Command> activeCards = new ArrayList<>();
    public List<Command> discardedCards = new ArrayList<>();
    public Command[] registers = new Command[Player.NO_REGISTERS];
    public Command[] cardsOnHand = new Command[Player.NO_CARDS];

    public String name;
    public String color;
    public Heading heading;
    public SpaceTemplate spaceTemplate;
    public int tabNumber;
    public boolean movedByConveyorThisTurn;
    public int energyCubes;
    public boolean playersTurn;
}
