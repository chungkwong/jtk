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
import cc.fooledit.*;
import cc.fooledit.control.*;
import cc.fooledit.model.*;
import com.github.chungkwong.jschememin.type.*;
import java.util.*;
import java.util.function.*;
import java.util.logging.*;
import java.util.stream.*;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javax.script.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class MiniBuffer extends BorderPane{
	private final TextField input=new TextField();
	private final Main main;
	private final AutoCompleteProvider commandHints=new CommandComplete();
	private final AutoCompleteService completeService;
	private AutoCompleteProvider hints;
	public MiniBuffer(Main main){
		this.main=main;
		setFocusTraversable(false);
		completeService=new AutoCompleteService(input,(text,pos)->hints==null?Stream.empty():hints.checkForHints(text,pos));
		setCenter(input);
		restore();
	}
	public void setMode(Consumer<String> action,AutoCompleteProvider hints,String init,Node left,Node right){
		this.hints=hints;
		input.setText(init);
		input.setOnAction((e)->action.accept(input.getText()));
		setLeft(left);
		setRight(right);
		input.requestFocus();
	}
	public void restore(){
		hints=commandHints;
		setLeft(null);
		setRight(null);
		input.setText("");
		input.setOnAction((e)->{
			Command command=main.getCommandRegistry().get(input.getText());
			if(command!=null){
				executeCommand(command);
			}else{
				try{
					main.getNotifier().notify(Objects.toString(main.getScriptAPI().eval(input.getText())));
					focusCurrentNode();
				}catch(ScriptException ex){
					Logger.getGlobal().log(Level.SEVERE,MessageRegistry.getString("FAILED"),ex);
				}
			}
		});
		focusCurrentNode();
	}
	private void focusCurrentNode(){
		Node curr=Main.INSTANCE.getCurrentNode();
		if(curr!=null)
			curr.requestFocus();
	}
	public void executeCommand(Command command){
		executeCommand(command,new ArrayList<>(),command.getParameters());
	}
	private void executeCommand(Command command,List<ScmString> collected,List<String> missing){
		if(missing.isEmpty()){
			main.getNotifier().notifyStarted(command.getDisplayName());
			ScmObject obj=command.accept(ScmList.toList(collected));
			if(obj!=null)
				main.getNotifier().notify(obj.toExternalRepresentation());
			else
				main.getNotifier().notifyFinished(command.getDisplayName());
			restore();
		}else
			setMode((p)->{
				collected.add(new ScmString(p));
				executeCommand(command,collected,missing.subList(1,missing.size()));
			},null,"",new Label(MessageRegistry.getString(missing.get(0))),null);
	}
	@Override
	public void requestFocus(){
		input.requestFocus();
	}
	private class CommandComplete implements AutoCompleteProvider{
		@Override
		public Stream<AutoCompleteHint> checkForHints(String text,int pos){
			String prefix=text.substring(0,pos);
			return main.getCommandRegistry().keySet().stream().filter((name)->name.startsWith(prefix)&&name.length()>pos)
					.sorted().map((name)->AutoCompleteHint.create(name,name.substring(pos),""));
		}
	}
}
