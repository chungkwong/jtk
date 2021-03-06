/*
 * Copyright (C) 2018 Chan Chung Kwong <1m02math@126.com>
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
package cc.fooledit.editor.djvu;
import cc.fooledit.core.*;
import cc.fooledit.editor.djvu.Activator;
import cc.fooledit.spi.*;
import org.osgi.framework.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class Activator implements BundleActivator{
	public static final String NAME=Activator.class.getPackage().getName();
	public static final String CONTENT_TYPE="image/x-djvu";
	public static void onLoad(){
		DataObjectTypeRegistry.addDataObjectType(DjvuObjectType.INSTANCE);
		DataObjectTypeRegistry.addDataEditor(DjvuEditor.INSTANCE,DjvuObject.class);
	}
	public static void onUnLoad(){
	}
	public static void onInstall(){
		CoreModule.CONTENT_TYPE_LOADER_REGISTRY.put(CONTENT_TYPE,DjvuObjectType.class.getName());
		MultiRegistryNode.addChildElement("djv",CONTENT_TYPE,CoreModule.SUFFIX_REGISTRY);
		MultiRegistryNode.addChildElement("djvu",CONTENT_TYPE,CoreModule.SUFFIX_REGISTRY);
	}
	@Override
	public void start(BundleContext bc) throws Exception{
		onInstall();
		onLoad();
	}
	@Override
	public void stop(BundleContext bc) throws Exception{
	}
}
