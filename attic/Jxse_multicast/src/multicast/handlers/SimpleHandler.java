package multicast.handlers;

import multicast.interfaces.OnReceiveHandler;

public class SimpleHandler implements OnReceiveHandler {

	@Override
	public void execute(String message) {
		System.out.println(message);
	}
}
