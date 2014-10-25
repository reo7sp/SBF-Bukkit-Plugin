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

package net.sbfmc.modules.vanilla;

import com.griefcraft.lwc.LWC;
import com.griefcraft.scripting.Module;
import com.griefcraft.scripting.event.LWCAccessEvent;
import com.griefcraft.scripting.event.LWCBlockInteractEvent;
import com.griefcraft.scripting.event.LWCCommandEvent;
import com.griefcraft.scripting.event.LWCDropItemEvent;
import com.griefcraft.scripting.event.LWCProtectionDestroyEvent;
import com.griefcraft.scripting.event.LWCProtectionInteractEvent;
import com.griefcraft.scripting.event.LWCProtectionRegisterEvent;
import com.griefcraft.scripting.event.LWCProtectionRegistrationPostEvent;
import com.griefcraft.scripting.event.LWCProtectionRemovePostEvent;
import com.griefcraft.scripting.event.LWCRedstoneEvent;
import com.griefcraft.scripting.event.LWCReloadEvent;
import com.griefcraft.scripting.event.LWCSendLocaleEvent;

public class VanillaLWCModule implements Module {
	@Override
	public void load(LWC event) {
	}

	@Override
	public void onAccessRequest(LWCAccessEvent event) {
	}

	@Override
	public void onBlockInteract(LWCBlockInteractEvent event) {
	}

	@Override
	public void onCommand(LWCCommandEvent event) {
	}

	@Override
	public void onDestroyProtection(LWCProtectionDestroyEvent event) {
	}

	@Override
	public void onDropItem(LWCDropItemEvent event) {
	}

	@Override
	public void onPostRegistration(LWCProtectionRegistrationPostEvent event) {
	}

	@Override
	public void onPostRemoval(LWCProtectionRemovePostEvent event) {
	}

	@Override
	public void onProtectionInteract(LWCProtectionInteractEvent event) {
	}

	@Override
	public void onRedstone(LWCRedstoneEvent event) {
	}

	@Override
	public void onRegisterProtection(LWCProtectionRegisterEvent event) {
		if (event.getBlock().getWorld().getName().startsWith("Vanilla")) {
			event.setCancelled(true);
		}
	}

	@Override
	public void onReload(LWCReloadEvent event) {
	}

	@Override
	public void onSendLocale(LWCSendLocaleEvent event) {
	}
}
