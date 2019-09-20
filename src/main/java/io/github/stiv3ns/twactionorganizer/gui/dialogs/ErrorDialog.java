package io.github.stiv3ns.twactionorganizer.gui.dialogs;

import javafx.scene.control.Alert;

public class ErrorDialog extends Alert {
    public ErrorDialog(String title, String header, String message) {
        super(AlertType.WARNING);

        setTitle(title);
        setHeaderText(header);
        setContentText(message);

        showAndWait();
    }
}
