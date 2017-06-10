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
package com.github.chungkwong.fooledit.model;
import com.github.chungkwong.jschememin.type.*;
import java.util.function.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public abstract class Command implements Consumer<ScmPairOrNil>{
	public abstract String getDisplayName();
	public static Command create(String name,Consumer<ScmPairOrNil> action){
		return new SimpleCommand(name,action);
	}
	private static class SimpleCommand extends Command{
		private final String name;
		private final Consumer<ScmPairOrNil> action;
		public SimpleCommand(String name,Consumer<ScmPairOrNil> action){
			this.action=action;
			this.name=name;
		}
		@Override
		public String getDisplayName(){
			return name;
		}
		@Override
		public void accept(ScmPairOrNil t){
			action.accept(t);
		}
	}
}