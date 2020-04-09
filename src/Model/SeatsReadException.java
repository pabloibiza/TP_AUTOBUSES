package Model;

public class SeatsReadException extends NumberFormatException{
    private final int ERROR_CODE;
    private Object extraInfo;

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
                message = "Error while reading a travels status file element. Readed: " + (String) extraInfo;
                break;

            case 2:
                message = "Error assigning a seat.";
                break;
        }

        return message;
    }
}
