package engine.exception;

/**
 * @program: regex-engine
 * @description:
 * @author: TUITU
 * @create: 2018-05-19 12:36
 **/
public class BadREException extends Exception {
    public BadREException(){
        super();
    }

    public BadREException(String message){
        super(message);
    }

    public BadREException(String message, Throwable cause){
        super(message, cause);
    }

    public BadREException(Throwable cause){
        super(cause);
    }
}
