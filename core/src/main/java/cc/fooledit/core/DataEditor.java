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
import cc.fooledit.spi.*;
import javafx.scene.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public interface DataEditor<T extends DataObject<T>>{
	Node edit(T data,Object remark,RegistryNode<String,Object> meta);
	default void dispose(Node node,RegistryNode<String,Object> meta){
		
	}
	default RegistryNode<String,Command> getCommandRegistry(){
		return new SimpleRegistryNode<>();
	}
	default NavigableRegistryNode<String,String> getKeymapRegistry(){
		return new NavigableRegistryNode<>();
	}
	default MenuRegistry getMenuRegistry(){
		return new MenuRegistry();
	}
	default Object getRemark(Node node){
		return null;
	}
	String getName();
}