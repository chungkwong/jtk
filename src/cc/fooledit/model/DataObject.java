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
package cc.fooledit.model;
import java.util.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public interface DataObject<T extends DataObject>{
	public static final String MIME="MIME";
	public static final String URI="URI";
	public static final String DEFAULT_NAME="DEFAULT_NAME";
	public static final String BUFFER_NAME="BUFFER_NAME";
	public static final String TYPE="TYPE";
	public static final String MODIFIED="MODIFIED";
	DataObjectType<T> getDataObjectType();
	Map<String,String> getProperties();
}
