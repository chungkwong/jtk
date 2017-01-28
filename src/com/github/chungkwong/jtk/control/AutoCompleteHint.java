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
package com.github.chungkwong.jtk.control;
import java.io.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public interface AutoCompleteHint{
	String getDisplayText();
	String getInputText();
	Reader getDocument();
	static AutoCompleteHint createSimple(String displayText,String inputText,String document){
		return new SimpleHint(displayText,inputText,document);
	}
}
class SimpleHint implements AutoCompleteHint{
	private final String displayText,inputText,document;
	public SimpleHint(String displayText,String inputText,String document){
		this.displayText=displayText;
		this.inputText=inputText;
		this.document=document;
	}
	@Override
	public String getDisplayText(){
		return displayText;
	}
	@Override
	public String getInputText(){
		return inputText;
	}
	@Override
	public Reader getDocument(){
		return new StringReader(document);
	}
}