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
package com.github.chungkwong.fooledit.project;
import com.github.chungkwong.fooledit.api.*;
import com.github.chungkwong.fooledit.model.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class ProjectRegistry{
	private static final Set<Project> projects=new HashSet<>();
	public static void register(Project project){
		projects.add(project);
	}
	public static Project getProject(DataObject obj){
		try{
			Optional<Project> project=ProjectTypeManager.gaussProject(new File(new URI(DataObjectRegistry.getURL(obj))));
			if(project.isPresent()){
				projects.add(project.get());
				return project.get();
			}
		}catch(Exception ex){
			Logger.getGlobal().log(Level.INFO,null,ex);
		}
		return null;
	}
}
