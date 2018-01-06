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
package cc.fooledit.editor.text;
import cc.fooledit.*;
import cc.fooledit.core.*;
import cc.fooledit.spi.*;
import java.util.logging.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class TextEditorModule{
	public static final String NAME="editor.text";
	public static final RegistryNode<String,Object,String> REGISTRY=new SimpleRegistryNode<>();
	public static final RegistryNode<String,Command,String> COMMAND_REGISTRY=new SimpleRegistryNode<>();
	public static final RegistryNode<String,String,String> KEYMAP_REGISTRY=new SimpleRegistryNode<>();
	public static final RegistryNode<String,String,String> LOCALE_REGISTRY=new SimpleRegistryNode<>();
	public static final RegistryNode<String,Object,String> MENU_REGISTRY=new SimpleRegistryNode<>();
	public static void onLoad(){
		DataObjectTypeRegistry.addDataObjectType(TextObjectType.INSTANCE);
		DataObjectTypeRegistry.addDataEditor(()->new StructuredTextEditor(),TextObject.class);
		CoreModule.TEMPLATE_TYPE_REGISTRY.addChild("text",(obj)->new TextTemplate((String)obj.get("name"),(String)obj.get("description"),(String)obj.get("file"),(String)obj.get("mime"),(String)obj.get("module")));
		try{
			((ListRegistryNode)CoreModule.TEMPLATE_REGISTRY.getChild("children")).addChild(
					StandardSerializiers.JSON_SERIALIZIER.decode(Helper.readText(Main.INSTANCE.getFile("templates.json",NAME))));
		}catch(Exception ex){
			Logger.getGlobal().log(Level.INFO,null,ex);
		}
	}
	public static void onUnLoad(){

	}
}