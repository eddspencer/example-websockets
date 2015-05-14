package com.espencer.examples.websockets;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;

/**
 * Allows for cancellation of tasks by id so that they can be run for some finite time
 */
public class ScheduledTaskManager {

	private final Map<String, ScheduledFuture<?>> mapFutures = new ConcurrentHashMap<>();

	public void addTask(final String id, final ScheduledFuture<?> future) {
		mapFutures.put(id, future);
	}

	public boolean cancelTask(final String id) {
		final ScheduledFuture<?> future = mapFutures.get(id);
		if (null != future) {
			future.cancel(false);
			mapFutures.remove(id);
			return true;
		}
		return false;
	}

	public int taskCount() {
		return mapFutures.size();
	}

}
