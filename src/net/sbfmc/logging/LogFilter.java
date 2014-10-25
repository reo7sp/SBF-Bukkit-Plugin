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

import java.util.Collection;
import java.util.HashSet;
import java.util.logging.Filter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class LogFilter implements Filter {
	private static LogFilter instance;
	private static Collection<Filter> filters = new HashSet<Filter>();

	private LogFilter() {
	}

	@Override
	public boolean isLoggable(LogRecord record) {
		for (Filter filter : filters) {
			if (!filter.isLoggable(record)) {
				return false;
			}
		}
		return true;
	}

	public static void registerMe() {
		for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
			plugin.getLogger().setFilter(getInstance());
		}
		Bukkit.getLogger().setFilter(getInstance());
		Bukkit.getServer().getLogger().setFilter(getInstance());
		Logger.getLogger("Minecraft").setFilter(getInstance());
	}

	public static void unregisterMe() {
		for (Plugin plugin : Bukkit.getPluginManager().getPlugins()) {
			plugin.getLogger().setFilter(null);
		}
		Bukkit.getLogger().setFilter(null);
		Logger.getLogger("Minecraft").setFilter(null);

		filters.clear();
	}

	public static void registerFilter(Filter filter) {
		filters.add(filter);
	}

	public static void unregisterFilter(Filter filter) {
		filters.remove(filter);
	}

	public static LogFilter getInstance() {
		if (instance == null) {
			instance = new LogFilter();
		}
		return instance;
	}
}
