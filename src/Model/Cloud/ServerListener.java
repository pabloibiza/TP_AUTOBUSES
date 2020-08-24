/**
 *  Serverlistener.java
 *
 *
 */

package Model.Cloud;

import java.io.IOException;
import java.util.List;

/**
 * Listener interface to receive server's requests.
 *
 */
public interface ServerListener {
    /**
     * Used to notify a server's request.
     *
     */
    public boolean serverRequestFired(CommunicationPrimitive request, List<String> parameters) throws IOException;
}
