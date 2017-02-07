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
package com.github.chungkwong.jtk.control;
import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.logging.*;
import java.util.stream.*;
import javafx.application.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class FileSystemViewer extends Application{
	private final TreeView<File> tree=new TreeView<>();
	public FileSystemViewer(){
		tree.setCellFactory((t)->new FileCell());
		TreeItem<File> root=new LazyTreeItem<File>(()->Arrays.stream(File.listRoots()).sorted().map((r)->new LazyTreeItem<>(()->getChildren(r),r)).collect(Collectors.toList()),null);
		tree.setShowRoot(false);
		tree.setRoot(root);
		tree.setOnDragDetected((e)->{
			Dragboard board=tree.startDragAndDrop(TransferMode.MOVE);
			ClipboardContent content=new ClipboardContent();
			content.putFiles(tree.getSelectionModel().getSelectedItems().stream().map((item)->item.getValue()).collect(Collectors.toList()));
			board.setContent(content);
			e.consume();
		});
		tree.setOnDragOver((e)->{
			if(e.getDragboard().hasFiles())
				e.acceptTransferModes(TransferMode.MOVE);
		});
		tree.setOnDragDropped((e)->{
			Dragboard board=e.getDragboard();
			if(board.hasFiles()){
				try{
					for(File f:board.getFiles()){
						Files.move(f.toPath(),((TreeItem<File>)e.getAcceptingObject()).getValue().toPath().resolve(f.getName()));
					}
				}catch(IOException ex){
					Logger.getGlobal().log(Level.SEVERE,null,ex);
					e.setDropCompleted(true);
				}
			}else{
				e.setDropCompleted(false);
			}
			e.consume();
		});
	}
	private static Collection<TreeItem<File>> getChildren(File item){
		return Arrays.stream(item.listFiles()).sorted()
				.map((f)->f.isDirectory()?new LazyTreeItem<File>(()->getChildren(f),f):new TreeItem<>(f))
				.collect(Collectors.toSet());
	}
	public static void main(String[] args){
		launch(args);
	}
	@Override
	public void start(Stage stage) throws Exception{
		stage.setScene(new Scene(new BorderPane(tree)));
		stage.show();
	}
	static class FileCell extends TreeCell<File>{
		@Override
		protected void updateItem(File item,boolean empty){
			super.updateItem(item,empty);
			if(empty||item==null){
				setText(null);
				setGraphic(null);
			}else{
				setText(item.getName());
			}
		}
	}
}
