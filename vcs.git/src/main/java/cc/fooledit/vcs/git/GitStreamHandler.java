/*
 * Copyright (C) 2018 Chan Chung Kwong <1m02math@126.com>
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
package cc.fooledit.vcs.git;
import java.io.*;
import java.net.*;
import java.net.URLConnection;
import java.util.logging.*;
import org.eclipse.jgit.api.*;
import org.eclipse.jgit.lib.*;
import org.osgi.service.url.*;
import sun.net.www.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class GitStreamHandler extends AbstractURLStreamHandlerService{
	private static final String separator="!/";
	public URLConnection openConnection(URL u) throws IOException{
		return new GitConnection(u);
	}
	private static int indexOfBangSlash(String spec){
		int indexOfBang=spec.length();
		while((indexOfBang=spec.lastIndexOf('!',indexOfBang))!=-1){
			if((indexOfBang!=(spec.length()-1))
					&&(spec.charAt(indexOfBang+1)=='/')){
				return indexOfBang+1;
			}else{
				indexOfBang--;
			}
		}
		return -1;
	}
	@Override
	@SuppressWarnings("deprecation")
	protected void parseURL(URL url,String spec,
			int start,int limit){
		String file=null;
		String ref=null;
		// first figure out if there is an anchor
		int refPos=spec.indexOf('#',limit);
		boolean refOnly=refPos==start;
		if(refPos>-1){
			ref=spec.substring(refPos+1,spec.length());
			if(refOnly){
				file=url.getFile();
			}
		}
		// then figure out if the spec is
		// 1. absolute (jar:)
		// 2. relative (i.e. url + foo/bar/baz.ext)
		// 3. anchor-only (i.e. url + #foo), which we already did (refOnly)
		boolean absoluteSpec=false;
		if(spec.length()>=4){
			absoluteSpec=spec.substring(0,4).equalsIgnoreCase("git:");
		}
		spec=spec.substring(start,limit);
		if(absoluteSpec){
			file=parseAbsoluteSpec(spec);
		}else if(!refOnly){
			file=parseContextSpec(url,spec);
			// Canonize the result after the bangslash
			int bangSlash=indexOfBangSlash(file);
			String toBangSlash=file.substring(0,bangSlash);
			String afterBangSlash=file.substring(bangSlash);
			sun.net.www.ParseUtil canonizer=new ParseUtil();
			afterBangSlash=canonizer.canonizeString(afterBangSlash);
			file=toBangSlash+afterBangSlash;
		}
		setURL(url,"git","",-1,file,ref);
	}
	private String parseAbsoluteSpec(String spec){
		URL url=null;
		int index=-1;
		if((index=indexOfBangSlash(spec))==-1){
			throw new NullPointerException("no !/ in spec");
		}
		try{
			String innerSpec=spec.substring(0,index-1);
			url=new URL(innerSpec);
		}catch(MalformedURLException e){
			throw new NullPointerException("invalid url: "
					+spec+" ("+e+")");
		}
		return spec;
	}
	private String parseContextSpec(URL url,String spec){
		String ctxFile=url.getFile();
		// if the spec begins with /, chop up the jar back !/
		if(spec.startsWith("/")){
			int bangSlash=indexOfBangSlash(ctxFile);
			if(bangSlash==-1){
				throw new NullPointerException("malformed "
						+"context url:"
						+url
						+": no !/");
			}
			ctxFile=ctxFile.substring(0,bangSlash);
		}
		if(!ctxFile.endsWith("/")&&(!spec.startsWith("/"))){
			// chop up the last component
			int lastSlash=ctxFile.lastIndexOf('/');
			if(lastSlash==-1){
				throw new NullPointerException("malformed "
						+"context url:"
						+url);
			}
			ctxFile=ctxFile.substring(0,lastSlash+1);
		}
		return (ctxFile+spec);
	}
}
class GitConnection extends URLConnection{
	private Git git;
	private AnyObjectId id;
	private String contentType;
	private ObjectStream in;
	public GitConnection(AnyObjectId id,Git git) throws MalformedURLException{
		super(new URL("git","",git.getRepository().getWorkTree().toURI().toString()+"!/"+id.getName()));
		this.id=id;
		this.git=git;
	}
	public GitConnection(URL url)
			throws MalformedURLException,IOException{
		super(url);
		try{
			parseSpecs(url);
		}catch(URISyntaxException ex){
			Logger.getGlobal().log(Level.SEVERE,null,ex);
		}
	}
	@Override
	public void connect() throws IOException{
		if(!connected){
			in=git.getRepository().newObjectReader().open(id).openStream();
			connected=true;
		}
	}
	@Override
	public InputStream getInputStream() throws IOException{
		connect();
		return in;
	}
	@Override
	public int getContentLength(){
		try{
			connect();
			long size=in.getSize();
			if(size>Integer.MAX_VALUE){
				return -1;
			}else{
				return (int)size;
			}
		}catch(IOException ex){
			Logger.getGlobal().log(Level.SEVERE,null,ex);
			return -1;
		}
	}
	@Override
	public long getContentLengthLong(){
		try{
			connect();
			return in.getSize();
		}catch(IOException ex){
			Logger.getGlobal().log(Level.SEVERE,null,ex);
			return -1;
		}
	}
	private void parseSpecs(URL url) throws MalformedURLException,URISyntaxException,IOException{
		String spec=url.getFile();
		int separator=spec.lastIndexOf("!/");
		if(separator==-1){
			throw new MalformedURLException("no !/ found in url spec:"+spec);
		}
		git=Git.open(new File(new URL(spec.substring(0,separator++)).toURI()));
		id=null;
		if(++separator!=spec.length()){
			id=git.getRepository().resolve(ParseUtil.decode(spec.substring(separator,spec.length())));
		}
	}
	public Git getGit(){
		return git;
	}
	public AnyObjectId getObjectId(){
		return id;
	}
}
