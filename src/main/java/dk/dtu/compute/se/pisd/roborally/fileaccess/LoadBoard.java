/**
 * This file is part of the initial project provided for the
 * course "Project in Software Development (02362)" held at
 * DTU Compute at the Technical University of Denmark.
 * <p>
 * Copyright (C) 2019, 2020: Ekkart Kindler, ekki@dtu.dk
 * <p>
 * This software is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; version 2 of the License.
 * <p>
 * This project is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with this project; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */
package dk.dtu.compute.se.pisd.roborally.fileaccess;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.BoardElementTemplate;
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.BoardTemplate;
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.ElementsEnum;
import dk.dtu.compute.se.pisd.roborally.fileaccess.model.SpaceTemplate;
import dk.dtu.compute.se.pisd.roborally.model.Board;
import dk.dtu.compute.se.pisd.roborally.model.BoardElements.*;
import dk.dtu.compute.se.pisd.roborally.model.BoardElements.Conveyors.BlueConveyor;
import dk.dtu.compute.se.pisd.roborally.model.BoardElements.Conveyors.GreenConveyor;
import dk.dtu.compute.se.pisd.roborally.model.BoardElements.Walls.CornerWall;
import dk.dtu.compute.se.pisd.roborally.model.BoardElements.Walls.Wall;
import dk.dtu.compute.se.pisd.roborally.model.Space;

import java.io.*;
import java.util.ArrayList;

/**
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class LoadBoard
{

    private static final String BOARDSFOLDER = "boards";
    private static final String DEFAULTBOARD = "default";
    private static final String JSON_EXT = "json";

    /**
     * @param boardname
     * @return
     * @author Elias & Frederik
     */
    public static Board loadBoard(String boardname)
    {
        if (boardname == null)
        {
            boardname = DEFAULTBOARD;
        }
        InputStream inputStream; // = classLoader.getResourceAsStream(PLAYERFOLDER + "/" + name + "." + JSON_EXT);
        try
        {
            inputStream = new FileInputStream("src/main/Resources/boards/" + boardname + ".json");
        }
        catch (FileNotFoundException e)
        {
            return new Board(8, 8);
        }
        // In simple cases, we can create a Gson object with new Gson():
        GsonBuilder simpleBuilder = new GsonBuilder().registerTypeAdapter(Board.class, new Adapter<Board>());
        Gson gson = simpleBuilder.create();
        Board result;
        // FileReader fileReader = null;
        JsonReader reader = null;
        try
        {
            // fileReader = new FileReader(filename);
            reader = gson.newJsonReader(new InputStreamReader(inputStream));
            BoardTemplate template = gson.fromJson(reader, BoardTemplate.class);
            result = new Board(template.width, template.height, boardname);
            for (int i = 0; i < Board.NOT_ACTIVATE_ABLE_INDEX + 1; i++)
            {
                for (BoardElementTemplate boardElementTemplate : template.boardElements[i])
                {
                    Space space = result.getSpace(boardElementTemplate.spaceTemplate.x,
                            boardElementTemplate.spaceTemplate.y);
                    switch (i)
                    {
                        case Board.GREEN_CONVEYOR_INDEX:
                        {
                            new GreenConveyor(boardElementTemplate.heading, space);
                            break;
                        }
                        case Board.BLUE_CONVEYOR_INDEX:
                        {
                            new BlueConveyor(boardElementTemplate.heading, space);
                            break;
                        }
                        case Board.PUSH_PANELS_INDEX:
                        {
                            new PushPanel(boardElementTemplate.heading, space);
                            break;
                        }
                        case Board.GEARS_INDEX:
                        {
                            new Gear(space, boardElementTemplate.isClockwise);
                            break;
                        }
                        case Board.BOARD_LASER_INDEX:
                        {
                            new BoardLaser(space, boardElementTemplate.heading);
                            break;
                        }
                        case Board.ENERGY_SPACE_INDEX:
                        {
                            new EnergyCube(space);
                            break;
                        }
                        case Board.CHECKPOINTS_INDEX:
                        {
                            new Checkpoint(space);
                            break;
                        }
                        case Board.ANTENNA_INDEX:
                        {
                            new Antenna(space);
                            break;
                        }
                        case Board.NOT_ACTIVATE_ABLE_INDEX:
                        {
                            if (boardElementTemplate.type == null)
                            {
                                break;
                            }
                            switch (boardElementTemplate.type)
                            {
                                case WALL:
                                    new Wall(boardElementTemplate.heading, space);
                                    break;
                                case CORNERWALL:
                                    new CornerWall(boardElementTemplate.heading, boardElementTemplate.heading2, space);
                                    break;
                                case REBOOTTOKEN:
                                    new RebootToken(boardElementTemplate.heading, space);
                                    break;
                                case SPAWNPOINT:
                                    new SpawnPoint(space);
                                    break;
                                case PIT:
                                    new Pit(space);
                                    break;
                            }
                        }
                    }
                }
            }
            reader.close();
            return result;
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

    /**
     * @param board
     * @param name
     * @author Elias & Frederik
     */
    public static void saveBoard(Board board, String name)
    {
        BoardTemplate template = new BoardTemplate();
        for (int i = 0; i < Board.NOT_ACTIVATE_ABLE_INDEX + 1; i++)
        {
            template.boardElements[i] = new ArrayList<>();
        }
        template.width = board.width;
        template.height = board.height;
        for (int i = 0; i < Board.NOT_ACTIVATE_ABLE_INDEX + 1; i++)
        {
            for (BoardElement boardElement : board.getBoardElementsWithIndex(i))
            {
                SpaceTemplate spaceTemplate = new SpaceTemplate();
                spaceTemplate.x = boardElement.getSpace().x;
                spaceTemplate.y = boardElement.getSpace().y;
                BoardElementTemplate boardElementTemplate = new BoardElementTemplate();
                boardElementTemplate.spaceTemplate = spaceTemplate;
                boardElementTemplate.isWalkable = boardElement.getIsWalkable();
                boardElementTemplate.heading = boardElement.getHeading();
                switch (i)
                {
                    case Board.GEARS_INDEX:
                    {
                        boardElementTemplate.isClockwise = ((Gear) boardElement).isClockwise;
                        break;
                    }
                    case Board.NOT_ACTIVATE_ABLE_INDEX:
                    {
                        if (boardElement instanceof CornerWall)
                        {
                            boardElementTemplate.heading2 = ((CornerWall) boardElement).getHeading2();
                            boardElementTemplate.type = ElementsEnum.CORNERWALL;
                        }
                        if (boardElement instanceof Wall)
                        {
                            boardElementTemplate.type = ElementsEnum.WALL;
                        }
                        break;
                    }
                }
                template.boardElements[i].add(boardElementTemplate);
            }

        }

        String filename = "src/main/Resources/" + BOARDSFOLDER + "/" + name + "." + JSON_EXT;

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
}
