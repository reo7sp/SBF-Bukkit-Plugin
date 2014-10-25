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

package net.sbfmc.module.conf;

import java.io.File;
import java.io.IOException;

public abstract class DefaultConf extends Conf {
	protected File confFile;

	public abstract void loadConf() throws IOException;

	public abstract void saveConf() throws IOException;

	public File getConfFile() {
		return confFile;
	}

	@Override
	protected void createConf() throws IOException {
		if (!confFile.exists()) {
			confFile.getParentFile().mkdirs();
			confFile.createNewFile();
		}
	}
}
