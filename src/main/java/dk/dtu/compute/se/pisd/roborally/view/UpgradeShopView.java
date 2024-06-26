package dk.dtu.compute.se.pisd.roborally.view;

import dk.dtu.compute.se.pisd.roborally.model.Player;
import dk.dtu.compute.se.pisd.roborally.model.UpgradeCard;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.layout.VBox;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * A dialog for the player to purchase upgrades from the upgrade shop.
 * The player can select an upgrade from a list of available upgrades and purchase it if they have enough energy cubes.
 * If the player does not have enough energy cubes, an error alert will be shown.
 * If the player successfully purchases an upgrade, an information alert will be shown.
 * The dialog will return the selected upgrade card if the player clicks the purchase button, otherwise it will
 * return null.
 *
 * @Author Emil
 */
public class UpgradeShopView extends Dialog<UpgradeCard>
{

    private final ListView<UpgradeCard> upgradeListView;
    RestTemplate restTemplate = new RestTemplate();

    public UpgradeShopView(Player player)
    {
        setTitle("Upgrade Shop");
        setHeaderText("Select an upgrade to purchase:");

        upgradeListView = new ListView<>();
        upgradeListView.getItems().clear();
        List<UpgradeCard> availableUpgrades = new ArrayList<>();
        for (UpgradeCard upgradeCard : player.board.getUpgradeCards())
        {
            if (upgradeCard.getPlayerID() == null)
            {
                availableUpgrades.add(upgradeCard);
            }
        }
        upgradeListView.getItems().addAll(availableUpgrades);

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
                    sendUpgradePurchase(player, selectedUpgrade);
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

    /**
     * Shows an alert to the player that they have successfully purchased an upgrade.
     *
     * @param player       the player who purchased the upgrade
     * @param upgradeCard  the upgrade card that was purchased
     * @Author Mustafa & Adel
     */
    private void showUpgradePurchasedAlert(Player player, UpgradeCard upgradeCard)
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Upgrade Purchased");
        alert.initOwner(this.getDialogPane().getScene().getWindow());
        alert.setHeaderText("You have successfully purchased the " + upgradeCard.getName() + " upgrade.");
        alert.setContentText("You now have " + (player.getEnergyCubes() - upgradeCard.getPrice()) + " Energy Cube(s) left.");
        alert.showAndWait();
    }

    /**
     * Sends a request to the server to add the upgrade card to the player's upgrade cards.
     *
     * @param player       the player who purchased the upgrade
     * @param upgradeCard  the upgrade card that was purchased
     * @Author Mustafa & Adel
     */
    private void sendUpgradePurchase(Player player, UpgradeCard upgradeCard)
    {
        String urlToSend =
                "http://localhost:8080/set/boards/upgradeCards/addToPlayer?gameID=" + player.board.getGameID() +
                        "&playerID=" + player.getPlayerID() + "&upgradeCardName=" + upgradeCard.getName();

        new Thread(() -> {
            try
            {
                ResponseEntity<Boolean> response = restTemplate.exchange(urlToSend, HttpMethod.GET, null,
                        new ParameterizedTypeReference<Boolean>()
                {
                });

            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * Shows an alert to the player that they do not have enough energy cubes to purchase the upgrade.
     *
     * @Author Mustafa & Adel
     */
    private void showNotEnoughEnergyCubesAlert()
    {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Not enough Energy Cubes");
        alert.initOwner(this.getDialogPane().getScene().getWindow());
        alert.setHeaderText("You do not have enough Energy Cubes to purchase this upgrade.");
        alert.showAndWait();
    }
}

