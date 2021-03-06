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
package cc.fooledit.editor.email;
import java.io.*;
import java.net.*;
import org.osgi.service.url.*;
/**
 *
 * @author Chan Chung Kwong
 */
public class NaiveStreamHandler extends AbstractURLStreamHandlerService{
	@Override
	public URLConnection openConnection(URL u) throws IOException{
		return new NaiveConnection(u);
	}
	private static class NaiveConnection extends URLConnection{
		public NaiveConnection(URL url){
			super(url);
		}
		@Override
		public String getContentType(){
			return "application/"+url.getProtocol();
		}
		@Override
		public void connect() throws IOException{
		}
	}
}
