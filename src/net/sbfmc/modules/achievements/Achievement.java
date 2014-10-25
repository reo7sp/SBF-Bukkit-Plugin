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

package net.sbfmc.modules.achievements;

import java.util.Map;

import org.bukkit.event.Event;

public abstract class Achievement {
	public void fireBukkitEvent(AchievementsPlayerSession session, Event event) {
	}

	public abstract void fireEvent(AchievementsPlayerSession session, String name, Map<String, Object> args);

	public abstract String getHumanName();

	public String getName() {
		return getClass().getSimpleName();
	}

	public abstract String getDescription();

	public abstract int getMaxPoints();
}
