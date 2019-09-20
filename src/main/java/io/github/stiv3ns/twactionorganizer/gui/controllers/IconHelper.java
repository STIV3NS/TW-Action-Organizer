package io.github.stiv3ns.twactionorganizer.gui.controllers;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

interface IconHelper {
    default ImageView getIcon(String path) {
        return new ImageView(
                new Image(getClass().getResourceAsStream(path))
        );
    }
}
