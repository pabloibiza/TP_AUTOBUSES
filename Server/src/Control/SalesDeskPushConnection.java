package Control;

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
import java.util.concurrent.CountDownLatch;


class SalesDeskPushConnection {
    private String connectionID;
    private String companyID;
    private Socket socket;
    private BufferedReader influx;
    private PrintWriter outflux;
    private CountDownLatch countDownLatch;

    /**
     * Constructor method.
     * @param connectionID String
     * @param companyID String
     * @param socket Socket
     * @param countDownLatch CountDownLatch
     * @throws IOException
     */
    public SalesDeskPushConnection(String connectionID, String companyID, Socket socket,
                                   CountDownLatch countDownLatch) throws IOException {
        this.connectionID = connectionID;
        this.companyID = companyID;
        this.socket = socket;
        this.countDownLatch = countDownLatch;

        influx = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        //Autoflush
        outflux = new PrintWriter(new BufferedWriter(new OutputStreamWriter(
                socket.getOutputStream())), true);
    }

    /**
     * Returns the connection ID.
     * @return String
     */
    public String getConnectionID() {
        return connectionID;
    }

    /**
     * Returns the company  ID.
     * @return String
     */
    public String getCompanyID() {
        return companyID;
    }

    /**
     * Sends a request.
     * @param request CommunicationPrimitive
     * @param waitTime Integer
     * @param parameters String
     * @throws IOException
     */
    private void send(CommunicationPrimitive request, int waitTime, String parameters)
            throws IOException {

        socket.setSoTimeout(waitTime);
        outflux.println(request.toString());

        if (parameters != null) {
            outflux.println(parameters);
        }

        outflux.println(CommunicationPrimitive.END);
    }

    /**
     * Receives a response.
     * @param results List<String>
     * @return CommunicationPrimitive
     * @throws IOException
     */
    private CommunicationPrimitive receive(List<String> results) throws IOException {
        CommunicationPrimitive response = CommunicationPrimitive.NOK;

        String line = influx.readLine();
        if (line != null) {
            response = CommunicationPrimitive.newPrimitive(new Scanner(new StringReader(line)));
            results.clear();

            while(influx.ready()) {
                results.add(influx.readLine());
            }
        }
        return response;
    }

    /**
     * Sends a request to SalesDesk.
     * @param request CommunicationPrimitive
     * @param waitTime Integer
     * @param parameters String
     * @param results List<String>
     * @return CommunicationPrimitive
     * @throws IOException
     */
    synchronized CommunicationPrimitive sendRequest(CommunicationPrimitive request, int waitTime,
                                                    String parameters, List<String> results)
            throws IOException {

        send(request, waitTime, parameters);

        return receive(results);
    }

    /**
     * Sends a request to SalesDesk without results.
     * @param request CommunicationPrimitive
     * @param waitTime Integer
     * @param parameters String
     * @return CommunicationPrimitive
     * @throws IOException
     */
    synchronized CommunicationPrimitive sendRequest(CommunicationPrimitive request, int waitTime,
                                                    String parameters) throws IOException {

        return sendRequest(request, waitTime, parameters, new ArrayList());
    }

    /**
     * Sends a request to SalesDesk without parameters and results.
     * @param request CommunicationPrimitive
     * @param waitTime Integer
     * @return CommunicationPrimitive
     * @throws IOException
     */
    synchronized CommunicationPrimitive sendRequest(CommunicationPrimitive request, int waitTime)
            throws IOException {

        return sendRequest(request, waitTime, null, new ArrayList());
    }

    /**
     * Ends the connection.
     * @throws IOException
     */
    synchronized void endConnection() throws IOException {
        influx.close();
        outflux.close();
        socket.close();

        countDownLatch.countDown();
    }

    /**
     * To string method.
     * @return String
     */
    public String toString() {
        return  companyID + " " + connectionID;
    }
}