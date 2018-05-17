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
package cc.fooledit.editor.msoffice.visio;
import cc.fooledit.core.*;
import cc.fooledit.editor.msoffice.*;
import cc.fooledit.spi.*;
import javafx.scene.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class VsdEditor implements DataEditor<VsdObject>{
	public static final VsdEditor INSTANCE=new VsdEditor();
	private VsdEditor(){
	}
	@Override
	public Node edit(VsdObject data,Object remark,RegistryNode<String,Object> meta){
		return new VsdViewer(data.getDocument());
	}
	@Override
	public String getName(){
		return MessageRegistry.getString("DIAGRAM_EDITOR",MsOfficeModule.NAME);
	}
}
