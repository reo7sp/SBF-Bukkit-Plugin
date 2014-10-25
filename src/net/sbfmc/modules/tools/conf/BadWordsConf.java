package net.sbfmc.modules.tools.conf;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import net.sbfmc.def.SBFPlugin;
import net.sbfmc.module.conf.DefaultConf;
import net.sbfmc.modules.tools.ToolsModule;

public class BadWordsConf extends DefaultConf {
	private ToolsModule module = (ToolsModule) getModule();

	@Override
	public void initConf() throws IOException {
		confFile = new File(SBFPlugin.getPlugin().getDataFolder(), "badwords.txt");
		createConf();
	}

	@Override
	public void deinitConf() throws IOException {
		confFile = null;
	}

	@Override
	public void loadConf() throws IOException {
		module.getBadWords().clear();

		try {
			BufferedReader buffR = new BufferedReader(new FileReader(confFile));
			String s;
			while ((s = buffR.readLine()) != null) {
				module.getBadWords().add(s.toLowerCase().trim());
			}
			buffR.close();
		} catch (IOException err) {
		}
	}

	@Override
	public void saveConf() throws IOException {
		BufferedWriter buffW = new BufferedWriter(new FileWriter(confFile));
		for (String word : module.getBadWords()) {
			buffW.write(word + "\n");
		}
		buffW.close();
	}

	@Override
	public int getModuleID() {
		return 1;
	}
}
