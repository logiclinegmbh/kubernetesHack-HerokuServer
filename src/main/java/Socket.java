import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;

import java.io.*;
import java.util.*;
import java.util.concurrent.*;


@WebSocket
public class Socket {

    // Store sessions if you want to, for example, broadcast a message to all users
    private static final Queue<Session> sessions = new ConcurrentLinkedQueue<>();

    private static final Queue<Map<String, Integer>> counter = new ConcurrentLinkedQueue<>();

    @OnWebSocketConnect
    public void connected(Session session) {
        sessions.add(session);
    }

    @OnWebSocketClose
    public void closed(Session session, int statusCode, String reason) {
        sessions.remove(session);
    }

    @OnWebSocketMessage
    public void message(Session session, String message) throws IOException {
        System.out.println("Got: " + message);   // Print message
        session.getRemote().sendString(message); // and send it back
    }

    public static void sendBroadcastMessage(String message) {
        sessions.forEach(session -> {
            try {
                session.getRemote().sendString(message);
            } catch (IOException e) {
                System.out.println("failed to send message");
            }
        });
    }

    public static void sendGraphData() {

        PingCounter.getInstance().intervalElapsed();

        List<List<Object>> graphData = new LinkedList<>();

        PingCounter.getInstance().history.forEach((key, values) -> {
            values.set(0, key);
            graphData.add(values);
        });

        sendBroadcastMessage(new Gson().toJson(graphData));
    }
}
