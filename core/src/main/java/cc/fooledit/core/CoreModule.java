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
package cc.fooledit.core;
import cc.fooledit.control.property.*;
import cc.fooledit.spi.*;
import java.io.*;
import java.util.*;
import java.util.function.*;
import java.util.logging.*;
import javafx.collections.*;
import javafx.concurrent.*;
import javafx.scene.control.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class CoreModule{
	public static final String NAME="core";
	public static final String APPLICATION_REGISTRY_NAME="application";
	public static final String CLIP_REGISTRY_NAME="clip";
	public static final String CONTENT_TYPE_ALIAS_REGISTRY_NAME="content_type_alias";
	public static final String CONTENT_TYPE_SUPERCLASS_REGISTRY_NAME="content_type_superclass";
	public static final String CONTENT_TYPE_DETECTOR_REGISTRY_NAME="content_type_detector";
	public static final String CONTENT_TYPE_LOADER_REGISTRY_NAME="content_type_loader";
	public static final String SUFFIX_REGISTRY_NAME="suffix";
	public static final String GLOB_REGISTRY_NAME="glob";
	public static final String COMMAND_REGISTRY_NAME="command";
	public static final String DATA_OBJECT_REGISTRY_NAME="data_object";
	public static final String DATA_OBJECT_TYPE_REGISTRY_NAME="data_object_type";
	public static final String DATA_OBJECT_EDITOR_REGISTRY_NAME="data_object_editor";
	public static final String DYNAMIC_MENU_REGISTRY_NAME="dynamic_menu";
	public static final String TYPE_TO_EDITOR_REGISTRY_NAME="type_to_editor";
	public static final String EDITOR_TO_TOOLBOX_REGISTRY_NAME="editor_to_toolbox";
	public static final String EVENT_REGISTRY_NAME="event";
	public static final String HISTORY_REGISTRY_NAME="history";
	public static final String INSTALLED_MODULE_REGISTRY_NAME="installed_module";
	public static final String INSTALLING_MODULE_REGISTRY_NAME="installing_module";
	public static final String KEYMAP_REGISTRY_NAME="keymap";
	public static final String MESSAGE_REGISTRY_NAME="message";
	public static final String MENU_REGISTRY_NAME="menu";
	public static final String PROPERTY_EDITOR_REGISTRY_NAME="property_editor";
	public static final String SERIALIZIER_REGISTRY_NAME="serializier";
	public static final String TASK_REGISTRY_NAME="task";
	public static final String TEMPLATE_REGISTRY_NAME="template";
	public static final String TEMPLATE_TYPE_REGISTRY_NAME="template_type";
	public static final String TOOLBOX_REGISTRY_NAME="toolbox";
	public static final String WINDOW_REGISTRY_NAME="window";
	public static final String MISC_REGISTRY_NAME="misc";
	public static final String PERSISTENT_REGISTRY_NAME="persistent";
	public static final RegistryNode<String,Object> REGISTRY=(RegistryNode<String,Object>)Registry.ROOT.getOrCreateChild(NAME);
	public static final RegistryNode<String,String> APPLICATION_REGISTRY=(RegistryNode<String,String>)REGISTRY.getOrCreateChild(APPLICATION_REGISTRY_NAME);
	public static final RegistryNode<String,Object> CLIP_REGISTRY=(RegistryNode<String,Object>)REGISTRY.getOrCreateChild(CLIP_REGISTRY_NAME);
	public static final RegistryNode<String,ContentTypeDetector> CONTENT_TYPE_DETECTOR_REGISTRY=(RegistryNode<String,ContentTypeDetector>)REGISTRY.getOrCreateChild(CONTENT_TYPE_DETECTOR_REGISTRY_NAME);
	public static final RegistryNode<String,String> CONTENT_TYPE_ALIAS_REGISTRY=(RegistryNode<String,String>)REGISTRY.getOrCreateChild(CONTENT_TYPE_ALIAS_REGISTRY_NAME);
	public static final RegistryNode<String,String> CONTENT_TYPE_SUPERCLASS_REGISTRY=(RegistryNode<String,String>)REGISTRY.getOrCreateChild(CONTENT_TYPE_SUPERCLASS_REGISTRY_NAME);
	public static final RegistryNode<String,String> CONTENT_TYPE_LOADER_REGISTRY=(RegistryNode<String,String>)REGISTRY.getOrCreateChild(CONTENT_TYPE_LOADER_REGISTRY_NAME);
	public static final RegistryNode<String,ListRegistryNode<String>> SUFFIX_REGISTRY=(RegistryNode<String,ListRegistryNode<String>>)REGISTRY.getOrCreateChild(SUFFIX_REGISTRY_NAME);
	public static final RegistryNode<String,String> GLOB_REGISTRY=(RegistryNode<String,String>)REGISTRY.getOrCreateChild(GLOB_REGISTRY_NAME);
	public static NavigableRegistryNode<String,RegistryNode> DATA_OBJECT_REGISTRY=new NavigableRegistryNode<>();
	public static final RegistryNode<String,DataObjectType> DATA_OBJECT_TYPE_REGISTRY=(RegistryNode<String,DataObjectType>)REGISTRY.getOrCreateChild(DATA_OBJECT_TYPE_REGISTRY_NAME);
	public static final RegistryNode<String,DataEditor> DATA_OBJECT_EDITOR_REGISTRY=(RegistryNode<String,DataEditor>)REGISTRY.getOrCreateChild(DATA_OBJECT_EDITOR_REGISTRY_NAME);
	public static final RegistryNode<String,Consumer<ObservableList<MenuItem>>> DYNAMIC_MENU_REGISTRY=(RegistryNode<String,Consumer<ObservableList<MenuItem>>>)REGISTRY.getOrCreateChild(DYNAMIC_MENU_REGISTRY_NAME);
	public static final RegistryNode<String,ListRegistryNode<String>> TYPE_TO_EDITOR_REGISTRY=(RegistryNode<String,ListRegistryNode<String>>)REGISTRY.getOrCreateChild(TYPE_TO_EDITOR_REGISTRY_NAME);
	public static final RegistryNode<String,ListRegistryNode<String>> EDITOR_TO_TOOLBOX_REGISTRY=(RegistryNode<String,ListRegistryNode<String>>)REGISTRY.getOrCreateChild(EDITOR_TO_TOOLBOX_REGISTRY_NAME);
	public static final RegistryNode<String,ListRegistryNode<Consumer>> EVENT_REGISTRY=(RegistryNode<String,ListRegistryNode<Consumer>>)REGISTRY.getOrCreateChild(EVENT_REGISTRY_NAME);
	public static ListRegistryNode<RegistryNode<String,Object>> HISTORY_REGISTRY=fromJSON("file_history.json",()->new ListRegistryNode<>(new LinkedList<>()));
	public static final RegistryNode<String,Class> INSTALLED_MODULE_REGISTRY=(RegistryNode<String,Class>)REGISTRY.getOrCreateChild(INSTALLED_MODULE_REGISTRY_NAME);
	public static final RegistryNode<String,Object> INSTALLING_MODULE_REGISTRY=(RegistryNode<String,Object>)REGISTRY.getOrCreateChild(INSTALLING_MODULE_REGISTRY_NAME);
	public static final RegistryNode<Class,PropertyEditorFactory> PROPERTY_EDITOR_REGISTRY=(RegistryNode<Class,PropertyEditorFactory>)REGISTRY.getOrCreateChild(PROPERTY_EDITOR_REGISTRY_NAME);
	public static final RegistryNode<String,Serializier> SERIALIZIER_REGISTRY=(RegistryNode<String,Serializier>)REGISTRY.getOrCreateChild(SERIALIZIER_REGISTRY_NAME);
	public static final RegistryNode<String,Task> TASK_REGISTRY=(RegistryNode<String,Task>)REGISTRY.getOrCreateChild(TASK_REGISTRY_NAME);
	public static final RegistryNode<String,Object> TEMPLATE_REGISTRY=(RegistryNode<String,Object>)REGISTRY.getOrCreateChild(TEMPLATE_REGISTRY_NAME);
	public static final RegistryNode<String,Function<Map<Object,Object>,Template>> TEMPLATE_TYPE_REGISTRY=(RegistryNode<String,Function<Map<Object,Object>,Template>>)REGISTRY.getOrCreateChild(TEMPLATE_TYPE_REGISTRY_NAME);
	public static final RegistryNode<String,ToolBox> TOOLBOX_REGISTRY=(RegistryNode<String,ToolBox>)REGISTRY.getOrCreateChild(TOOLBOX_REGISTRY_NAME);
	public static final RegistryNode<String,Object> MISC_REGISTRY=(RegistryNode<String,Object>)REGISTRY.getOrCreateChild(MISC_REGISTRY_NAME);
	public static final RegistryNode<String,Command> COMMAND_REGISTRY=(RegistryNode<String,Command>)REGISTRY.getOrCreateChild(COMMAND_REGISTRY_NAME);
	public static final ListRegistryNode<String> PERSISTENT_REGISTRY=new ListRegistryNode<>();
	private static <T> T fromJSON(String file,Supplier<T> def){
		try{
			return (T)StandardSerializiers.JSON_SERIALIZIER.decode(Helper.readText(new File(Main.INSTANCE.getUserPath(),file)));
		}catch(Exception ex){
			Logger.getLogger(CoreModule.class.getName()).log(Level.INFO,null,ex);
			return def.get();
		}
	}
	static{
		TEMPLATE_REGISTRY.put("name","");
		TEMPLATE_REGISTRY.put("module",Activator.class.getPackage().getName());
		TEMPLATE_REGISTRY.put("children",new ListRegistryNode<>());
		REGISTRY.put(PERSISTENT_REGISTRY_NAME,PERSISTENT_REGISTRY);
		PERSISTENT_REGISTRY.put(NAME+"/"+PERSISTENT_REGISTRY_NAME);
		MISC_REGISTRY.putIfAbsent(ModuleRegistry.REPOSITORY,"https://raw.githubusercontent.com/chungkwong/fooledit/master/MODULES");
		MISC_REGISTRY.putIfAbsent(ScriptAPI.ENGINE,"JSchemeMin");
		PROPERTY_EDITOR_REGISTRY.put(String.class,new StringEditorFactory());
		PROPERTY_EDITOR_REGISTRY.put(Boolean.class,new BooleanEditorFactory());
		PROPERTY_EDITOR_REGISTRY.put(Number.class,new NumberEditorFactory());
		PROPERTY_EDITOR_REGISTRY.put(List.class,new ListEditorFactory());
		PROPERTY_EDITOR_REGISTRY.put(Map.class,new MapEditorFactory());
		PROPERTY_EDITOR_REGISTRY.put(Object.class,new BeanEditorFactory());
	}
}
