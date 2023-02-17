package com.lawman.app;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.HashMap;
import java.util.Map;
import java.util.Collections;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.lawman.daemon.IDaemon;
import org.lawman.command.Command;
import org.lawman.command.InsertFileCommand;

public class AppDaemon implements IDaemon {

    private static final Map<String, String> commandMap;
    static {
        Map<String, String> aMap = new HashMap<>();
        aMap.put("InsertFileCommand", new InsertFileCommand().Init());
        commandMap = Collections.unmodifiableMap(aMap);
    }

    /**
     * This worker runnable represents the ongoing work to
     * be done by the daemon process.
     * In a real application, it would be doing something useful
     * such as listening for client requests, doing periodic
     * tasks, etc.  This implementation just waits for
     * commands to arrive via a queue.
     */
    private class Worker implements Runnable {
	@Override
	    public void run() {
		System.out.println("worker thread is started");
		while (!shutdown) {
		    try {
			String command = commandQueue.take();
			System.out.println("Received a command: " + command);
			ObjectMapper objectMapper = new ObjectMapper();
			InsertFileCommand cmd = objectMapper.readValue(command, InsertFileCommand.class);
			cmd.execute();
		    } catch (InterruptedException e) {
			System.out.println("Worker thread interrupted, shutting down");
		    } catch (Exception e) {
			System.out.println("Worker thread shutdown " + e.getMessage());
		    }
		}
	    }
    }

    private LinkedBlockingQueue<String> commandQueue;
    private volatile boolean shutdown;
    private Thread workerThread;

    /**
     * Constructor.
     */
    public AppDaemon() {
	commandQueue = new LinkedBlockingQueue<String>();
	shutdown = false;
	workerThread = new Thread(new Worker());
    }

    @Override
	public void start(String instanceName) {
	    System.out.println("Daemon (instance name=" + instanceName + ") is starting!");

	    // start the worker thread
	    workerThread.start();
	}

    @Override
	public void handleCommand(String command) {
	    try {
		// Send the command to the worker
		System.out.println(command);
		commandQueue.put(command);
	    } catch (InterruptedException e) {
		System.out.println("This should not happen");
	    }
	}

    @Override
	public void shutdown() {
	    System.out.println("Daemon is shutting down!");

	    // Let the worker thread know that it is time to shut down
	    shutdown = true;
	    workerThread.interrupt();

	    // Wait for the worker thread to finish
	    System.out.println("Waiting for worker thread to finish...");
	    try {
		workerThread.join();
	    } catch (InterruptedException e) {
		System.out.println("This should not happen");
	    }
	    System.out.println("Worker thread finished");
	}
}
