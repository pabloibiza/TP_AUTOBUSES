package Model.Cloud;

import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 *  Communication primitive client-server
 *
 */
public enum CommunicationPrimitive {
    CONNECT_PUSH("connect"),
    DISCONNECT_PUSH("disconnect"),
    NEW_CONNECTION_ID("new_conection_id"),
    TEST("test"),
    NEW_PASSENGER("new_passenger"),
    DELETE_PASSENGER("delete_passenger"),
    SEARCH_PASSENGER("search_passenger"),
    SEARCH_TRAVEL("search_travel"),
    WHO_SITTING("who_is_sitting"),
    SEARCH_TRAVELS_PER_DATE("search_travels_per_date"),
    IS_SEAT_FREE("is_seat_free"),
    ASSIGN("assign"),
    DEALLOCATE("deallocate"),
    END("end"),
    OK("ok"),
    NOK("nok");

    private String simbol;
    private static final Pattern regularExpression =
            Pattern.compile(CONNECT_PUSH.toString() + "|" +
                    DISCONNECT_PUSH.toString() + "|" +
                    NEW_CONNECTION_ID + "|" +
                    TEST.toString() + "|" +
                    NEW_PASSENGER.toString() + "|" +
                    DELETE_PASSENGER.toString() + "|" +
                    SEARCH_PASSENGER.toString() + "|" +
                    SEARCH_TRAVEL.toString() + "|" +
                    WHO_SITTING.toString() + "|" +
                    SEARCH_TRAVELS_PER_DATE.toString() + "|" +
                    IS_SEAT_FREE.toString() + "|" +
                    ASSIGN.toString() + "|" +
                    DEALLOCATE.toString() + "|" +
                    END.toString() + "|" +
                    OK.toString() + "|" +
                    NOK.toString());

    /**
     *  constructor method.
     *
     */
    CommunicationPrimitive(String simbol) {
        this.simbol = simbol;
    }

    /**
     *  Returns a new primitive read form a scanner.
     *
     */
    public static CommunicationPrimitive newPrimitive(Scanner scanner)
            throws InputMismatchException {
        String token = scanner.next(regularExpression);

        if (token.equals(CONNECT_PUSH.toString())) {
            return CONNECT_PUSH;
        }
        else if (token.equals(DISCONNECT_PUSH.toString())) {
            return DISCONNECT_PUSH;
        }
        else if (token.equals(NEW_CONNECTION_ID.toString())) {
            return NEW_CONNECTION_ID;
        }
        else if (token.equals(TEST.toString())) {
            return TEST;
        }
        else if (token.equals(NEW_PASSENGER.toString())) {
            return NEW_PASSENGER;
        }
        else if (token.equals(DELETE_PASSENGER.toString())) {
            return DELETE_PASSENGER;
        }
        else if (token.equals(SEARCH_PASSENGER.toString())) {
            return SEARCH_PASSENGER;
        }
        else if (token.equals(SEARCH_TRAVEL.toString())) {
            return SEARCH_TRAVEL;
        }
        else if (token.equals(WHO_SITTING.toString())){
            return WHO_SITTING;
        }
        else if (token.equals(SEARCH_TRAVELS_PER_DATE.toString())) {
            return SEARCH_TRAVELS_PER_DATE;
        }
        else if (token.equals(IS_SEAT_FREE.toString())){
            return IS_SEAT_FREE;
        }
        else if (token.equals(ASSIGN.toString())) {
            return ASSIGN;
        }
        else if (token.equals(DEALLOCATE.toString())) {
            return DEALLOCATE;
        }
        else if (token.equals(END.toString())) {
            return END;
        }
        else if (token.equals(OK.toString())) {
            return OK;
        }
        else {
            return NOK;
        }
    }

    /**
     *  toString
     *
     */
    @Override
    public String toString() {
        return simbol;
    }
}
