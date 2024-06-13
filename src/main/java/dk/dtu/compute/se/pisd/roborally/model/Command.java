/*
 *  This file is part of the initial project provided for the
 *  course "Project in Software Development (02362)" held at
 *  DTU Compute at the Technical University of Denmark.
 *
 *  Copyright (C) 2019, 2020: Ekkart Kindler, ekki@dtu.dk
 *
 *  This software is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; version 2 of the License.
 *
 *  This project is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this project; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package dk.dtu.compute.se.pisd.roborally.model;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public enum Command
{

    // This is a very simplistic way of realizing different commands.

    FORWARD("Fwd"),
    RIGHT("Turn Right"),
    LEFT("Turn Left"),
    FAST_FORWARD("Fast Fwd"),
    MOVE_THREE("Move 3"),
    U_TURN("U-Turn"),
    MOVE_BACK("Move Back"),
    AGAIN("Again"),
    POWER_UP("Power Up"),
    OPTION_LEFT_RIGHT("Left OR Right", LEFT, RIGHT),
    TROJAN_HORSE("Trojan Horse"),
    SPAM("SPAM"),
    VIRUS("Virus"),
    WORM("Worm"),
    DONT_MOVE("Dont Move"),
    OPTION_FORWARD_OR_NOT("Forward or Not", FORWARD, DONT_MOVE),

    DEATH("You reeboted, you dont get to move");

    final public String displayName;

    final private List<Command> options;

    /**
     * @param displayName
     * @param options
     * @author Elias & Frederik
     */
    Command(String displayName, Command... options)
    {
        this.displayName = displayName;
        this.options = Collections.unmodifiableList(Arrays.asList(options));
    }

    /**
     * @return boolean as of whether the command has more than one option
     * @author Frederik
     */
    public boolean isInteractive()
    {
        return !options.isEmpty();
    }

    /**
     * @return list of all the command-options the player must choose between.
     * @author Frederik
     */
    public List<Command> getOptions()
    {
        return options;
    }
}
