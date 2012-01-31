package multicast.handlers;

import java.io.IOException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.xlightweb.BodyDataSink;
import org.xlightweb.Event;
import org.xlightweb.HttpResponse;
import org.xlightweb.HttpResponseHeader;
import org.xlightweb.IHttpExchange;
import org.xlightweb.IHttpRequestHandler;

class ServerHandler implements IHttpRequestHandler {
	private final Timer timer = new Timer(false);

	public void onRequest(IHttpExchange exchange) throws IOException {
		String requestURI = exchange.getRequest().getRequestURI();
		if (requestURI.equals("/ServerSentEventExample")) {
			sendServerSendPage(exchange, requestURI);
		} else if (requestURI.equals("/Events")) {
			sendEventStream(exchange);
		} else {
			exchange.sendError(404);
		}
	}

	private void sendServerSendPage(IHttpExchange exchange, String uri)
			throws IOException {
		String page = "<html>\r\n " + " <head>\r\n"
				+ "     <script type='text/javascript'>\r\n"
				+ "        var source = new EventSource('Events');\r\n"
				+ "        source.onmessage = function (event) {\r\n"
				+ "          ev = document.getElementById('events');\r\n"
				+ "          ev.innerHTML += \"<br>[in] \" + event.data;\r\n"
				+ "        };\r\n" + "     </script>\r\n" + " </head>\r\n"
				+ " <body>\r\n" + "    <div id=\"events\"></div>\r\n"
				+ " </body>\r\n" + "</html>\r\n ";
		exchange.send(new HttpResponse(200, "text/html", page));
	}

	private void sendEventStream(final IHttpExchange exchange)
			throws IOException {
		// get the last id string
		final String idString = exchange.getRequest().getHeader(
				"Last-Event-Id", "0");
		// sending the response header
		final BodyDataSink sink = exchange.send(new HttpResponseHeader(200,
				"text/event-stream"));
		TimerTask tt = new TimerTask() {
			private int id = Integer.parseInt(idString);

			public void run() {
				try {
					Event event = new Event(new Date().toString(), ++id);
					sink.write(event.toString());
				} catch (IOException ioe) {
					cancel();
					sink.destroy();
				}
			};
		};
		Event event = new Event();
		event.setRetryMillis(5 * 1000);
		event.setComment("time stream");
		sink.write(event.toString());
		timer.schedule(tt, 3000, 3000);
	}
}
