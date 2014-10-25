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

package net.sbfmc.utils;

import net.minecraft.server.v1_5_R3.NBTTagCompound;
import net.minecraft.server.v1_5_R3.NBTTagList;
import net.minecraft.server.v1_5_R3.NBTTagString;

import org.bukkit.craftbukkit.v1_5_R3.inventory.CraftItemStack;

public class BookItem {
	private net.minecraft.server.v1_5_R3.ItemStack item;

	public BookItem(org.bukkit.inventory.ItemStack item) {
		this.item = CraftItemStack.asNMSCopy(item);
	}

	public String[] getPages() {
		NBTTagCompound tags = item.getTag();
		if (tags == null) {
			return null;
		}
		NBTTagList pages = tags.getList("pages");
		String[] pagestrings = new String[pages.size()];
		for (int i = 0; i < pages.size(); i++) {
			pagestrings[i] = pages.get(i).toString();
		}
		return pagestrings;
	}

	public String getAuthor() {
		NBTTagCompound tags = item.getTag();
		if (tags == null) {
			return null;
		}
		String author = tags.getString("author");
		return author;
	}

	public String getTitle() {
		NBTTagCompound tags = item.getTag();
		if (tags == null) {
			return null;
		}
		String title = tags.getString("title");
		return title;
	}

	public void setPages(String[] newpages) {
		NBTTagCompound tags = item.tag;
		if (tags == null) {
			tags = item.tag = new NBTTagCompound();
		}
		NBTTagList pages = new NBTTagList("pages");
		// we don't want to throw any errors if the book is blank!
		if (newpages.length == 0) {
			pages.add(new NBTTagString("1", ""));
		} else {
			for (int i = 0; i < newpages.length; i++) {
				pages.add(new NBTTagString("" + i + "", newpages[i]));
			}
		}
		tags.set("pages", pages);
	}

	public void addPages(String[] newpages) {
		NBTTagCompound tags = item.tag;
		if (tags == null) {
			tags = item.tag = new NBTTagCompound();
		}
		NBTTagList pages;
		if (getPages() == null) {
			pages = new NBTTagList("pages");
		} else {
			pages = tags.getList("pages");
		}
		// we don't want to throw any errors if the book is blank!
		if (newpages.length == 0 && pages.size() == 0) {
			pages.add(new NBTTagString("1", ""));
		} else {
			for (int i = 0; i < newpages.length; i++) {
				pages.add(new NBTTagString("" + pages.size() + "", newpages[i]));
			}
		}
		tags.set("pages", pages);
	}

	public void setAuthor(String author) {
		NBTTagCompound tags = item.tag;
		if (tags == null) {
			tags = item.tag = new NBTTagCompound();
		}
		if (author != null && !author.equals("")) {
			tags.setString("author", author);
		}
	}

	public void setTitle(String title) {
		NBTTagCompound tags = item.tag;
		if (tags == null) {
			tags = item.tag = new NBTTagCompound();
		}
		if (title != null && !title.equals("")) {
			tags.setString("title", title);
		}
	}

	public org.bukkit.inventory.ItemStack getItemStack() {
		return CraftItemStack.asBukkitCopy(item);
	}
}
