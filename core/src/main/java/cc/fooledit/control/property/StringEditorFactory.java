/*
 * Copyright (C) 2018 Chan Chung Kwong
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
package cc.fooledit.control.property;
import javafx.beans.value.*;
import javafx.scene.control.*;
/**
 *
 * @author Chan Chung Kwong
 */
public class StringEditorFactory implements PropertyEditorFactory<String,TextArea>{
	@Override
	public TextArea create(String value,boolean editable){
		TextArea textArea=new TextArea(value);
		textArea.setEditable(editable);
		return textArea;
	}
	@Override
	public String getValue(TextArea node){
		return node.getText();
	}
	@Override
	public void setValue(String value,TextArea node){
		node.setText(value);
	}
	@Override
	public void addPropertyChangeListener(ChangeListener<? super String> listener,TextArea node){
		node.textProperty().addListener(listener);
	}
	@Override
	public void removePropertyChangeListener(ChangeListener<? super String> listener,TextArea node){
		node.textProperty().removeListener(listener);
	}
}
