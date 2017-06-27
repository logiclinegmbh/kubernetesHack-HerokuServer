import spark.Request;
import spark.Response;

public class PingHandler {

	synchronized static String pingHandler(Request req, Response res) {

		PingCounter.getInstance().addCount(req.params("id"));
		res.status(200);
		return "";
	}

	public static Object pingHandlerAll(Request request, Response response) {


		return PingCounter.getInstance().map;

	}
}
