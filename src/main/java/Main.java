import com.google.gson.Gson;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import static java.util.concurrent.TimeUnit.*;


import static spark.Spark.*;


public class Main {

    public static int INTERVAL = 5;
	private static final ScheduledExecutorService scheduler =
			Executors.newScheduledThreadPool(1);

	public static void main(String[] args) {

		port(getHerokuAssignedPort());

		Gson gson = new Gson();


		// static files
		staticFiles.location("/static");

		// websocket
		webSocket("/ws", Socket.class);

		// counter endpoint
		get("/ping/:id", PingHandler::pingHandler);

        get("/ping/", PingHandler::pingHandlerAll, gson::toJson);

		sendBroadcast();
	}

	public static int getHerokuAssignedPort() {
		ProcessBuilder processBuilder = new ProcessBuilder();
		if (processBuilder.environment().get("PORT") != null) {
			return Integer.parseInt(processBuilder.environment().get("PORT"));
		}
		return 4567;
	}

	public static void sendBroadcast() {
		final Runnable beeper = () -> Socket.sendGraphData();


		final ScheduledFuture<?> beeperHandle =
				scheduler.scheduleAtFixedRate(beeper, 10, INTERVAL, SECONDS);
		scheduler.schedule((Runnable) () -> beeperHandle.cancel(true), 60 * 60, SECONDS);
	}
}