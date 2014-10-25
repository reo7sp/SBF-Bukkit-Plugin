package net.sbfmc.modules.tools.conf;

import java.io.File;
import java.io.IOException;

import net.sbfmc.def.SBFPlugin;
import net.sbfmc.module.PlayerSession;
import net.sbfmc.module.conf.PlayersConf;
import net.sbfmc.modules.tools.ToolsModule;
import net.sbfmc.modules.tools.ToolsPlayerSession;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;

public class KitConf extends PlayersConf {
	private ToolsModule module = (ToolsModule) getModule();

	@Override
	public PlayerSession loadPlayer(OfflinePlayer player) throws IOException {
		return loadPlayer(new ToolsPlayerSession(player));
	}

	@Override
	public PlayerSession loadPlayer(PlayerSession sessionRaw) throws IOException {
		ToolsPlayerSession session = (ToolsPlayerSession) sessionRaw;

		File file = new File(confFolder, session.getPlayer().getName() + ".yml");
		YamlConfiguration conf = YamlConfiguration.loadConfiguration(file);

		session.setStartKitUsed(conf.getBoolean("startKitUsed"));
		session.setGiftKitUsed(conf.getBoolean("giftKitUsed"));

		return session;
	}

	@Override
	public void savePlayer(OfflinePlayer player) throws IOException {
		savePlayer(module.getPlayerSession(player));
	}

	@Override
	public void savePlayer(PlayerSession sessionRaw) throws IOException {
		ToolsPlayerSession session = (ToolsPlayerSession) sessionRaw;

		File file = new File(confFolder, session.getPlayer().getName() + ".yml");
		YamlConfiguration conf = YamlConfiguration.loadConfiguration(file);

		conf.set("startKitUsed", session.isStartKitUsed());
		conf.set("giftKitUsed", session.isGiftKitUsed());

		conf.save(file);
	}

	@Override
	public void initConf() throws IOException {
		confFolder = new File(SBFPlugin.getPlugin().getDataFolder(), "tools_sessions");
		createConf();
	}

	@Override
	public void deinitConf() throws IOException {
		confFolder = null;
	}

	@Override
	public int getModuleID() {
		return 1;
	}
}
