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

import java.util.logging.Level;

import net.sbfmc.def.SBFPlugin;
import net.sbfmc.utils.GeneralUtils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class DebugUtils {
	private static boolean debug = true;

	public static void debug(String prefix, String message) {
		debug(prefix, message, Level.INFO, debug, debug);
	}

	public static void debug(String prefix, String message, boolean toConsole) {
		debug(prefix, message, Level.INFO, toConsole, debug);
	}

	public static void debug(String prefix, String message, Level level) {
		debug(prefix, message, level, debug, debug);
	}

	public static void debug(String prefix, String message, Level level, boolean toConsole) {
		debug(prefix, message, level, toConsole, debug);
	}

	public static void debug(String prefix, String message, Level level, boolean toConsole, boolean toAdmins) {
		// editing prefix
		if (prefix == null) {
			prefix = "";
		} else if (!prefix.equals("")) {
			prefix = "[" + prefix.toUpperCase() + "] ";
		}

		// sending message to admins
		if (toAdmins || level.equals(Level.SEVERE)) {
			String sendMessage;

			// generating message
			ChatColor color;

			if (level.equals(Level.WARNING)) {
				color = ChatColor.GOLD;
			} else if (level.equals(Level.SEVERE)) {
				color = ChatColor.DARK_RED;
			} else {
				color = ChatColor.WHITE;
			}

			sendMessage = ChatColor.LIGHT_PURPLE + "[DEBUG] " + color + prefix + message;

			// sending it
			for (Player player : Bukkit.getOnlinePlayers()) {
				if (GeneralUtils.checkAuth(player) && player.isOp()) {
					player.sendMessage(sendMessage);
				}
			}
		}

		// sending message to console
		if (toConsole) {
			SBFPlugin.getPlugin().getLogger().log(level, prefix + message);
		}

		// sending message to log
		DebugLog.writeToLog(prefix + message, level);
	}

	public static void debugError(String prefix, String message, Exception err) {
		debug(prefix, "Error! " + message + ". " + err, Level.SEVERE, true);
		err.printStackTrace();
	}

	public static boolean isDebug() {
		return debug;
	}

	public static void setDebug(boolean debug) {
		DebugUtils.debug = debug;
	}
}
