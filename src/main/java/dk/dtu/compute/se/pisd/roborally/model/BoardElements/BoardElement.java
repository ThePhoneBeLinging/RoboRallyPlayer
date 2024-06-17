package dk.dtu.compute.se.pisd.roborally.model.BoardElements;

import dk.dtu.compute.se.pisd.roborally.fileaccess.model.ElementsEnum;
import dk.dtu.compute.se.pisd.roborally.model.Heading;
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

    public Space getSpace()
    {
        return space;
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
