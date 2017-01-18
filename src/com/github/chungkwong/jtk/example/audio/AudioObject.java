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
package com.github.chungkwong.jtk.example.audio;
import com.github.chungkwong.jtk.model.*;
import javafx.beans.property.*;
import javafx.scene.media.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class AudioObject implements DataObject<AudioObject>{
	private final Property<MediaPlayer> property;
	public AudioObject(MediaPlayer audio){
		this.property=new SimpleObjectProperty<>(audio);
	}
	public Property<MediaPlayer> getProperty(){
		return property;
	}
	@Override
	public DataObjectType<AudioObject> getDataObjectType(){
		return AudioObjectType.INSTANCE;
	}
}