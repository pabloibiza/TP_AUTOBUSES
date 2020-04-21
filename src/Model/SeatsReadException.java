package Model;

public class SeatsReadException extends NumberFormatException{
    private final int ERROR_CODE;
    private Object extraInfo;
    private static final String TRAVELS_READ_ERROR = "Error while reading a travels status file element. Readed: ";
    private static final String SEAT_ASSIGN_ERROR = "Error assigning a seat.";

    public SeatsReadException(int ERROR_CODE, Object extraInfo) {
        super();
        this.ERROR_CODE = ERROR_CODE;
        this.extraInfo = extraInfo;
    }

    @Override
    public String getMessage(){
        String message = "";
        switch(ERROR_CODE){
            case 1:
                message = TRAVELS_READ_ERROR + (String) extraInfo;
                break;

            case 2:
                message = SEAT_ASSIGN_ERROR;
                break;
        }

        return message;
    }
}
