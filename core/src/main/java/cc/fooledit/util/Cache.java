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
package cc.fooledit.util;
import java.lang.ref.*;
import java.util.function.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class Cache<T> implements Supplier<T>{
	private final Supplier<T> supplier;
	private SoftReference<T> cache;
	private boolean nulled=false;
	public Cache(Supplier<T> supplier){
		this.supplier=supplier;
	}
	@Override
	public T get(){
		T obj;
		if(nulled)
			return null;
		if(cache!=null&&(obj=cache.get())!=null){
			return obj;
		}else{
			obj=supplier.get();
			if(obj==null)
				nulled=true;
			else{
				cache=new SoftReference<>(obj);
			}
			return obj;
		}
	}
}
