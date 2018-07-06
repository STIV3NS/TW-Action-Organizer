package gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

import java.util.ResourceBundle;

public class AssignerTabController implements IconHelper {
    private ResourceBundle bundle = ResourceBundle.getBundle("localization/gui");

    private final static String TARGET_ICON             = "/img/target.png";       /*Icon made by https://www.flaticon.com/authors/lucy-g licensed by CC 3.0 BY*/
    private final static String WORLD_ICON              = "/img/world.png";        /*Icon made by https://www.flaticon.com/authors/good-ware licensed by CC 3.0 BY*/
    private final static String ACTION_RESOURCES_ICON   = "/img/hr.png";           /*Icon made by http://www.freepik.com licensed by CC 3.0 BY*/

    @FXML
    private TreeView actionTreeView;

    @FXML
    private void initialize() {
        initActionTreeView();
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
}
