/*
 * Copyright (C) 2017 Chan Chung Kwong <1m02math@126.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package cc.fooledit.spi;
import cc.fooledit.*;
import cc.fooledit.api.*;
import cc.fooledit.setting.*;
import java.io.*;
import java.util.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class Registry extends SimpleRegistryNode<String,RegistryNode<?,?,String>,String>{
	public static final Registry ROOT=new Registry();
	private Registry(){
		super();
	}
	public <K,V> RegistryNode<K,V,String> resolve(String path){
		RegistryNode node=Registry.ROOT;
		for(String name:path.split("/"))
			node=(RegistryNode)node.getOrCreateChild(name);
		return node;
	}
	public NavigableRegistryNode<String,String,String> registerKeymap(String module){
		TreeMap<String,String> mapping=new TreeMap<>();
		File src=(File)SettingManager.getOrCreate(module).get("keymap-file",null);
		if(src!=null)
			mapping.putAll((Map<String,String>)(Object)Main.INSTANCE.loadJSON(src));
		NavigableRegistryNode<String,String,String> registry=new NavigableRegistryNode<>(mapping);
		((RegistryNode<String,?,String>)ROOT.getOrCreateChild(module)).addChild(CoreModule.KEYMAP_REGISTRY_NAME,registry);
		return registry;
	}
}
