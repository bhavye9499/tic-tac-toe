package io;

import java.io.Serializable;

public class Response implements Serializable {

    private String message;
    private boolean input;

    public Response (String message, boolean input) {
        this.message = message;
        this.input = input;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isInput() {
        return this.input;
    }

    public void setInput(boolean input) {
        this.input = input;
    }

}
