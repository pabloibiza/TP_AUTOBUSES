/**
 *  Client.java
 *
 *
 */

package Model.Cloud;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class Client {
    public static int WAITTIME_LONGPOOLING = 0;
    public static int WAITTIME_SERVER = 5000;
    public static int WAITTIME_SERVER_RECONECT_ATTEMPT = 10 *1000;

    private String serverURL;
    private int serverPort;
    private Socket socket;
    private BufferedReader influx;
    private PrintWriter outflux;


    /**
     * Constructor method.
     * @param serverURL String
     * @param serverPort Integer
     */
    public Client(String serverURL, int serverPort) {
        this.serverURL = serverURL;
        this.serverPort = serverPort;
    }

    /**
     * Sends a request to the server.
     * @param request CommunicationPrimitive
     * @param waitTime Integer
     * @param parameters String
     * @throws IOException
     */
    private synchronized void send(CommunicationPrimitive request, int waitTime, String parameters) throws IOException {
        socket = new Socket(serverURL, serverPort);
        socket.setSoTimeout(waitTime);

        influx = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        outflux = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

        outflux.println(request.toString());

        if (parameters != null) {
            outflux.println(parameters);
        }
    }

    /**
     * Receives server's response.
     * @param results List<String>
     * @throws Exception
     */
    private void receiveServerResponse(List<String> results){
        String line = "";
        try {
            while ((line = influx.readLine()) != null) {
                results.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Recieves a server's request.
     * @param results List<String>
     * @throws Exception
     */
    private void receiveServerRequest(List<String> results) throws Exception {
        String result = influx.readLine();

        while (! result.equals(CommunicationPrimitive.END.toString())) {
            results.add(result);
            result = influx.readLine();
        }
    }

    /**
     * Receives an answer or a request from the server.
     * @param results String
     * @param serverRequest Boolean
     * @return CommunicationPrimitive
     * @throws Exception
     */
    private synchronized CommunicationPrimitive receive(List<String> results, boolean serverRequest) throws Exception {
        // Waiting for server's request
        CommunicationPrimitive response = CommunicationPrimitive.newPrimitive(new Scanner(new StringReader(influx.readLine())));

        // Server sends test connection
        if (response == CommunicationPrimitive.TEST) {
            influx.readLine();
            outflux.println(CommunicationPrimitive.OK);
            return response;
        }
        results.clear();

        if ( ! serverRequest) {
            receiveServerResponse(results);
        } else {
            receiveServerRequest(results);
        }

        outflux.println(CommunicationPrimitive.OK);

        return response;
    }

    /**
     * Receives an answer from the server.
     * @param results List<String>
     * @throws Exception
     */
    private synchronized CommunicationPrimitive receive(List<String> results) throws Exception {
        return receive(results, false);
    }

    /**
     * Sends a request to the server returning the results.
     * @param request CommunicationPrimitives
     * @param waitTime Integer
     * @param parameters String
     * @param results List <String>
     * @return CommunicationPrimitive
     * @throws Exception
     */
    public synchronized CommunicationPrimitive sendRequest(CommunicationPrimitive request, int waitTime, String parameters, List<String> results) throws Exception {
        send(request, waitTime, parameters);

        CommunicationPrimitive response = receive(results);

        influx.close();
        outflux.close();
        socket.close();

        return response;
    }

    /**
     * Sends a request to server without returning results.
     * @param request CommunicationPrimitive
     * @param waitTime Integer
     * @param parameters String
     * @return
     * @throws Exception
     */
    public synchronized CommunicationPrimitive sendRequest(CommunicationPrimitive request, int waitTime, String parameters) throws Exception {

        return sendRequest(request, waitTime, parameters,  new ArrayList<String>());
    }

    /**
     * Sends a long polling request to server.
     * @param request CommunicationPrimitive
     * @param waitTime Integer
     * @param parameters String
     * @param serverListener String
     * @throws Exception
     */
    public synchronized void sendLongPollingRequest(CommunicationPrimitive request, int waitTime, String parameters, ServerListener serverListener) throws Exception {
        send(request, waitTime, parameters);
        List<String> results = new ArrayList<>();

        do {
            CommunicationPrimitive response = receive(results, true);

            if (response != CommunicationPrimitive.TEST) {
                if (serverListener.serverRequestFired(response, results)) {
                    outflux.println(CommunicationPrimitive.OK);
                } else {
                    outflux.println(CommunicationPrimitive.NOK);
                }
            }
        } while(true);
    }
}
