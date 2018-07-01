package gui;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Tab;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ResourceBundle;


public class Controller {
    private ResourceBundle bundle = ResourceBundle.getBundle("localization.gui");

    private final static String TARGET_ICON             = "resources/img/target.png";       /*Icon made by https://www.flaticon.com/authors/lucy-g licensed by CC 3.0 BY*/
    private final static String WORLD_ICON              = "resources/img/world.png";        /*Icon made by https://www.flaticon.com/authors/good-ware licensed by CC 3.0 BY*/
    private final static String ACTION_RESOURCES_ICON   = "resources/img/hr.png";           /*Icon made by http://www.freepik.com licensed by CC 3.0 BY*/
    private final static String ASSIGNER_TAB_ICON       = "resources/img/roadsplit.png";    /*Icon made by https://www.flaticon.com/authors/vaadin licensed by CC 3.0 BY*/
    private final static String MAP_TAB_ICON            = "resources/img/map.png";          /*Icon made by https://www.flaticon.com/authors/smashiconslicensed by CC 3.0 BY*/
    private final static String SENDER_TAB_ICON         = "resources/img/paperplane.png";   /*Icon made by http://www.freepik.com licensed by CC 3.0 BY*/
    private final static String TUTORIALS_TAB_ICON      = "resources/img/books.png";        /*Icon made by http://www.freepik.com licensed by CC 3.0 BY*/

    @FXML
    private Tab assignerTab;

    @FXML
    private Tab mapTab;

    @FXML
    private Tab senderTab;

    @FXML
    private Tab tutorialsTab;

    @FXML
    private TreeView actionTreeView;

    @FXML
    private void initialize() {
        initTabs();
        initActionTreeView();
    }

    private void initTabs() {
        assignerTab.setGraphic(getIcon(ASSIGNER_TAB_ICON));
        mapTab.setGraphic(getIcon(MAP_TAB_ICON));
        senderTab.setGraphic(getIcon(SENDER_TAB_ICON));
        tutorialsTab.setGraphic(getIcon(TUTORIALS_TAB_ICON));
    }

    private void initActionTreeView() {
        TreeItem<String> root = new TreeItem<>("root");
        actionTreeView.setRoot(root);
        actionTreeView.setShowRoot(false);

        TreeItem<String> world = createTreeItem("WORLD", WORLD_ICON);
        root.getChildren().add(world);

        TreeItem<String> actionResources = createTreeItem("ACTION_RESOURCES", ACTION_RESOURCES_ICON);
        root.getChildren().add(actionResources);

        TreeItem<String> targets = createTreeItem("TARGETS", TARGET_ICON);
        root.getChildren().add(targets);

        TreeItem<String> nobleTargets = createTreeItem("NOBLE_TARGETS");
        targets.getChildren().add(nobleTargets);

        TreeItem<String> astonishingTargets = createTreeItem("ASTONISHING_TARGETS");
        targets.getChildren().add(astonishingTargets);

        TreeItem<String> offTargets = createTreeItem("OFF_TARGETS");
        targets.getChildren().add(offTargets);

        TreeItem<String> fakeTargets = createTreeItem("FAKE_TARGETS");
        targets.getChildren().add(fakeTargets);

        TreeItem<String> fakeNobleTargets = createTreeItem("FAKENOBLE_TARGETS");
        targets.getChildren().add(fakeNobleTargets);
    }

    private TreeItem<String> createTreeItem(String text) {
        return new TreeItem<>(bundle.getString(text));
    }

    private TreeItem<String> createTreeItem(String text, String icon) {
        return new TreeItem<>(bundle.getString(text), getIcon(icon));
    }

    private Node getIcon(String path) {
        return new ImageView(
                new Image(getClass().getResourceAsStream(path))
        );
    }
}
