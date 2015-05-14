package com.espencer.examples.websockets;

import java.time.LocalTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.websocket.CloseReason;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/time")
public class SocketExampleEndpoint {

	private final long pushRateMillis = 1000;
	private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
	private final ScheduledTaskManager taskManager = new ScheduledTaskManager();

	@OnOpen
	public void onOpen(final Session session) {
		print(session, "opening");
		final ScheduledFuture<?> future = executor.scheduleAtFixedRate(() -> sendData(session), 0L, pushRateMillis, TimeUnit.MILLISECONDS);
		taskManager.addTask(session.getId(), future);
	}

	@OnClose
	public void onClose(final Session session, final CloseReason closeReason) {
		print(session, "closing");
		cancelTask(session);
	}

	@OnMessage
	public void onMessage(final Session session, final String msg) {
		print(session, "sent: " + msg);
	}

	private void print(final Session session, final String msg) {
		System.out.println("Socket [" + session.getId() + "] " + msg);
	}

	private void sendData(final Session session) {
		if (session.isOpen()) {
			final String data = "Time is now " + LocalTime.now().toString();
			session.getAsyncRemote().sendText(data);
			print(session, "sending data: " + data);
		} else {
			// Ensure the task is cancelled
			cancelTask(session);
		}
	}

	private void cancelTask(final Session session) {
		taskManager.cancelTask(session.getId());

		// End point is destroyed when no more sessions opened
		if (0 == taskManager.taskCount()) {
			executor.shutdown();
		}
	}

}
