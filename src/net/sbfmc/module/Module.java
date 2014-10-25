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

package net.sbfmc.module;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;

import net.sbfmc.module.commands.Command;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public abstract class Module {
	protected Collection<PlayerSession> playerSessions = Collections.synchronizedSet(new HashSet<PlayerSession>());
	protected Command[] commands;
	private boolean enabled;

	protected abstract void enable() throws Exception;

	protected abstract void disable() throws Exception;

	private void create() throws Exception {
		registerAllCommands();
		enabled = true;
	}

	private void destroy() throws Exception {
		playerSessions.clear();
		commands = null;
		enabled = false;
	}

	public boolean fireCommand(CommandSender sender, String commandName, String[] args) throws Exception {
		if (commands == null) {
			return false;
		}
		for (Command command : commands) {
			if (command.getModuleID() != getID()) {
				continue;
			}
			if (!command.getName().equalsIgnoreCase(commandName) && !command.getAlias().equalsIgnoreCase(commandName)) {
				continue;
			}
			if (!checkPermisson(sender, command.getPermisson(), false)) {
				return true;
			}
			if (command.isDangerous()) {
				if (sender.isOp()) {
					if (args.length != 0 && args[args.length - 1].equalsIgnoreCase("confirm")) {
						String[] newArgs = new String[args.length - 1];
						System.arraycopy(args, 0, newArgs, 0, args.length - 1);
						command.exec(sender, newArgs);
					} else {
						sender.sendMessage(ChatColor.RED + "Вы уверены, что хотите запустить эту комманду? " +
								"Если да, то после всей комманды добавьте слово \"confirm\"");
					}
				} else {
					sender.sendMessage(ChatColor.RED + "Вам нельзя это делать!");
				}
			} else {
				command.exec(sender, args);
			}
			return true;
		}
		return false;
	}

	protected abstract void registerAllCommands();

	public abstract void registerPlayer(Player player) throws Exception;

	public abstract void unregisterPlayer(Player player) throws Exception;

	public abstract int getID();

	public abstract String getName();

	public String getAlias() {
		return getName();
	}

	public boolean checkPermisson(CommandSender sender, String permisson) {
		return checkPermisson(sender, permisson, true);
	}

	public boolean checkPermisson(CommandSender sender, String permisson, boolean silent) {
		boolean result = sender.hasPermission("sbf." + getName() + "." + permisson);
		if (!silent && !result) {
			sender.sendMessage(ChatColor.RED + "Вам нельзя это делать!");
		}
		return result;
	}

	public PlayerSession getPlayerSession(OfflinePlayer player) {
		for (PlayerSession playerSession : playerSessions) {
			if (playerSession.getModuleID() == getID() &&
					playerSession.getPlayer() != null &&
					playerSession.getPlayer().equals(player)) {
				return playerSession;
			}
		}
		return null;
	}

	public PlayerSession getPlayerSession(Player player) {
		for (PlayerSession playerSession : playerSessions) {
			if (playerSession.getModuleID() == getID() &&
					playerSession.getPlayer() != null &&
					playerSession.getPlayer().getPlayer().equals(player)) {
				return playerSession;
			}
		}
		return null;
	}

	public Collection<PlayerSession> getPlayerSessions() {
		return playerSessions;
	}

	public Command[] getCommands() {
		return commands;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + getID();
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		Module other = (Module) obj;
		return other.getID() == getID();
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) throws Exception {
		if (enabled) {
			if (!this.enabled) {
				try {
					create();
					enable();
				} catch (Exception err) {
					this.enabled = false;
					throw err;
				}
			}
		} else {
			if (this.enabled) {
				try {
					disable();
					destroy();
				} catch (Exception err) {
					this.enabled = false;
					throw err;
				}
			}
		}
	}
}
