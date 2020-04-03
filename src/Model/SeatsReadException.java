package Model;

public class SeatsReadException extends NumberFormatException{
    private int errorCode;
    private Object extraInfo;

    public SeatsReadException(int errorCode, Object extraInfo) {
        super();
        this.errorCode = errorCode;
        this.extraInfo = extraInfo;
    }

    @Override
    public String getMessage(){
        String message = "";
        switch(errorCode){
            case 1:
                message = "Error while reading a travels status file element. Readed: " + (String) extraInfo;
                break;

            case 2:
                message = "Error assigning a seat.";
                break;
        }

        return message;
    }
}
