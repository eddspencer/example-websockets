package com.espencer.examples.websockets;

import java.io.IOException;

import javax.websocket.ClientEndpoint;
import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;

@ClientEndpoint
public class SocketExampleClient {

	@OnOpen
	public void onOpen(final Session session) throws IOException {
		System.out.println("Connected ... " + session.getId());
	}

	@OnMessage
	public String onMessage(final String message, final Session session) {
		System.out.println("Received ...." + message);
		return "Message understood: " + message;
	}

	@OnClose
	public void onClose(final Session session, final CloseReason closeReason) {
		System.out.println(String.format("Session %s close because of %s", session.getId(), closeReason));
	}

}
