package gui;

import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;


public class Controller {
    @FXML
    private TreeView treeView;

    @FXML
    private void initialize() {
        treeView.setRoot(new TreeItem<>("root"));
        treeView.setShowRoot(false);

        TreeItem<String> world = new TreeItem<>("World");
        treeView.getRoot().getChildren().add(world);

        TreeItem<String> resources = new TreeItem<>("Action resources");
        treeView.getRoot().getChildren().add(resources);

        TreeItem<String> targets = new TreeItem<>("Targets");
        treeView.getRoot().getChildren().add(targets);
    }
}
