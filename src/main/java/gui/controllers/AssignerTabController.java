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

    private TreeItem<String> root;
    private TreeItem<String> world;
    private TreeItem<String> actionResources;
    private TreeItem<String> targets;
    private TreeItem<String> nobleTargets;
    private TreeItem<String> astonishingTargets;
    private TreeItem<String> offTargets;
    private TreeItem<String> fakeTargets;
    private TreeItem<String> fakeNobleTargets;

    @FXML
    private void initialize() {
        initActionTreeView();
    }

    private void initActionTreeView() {
        root = new TreeItem<>("root");
        actionTreeView.setRoot(root);
        actionTreeView.setShowRoot(false);

        world = createTreeItem("WORLD", WORLD_ICON);
        actionResources = createTreeItem("ACTION_RESOURCES", ACTION_RESOURCES_ICON);
        targets = createTreeItem("TARGETS", TARGET_ICON);

        nobleTargets = createTreeItem("NOBLE_TARGETS");
        astonishingTargets = createTreeItem("ASTONISHING_TARGETS");
        offTargets = createTreeItem("OFF_TARGETS");
        fakeTargets = createTreeItem("FAKE_TARGETS");
        fakeNobleTargets = createTreeItem("FAKENOBLE_TARGETS");

        targets.getChildren().addAll(nobleTargets, astonishingTargets, offTargets, fakeTargets, fakeNobleTargets);
        root.getChildren().addAll(world, actionResources, targets);
    }

    private TreeItem<String> createTreeItem(String text) {
        return new TreeItem<>(bundle.getString(text));
    }

    private TreeItem<String> createTreeItem(String text, String icon) {
        return new TreeItem<>(bundle.getString(text), getIcon(icon));
    }
}
