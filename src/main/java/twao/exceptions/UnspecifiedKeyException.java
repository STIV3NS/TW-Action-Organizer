package twao.exceptions;

public class UnspecifiedKeyException extends Exception {
    public UnspecifiedKeyException() {
        super();
    }
    public UnspecifiedKeyException(String errtext) { super(errtext); }
}
