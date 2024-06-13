package dk.dtu.compute.se.pisd.roborally.model;

import java.util.ArrayList;

/**
 * @author Elias & Emil
 */
public class Deck
{
    public final ArrayList<Card> playerCards = new ArrayList<>();

    /**
     * @author Elias
     */
    public Deck()
    {

    }

    /**
     * @author Elias & Emil
     */
    public void initializeAPlayerDeck()
    {
        playerCards.clear();
        int amountOfForwards = 5;
        int amountOfRight = 5;
        int amountOfLeft = 5;
        int amountOfFastForward = 5;
        int amountOfMoveThree = 5;
        int amountOfUTurn = 5;
        int amountOfMoveBack = 5;
        int amountOfAgain = 5;
        int amountOfPowerUp = 5;
        int amountOfOptionLeftOrRight = 5;
        for (int i = 0; i < amountOfForwards; i++)
        {
            playerCards.add(new Card(Command.FORWARD));
        }
        for (int i = 0; i < amountOfRight; i++)
        {
            playerCards.add(new Card(Command.RIGHT));
        }
        for (int i = 0; i < amountOfLeft; i++)
        {
            playerCards.add(new Card(Command.LEFT));
        }
        for (int i = 0; i < amountOfFastForward; i++)
        {
            playerCards.add(new Card(Command.FAST_FORWARD));
        }
        for (int i = 0; i < amountOfMoveThree; i++)
        {
            playerCards.add(new Card(Command.MOVE_THREE));
        }
        for (int i = 0; i < amountOfUTurn; i++)
        {
            playerCards.add(new Card(Command.U_TURN));
        }
        for (int i = 0; i < amountOfMoveBack; i++)
        {
            playerCards.add(new Card(Command.MOVE_BACK));
        }
        for (int i = 0; i < amountOfAgain; i++)
        {
            playerCards.add(new Card(Command.AGAIN));
        }
        for (int i = 0; i < amountOfPowerUp; i++)
        {
            playerCards.add(new Card(Command.POWER_UP));
        }
        for (int i = 0; i < amountOfOptionLeftOrRight; i++)
        {
            playerCards.add(new Card(Command.OPTION_LEFT_RIGHT));
        }
    }

    /**
     * @param otherDeck
     * @author Elias
     */
    public void shuffleDeckIntoAnotherDeck(Deck otherDeck)
    {
        this.playerCards.addAll(otherDeck.playerCards);
        this.shuffleDeck();
        otherDeck.playerCards.clear();
    }

    /**
     * @author Elias
     */
    public void shuffleDeck()
    {
        for (int i = 0; i < playerCards.size(); i++)
        {
            int randomIndex = (int) (Math.random() * playerCards.size());
            Card tempCard = playerCards.get(randomIndex);
            playerCards.set(randomIndex, playerCards.get(i));
            playerCards.set(i, tempCard);
        }
    }

    /**
     * @return
     * @author Elias
     */
    public Card drawTopCard()
    {
        if (playerCards.isEmpty())
        {
            return null;
        }
        Card tempCard = playerCards.get(0);
        playerCards.remove(0);
        return tempCard;
    }

}
