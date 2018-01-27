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
import cc.fooledit.core.*;
import cc.fooledit.vcs.git.MenuItemBuilder;
import java.text.*;
import java.util.*;
import java.util.stream.*;
import javafx.beans.property.*;
import javafx.beans.value.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.util.*;
import org.eclipse.jgit.api.*;
import org.eclipse.jgit.dircache.*;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.revwalk.*;
import org.eclipse.jgit.transport.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class GitRepositoryViewer extends BorderPane{
	private final Git git;
	public GitRepositoryViewer(Git git){
		this.git=git;
		TreeTableView<Object> nav=new TreeTableView<>(createGitTreeItem());
		nav.setShowRoot(true);
		ContextMenu contextMenu=new ContextMenu();
		nav.setOnContextMenuRequested((e)->contextMenu.getItems().setAll(((NavigationTreeItem)nav.getSelectionModel().getSelectedItem()).getContextMenuItems()));
		nav.setContextMenu(contextMenu);
		setCenter(nav);
		setBottom(createColumnsChooser(nav));
	}
	private TreeItem<Object> createGitTreeItem(){
		return new SimpleTreeItem<>(git.getRepository().getDirectory().toString(),
				new TreeItem[]{createWorkingTreeItem(),
					createStageTreeItem(),
					createLogTreeItem(),
					createTagListTreeItem(),
					createLocalTreeItem(),
					createRemoteTreeItem()
				},new MenuItem[]{
					MenuItemBuilder.build("COLLECT GARGAGE",(e)->GitCommands.execute("git-gc")),
					MenuItemBuilder.build("CONFIGURE",(e)->GitCommands.execute("git-config"))
				});
	}
	private TreeItem<Object> createWorkingTreeItem(){
		MenuItem[] add=new MenuItem[]{
			MenuItemBuilder.build("ADD",(e)->GitCommands.add(null,git))//FIXME
		};
		return new SimpleTreeItem<>(MessageRegistry.getString("WORKING DIRECTORY",GitModuleReal.NAME),
				new TreeItem[]{new LazySimpleTreeItem(()->git.status().call().getIgnoredNotInIndex().stream().map((file)->new SimpleTreeItem<String>(file)).collect(Collectors.toList()),
					MessageRegistry.getString("IGNORED",GitModuleReal.NAME)),
					new LazySimpleTreeItem(()->git.status().call().getUntracked().stream().map((file)->new SimpleTreeItem<String>(file,add)).collect(Collectors.toList()),
					MessageRegistry.getString("UNTRACKED",GitModuleReal.NAME)),
					new LazySimpleTreeItem(()->git.status().call().getConflicting().stream().map((file)->new SimpleTreeItem<String>(file)).collect(Collectors.toList()),
					MessageRegistry.getString("CONFLICTING",GitModuleReal.NAME)),
					new LazySimpleTreeItem(()->git.status().call().getMissing().stream().map((file)->new SimpleTreeItem<String>(file)).collect(Collectors.toList()),
					MessageRegistry.getString("MISSING",GitModuleReal.NAME)),
					new LazySimpleTreeItem(()->git.status().call().getModified().stream().map((file)->new SimpleTreeItem<String>(file,add)).collect(Collectors.toList()),
					MessageRegistry.getString("MODIFIED",GitModuleReal.NAME))
				});
	}
	private TreeItem<Object> createStageTreeItem(){
		MenuItem[] rm=new MenuItem[]{
			MenuItemBuilder.build("REMOVE",(e)->GitCommands.execute("git-remove"))
		};
		MenuItem[] all=new MenuItem[]{
			MenuItemBuilder.build("REMOVE",(e)->GitCommands.execute("git-remove")),
			MenuItemBuilder.build("BLAME",(e)->GitCommands.execute("git-blame"))
		};
		return new SimpleTreeItem<>(MessageRegistry.getString("STAGING AREA",GitModuleReal.NAME),
				new TreeItem[]{new LazySimpleTreeItem(()->git.status().call().getAdded().stream().map((file)->new SimpleTreeItem<String>(file,rm)).collect(Collectors.toList()),
				MessageRegistry.getString("ADDED",GitModuleReal.NAME)),
					new LazySimpleTreeItem(()->git.status().call().getRemoved().stream().map((file)->new SimpleTreeItem<String>(file)).collect(Collectors.toList()),
					MessageRegistry.getString("REMOVED",GitModuleReal.NAME)),
					new LazySimpleTreeItem(()->git.status().call().getChanged().stream().map((file)->new SimpleTreeItem<String>(file,rm)).collect(Collectors.toList()),
					MessageRegistry.getString("CHANGED",GitModuleReal.NAME)),
					new LazySimpleTreeItem(()->{
						LinkedList<TreeItem<String>> files=new LinkedList<>();
						DirCache cache=git.getRepository().readDirCache();
						for(int i=0;i<cache.getEntryCount();i++)
							files.add(new SimpleTreeItem<>(cache.getEntry(i).getPathString(),all));
						return files;
					},MessageRegistry.getString("ALL",GitModuleReal.NAME))
				},new MenuItem[]{
					MenuItemBuilder.build("COMMIT",(e)->GitCommands.execute("git-commit"))
				});
	}
	private TreeItem<Object> createTagListTreeItem(){
		return new LazySimpleTreeItem<>(()->git.tagList().call().stream().map((ref)->new TagTreeItem(ref)).collect(Collectors.toList()),
				MessageRegistry.getString("TAG",GitModuleReal.NAME));
	}
	private TreeItem<Object> createLogTreeItem(){
		return new LazySimpleTreeItem<>(()->StreamSupport.stream(git.log().call().spliterator(),false).map((rev)->new CommitTreeItem(rev,git)).collect(Collectors.toList())
				,MessageRegistry.getString("COMMIT",GitModuleReal.NAME),
				new MenuItem[0]);
	}
	private TreeItem<Object> createLocalTreeItem(){
		return new LazySimpleTreeItem<>(()->git.branchList().call().stream().map((ref)->new BranchTreeItem(ref)).collect(Collectors.toList()),
				MessageRegistry.getString("LOCAL BRANCH",GitModuleReal.NAME),
			new MenuItem[]{
				MenuItemBuilder.build("BRANCH",(e)->GitCommands.execute("git-branch-add"))
			});
	}
	private TreeItem<Object> createRemoteTreeItem(){
		return new LazySimpleTreeItem<>(()->git.remoteList().call().stream().map((ref)->new RemoteTreeItem(ref)).collect(Collectors.toList()),
				MessageRegistry.getString("REMOTE BRANCH",GitModuleReal.NAME),
			new MenuItem[]{
				MenuItemBuilder.build("REMOTE ADD",(e)->GitCommands.execute("git-remote-add"))
			});
	}
	private FlowPane createColumnsChooser(TreeTableView<Object> nav){
		FlowPane chooser=new FlowPane();
		chooser.getChildren().add(createColumnChooser(MessageRegistry.getString("NAME",GitModuleReal.NAME),new Callback<TreeTableColumn.CellDataFeatures<Object, String>,ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Object,String> p){
				return new ReadOnlyObjectWrapper<>(p.getValue().toString());
			}
		},true,nav));
		chooser.getChildren().add(createColumnChooser(MessageRegistry.getString("MESSAGE",GitModuleReal.NAME),new Callback<TreeTableColumn.CellDataFeatures<Object, String>,ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Object,String> p){
				if(p.getValue() instanceof CommitTreeItem)
					return new ReadOnlyObjectWrapper<>(((RevCommit)p.getValue().getValue()).getShortMessage());
				else
					return new ReadOnlyObjectWrapper<>("");
			}
		},false,nav));
		chooser.getChildren().add(createColumnChooser(MessageRegistry.getString("AUTHOR",GitModuleReal.NAME),new Callback<TreeTableColumn.CellDataFeatures<Object, String>,ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Object,String> p){
				if(p.getValue() instanceof CommitTreeItem)
					return new ReadOnlyObjectWrapper<>(((RevCommit)p.getValue().getValue()).getAuthorIdent().toExternalString());
				else
					return new ReadOnlyObjectWrapper<>("");
			}
		},false,nav));
		chooser.getChildren().add(createColumnChooser(MessageRegistry.getString("COMMITTER",GitModuleReal.NAME),new Callback<TreeTableColumn.CellDataFeatures<Object, String>,ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Object,String> p){
				if(p.getValue() instanceof CommitTreeItem)
					return new ReadOnlyObjectWrapper<>(((RevCommit)p.getValue().getValue()).getCommitterIdent().toExternalString());
				else
					return new ReadOnlyObjectWrapper<>("");
			}
		},false,nav));
		chooser.getChildren().add(createColumnChooser(MessageRegistry.getString("TIME",GitModuleReal.NAME),new Callback<TreeTableColumn.CellDataFeatures<Object, String>,ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Object,String> p){
				if(p.getValue() instanceof CommitTreeItem)
					return new ReadOnlyObjectWrapper<>(timeToString(((RevCommit)p.getValue().getValue()).getCommitTime()));
				else
					return new ReadOnlyObjectWrapper<>("");
			}
		},false,nav));
		chooser.getChildren().add(createColumnChooser(MessageRegistry.getString("REFERNECE",GitModuleReal.NAME),new Callback<TreeTableColumn.CellDataFeatures<Object, String>,ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Object,String> p){
				if(p.getValue() instanceof CommitTreeItem){
					ObjectId id=((RevCommit)p.getValue().getValue()).getId();
					return new ReadOnlyObjectWrapper<>(id==null?"":id.getName());
				}else if(p.getValue() instanceof BranchTreeItem){
					ObjectId id=((Ref)p.getValue().getValue()).getLeaf().getObjectId();
					return new ReadOnlyObjectWrapper<>(id==null?"":id.getName());
				}else if(p.getValue() instanceof TagTreeItem){
					ObjectId id=((Ref)p.getValue().getValue()).getTarget().getLeaf().getObjectId();
					return new ReadOnlyObjectWrapper<>(id==null?"":id.getName());
				}else
					return new ReadOnlyObjectWrapper<>("");
			}
		},false,nav));
		chooser.getChildren().add(createColumnChooser(MessageRegistry.getString("URI",GitModuleReal.NAME),new Callback<TreeTableColumn.CellDataFeatures<Object, String>,ObservableValue<String>>() {
			@Override
			public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<Object,String> p){
				if(p.getValue() instanceof RemoteTreeItem){
					String uris=((RemoteConfig)p.getValue().getValue()).getURIs().stream().map((url)->url.toString()).collect(Collectors.joining(" "));
					return new ReadOnlyObjectWrapper<>(uris);
				}else
					return new ReadOnlyObjectWrapper<>("");
			}
		},false,nav));

		return chooser;
	}
	private static String timeToString(int time){
		return DateFormat.getDateTimeInstance().format(new Date(time*1000l));
	}
	private CheckBox createColumnChooser(String name,Callback callback,boolean visible,TreeTableView<Object> nav){
		TreeTableColumn<Object,String> column=new TreeTableColumn<>(name);
		column.setCellValueFactory(callback);
		CheckBox chooser=new CheckBox(name);
		chooser.setSelected(visible);
		if(visible)
			nav.getColumns().add(column);
		chooser.selectedProperty().addListener((v)->{
			if(chooser.isSelected())
				nav.getColumns().add(column);
			else
				nav.getColumns().remove(column);
		});
		return chooser;
	}
}
