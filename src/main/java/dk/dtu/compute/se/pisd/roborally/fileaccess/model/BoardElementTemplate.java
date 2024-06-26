package dk.dtu.compute.se.pisd.roborally.fileaccess.model;

import dk.dtu.compute.se.pisd.roborally.model.Heading;

/**
 * This class represents a board element template.
 * A board element template is used to create a board element.
 * @author Elias
 */
public class BoardElementTemplate
{
    public Heading heading;
    public boolean isWalkable;
    public SpaceTemplate spaceTemplate;
    public boolean isClockwise;
    public Heading heading2;
    public ElementsEnum type;

}
