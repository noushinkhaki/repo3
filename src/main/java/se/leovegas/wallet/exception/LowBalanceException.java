package se.leovegas.wallet.exception;

import java.util.function.Supplier;

public class LowBalanceException extends Exception implements Supplier<LowBalanceException> {

    private String message;

    public LowBalanceException() {
        super();
    }

    public LowBalanceException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public LowBalanceException get() {
        return this;
    }
}
