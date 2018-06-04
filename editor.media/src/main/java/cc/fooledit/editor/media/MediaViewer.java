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
package cc.fooledit.editor.media;
import javafx.beans.property.*;
import javafx.scene.media.*;
import javafx.util.*;
/**
 *
 * @author Chan Chung Kwong
 */
public interface MediaViewer{
	void play();
	void pause();
	void seek(Duration duration);
	void dispose();
	DoubleProperty rateProperty();
	DoubleProperty volumeProperty();
	ReadOnlyObjectProperty<Duration> currentTimeProperty();
	ReadOnlyObjectProperty<Duration> totalTimeProperty();
	ReadOnlyObjectProperty<MediaPlayer.Status> statusProperty();
}
