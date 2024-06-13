package dk.dtu.compute.se.pisd.roborally.fileaccess;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.GameStateInfo;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.Player;

import java.io.*;

public class LoadSaveGameState
{

    public static void saveGameState(GameController gameController, String name)
    {
        saveGameStateToFile(gameController.board, name);
        //LoadBoard.saveBoard(gameController.board, name);
        for (int i = 0; i < gameController.board.getPlayersNumber(); i++)
        {
            LoadSavePlayer.savePlayer(gameController.board.getPlayer(i), name + i);
        }
        for (int i = gameController.board.getPlayersNumber(); i < 10; i++)
        {
            File file = new File("src/main/Resources/Players/default" + i + ".json");
            file.delete();
        }
    }

    public static void saveGameStateToFile(Board board, String name)
    {
        GameStateInfo template = new GameStateInfo();
        //template.gameID = board.getGameId();
        template.step = board.getStep();
        template.phase = board.getPhase();
        template.boardName = board.boardName;
        String filename = "src/main/Resources/" + "GameStateInfo" + "/" + name + "." + "json";

        // In simple cases, we can create a Gson object with new:
        //
        //   Gson gson = new Gson();
        //
        // But, if you need to configure it, it is better to create it from
        // a builder (here, we want to configure the JSON serialisation with
        // a pretty printer):
        GsonBuilder simpleBuilder =
                new GsonBuilder().registerTypeAdapter(Board.class, new Adapter<Board>()).setPrettyPrinting();
        Gson gson = simpleBuilder.create();

        FileWriter fileWriter = null;
        JsonWriter writer = null;
        try
        {
            fileWriter = new FileWriter(filename);
            writer = gson.newJsonWriter(fileWriter);
            gson.toJson(template, template.getClass(), writer);
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

    public static GameController loadGameState(String name)
    {
        GameStateInfo gameStateInfo = loadGameStateFromFile(name);
        GameController gameController = new GameController(LoadBoard.loadBoard(gameStateInfo.boardName));
        gameController.board.setPhase(gameStateInfo.phase);
        gameController.board.setStep(gameStateInfo.step);
        Player[] players = new Player[10];
        Player playerTurn = null;

        //TODO Currently hardcoded to not allow more than 10 players
        for (int i = 0; i < 10; i++)
        {
            Player player = LoadSavePlayer.loadPlayer(gameController, name + i);
            if (player != null)
            {
                //gameController.board.addPlayer(player);
                players[i] = player;
                if (player.isThisPlayerTurn())
                {
                    playerTurn = player;
                }

            }
        }
        gameController.board.setPlayers(players);
        gameController.board.setCurrentPlayer(playerTurn);
        return gameController;

    }

    public static GameStateInfo loadGameStateFromFile(String boardname)
    {
        InputStream inputStream; // = classLoader.getResourceAsStream(PLAYERFOLDER + "/" + name + "." + JSON_EXT);
        try
        {
            inputStream = new FileInputStream("src/main/Resources/GameStateInfo/" + boardname + ".json");
        }
        catch (FileNotFoundException e)
        {
            return null;
        }

        GsonBuilder simpleBuilder = new GsonBuilder().registerTypeAdapter(Board.class, new Adapter<Board>());
        Gson gson = simpleBuilder.create();
        // FileReader fileReader = null;
        JsonReader reader = null;
        try
        {
            // fileReader = new FileReader(filename);
            reader = gson.newJsonReader(new InputStreamReader(inputStream));
            GameStateInfo template = gson.fromJson(reader, GameStateInfo.class);
            reader.close();
            return template;
        }
        catch (IOException e1)
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
