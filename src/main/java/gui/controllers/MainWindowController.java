package gui.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Tab;

public class MainWindowController implements IconHelper {
    private final static String ASSIGNER_TAB_ICON       = "/img/roadsplit.png";    /*Icon made by https://www.flaticon.com/authors/vaadin licensed by CC 3.0 BY*/
    private final static String MAP_TAB_ICON            = "/img/map.png";          /*Icon made by https://www.flaticon.com/authors/smashiconslicensed by CC 3.0 BY*/
    private final static String SENDER_TAB_ICON         = "/img/paperplane.png";   /*Icon made by http://www.freepik.com licensed by CC 3.0 BY*/
    private final static String TUTORIALS_TAB_ICON      = "/img/books.png";        /*Icon made by http://www.freepik.com licensed by CC 3.0 BY*/

    @FXML
    private Tab assignerTab;

    @FXML
    private Tab mapTab;

    @FXML
    private Tab senderTab;

    @FXML
    private Tab tutorialsTab;

    @FXML
    private void initialize() {
        initTabs();
    }

    private void initTabs() {
        assignerTab.setGraphic(getIcon(ASSIGNER_TAB_ICON));
        mapTab.setGraphic(getIcon(MAP_TAB_ICON));
        senderTab.setGraphic(getIcon(SENDER_TAB_ICON));
        tutorialsTab.setGraphic(getIcon(TUTORIALS_TAB_ICON));
    }
}
