package dk.dtu.compute.se.pisd.roborally.model.BoardElements;

import dk.dtu.compute.se.pisd.roborally.fileaccess.model.ElementsEnum;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.Space;
import javafx.scene.image.Image;

/**
 * @author Elias & Frederik
 */
public abstract class BoardElement
{
    private final boolean isWalkable;
    private final Heading heading;
    private final Space space;
    private Image image;
    private ElementsEnum type;

    //All method calls to this method should be looked at, to ensure it is the proper functionality
    protected BoardElement(Heading heading, boolean isWalkable, Space space)
    {
        this(heading, isWalkable, space, null);
    }

    protected BoardElement(Heading heading, boolean isWalkable, Space space, Image image)
    {
        this.isWalkable = isWalkable;
        this.heading = heading;
        this.space = space;
        this.space.setBoardElement(this);
        this.setImage(image);
    }

    /**
     * @param heading
     * @return
     * @author Elias
     */
    public boolean getCanWalkOutOf(Heading heading)
    {
        return this.isWalkable && this.heading != heading;
    }

    /**
     * @param heading
     * @return
     * @author Elias & Adel
     */
    public boolean getCanWalkInto(Heading heading)
    {
        Heading headingToCheck = heading.next().next();
        return this.isWalkable && this.heading != headingToCheck;
    }

    /**
     * @return
     * @author Elias
     */
    public Space getSpace()
    {
        return this.space;
    }

    /**
     * @author Elias
     */
    public void activate()
    {
        //
    }

    /**
     * @param player
     * @author Elias
     */
    public void onWalkOver(Player player)
    {
        
    }

    /**
     * @return
     * @author Frederik
     */
    public boolean getIsWalkable()
    {
        return this.isWalkable;
    }

    /**
     * @return
     * @author Frederik
     */
    public Heading getHeading()
    {
        return this.heading;
    }

    /**
     * @return
     * @author Elias
     */
    public Image getImage()
    {
        return image;
    }

    /**
     * @param image
     * @author Elias
     */
    public void setImage(Image image)
    {
        if (image == null)
        {
            this.image = new Image("file:src/main/Resources/Images/empty.png");
        }
        else
        {
            this.image = image;
        }
    }
}
