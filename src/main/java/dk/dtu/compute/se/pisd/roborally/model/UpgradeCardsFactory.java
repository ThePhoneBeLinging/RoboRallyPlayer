package dk.dtu.compute.se.pisd.roborally.model;

import java.util.ArrayList;

/**
 * @author Mads
 */
public class UpgradeCardsFactory
{

    /**
     * @return
     * @author Mads
     */
    public static ArrayList<UpgradeCard> createUpgradeCards()
    {

        ArrayList<UpgradeCard> upgradeCards = new ArrayList<>();

        upgradeCards.add(new UpgradeCard("BRAKES", 3));

        upgradeCards.add(new UpgradeCard("FIREWALL", 3));

        upgradeCards.add(new UpgradeCard("MEMORY STICK", 3));

        upgradeCards.add(new UpgradeCard("HOVER UNIT", 1));

        upgradeCards.add(new UpgradeCard("DOUBLE BARREL LASER", 2));

        return upgradeCards;
    }

}
