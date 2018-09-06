package cz.kalina.stampie.utils;

import java.util.Arrays;

import cz.kalina.stampie.MainActivity;
import cz.kalina.stampie.R;

/**
 * Exception generated in Stampie App.
 *
 * @author Miroslav Kalina <xkalinam@email.cz>
 */
public class STPException extends Exception {

    /**
     * Code specifying, what exception has occurred.
     */
    private String errorCode = null;

    /**
     * Parameters to create a message of the exception.
     */
    private Object[] parameters = null;

    /**
     * Original cause of the exception.
     */
    private Throwable cause = null;

    /**
     * Parameterless constructor.
     */
    public STPException() {
        super();
        this.errorCode = MainActivity.getContext().getResources().getString(R.string.STP_EXC_SYSTEM);
        this.parameters = new Object[0];
        this.cause = null;
    }

    /**
     * Constructor with the error code.
     *
     * @param errorCode Code of the error (STP_EXC_xxx error codes).
     */
    public STPException(String errorCode) {
        super(errorCode);
        this.errorCode = errorCode;
        this.parameters = new Object[0];
        this.cause = null;
    }

    /**
     * Constructor with the cause.
     *
     * @param cause Cause of the exception.
     */
    public STPException(Throwable cause) {
        super(cause);
        this.errorCode = MainActivity.getContext().getResources().getString(R.string.STP_EXC_SYSTEM);
        this.parameters = new Object[0];
        this.cause = cause;
    }

    /**
     * Constructor with the message and the cause.
     *
     * @param errorCode Code of the error (KMC_EXC_xxx error codes).
     * @param cause Cause of the exception.
     */
    public STPException(String errorCode, Throwable cause) {
        super(errorCode, cause);
        this.errorCode = errorCode;
        this.parameters = new Object[0];
        this.cause = cause;
    }

    /**
     * Constructor with the message and the cause.
     *
     * @param errorCode Code of the error (KMC_EXC_xxx error codes).
     * @param cause Cause of the exception.
     * @param enableSuppression Whether or not suppression is enabled or disabled
     * @param writableStackTrace Whether or not the stack trace should be writable
     */
    public STPException(String errorCode, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(errorCode, cause);
        this.errorCode = errorCode;
        this.parameters = new Object[0];
        this.cause = cause;
    }

    /**
     * Constructor with the error code and parameters to generate a message.
     *
     * @param errorCode One of KMC_EXC_xxx error codes.
     * @param parameters Parameters for the exception.
     */
    public STPException(String errorCode, Object[] parameters) {
        super(errorCode);
        this.errorCode = errorCode;
        this.parameters = parameters;
        this.cause = null;
    }

    /**
     * Constructor with the error code and parameters to generate a message.
     *
     * @param errorCode One of KMC_EXC_xxx error codes.
     * @param parameters Parameters for the exception.
     * @param cause Cause of the exception.
     */
    public STPException(String errorCode, Object[] parameters, Throwable cause) {
        super(errorCode);
        this.errorCode = errorCode;
        this.parameters = parameters;
        this.cause = cause;
    }

    /**
     * Gets the message.
     */
    @Override
    public String getMessage() {
        return super.getMessage();
    }

    /**
     * Gets the message.
     */
    @Override
    public String getLocalizedMessage() {
        return super.getMessage();
    }

    /**
     * Gets the localized message.
     */
    public String getLocalizedMessage(String langCode) {
        return super.getMessage();
    }

    /**
     * @return the errorCode
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * @param errorCode the errorCode to set
     */
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * @return the parameters
     */
    public Object[] getParameters() {
        return parameters;
    }

    /**
     * @param parameters the parameters to set
     */
    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }

    /**
     * @return the cause
     */
    public Throwable getCause() {
        return cause;
    }

    /**
     * @param cause the cause to set
     */
    public void setCause(Throwable cause) {
        this.cause = cause;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "KMCException [errorCode=" + errorCode + ", parameters=" + (parameters != null ? Arrays.toString(parameters) : "NULL") + ", cause=" + cause + "]";
    }
}