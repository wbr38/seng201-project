package seng201.team53.gui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import seng201.team53.App;
import seng201.team53.game.GameEnvironment;
import seng201.team53.game.GameState;
import seng201.team53.game.map.Map;
import seng201.team53.game.map.MapInteraction;
import seng201.team53.game.map.Tile;
import seng201.team53.items.towers.Tower;
import seng201.team53.items.towers.TowerType;

public class GameController {
    @FXML public AnchorPane test;
    @FXML private GridPane gridPane;
    @FXML private Pane randomEventPane;
    @FXML private Pane roundCompletePane;
    @FXML private Text randomEventTest;
    @FXML private AnchorPane inventoryPane;
    @FXML private Button pauseButton;
    @FXML private Button startButton;
    @FXML private Button resumeButton;
    @FXML private Text roundCounterLabel;
    @FXML public TextField stateTextField; // debugging
    private GameEnvironment game;

    public void init() {
        var scene = App.getPrimaryStage().getScene();
        scene.setOnMousePressed(this::onMousePressed);
    }

    @FXML
    private void onStartButtonMouseClick(MouseEvent event) {
        if (event.getButton() != MouseButton.PRIMARY)
            return;
        if (game.getStateHandler().getState() != GameState.ROUND_NOT_STARTED)
            return;
        game.getRound().begin();
    }

    @FXML
    private void onPauseButtonMouseClick(MouseEvent event) {
        if (event.getButton() != MouseButton.PRIMARY)
            return;
        if (game.getStateHandler().getState() != GameState.ROUND_ACTIVE)
            return;
        game.getStateHandler().setState(GameState.ROUND_PAUSE);
    }

    @FXML
    private void onResumeButtonMouseClick(MouseEvent event) {
        if (event.getButton() != MouseButton.PRIMARY)
            return;
        if (game.getStateHandler().getState() != GameState.ROUND_PAUSE)
            return;
        game.getStateHandler().setState(GameState.ROUND_ACTIVE);
    }

    @FXML
    private void onInventoryButtonMouseClick(MouseEvent event) {
        if (event.getButton() != MouseButton.PRIMARY)
            return;
        inventoryPane.setVisible(!inventoryPane.isVisible());
        inventoryPane.setDisable(!inventoryPane.isDisable());
    }

    @FXML
    private void onRandomEventDialogExistClick(MouseEvent event) {
        if (event.getButton() != MouseButton.PRIMARY)
            return;
        if (game.getStateHandler().getState() != GameState.RANDOM_EVENT_DIALOG_OPEN)
            return;
        randomEventPane.setVisible(false);
        randomEventPane.setDisable(true);
        game.getStateHandler().setState(GameState.ROUND_ACTIVE);
    }

    @FXML
    private void onRoundCompleteDialogExistClick(MouseEvent event) {
        if (event.getButton() != MouseButton.PRIMARY)
            return;
        if (game.getStateHandler().getState() != GameState.ROUND_COMPLETE)
            return;
        roundCompletePane.setVisible(false);
        roundCompletePane.setDisable(true);
        game.getStateHandler().setState(GameState.ROUND_NOT_STARTED);
    }

    @FXML
    private void onShopLumberMillTowerClick(MouseEvent event) {
        onShopTowerClick(event, TowerType.LUMBER_MILL);
    }

    @FXML
    private void onShopMineTowerClick(MouseEvent event) {
        onShopTowerClick(event, TowerType.MINE);
    }

    @FXML
    private void onShopQuarryTowerClick(MouseEvent event) {
        onShopTowerClick(event, TowerType.QUARRY);
    }

    @FXML
    private void onShopWindMillTowerClick(MouseEvent event) {
        onShopTowerClick(event, TowerType.WIND_MILL);
    }

    private void onShopTowerClick(MouseEvent event, TowerType towerType) {
        if (event.getButton() != MouseButton.PRIMARY)
            return;
        Map map = game.getRound().getMap();
        if (map.getCurrentInteraction() != MapInteraction.NONE) // prevents starting the "placing" methods running again
            return;
        Tower tower = towerType.create();
        map.startPlacingTower(tower);
    }

    /*
     * Handle when the user clicks their mouse. Used for when interacting with the
     * map (placing towers, etc)
     */
    private void onMousePressed(MouseEvent event) {
        if (event.getButton() != MouseButton.PRIMARY)
            return;
        var map = game.getRound().getMap();
        if (map.getCurrentInteraction() == MapInteraction.NONE)
            return;

        int mouseX = (int) Math.round(event.getSceneX());
        int mouseY = (int) Math.round(event.getSceneY());
        Tile tile = map.getTileFromScreenPosition(mouseX, mouseY);
        switch (map.getCurrentInteraction()) {
            case PLACE_TOWER:
                if (!tile.isBuildable() || tile.getTower() != null)
                    return;
                var selectedTower = map.getSelectedTower();
                map.placeTower(selectedTower, tile);
                map.setInteraction(MapInteraction.NONE);
                map.stopPlacingTower();
                break;
            default:
                break;
        }
    }

    public GridPane getGridPane() {
        return gridPane;
    }

    public void setGame(GameEnvironment game) {
        this.game = game;
    }
    public void updateRoundCounter(int currentRound) {
        int rounds = game.getRounds();
        roundCounterLabel.setText(currentRound + "/" + rounds);
    }

    public void showStartButton() {
        showButton(startButton, true);
        showButton(pauseButton, false);
        showButton(resumeButton, false);
    }
    public void showPauseButton() {
        showButton(startButton, false);
        showButton(pauseButton, true);
        showButton(resumeButton, false);
    }
    public void showResumeButton() {
        showButton(startButton, false);
        showButton(pauseButton, false);
        showButton(resumeButton, true);
    }
    public void showRandomEventDialog(String text) {
        randomEventTest.setText(text);
        randomEventPane.setVisible(true);
        randomEventPane.setDisable(false);
    }
    public void showRoundCompleteDialog() {
        roundCompletePane.setVisible(true);
        roundCompletePane.setDisable(false);
    }
    private void showButton(Button button, boolean show) {
        button.setVisible(show);
        button.setDisable(!show);
    }
}