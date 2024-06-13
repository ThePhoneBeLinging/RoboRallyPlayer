package dk.dtu.compute.se.pisd.roborally.view;

import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.UpgradeCard;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.VBox;

/**
 * A dialog for the player to purchase upgrades from the upgrade shop.
 * The player can select an upgrade from a list of available upgrades and purchase it if they have enough energy cubes.
 * If the player does not have enough energy cubes, an error alert will be shown.
 * If the player successfully purchases an upgrade, an information alert will be shown.
 * The dialog will return the selected upgrade card if the player clicks the purchase button, otherwise it will return null.
 * @Author Emil

 */
public class UpgradeShopView extends Dialog<UpgradeCard>
{

    private final ListView<UpgradeCard> upgradeListView;

    public UpgradeShopView(Player player)
    {
        setTitle("Upgrade Shop");
        setHeaderText("Select an upgrade to purchase:");

        upgradeListView = new ListView<>();
        upgradeListView.getItems().addAll(player.board.getUpgradeCards());

        upgradeListView.setCellFactory(lv -> new ListCell<>()
        {
            @Override
            protected void updateItem(UpgradeCard upgrade, boolean empty)
            {
                super.updateItem(upgrade, empty);
                if (empty)
                {
                    setText(null);
                }
                else
                {
                    setText(upgrade.getName() + " - Price: " + upgrade.getPrice() + " Energy Cubes");
                }
            }
        });

        VBox vbox = new VBox();
        vbox.getChildren().add(new Label("Available Upgrades:"));
        vbox.getChildren().add(upgradeListView);
        getDialogPane().setContent(vbox);

        ButtonType purchaseButtonType = new ButtonType("Purchase", ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(purchaseButtonType, ButtonType.CANCEL);

        setResultConverter(dialogButton -> {
            if (dialogButton == purchaseButtonType)
            {
                return upgradeListView.getSelectionModel().getSelectedItem();
            }
            return null;
        });


        setOnShowing(event -> {
            Button purchaseButton = (Button) getDialogPane().lookupButton(purchaseButtonType);
            purchaseButton.addEventFilter(ActionEvent.ACTION, e -> {
                UpgradeCard selectedUpgrade = upgradeListView.getSelectionModel().getSelectedItem();
                if (selectedUpgrade != null && player.getEnergyCubes() >= selectedUpgrade.getPrice())
                {
                    player.board.buyUpgradeCard(player, selectedUpgrade);
                    showUpgradePurchasedAlert(player, selectedUpgrade);
                }
                else
                {
                    showNotEnoughEnergyCubesAlert();
                    event.consume();
                }
            });
        });
    }


    private void showNotEnoughEnergyCubesAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Not enough Energy Cubes");
        alert.setHeaderText("You do not have enough Energy Cubes to purchase this upgrade.");
        alert.showAndWait();
    }

    private void showUpgradePurchasedAlert(Player player, UpgradeCard upgradeCard) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Upgrade Purchased");
        alert.setHeaderText("You have successfully purchased the " + upgradeCard.getName() + " upgrade.");
        alert.setContentText("You now have " + player.getEnergyCubes() + " Energy Cubes left.");
        alert.showAndWait();
    }
}

