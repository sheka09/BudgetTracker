package io.dawn.budget.error;

public class ExpenseAlreadyExistsException extends Exception {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public ExpenseAlreadyExistsException() {
        super();
    }

    public ExpenseAlreadyExistsException(String message) {
        super(message);
    }

    public ExpenseAlreadyExistsException(String message, Throwable cause) {
        super(message, cause);
    }

    public ExpenseAlreadyExistsException(Throwable cause) {
        super(cause);
    }

    protected ExpenseAlreadyExistsException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
