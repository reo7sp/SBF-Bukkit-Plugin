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
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import net.sbfmc.logging.DebugUtils;

public class SBFServer {
	public static final int PORT = 17879;

	private static SBFServer instance;

	private boolean running;
	private Collection<RequestParser> parsers = new HashSet<RequestParser>();
	private Collection<Connection> connections = Collections.synchronizedCollection(new HashSet<Connection>());
	private ServerCommunicator communicator;

	private SBFServer() {
	}

	public static void startServer() throws IOException {
		instance = new SBFServer();

		instance.start();
	}

	public static void stopServer() throws IOException, NullPointerException {
		instance.stop();

		instance = null;
	}

	public void start() throws IOException {
		DebugUtils.debug("sbf-server", "Starting sbf sub server at port " + PORT, true);

		running = true;

		communicator = new ServerCommunicator(this);
		communicator.connect();
	}

	public void stop() throws IOException {
		DebugUtils.debug("sbf-server", "Stopping sbf sub server at port " + PORT, true);

		running = false;

		for (Connection connection : connections) {
			connection.disconnect();
		}

		communicator.disconnect();
		communicator = null;
	}

	public RequestParser getParser(String header) {
		for (RequestParser parser : parsers) {
			if (parser.getHeader().equalsIgnoreCase(header)) {
				return parser;
			}
		}
		return null;
	}

	public void addConnection(Connection connection) {
		connections.add(connection);
	}

	public void registerRequestParser(RequestParser parser) {
		parsers.add(parser);
	}

	public void unregisterRequestParser(RequestParser parser) {
		parsers.remove(parser);
	}

	public void parseRequest(Connection connection, String request) throws Exception {
		String[] requestParts = request.split(" ", 2);

		if (requestParts.length < 2) {
			requestParts = new String[] { requestParts[0], "" };
		}

		if (requestParts[0].equalsIgnoreCase("HEADER")) {
			// header
			connection.setHeader(requestParts[1]);
		} else if (requestParts[0].equalsIgnoreCase("SECRET")) {
			// client verify
			connection.verify(Long.parseLong(requestParts[1]));

			if (!connection.isVerified()) {
				// disconnect if bad verify code
				try {
					connection.disconnect("Bad verify code!");
				} catch (IOException err) {
					DebugUtils.debugError("sbf-server", "Can't disconnect from connection", err);
				}
			}
		} else if (requestParts[0].equalsIgnoreCase("NAME")) {
			// player name
			connection.setPlayerName(requestParts[1]);
		} else {
			if (connection.isVerified()) {
				// parse
				getParser(connection.getHeader()).parse(connection, requestParts[0], requestParts[1]);
			} else {
				// disconnect if not verified
				try {
					connection.disconnect("Not verified!");
				} catch (IOException err) {
					DebugUtils.debugError("sbf-server", "Can't disconnect from connection", err);
				}
			}
		}
	}

	public static SBFServer getInstance() {
		return instance;
	}

	public boolean isRunning() {
		return running;
	}

	public Collection<Connection> getConnections() {
		return Collections.unmodifiableCollection(connections);
	}
}
