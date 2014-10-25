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

package net.sbfmc.modules.anticheat.conf;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import net.sbfmc.def.SBFPlugin;
import net.sbfmc.module.conf.DefaultConf;
import net.sbfmc.modules.anticheat.AnticheatModule;

public class DeniedFilesConf extends DefaultConf {
	private AnticheatModule module = (AnticheatModule) getModule();

	@Override
	public void loadConf() throws IOException {
		module.getDeniedFiles().clear();

		BufferedReader buffR = new BufferedReader(new FileReader(confFile));
		String s;
		while ((s = buffR.readLine()) != null) {
			module.getDeniedFiles().add(s);
		}
		buffR.close();
	}

	@Override
	public void saveConf() throws IOException {
		BufferedWriter buffW = new BufferedWriter(new FileWriter(confFile));
		for (String fileName : module.getDeniedFiles()) {
			buffW.write(fileName + "\n");
		}
		buffW.close();
	}

	@Override
	public void initConf() throws IOException {
		confFile = new File(SBFPlugin.getPlugin().getDataFolder(), "deniedFiles.txt");
		createConf();
	}

	@Override
	public void deinitConf() throws IOException {
		confFile = null;
	}

	@Override
	public int getModuleID() {
		return 8;
	}
}
