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

package net.sbfmc.modules.sbfdevsite;

import net.sbfmc.logging.LogFilter;
import net.sbfmc.module.Module;
import net.sbfmc.server.SBFServer;

import org.bukkit.entity.Player;

public class SbfDevSiteModule extends Module {
	private SbfDevSiteRequestParser sbfDevSiteRequestParser;
	private LogListener logListener;
	private String[] log = new String[250];

	@Override
	public void enable() throws Exception {
		sbfDevSiteRequestParser = new SbfDevSiteRequestParser();
		SBFServer.getInstance().registerRequestParser(sbfDevSiteRequestParser);

		logListener = new LogListener();
		LogFilter.registerFilter(logListener);
	}

	@Override
	public void disable() throws Exception {
		SBFServer.getInstance().unregisterRequestParser(sbfDevSiteRequestParser);
		sbfDevSiteRequestParser = null;

		LogFilter.unregisterFilter(logListener);
		logListener = null;
	}

	@Override
	protected void registerAllCommands() {
	}

	@Override
	public void registerPlayer(Player player) throws Exception {
	}

	@Override
	public void unregisterPlayer(Player player) throws Exception {
	}

	public void addToLog(String string) {
		for (int i = 1; i < log.length; i++) {
			log[i - 1] = log[i];
		}
		log[log.length - 1] = string;
	}

	public String[] getLog() {
		return log;
	}

	@Override
	public int getID() {
		return 10;
	}

	@Override
	public String getName() {
		return "SBF-DEV site";
	}
}
