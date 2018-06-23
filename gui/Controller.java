package gui;

import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.ImageView;
import twao.Player;


public class Controller {
    @FXML
    private TreeView treeView;
    @FXML
    private ImageView imgMap;

    @FXML
    public void initialize() {
        treeView.setRoot(new TreeItem<>("root"));
        treeView.setShowRoot(false);

        TreeItem<String> item = new TreeItem<>("World");
        treeView.getRoot().getChildren().add(item);

        item = new TreeItem<>("Targets");
        treeView.getRoot().getChildren().add(item);
    }
}
