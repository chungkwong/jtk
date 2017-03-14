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
package com.github.chungkwong.jtk;

import com.github.chungkwong.jtk.api.*;
import com.github.chungkwong.jtk.command.*;
import com.github.chungkwong.jtk.control.*;
import com.github.chungkwong.jtk.example.text.*;
import com.github.chungkwong.jtk.example.tool.*;
import com.github.chungkwong.jtk.model.*;
import java.io.*;
import java.util.*;
import java.util.logging.*;
import javafx.application.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.stage.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class Main extends Application{
	private static final File PATH=new File(System.getProperty("user.dir"),".jtk");
	private final CommandRegistry commandRegistry=new CommandRegistry();
	private final MenuRegistry menuRegistry;
	private final KeymapRegistry keymapRegistry;
	private final Notifier notifier;
	private final DataObjectRegistry dataObjectRegistry=new DataObjectRegistry();
	private final BorderPane root;
	private final MiniBuffer input;
	private final Scene scene;
	private Stage stage;
	private Node currentNode;
	public Main(){
		notifier=new Notifier(this);
		Logger.getGlobal().setLevel(Level.INFO);
		Logger.getGlobal().addHandler(notifier);
		MenuBar bar=new MenuBar();
		input=new MiniBuffer(this);
		HBox commander=new HBox(bar,input);
		HBox.setHgrow(bar,Priority.NEVER);
		HBox.setHgrow(input,Priority.ALWAYS);
		TextObject welcome=new TextObject("Welcome");
		dataObjectRegistry.addDataObject(welcome,Helper.hashMap(DataObjectRegistry.DEFAULT_NAME,"Welcome"));
		root=new BorderPane(new WorkSheet(getDefaultEditor(welcome)));
		root.setTop(commander);
		root.setBottom(notifier.getStatusBar());
		scene=new Scene(root);
		//scene.setUserAgentStylesheet("com/github/chungkwong/jtk/dark.css");
		registerStandardCommand();
		menuRegistry=new MenuRegistry(bar.getMenus(),commandRegistry);
		keymapRegistry=new KeymapRegistry(scene,commandRegistry);
		scene.focusOwnerProperty().addListener((e,o,n)->updateCurrentNode(n));
		bar.getMenus().add(getBufferMenu());
		//notifier.addItem(Notifier.createTimeField(DateFormat.getDateTimeInstance()));
	}
	private void registerStandardCommand(){
		FileCommands fileCommands=new FileCommands(this);
		commandRegistry.put("new",()->fileCommands.create());
		commandRegistry.put("open-file",()->fileCommands.open());
		commandRegistry.put("save",()->fileCommands.save());
		commandRegistry.put("full_screen",()->stage.setFullScreen(true));
		commandRegistry.put("maximize_frame",()->stage.setMaximized(true));
		commandRegistry.put("iconify_frame",()->stage.setIconified(true));
		commandRegistry.put("always_on_top_frame",()->stage.setAlwaysOnTop(true));
		commandRegistry.put("split_vertically",()->currentWorkSheet().splitVertically(getDefaultEditor(getCurrentDataObject())));
		commandRegistry.put("split_horizontally",()->currentWorkSheet().splitHorizontally(getDefaultEditor(getCurrentDataObject())));
		commandRegistry.put("keep_only",()->((WorkSheet)root.getCenter()).keepOnly(getCurrentNode()));
		commandRegistry.put("browser",()->addAndShow(new BrowserData(),Helper.hashMap(DataObjectRegistry.DEFAULT_NAME,"Browser")));
		commandRegistry.put("command",()->input.requestFocus());
		commandRegistry.put("next_buffer",()->currentWorkSheet().keepOnly(getDefaultEditor(dataObjectRegistry.getNextDataObject(getCurrentDataObject()))));
		commandRegistry.put("previous_buffer",()->currentWorkSheet().keepOnly(getDefaultEditor(dataObjectRegistry.getPreviousDataObject(getCurrentDataObject()))));
	}
	private OnDemandMenu getBufferMenu(){
		return new OnDemandMenu(MessageRegistry.getString("BUFFERS"),(l)->{
			for(String name:dataObjectRegistry.getDataObjectNames()){
				MenuItem item=new MenuItem(name);
				item.setOnAction((e)->currentWorkSheet().setCenter(getDefaultEditor(dataObjectRegistry.getDataObject(name))));
				l.add(item);
			}
			l.add(new SeparatorMenuItem());
			DataObject curr=getCurrentDataObject();
			DataObjectTypeRegistry.getDataEditors(curr.getClass()).forEach((editor)->{
				MenuItem item=new MenuItem(editor.getName());
				item.setOnAction((e)->currentWorkSheet().setCenter(getEditor(curr,editor)));
				l.add(item);
			});
			l.add(new SeparatorMenuItem());
			l.add(createCommandMenuItem("next_buffer"));
			l.add(createCommandMenuItem("previous_buffer"));
		});
	}
	private MenuItem createCommandMenuItem(String name){
		MenuItem item=new MenuItem(MessageRegistry.getString(name.toUpperCase()));
		item.setOnAction((e)->commandRegistry.get(name).run());
		return item;
	}
	private void updateCurrentNode(Node node){
		while(!(node instanceof WorkSheet)&&node!=null){
			node=node.getParent();
		}
		if(node!=null)
			currentNode=((WorkSheet)node).getCenter();
	}
	public void addAndShow(DataObject data,HashMap<Object,Object> prop){
		getDataObjectRegistry().addDataObject(data,prop);
		Node editor=getDefaultEditor(data);
		currentWorkSheet().keepOnly(editor);
	}
	public Node getDefaultEditor(DataObject data){
		return getEditor(data,DataObjectTypeRegistry.getDataEditors(data.getClass()).get(0));
	}
	private Node getEditor(DataObject data,DataEditor editor){
		Node node=editor.edit(data);
		node.setUserData(data);
		return node;
	}
	public DataObject getCurrentDataObject(){
		return (DataObject)currentNode.getUserData();
	}
	public Node getCurrentNode(){
		return currentNode;
	}
	public WorkSheet currentWorkSheet(){
		return (WorkSheet)currentNode.getParent();
	}
	public CommandRegistry getCommandRegistry(){
		return commandRegistry;
	}
	public MenuRegistry getMenuRegistry(){
		return menuRegistry;
	}
	public DataObjectRegistry getDataObjectRegistry(){
		return dataObjectRegistry;
	}
	public MiniBuffer getMiniBuffer(){
		return input;
	}
	public Stage getStage(){
		return stage;
	}
	public Notifier getNotifier(){
		return notifier;
	}
	@Override
	public void start(Stage primaryStage){
		stage=primaryStage;
		primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("/com/github/chungkwong/jtk/cross.png")));
		primaryStage.setTitle("IDEM");
		primaryStage.setScene(scene);
		primaryStage.setMaximized(true);
		primaryStage.show();
	}
	public static File getPath(){
		return PATH;
	}
	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args){
		launch(args);
	}
}
