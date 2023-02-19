package com.lawman.app;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.HashMap;
import java.util.Map;
import java.util.Collections;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.lawman.daemon.IDaemon;
import org.lawman.command.Command;
import org.lawman.command.InsertFileCommand;
import org.lawman.command.SearchFileCommand;

public class AppDaemon implements IDaemon {

    private static final Map<String, Class> commandMap;
    static {
        Map<String, Class> aMap = new HashMap<>();
	InsertFileCommand.Init();
//	SearchFileCommand.Init();
	aMap.put("InsertFile", InsertFileCommand.class);
        aMap.put("SearchFile", SearchFileCommand.class);
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
			String[] result = command.split("-", 2);
			if (result.length != 2) {
			    throw new IllegalArgumentException("command not in correct format");
			}
			if(commandMap.containsKey(result[0])) {
			    ObjectMapper objectMapper = new ObjectMapper();
			    Command cmd = null;
			    if (result[0].equals("InsertFile")) {
				cmd = (InsertFileCommand) objectMapper.readValue(result[1], commandMap.get(result[0]));
			    } else if (result[0].equals("SearchFile")) {
                                cmd = (SearchFileCommand) objectMapper.readValue(result[1], commandMap.get(result[0]));
			    }
			    cmd.execute();
			}
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
