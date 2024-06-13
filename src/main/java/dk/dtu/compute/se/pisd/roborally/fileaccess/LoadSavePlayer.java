package dk.dtu.compute.se.pisd.roborally.fileaccess;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.PlayerTemplate;
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.SpaceTemplate;
import dk.dtu.compute.se.pisd.roborally.model.Card;
import dk.dtu.compute.se.pisd.roborally.model.Command;
import dk.dtu.compute.se.pisd.roborally.model.Player;

import java.io.*;

public class LoadSavePlayer
{
    private static final String PLAYERFOLDER = "Players";
    private static final String JSON_EXT = ".json";

    public static void savePlayer(Player player, String name)
    {
        PlayerTemplate playerTemplate = new PlayerTemplate();


        String filename = "src/main/Resources/" + PLAYERFOLDER + "/" + name + JSON_EXT;

        playerTemplate.spaceTemplate = new SpaceTemplate();
        playerTemplate.spaceTemplate.x = player.getSpace().x;
        playerTemplate.spaceTemplate.y = player.getSpace().y;

        for (int i = 0; i < Player.NO_REGISTERS; i++)
        {
            if (player.getProgramField(i).getCard() == null)
            {
                playerTemplate.registers[i] = null;
                continue;
            }
            playerTemplate.registers[i] = player.getProgramField(i).getCard().command;
        }
        for (int i = 0; i < Player.NO_CARDS; i++)
        {
            if (player.getCardField(i).getCard() == null)
            {
                playerTemplate.cardsOnHand[i] = null;
                continue;
            }
            playerTemplate.cardsOnHand[i] = player.getCardField(i).getCard().command;
        }

        playerTemplate.color = player.getColor();
        playerTemplate.heading = player.getHeading();
        playerTemplate.name = player.getName();
        for (int i = 0; i < player.activeCardsPile.playerCards.size(); i++)
        {
            playerTemplate.activeCards.add(player.activeCardsPile.playerCards.get(i).command);
        }
        for (int i = 0; i < player.discardedCardsPile.playerCards.size(); i++)
        {
            playerTemplate.discardedCards.add(player.discardedCardsPile.playerCards.get(i).command);
        }
        playerTemplate.energyCubes = player.getEnergyCubes();
        playerTemplate.movedByConveyorThisTurn = player.getMovedByConveyorThisTurn();
        playerTemplate.tabNumber = player.getTabNumber();
        playerTemplate.playersTurn = player.board.getCurrentPlayer().equals(player);

        playerTemplate.lastVisitedCheckpoint = player.getLastVisitedCheckPoint();


        // In simple cases, we can create a Gson object with new:
        //
        //   Gson gson = new Gson();
        //
        // But, if you need to configure it, it is better to create it from
        // a builder (here, we want to configure the JSON serialisation with
        // a pretty printer):
        GsonBuilder simpleBuilder =
                new GsonBuilder().registerTypeAdapter(Player.class, new Adapter<Player>()).setPrettyPrinting();
        Gson gson = simpleBuilder.create();

        FileWriter fileWriter = null;
        JsonWriter writer = null;
        try
        {
            fileWriter = new FileWriter(filename);
            writer = gson.newJsonWriter(fileWriter);
            gson.toJson(playerTemplate, playerTemplate.getClass(), writer);
            writer.close();
        }
        catch (IOException e1)
        {
            if (writer != null)
            {
                try
                {
                    writer.close();
                    fileWriter = null;
                }
                catch (IOException e2)
                {
                }
            }
            if (fileWriter != null)
            {
                try
                {
                    fileWriter.close();
                }
                catch (IOException e2)
                {
                }
            }
        }
    }

    public static Player loadPlayer(GameController gameController, String name)
    {
        InputStream inputStream; // = classLoader.getResourceAsStream(PLAYERFOLDER + "/" + name + "." + JSON_EXT);
        try
        {
            inputStream = new FileInputStream("src/main/Resources/Players/" + name + JSON_EXT);
        }
        catch (FileNotFoundException e)
        {
            return null;
        }
        // In simple cases, we can create a Gson object with new Gson():
        GsonBuilder simpleBuilder = new GsonBuilder().registerTypeAdapter(Player.class, new Adapter<Player>());
        Gson gson = simpleBuilder.create();
        Player result;
        // FileReader fileReader = null;
        JsonReader reader = null;
        try
        {
            reader = gson.newJsonReader(new InputStreamReader(inputStream));
            PlayerTemplate playerTemplate = gson.fromJson(reader, PlayerTemplate.class);
            result = new Player(gameController.board, playerTemplate.color, playerTemplate.name,
                    gameController.moveController);
            result.activeCardsPile.playerCards.clear();
            for (Command command : playerTemplate.activeCards)
            {
                result.activeCardsPile.playerCards.add(new Card(command));
            }
            for (Command command : playerTemplate.discardedCards)
            {
                result.discardedCardsPile.playerCards.add(new Card(command));
            }
            int cardFieldToModify = 0;
            for (Command command : playerTemplate.registers)
            {
                if (command == null)
                {
                    cardFieldToModify++;
                    continue;
                }
                result.getProgramField(cardFieldToModify).setCard(new Card(command));
                cardFieldToModify++;
            }
            cardFieldToModify = 0;
            for (Command command : playerTemplate.cardsOnHand)
            {
                if (command == null)
                {
                    cardFieldToModify++;
                    continue;
                }
                result.getCardField(cardFieldToModify).setCard(new Card(command));
                cardFieldToModify++;
            }
            result.setHeading(playerTemplate.heading);
            result.setTabNumber(playerTemplate.tabNumber);
            result.setEnergyCubes(playerTemplate.energyCubes);
            result.setMovedByConveyorThisTurn(playerTemplate.movedByConveyorThisTurn);
            result.setSpace(gameController.board.getSpace(playerTemplate.spaceTemplate.x,
                    playerTemplate.spaceTemplate.y));
            result.setLastVisitedCheckPoint(playerTemplate.lastVisitedCheckpoint);

            if (playerTemplate.playersTurn)
            {
                result.setThisPlayerTurn(true);
            }
            return result;
        }
        catch (Exception e1)
        {
            if (reader != null)
            {
                try
                {
                    reader.close();
                    inputStream = null;
                }
                catch (IOException e2)
                {
                }
            }
            if (inputStream != null)
            {
                try
                {
                    inputStream.close();
                }
                catch (IOException e2)
                {
                }
            }
        }

        return null;
    }
}
