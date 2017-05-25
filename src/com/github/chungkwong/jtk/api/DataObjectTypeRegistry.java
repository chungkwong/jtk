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
package com.github.chungkwong.jtk.api;
import com.github.chungkwong.jtk.example.binary.*;
import com.github.chungkwong.jtk.example.image.*;
import com.github.chungkwong.jtk.example.media.*;
import com.github.chungkwong.jtk.example.text.*;
import com.github.chungkwong.jtk.example.tool.*;
import com.github.chungkwong.jtk.model.*;
import com.github.chungkwong.jtk.util.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class DataObjectTypeRegistry{
	private static final HashMap<Class<? extends DataObject>,List<Cache<DataEditor>>> editors=new HashMap<>();
	private static final List<DataObjectType> types=new ArrayList<>();
	public static void addDataObjectType(DataObjectType type){
		types.add(type);
	}
	public static void addDataEditor(Supplier<DataEditor> editor,Class<? extends DataObject> objectClass){
		if(!editors.containsKey(objectClass))
			editors.put(objectClass,new LinkedList<>());
		editors.get(objectClass).add(0,new Cache<>(editor));
	}
	public static List<DataEditor> getDataEditors(Class<? extends DataObject> cls){
		return ((List<Cache<DataEditor>>)editors.getOrDefault(cls,Collections.EMPTY_LIST)).stream().map((c)->c.get()).collect(Collectors.toList());
	}
	public static List<DataObjectType> getPreferedDataObjectType(String mime){
		LinkedList<DataObjectType> cand=new LinkedList<DataObjectType>();
		if(mime.startsWith("video/")||mime.startsWith("audio/"))
			cand.add(MediaObjectType.INSTANCE);
		else if(mime.startsWith("image/"))
			cand.add(ImageObjectType.INSTANCE);
		else if(mime.startsWith("text/")||mime.startsWith("application/"))
			cand.add(TextObjectType.INSTANCE);
		else
			cand.add(BinaryObjectType.INSTANCE);
		return cand;
	}
	public static List<DataObjectType> getDataObjectTypes(){
		return types;
	}
	static{
		addDataObjectType(TextObjectType.INSTANCE);
		addDataEditor(()->new StructuredTextEditor(),TextObject.class);
		addDataEditor(()->new TextEditor(),TextObject.class);
		addDataObjectType(ImageObjectType.INSTANCE);
		addDataEditor(()->new IconEditor(),ImageObject.class);
		addDataEditor(()->new ImageEditor(),ImageObject.class);
		addDataObjectType(MediaObjectType.INSTANCE);
		addDataEditor(()->new MediaEditor(),MediaObject.class);
		addDataObjectType(BinaryObjectType.INSTANCE);
		addDataEditor(()->new BinaryEditor(),BinaryObject.class);
		addDataEditor(()->new Browser(),BrowserData.class);
	}
}
