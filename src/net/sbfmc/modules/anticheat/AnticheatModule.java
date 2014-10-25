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

package net.sbfmc.modules.anticheat;

import java.util.Collection;
import java.util.HashSet;

import net.sbfmc.module.Module;
import net.sbfmc.module.commands.Command;
import net.sbfmc.modules.anticheat.commands.ImportCommand;
import net.sbfmc.modules.anticheat.commands.ReloadCommand;
import net.sbfmc.modules.anticheat.conf.AllowedFilesConf;
import net.sbfmc.modules.anticheat.conf.AnticheatReportsFilesConf;
import net.sbfmc.modules.anticheat.conf.AnticheatReportsNicksConf;
import net.sbfmc.modules.anticheat.conf.DeniedFilesConf;
import net.sbfmc.server.SBFServer;

import org.bukkit.entity.Player;

public class AnticheatModule extends Module {
	private AllowedFilesConf allowedFilesConf;
	private DeniedFilesConf deniedFilesConf;
	private AnticheatReportsFilesConf reportsFilesConf;
	private AnticheatReportsNicksConf reportsNicksConf;
	private Collection<String> allowedFiles = new HashSet<String>();
	private Collection<String> deniedFiles = new HashSet<String>();
	private AnticheatRequestParser anticheatRequestParser;

	@Override
	public void enable() throws Exception {
		// initing confs
		allowedFilesConf = new AllowedFilesConf();
		deniedFilesConf = new DeniedFilesConf();
		reportsFilesConf = new AnticheatReportsFilesConf();
		reportsNicksConf = new AnticheatReportsNicksConf();
		allowedFilesConf.initConf();
		deniedFilesConf.initConf();
		reportsFilesConf.initConf();
		reportsNicksConf.initConf();
		allowedFilesConf.loadConf();
		deniedFilesConf.loadConf();

		// registering request parser
		anticheatRequestParser = new AnticheatRequestParser();
		SBFServer.getInstance().registerRequestParser(anticheatRequestParser);
	}

	@Override
	public void disable() throws Exception {
		// deiniting conf
		allowedFilesConf.deinitConf();
		deniedFilesConf.deinitConf();
		reportsFilesConf.deinitConf();
		reportsNicksConf.deinitConf();
		allowedFilesConf = null;
		deniedFilesConf = null;
		reportsFilesConf = null;
		reportsNicksConf = null;

		// unregistering request parser
		SBFServer.getInstance().unregisterRequestParser(anticheatRequestParser);
		anticheatRequestParser = null;
	}

	@Override
	protected void registerAllCommands() {
		commands = new Command[] {
				new ReloadCommand(),
				new ImportCommand(),
		};
	}

	@Override
	public void registerPlayer(Player player) throws Exception {
		playerSessions.add(new AnticheatPlayerSession(player));
	}

	@Override
	public void unregisterPlayer(Player player) throws Exception {
		playerSessions.remove(new AnticheatPlayerSession(player));
	}

	@Override
	public int getID() {
		return 8;
	}

	@Override
	public String getName() {
		return "Anticheat";
	}

	public Collection<String> getAllowedFiles() {
		return allowedFiles;
	}

	public Collection<String> getDeniedFiles() {
		return deniedFiles;
	}

	public AllowedFilesConf getAllowedFilesConf() {
		return allowedFilesConf;
	}

	public DeniedFilesConf getDeniedFilesConf() {
		return deniedFilesConf;
	}

	public AnticheatReportsFilesConf getReportsFilesConf() {
		return reportsFilesConf;
	}

	public AnticheatReportsNicksConf getReportsNicksConf() {
		return reportsNicksConf;
	}
}
