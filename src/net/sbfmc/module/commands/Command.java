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

package net.sbfmc.module.commands;

import net.sbfmc.module.ModuleIntegration;

import org.bukkit.command.CommandSender;

public abstract class Command extends ModuleIntegration {
	public abstract void exec(CommandSender sender, String[] args) throws Exception;

	public abstract String getName();

	public String getAlias() {
		return getName();
	}

	public String getDescription() {
		return "";
	}

	public String getPermisson() {
		return getName();
	}

	public boolean isDangerous() {
		return false;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + getName().hashCode();
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
		Command other = (Command) obj;
		if (other.getModuleID() != getModuleID()) {
			return false;
		}
		return true;
	}
}
