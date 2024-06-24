package dk.dtu.compute.se.pisd.roborally.model;

import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;

public class UpgradeCard extends Subject
{

    private final String name;
    private final int price;
    private Long playerID;

    /**
     * @param name
     * @param price
     * @author Mads
     */
    UpgradeCard(String name, int price)
    {
        this.name = name;
        this.price = price;
    }

    /**
     * @return
     * @author Mads
     */
    public String getName()
    {
        return this.name;
    }


    /**
     * @return
     * @author Mads
     */
    public int getPrice()
    {
        return this.price;
    }

    public Long getPlayerID()
    {
        return playerID;
    }

    public void setPlayerID(Long playerID)
    {
        this.playerID = playerID;
    }
}
