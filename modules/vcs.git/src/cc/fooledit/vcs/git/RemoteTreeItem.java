/*
 * Copyright (C) 2016 Chan Chung Kwong <1m02math@126.com>
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
import java.util.*;
import java.util.logging.*;
import javafx.application.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import org.eclipse.jgit.api.*;
import org.eclipse.jgit.lib.*;
import org.eclipse.jgit.transport.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class RemoteTreeItem extends TreeItem<Object> implements NavigationTreeItem{
	public RemoteTreeItem(RemoteConfig ref){
		super(ref);
		for(RefSpec refSpec:ref.getFetchRefSpecs()){
			getChildren().add(new RemoteSpecTreeItem(refSpec,true));
		}
		for(RefSpec refSpec:ref.getPushRefSpecs()){
			getChildren().add(new RemoteSpecTreeItem(refSpec,false));
		}
	}
	@Override
	public String toString(){
		return ((RemoteConfig)getValue()).getName();
	}
	@Override
	public MenuItem[] getContextMenuItems(){
		MenuItem push=new MenuItem(MessageRegistry.getString("PUSH",GitModuleReal.NAME));
		push.setOnAction((e)->gitPush());
		MenuItem pull=new MenuItem(MessageRegistry.getString("PULL",GitModuleReal.NAME));
		pull.setOnAction((e)->gitPull());
		MenuItem fetch=new MenuItem(MessageRegistry.getString("FETCH",GitModuleReal.NAME));
		fetch.setOnAction((e)->gitFetch());
		MenuItem remove=new MenuItem(MessageRegistry.getString("REMOVE REMOTE",GitModuleReal.NAME));
		remove.setOnAction((e)->gitRemoteRemove());
		MenuItem resetURL=new MenuItem(MessageRegistry.getString("RESET URL",GitModuleReal.NAME));
		resetURL.setOnAction((e)->gitRemoteResetURL());
		return new MenuItem[]{push,pull,fetch,remove,resetURL};
	}
	private void gitRemoteResetURL(){
		TextInputDialog branchDialog=new TextInputDialog();
		branchDialog.setTitle(MessageRegistry.getString("CHOOSE A NEW URL FOR THE REMOTE CONFIGURE",GitModuleReal.NAME));
		branchDialog.setHeaderText(MessageRegistry.getString("ENTER THE NEW URL OF THE REMOTE CONFIGURE:",GitModuleReal.NAME));
		Optional<String> name=branchDialog.showAndWait();
		if(name.isPresent())
			try{
				RemoteSetUrlCommand command=((Git)getParent().getValue()).remoteSetUrl();
				command.setName(((RemoteConfig)getValue()).getName());
				command.setUri(new URIish(name.get()));
				command.setPush(true);
				command.call();
				command=((Git)getParent().getValue()).remoteSetUrl();
				command.setName(((RemoteConfig)getValue()).getName());
				command.setUri(new URIish(name.get()));
				command.setPush(false);
				command.call();
			}catch(Exception ex){
				Logger.getGlobal().log(Level.SEVERE,null,ex);
			}
	}
	private void gitRemoteRemove(){
		try{
			RemoteRemoveCommand command=((Git)getParent().getValue()).remoteRemove();
			command.setName(((RemoteConfig)getValue()).getName());
			command.call();
			getParent().getChildren().remove(this);
		}catch(Exception ex){
			Logger.getGlobal().log(Level.SEVERE,null,ex);
		}
	}
	private void gitFetch(){
		ProgressDialog progressDialog=new ProgressDialog(MessageRegistry.getString("FETCH",GitModuleReal.NAME));
		FetchCommand command=((Git)getParent().getValue()).fetch().setRemote(((RemoteConfig)getValue()).getName()).setProgressMonitor(progressDialog);
		new Thread(()->{
			try{
				FetchResult result=command.call();
				ArrayList<CommitTreeItem> commits=new ArrayList<>();
				Platform.runLater(()->{
					for(Ref ref:result.getAdvertisedRefs())
						try{
							commits.add(new CommitTreeItem(((Git)getParent().getValue()).log().addRange(ref.getObjectId(),ref.getObjectId()).call().iterator().next()));
						}catch(Exception ex){
							Logger.getLogger(RemoteTreeItem.class.getName()).log(Level.SEVERE,null,ex);
						}
					getParent().getChildren().filtered(item->item instanceof LocalTreeItem).
						forEach((item)->item.getChildren().addAll(commits));
				});
			}catch(Exception ex){
				Logger.getLogger(GitTreeItem.class.getName()).log(Level.SEVERE,null,ex);
				Platform.runLater(()->{
					progressDialog.hide();
				});
			}
		}).start();
	}
	private void gitPull(){
		ProgressDialog progressDialog=new ProgressDialog(MessageRegistry.getString("PULLING",GitModuleReal.NAME));
		PullCommand command=((Git)getParent().getValue()).pull().setRemote(((RemoteConfig)getValue()).getName()).setProgressMonitor(progressDialog);
		new Thread(()->{
			try{
				PullResult result=command.call();
				HashSet<CommitTreeItem> commits=new HashSet<>();
				Platform.runLater(()->{
					if(result.getFetchResult()!=null){
						for(Ref ref:result.getFetchResult().getAdvertisedRefs())
							try{
								commits.add(new CommitTreeItem(((Git)getParent().getValue()).log().addRange(ref.getObjectId(),ref.getObjectId()).call().iterator().next()));
							}catch(Exception ex){
								Logger.getLogger(RemoteTreeItem.class.getName()).log(Level.SEVERE,null,ex);
							}
					}
					if(result.getMergeResult()!=null&&result.getMergeResult().getMergeStatus().equals(MergeResult.MergeStatus.MERGED)){
						try{
							ObjectId head=result.getMergeResult().getNewHead();
							commits.add(new CommitTreeItem(((Git)getParent().getValue()).log().addRange(head,head).call().iterator().next()));
						}catch(Exception ex){
							Logger.getLogger(RemoteTreeItem.class.getName()).log(Level.SEVERE,null,ex);
						}
					}else{
						new Alert(Alert.AlertType.INFORMATION,result.toString(),ButtonType.CLOSE).show();
					}
					getParent().getChildren().filtered(item->item instanceof LocalTreeItem).
						forEach((item)->item.getChildren().addAll(commits));
				});
			}catch(Exception ex){
				Logger.getLogger(GitTreeItem.class.getName()).log(Level.SEVERE,null,ex);
				Platform.runLater(()->{
					progressDialog.hide();
				});
			}
		}).start();
	}
	private void gitPush(){
		ProgressDialog progressDialog=new ProgressDialog(MessageRegistry.getString("PUSHING",GitModuleReal.NAME));
		PushCommand command=((Git)getParent().getValue()).push().setRemote(((RemoteConfig)getValue()).getName()).setProgressMonitor(progressDialog);
		TextField user=new TextField();
		PasswordField pass=new PasswordField();
		GridPane auth=new GridPane();
		auth.addRow(0,new Label(MessageRegistry.getString("USER",GitModuleReal.NAME)),user);
		auth.addRow(1,new Label(MessageRegistry.getString("PASSWORD",GitModuleReal.NAME)),pass);
		Dialog dialog=new Dialog();
		dialog.getDialogPane().setContent(auth);
		dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CLOSE,ButtonType.APPLY);
		dialog.showAndWait();
		if(true){
			command.setCredentialsProvider(new UsernamePasswordCredentialsProvider(user.getText(),pass.getText()));
		}
		new Thread(()->{
			try{
				command.call();
			}catch(Exception ex){
				Logger.getLogger(GitTreeItem.class.getName()).log(Level.SEVERE,null,ex);
				Platform.runLater(()->{
					progressDialog.hide();
				});
			}
		}).start();
	}
}