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

package net.sbfmc.modules.mooncraft.economic.items;

import net.sbfmc.modules.mooncraft.MooncraftPlayerSession;

public class Oxygen extends BuyableItem {
	public Oxygen() {
		super(3, "oxygen");
	}

	@Override
	public void buyAction(MooncraftPlayerSession session) {
		session.setOxygen(99);
	}
}
