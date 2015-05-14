package com.espencer.examples.websockets;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.concurrent.CountDownLatch;

import javax.websocket.DeploymentException;
import javax.websocket.Session;

import junit.framework.TestCase;

import org.glassfish.tyrus.client.ClientManager;
import org.glassfish.tyrus.server.Server;
import org.junit.Test;

public class TestSocketExampleEndpoint extends TestCase {

	private final CountDownLatch latch = new CountDownLatch(1);

	private void runServer() {
		final Server server = new Server("localhost", 18080, "/", SocketExampleEndpoint.class);
		try {
			server.start();
			latch.await();
		} catch (final InterruptedException | DeploymentException e) {
			e.printStackTrace();
		} finally {
			server.stop();
		}
	}

	private Session connect() throws DeploymentException, URISyntaxException {
		final ClientManager client = ClientManager.createClient();
		client.setAsyncSendTimeout(3000);
		return client.connectToServer(SocketExampleClient.class, new URI("ws://localhost:18080/time"));
	}

	@Test
	public void testSocketConnection() throws Exception {
		new Thread(() -> runServer()).start();

		try {
			final Session session = connect();
			session.getBasicRemote().sendText("Test");
			Thread.sleep(2000);
		} finally {
			latch.countDown();
		}
	}
}
