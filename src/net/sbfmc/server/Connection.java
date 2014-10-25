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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;

import net.sbfmc.logging.DebugUtils;

public class Connection extends Thread {
	private SBFServer server;
	private Socket socket;
	private BufferedReader input;
	private PrintWriter output;
	private String name;
	private String header;
	private boolean verified;

	public Connection(Socket socket, SBFServer server) throws IOException {
		super("SBF-Server-Connection");

		this.socket = socket;
		this.server = server;

		socket.setSoTimeout(60000);

		input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		output = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);

		start();
	}

	@Override
	public void run() {
		try {
			String request;
			while ((request = input.readLine()) != null) {
				server.parseRequest(this, request);
			}
		} catch (SocketException err) {
			// if server socket closed
		} catch (SocketTimeoutException err) {
			// if socket timeouted
		} catch (Exception err) {
			DebugUtils.debugError("sbf-server", "Can't parse request", err);
		}
		try {
			disconnect();
		} catch (IOException err) {
		}
	}

	public void disconnect() throws IOException {
		disconnect("");
	}

	public void disconnect(String message) throws IOException {
		output.println("DISCONNECT " + message);

		input.close();
		output.close();
		socket.close();
	}

	public void verify(long secret) {
		if (secret == SBFServer.getInstance().getParser(getHeader()).getClientSecret()) {
			verified = true;
			sendResponse("SECRET " + SBFServer.getInstance().getParser(getHeader()).getServerSecret());
		}
	}

	public void sendResponse(String response) {
		output.println(response);
	}

	public String getPlayerName() {
		return name;
	}

	public void setPlayerName(String name) {
		this.name = name;
	}

	public boolean isVerified() {
		return verified;
	}

	public SBFServer getServer() {
		return server;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		if (header != null && !header.isEmpty()) {
			this.header = header;
		}
	}
}
