package io.github.stiv3ns.twactionorganizer.core.utils.exceptions;

public class MissingConfigurationException extends Exception {
    public MissingConfigurationException() {
        super();
    }

    public MissingConfigurationException(String errtext) {
        super(errtext);
    }
}
