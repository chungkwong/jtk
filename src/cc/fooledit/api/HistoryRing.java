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
package cc.fooledit.api;
import java.util.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class HistoryRing<T>{
	private final List<T> list=new ArrayList<>();
	private final Map<String,T> tags=new HashMap<>();
	private int currentIndex=-1;
	private int limit;
	public HistoryRing(){
		this(Integer.MAX_VALUE);
	}
	public HistoryRing(int limit){
		this.limit=limit;
	}
	public void add(T obj){
		currentIndex=list.size();
		list.add(obj);
	}
	public void add(T obj,String tag){
		currentIndex=list.size();
		list.add(obj);
		tags.put(tag,obj);
	}
	public int size(){
		return list.size();
	}
	public int getCurrentIndex(){
		return currentIndex;
	}
	public T get(int index){
		return list.get(index);
	}
	public T get(String tag){
		return tags.get(tag);
	}
	public int previous(){
		if(currentIndex==0){
			currentIndex=list.size()-1;
		}else{
			--currentIndex;
		}
		return currentIndex;
	}
	public int next(){
		++currentIndex;
		if(currentIndex==list.size()){
			currentIndex=0;
		}
		return currentIndex;
	}
}