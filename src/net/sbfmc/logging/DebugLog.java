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

package net.sbfmc.logging;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;

import net.sbfmc.def.SBFPlugin;
import net.sbfmc.module.conf.DefaultConf;

public class DebugLog extends DefaultConf {
	private static final DebugLog INSTANCE = new DebugLog();
	private static String lastLogMessage;

	private DebugLog() {
		try {
			initConf();
		} catch (IOException err) {
			err.printStackTrace();
		}
	}

	public static void writeToLog(String message) {
		writeToLog(message, Level.INFO);
	}

	public static void writeToLog(String message, Level level) {
		lastLogMessage = "[" + new SimpleDateFormat("dd-MM-yy HH:mm").format(new Date()) + "] [" + level + "] " + message;
		try {
			INSTANCE.saveConf();
		} catch (IOException err) {
			err.printStackTrace();
		}
	}

	@Override
	public void loadConf() throws IOException {
	}

	@Override
	public void saveConf() throws IOException {
		if (lastLogMessage == null) {
			return;
		}
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(confFile, true));
			writer.write(lastLogMessage + "\n");
			writer.close();
		} catch (IOException err) {
			throw err;
		} finally {
			lastLogMessage = null;
		}
	}

	@Override
	public void initConf() throws IOException {
		confFile = new File(SBFPlugin.getPlugin().getDataFolder(), "debugLog.txt");

		if (confFile.exists() && confFile.length() / 1024 / 1024 > 64) {
			confFile.delete();
		}

		createConf();
	}

	@Override
	public void deinitConf() throws IOException {
		confFile = null;
	}

	@Override
	public int getModuleID() {
		return 3;
	}
}
