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

package net.sbfmc.modules.psycho;

import java.io.IOException;
import java.util.Collection;

import net.sbfmc.def.SBFPlugin;
import net.sbfmc.module.Module;
import net.sbfmc.module.PlayerSession;
import net.sbfmc.module.commands.Command;
import net.sbfmc.modules.psycho.commands.AddCommand;
import net.sbfmc.modules.psycho.commands.RemoveCommand;
import net.sbfmc.modules.psycho.conf.PsychoConf;
import net.sbfmc.modules.tools.ToolsPlayerSession;
import net.sbfmc.modules.tools.ToolsUtils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class PsychoModule extends Module {
	private PsychoConf psychoConf;

	@Override
	public void enable() throws Exception {
		// registering commands
		registerAllCommands();

		// initing conf
		psychoConf = new PsychoConf();
		psychoConf.initConf();

		// registering listener
		Bukkit.getPluginManager().registerEvents(new PsychoListener(), SBFPlugin.getPlugin());
	}

	@Override
	public void disable() throws Exception {
		// deiniting conf
		for (PlayerSession session : playerSessions) {
			psychoConf.savePlayer(session);
		}
		psychoConf.deinitConf();
		psychoConf = null;
	}

	@Override
	protected void registerAllCommands() {
		commands = new Command[] {
				new AddCommand(),
				new RemoveCommand(),
		};
	}

	@Override
	public void registerPlayer(Player player) throws Exception {
		PsychoPlayerSession session = (PsychoPlayerSession) psychoConf.loadPlayer(player);
		playerSessions.add(session);

		ToolsPlayerSession toolsSession = (ToolsPlayerSession) SBFPlugin.getModule(1).getPlayerSession(player);
		Collection<ToolsPlayerSession> sessionsWithSameIP = ToolsUtils.checkIP(toolsSession.getIP());
		if (sessionsWithSameIP == null) {
			return;
		}
		for (ToolsPlayerSession loopToolsSession : sessionsWithSameIP) {
			PsychoPlayerSession loopSession = (PsychoPlayerSession) getPlayerSession(loopToolsSession.getPlayer());

			if (loopSession == null) {
				continue;
			}

			if (loopSession.isInPsycho()) {
				PsychoUtils.sendToPsycho(session, "Пациент " + loopSession.getPlayer().getName() + " хотел нагло зайти под ником " + player.getName() + " и уйти от наказания! Возвращаем его обратно!");
				player.sendMessage(ChatColor.RED + "Думаешь созданием нового ника, ты сможешь уйти от наказания? >:3");

				break;
			}
		}

		if (session.isInPsycho() && System.currentTimeMillis() - session.getInPsychoTime() > session.getPsychoTime()) {
			PsychoUtils.returnFromPsycho(session);
			player.sendMessage(ChatColor.GREEN + "Ура! Ты вышел из нашей психушки! Надеюсь, ты больше не будешь нарушать правила? Иначе ты сюда вернёшься >:3");
		}
	}

	@Override
	public void unregisterPlayer(Player player) throws Exception {
		playerSessions.remove(getPlayerSession(player));
	}

	@Override
	public int getID() {
		return 6;
	}

	@Override
	public String getName() {
		return "Psycho";
	}

	public PsychoConf getPsychoConf() {
		return psychoConf;
	}

	@Override
	public PlayerSession getPlayerSession(OfflinePlayer player) {
		PlayerSession session = super.getPlayerSession(player);
		if (session == null) {
			try {
				session = psychoConf.loadPlayer(player);
			} catch (IOException err) {
			}
		}
		return session;
	}
}
