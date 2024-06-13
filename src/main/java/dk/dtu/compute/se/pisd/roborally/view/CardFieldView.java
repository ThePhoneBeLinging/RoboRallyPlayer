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
package dk.dtu.compute.se.pisd.roborally.view;

import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import dk.dtu.compute.se.pisd.roborally.controller.GameController;
import dk.dtu.compute.se.pisd.roborally.model.Card;
import dk.dtu.compute.se.pisd.roborally.model.CardField;
import dk.dtu.compute.se.pisd.roborally.model.Phase;
import dk.dtu.compute.se.pisd.roborally.model.Player;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 */
public class CardFieldView extends GridPane implements ViewObserver
{

    // This data format helps avoiding transfers of e.g. Strings from other
    // programs which can copy/paste Strings.
    final public static DataFormat ROBO_RALLY_CARD = new DataFormat("games/roborally/cards");

    final public static int CARDFIELD_WIDTH = 65;
    final public static int CARDFIELD_HEIGHT = 100;

    final public static Border BORDER_DEFAULT = new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID,
            null, new BorderWidths(2)));
    final public static Border BORDER_ACTIVE = new Border(new BorderStroke(Color.YELLOW, BorderStrokeStyle.SOLID,
            null, new BorderWidths(2)));
    final public static Border BORDER_DONE = new Border(new BorderStroke(Color.GREENYELLOW, BorderStrokeStyle.SOLID,
            null, new BorderWidths(2)));
    final public static Background BG_DEFAULT = new Background(new BackgroundFill(Color.WHITE, null, null));
    final public static Background BG_DRAG = new Background(new BackgroundFill(Color.GRAY, null, null));
    final public static Background BG_DROP = new Background(new BackgroundFill(Color.LIGHTGRAY, null, null));

    final public static Background BG_ACTIVE = new Background(new BackgroundFill(Color.YELLOW, null, null));
    final public static Background BG_DONE = new Background(new BackgroundFill(Color.GREENYELLOW, null, null));

    private final CardField field;

    private final Label label;
    private final ImageView imageView;

    private final GameController gameController;

    /**
     * @param gameController
     * @param field
     * @author Elias, Adel & Mustafa
     */
    public CardFieldView(@NotNull GameController gameController, @NotNull CardField field)
    {
        this.gameController = gameController;
        this.field = field;

        this.setAlignment(Pos.CENTER);
        this.setPadding(new Insets(5, 5, 5, 5));

        this.setBorder(BORDER_DEFAULT);
        this.setBackground(BG_DEFAULT);

        this.setPrefWidth(CARDFIELD_WIDTH);
        this.setMinWidth(CARDFIELD_WIDTH);
        this.setMaxWidth(CARDFIELD_WIDTH);
        this.setPrefHeight(CARDFIELD_HEIGHT);
        this.setMinHeight(CARDFIELD_HEIGHT);
        this.setMaxHeight(CARDFIELD_HEIGHT);

        label = new Label("This is a slightly longer text");
        label.setWrapText(true);
        label.setMouseTransparent(true);
        this.add(label, 0, 0);

        imageView = new ImageView();
        imageView.setFitWidth(CARDFIELD_WIDTH - 4);
        imageView.setFitHeight(CARDFIELD_HEIGHT - 4);
        imageView.setMouseTransparent(true);
        this.add(imageView, 0, 0);

        this.setOnDragDetected(new OnDragDetectedHandler());
        this.setOnDragOver(new OnDragOverHandler());
        this.setOnDragEntered(new OnDragEnteredHandler());
        this.setOnDragExited(new OnDragExitedHandler());
        this.setOnDragDropped(new OnDragDroppedHandler());
        this.setOnDragDone(new OnDragDoneHandler());

        field.attach(this);
        update(field);
    }

    /**
     * @param cardField
     * @return
     * @author Elias & Frederik
     */
    private String cardFieldRepresentation(CardField cardField)
    {
        if (cardField.player != null)
        {

            for (int i = 0; i < Player.NO_REGISTERS; i++)
            {
                CardField other = cardField.player.getProgramField(i);
                if (other == cardField)
                {
                    return "P," + i;
                }
            }

            for (int i = 0; i < Player.NO_CARDS; i++)
            {
                CardField other = cardField.player.getCardField(i);
                if (other == cardField)
                {
                    return "C," + i;
                }
            }
        }
        return null;

    }

    /**
     * @param rep
     * @return
     * @author Elias
     */
    private CardField cardFieldFromRepresentation(String rep)
    {
        if (rep != null && field.player != null)
        {
            String[] strings = rep.split(",");
            if (strings.length == 2)
            {
                int i = Integer.parseInt(strings[1]);
                if ("P".equals(strings[0]))
                {
                    if (i < Player.NO_REGISTERS)
                    {
                        return field.player.getProgramField(i);
                    }
                }
                else if ("C".equals(strings[0]))
                {
                    if (i < Player.NO_CARDS)
                    {
                        return field.player.getCardField(i);
                    }
                }
            }
        }
        return null;
    }

    /**
     * @param subject
     * @author Mustafa, Elias & Frederik
     */
    @Override
    public void updateView(Subject subject)
    {
        if (subject == field && subject != null)
        {
            Card card = field.getCard();

            if (card != null && field.isVisible())
            {
                label.setText(card.getName());
                imageView.setImage(card.getImage());
            }
            else
            {
                label.setText("");
                if (card != null && !field.isVisible())
                {
                    imageView.setImage(new Image("file:src/main/Resources/Images/facedownCard.png"));
                }
                else
                {
                    imageView.setImage(null);
                }

            }
        }
    }

    /**
     * @author Elias & Frederik
     */
    private class OnDragDetectedHandler implements EventHandler<MouseEvent>
    {

        /**
         * @param event
         * @author Elias & Frederik
         */
        @Override
        public void handle(MouseEvent event)
        {
            Object t = event.getTarget();
            if (t instanceof CardFieldView source)
            {
                CardField cardField = source.field;
                if (cardField != null && cardField.getCard() != null && cardField.player != null && cardField.player.board != null && cardField.player.board.getPhase().equals(Phase.PROGRAMMING))
                {
                    Dragboard db = source.startDragAndDrop(TransferMode.MOVE);
                    Image image = source.snapshot(null, null);
                    db.setDragView(image);

                    ClipboardContent content = new ClipboardContent();
                    content.put(ROBO_RALLY_CARD, cardFieldRepresentation(cardField));

                    db.setContent(content);
                    source.setBackground(BG_DRAG);
                }
            }
            event.consume();
        }

    }

    /**
     * @author Elias & Frederik
     */
    private class OnDragOverHandler implements EventHandler<DragEvent>
    {

        /**
         * @param event
         * @author Elias & Frederik
         */
        @Override
        public void handle(DragEvent event)
        {
            Object t = event.getTarget();
            if (t instanceof CardFieldView target)
            {
                CardField cardField = target.field;
                if (cardField != null && (cardField.getCard() == null || event.getGestureSource() == target) && cardField.player != null && cardField.player.board != null)
                {
                    if (event.getDragboard().hasContent(ROBO_RALLY_CARD))
                    {
                        event.acceptTransferModes(TransferMode.MOVE);
                    }
                }
            }
            event.consume();
        }

    }

    /**
     * @author Elias & Frederik
     */
    private class OnDragEnteredHandler implements EventHandler<DragEvent>
    {

        /**
         * @param event
         * @author Elias & Frederik
         */
        @Override
        public void handle(DragEvent event)
        {
            Object t = event.getTarget();
            if (t instanceof CardFieldView target)
            {
                CardField cardField = target.field;
                if (cardField != null && cardField.getCard() == null && cardField.player != null && cardField.player.board != null)
                {
                    if (event.getGestureSource() != target && event.getDragboard().hasContent(ROBO_RALLY_CARD))
                    {
                        target.setBackground(BG_DROP);
                    }
                }
            }
            event.consume();
        }

    }

    /**
     * @author Elias & Frederik
     */
    private class OnDragExitedHandler implements EventHandler<DragEvent>
    {

        @Override
        public void handle(DragEvent event)
        {
            Object t = event.getTarget();
            if (t instanceof CardFieldView target)
            {
                CardField cardField = target.field;
                if (cardField != null && cardField.getCard() == null && cardField.player != null && cardField.player.board != null)
                {
                    if (event.getGestureSource() != target && event.getDragboard().hasContent(ROBO_RALLY_CARD))
                    {
                        target.setBackground(BG_DEFAULT);
                    }
                }
            }
            event.consume();
        }

    }

    /**
     * @author Elias & Frederik
     */
    private class OnDragDroppedHandler implements EventHandler<DragEvent>
    {

        /**
         * @param event
         * @author Elias & Frederik
         */
        @Override
        public void handle(DragEvent event)
        {
            Object t = event.getTarget();
            if (t instanceof CardFieldView target)
            {
                CardField cardField = target.field;

                Dragboard db = event.getDragboard();
                boolean success = false;
                if (cardField != null && cardField.getCard() == null && cardField.player != null && cardField.player.board != null)
                {
                    if (event.getGestureSource() != target && db.hasContent(ROBO_RALLY_CARD))
                    {
                        Object object = db.getContent(ROBO_RALLY_CARD);
                        if (object instanceof String)
                        {
                            CardField source = cardFieldFromRepresentation((String) object);
                            if (source != null && gameController.moveCards(source, cardField))
                            {
                                // CommandCard card = source.getCard();
                                // if (card != null) {
                                // if (gameController.moveCards(source, cardField)) {
                                // cardField.setCard(card);
                                success = true;
                                // }
                            }
                        }
                    }
                }
                event.setDropCompleted(success);
                target.setBackground(BG_DEFAULT);
            }
            event.consume();
        }

    }

    /**
     * @author Elias
     */
    private class OnDragDoneHandler implements EventHandler<DragEvent>
    {

        /**
         * @param event
         * @author Elias
         */
        @Override
        public void handle(DragEvent event)
        {
            Object t = event.getTarget();
            if (t instanceof CardFieldView source)
            {
                source.setBackground(BG_DEFAULT);
            }
            event.consume();
        }

    }

}




