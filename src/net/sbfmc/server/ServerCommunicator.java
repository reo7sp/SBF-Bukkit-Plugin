/*
 Copyright 2014 Reo_SP

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 	http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
*/

package net.sbfmc.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;

import net.sbfmc.logging.DebugUtils;

public class ServerCommunicator extends Thread {
	private ServerSocket serverSocket;
	private SBFServer server;

	ServerCommunicator(SBFServer server) throws IOException {
		super("SBF-Server-Communicator");

		serverSocket = new ServerSocket(SBFServer.PORT);
		this.server = server;
	}

	@Override
	public void run() {
		try {
			while (server.isRunning() && !serverSocket.isClosed()) {
				server.addConnection(new Connection(serverSocket.accept(), server));
			}
		} catch (SocketException err) {
			// if server socket closed
		} catch (IOException err) {
			DebugUtils.debugError("sbf-server", "Can't accept connection", err);
		}
		try {
			disconnect();
		} catch (IOException err) {
			DebugUtils.debugError("sbf-server", "Can't close server socket", err);
		}
	}

	public void connect() {
		start();
	}

	public void disconnect() throws IOException {
		if (serverSocket != null && !serverSocket.isClosed()) {
			serverSocket.close();
		}
	}

	public SBFServer getServer() {
		return server;
	}
}
