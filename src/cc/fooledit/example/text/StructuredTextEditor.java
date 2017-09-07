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
package cc.fooledit.example.text;
import cc.fooledit.*;
import cc.fooledit.api.*;
import cc.fooledit.editor.*;
import cc.fooledit.editor.lex.*;
import cc.fooledit.model.*;
import cc.fooledit.setting.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.function.*;
import java.util.logging.*;
import javafx.scene.*;
import org.fxmisc.richtext.model.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class StructuredTextEditor implements DataEditor<TextObject>{
	private final MenuRegistry menuRegistry=new MenuRegistry();
	private final CommandRegistry commandRegistry=new CommandRegistry();
	private final KeymapRegistry keymapRegistry=new KeymapRegistry();
	private final Map<String,Language> languages=new HashMap<>();
	public StructuredTextEditor(){
		menuRegistry.setMenus(Main.loadJSON((File)SettingManager.getOrCreate(TextEditorModule.NAME).get("menubar-file",null)));

		addCommand("undo",(area)->area.getArea().undo());
		addCommand("redo",(area)->area.getArea().redo());
		addCommand("cut",(area)->area.getArea().cut());
		addCommand("copy",(area)->area.getArea().copy());
		addCommand("paste",(area)->area.getArea().paste());
		addCommand("select-all",(area)->area.getArea().selectAll());
		addCommand("select-word",(area)->area.getArea().selectWord());
		addCommand("select-line",(area)->area.getArea().selectLine());
		addCommand("select-paragraph",(area)->area.getArea().selectParagraph());
		addCommand("delete-selection-or-next-character",(area)->area.delete());
		addCommand("delete-selection-or-previous-character",(area)->area.backspace());
		addCommand("delete-next-character",(area)->area.deleteNextChar());
		addCommand("delete-previous-character",(area)->area.deletePreviousChar());
		addCommand("delete-next-word",(area)->area.deleteNextWord());
		addCommand("delete-previous-word",(area)->area.deletePreviousWord());
		addCommand("delete-line",(area)->area.deleteLine());
		addCommand("new-line",(area)->area.newline());
		addCommand("new-line-no-indent",(area)->area.getArea().replaceSelection("\n"));
		addCommand("indent",(area)->area.getArea().replaceSelection("\t"));
		addCommand("move-to-next-word",(area)->area.nextWord(NavigationActions.SelectionPolicy.CLEAR));
		addCommand("move-to-previous-word",(area)->area.previousWord(NavigationActions.SelectionPolicy.CLEAR));
		addCommand("move-to-next-character",(area)->area.getArea().nextChar(NavigationActions.SelectionPolicy.CLEAR));
		addCommand("move-to-previous-character",(area)->area.getArea().previousChar(NavigationActions.SelectionPolicy.CLEAR));
		addCommand("move-to-next-line",(area)->area.nextLine(NavigationActions.SelectionPolicy.CLEAR));
		addCommand("move-to-previous-line",(area)->area.previousLine(NavigationActions.SelectionPolicy.CLEAR));
		addCommand("move-to-next-page",(area)->area.getArea().nextPage(NavigationActions.SelectionPolicy.CLEAR));
		addCommand("move-to-previous-page",(area)->area.getArea().prevPage(NavigationActions.SelectionPolicy.CLEAR));
		addCommand("move-to-line-end",(area)->area.getArea().lineEnd(NavigationActions.SelectionPolicy.CLEAR));
		addCommand("move-to-line-begin",(area)->area.getArea().lineStart(NavigationActions.SelectionPolicy.CLEAR));
		addCommand("move-to-paragraph-end",(area)->area.getArea().paragraphEnd(NavigationActions.SelectionPolicy.CLEAR));
		addCommand("move-to-paragraph-begin",(area)->area.getArea().paragraphStart(NavigationActions.SelectionPolicy.CLEAR));
		addCommand("move-to-file-end",(area)->area.getArea().end(NavigationActions.SelectionPolicy.CLEAR));
		addCommand("move-to-file-begin",(area)->area.getArea().start(NavigationActions.SelectionPolicy.CLEAR));

		addCommand("select-to-next-word",(area)->area.nextWord(NavigationActions.SelectionPolicy.ADJUST));
		addCommand("select-to-previous-word",(area)->area.previousWord(NavigationActions.SelectionPolicy.ADJUST));
		addCommand("select-to-next-character",(area)->area.getArea().nextChar(NavigationActions.SelectionPolicy.ADJUST));
		addCommand("select-to-previous-character",(area)->area.getArea().previousChar(NavigationActions.SelectionPolicy.ADJUST));
		addCommand("select-to-next-line",(area)->area.nextLine(NavigationActions.SelectionPolicy.ADJUST));
		addCommand("select-to-previous-line",(area)->area.previousLine(NavigationActions.SelectionPolicy.ADJUST));
		addCommand("select-to-next-page",(area)->area.getArea().nextPage(NavigationActions.SelectionPolicy.ADJUST));
		addCommand("select-to-previous-page",(area)->area.getArea().prevPage(NavigationActions.SelectionPolicy.ADJUST));
		addCommand("select-to-line-end",(area)->area.getArea().lineEnd(NavigationActions.SelectionPolicy.ADJUST));
		addCommand("select-to-line-begin",(area)->area.getArea().lineStart(NavigationActions.SelectionPolicy.ADJUST));
		addCommand("select-to-paragraph-end",(area)->area.getArea().paragraphEnd(NavigationActions.SelectionPolicy.ADJUST));
		addCommand("select-to-paragraph-begin",(area)->area.getArea().paragraphStart(NavigationActions.SelectionPolicy.ADJUST));
		addCommand("select-to-file-end",(area)->area.getArea().end(NavigationActions.SelectionPolicy.ADJUST));
		addCommand("select-to-file-begin",(area)->area.getArea().start(NavigationActions.SelectionPolicy.ADJUST));
		addCommand("deselect",(area)->area.getArea().deselect());

		addCommand("to-lowercase",(area)->area.transform(String::toLowerCase));
		addCommand("to-uppercase",(area)->area.transform(String::toUpperCase));
		addCommand("encode-url",(area)->area.transform(StructuredTextEditor::encodeURL));
		addCommand("decode-url",(area)->area.transform(StructuredTextEditor::decodeURL));
		addCommand("scroll-to-top",(area)->area.getArea().showParagraphAtTop(area.getArea().getCurrentParagraph()));
		addCommand("scroll-to-bottom",(area)->area.getArea().showParagraphAtBottom(area.getArea().getCurrentParagraph()));
		
		keymapRegistry.registerKeys((Map<String,String>)(Object)Main.loadJSON((File)SettingManager.getOrCreate(TextEditorModule.NAME).get("keymap-file",null)));

		try{
			//scene.setUserAgentStylesheet("com/github/chungkwong/jtk/dark.css");
			Main.getScene().getStylesheets().add(((File)SettingManager.getOrCreate(TextEditorModule.NAME).get("stylesheet-file",null)).toURI().toURL().toString());
		}catch(MalformedURLException ex){
			Logger.getGlobal().log(Level.SEVERE,null,ex);
		}

		List<Map<String,Object>> json=((Map<String,List<Map<String,Object>>>)(Object)Main.loadJSON(new File(Main.getModulePath(TextEditorModule.NAME),"modes.json"))).get("languages");
		json.stream().map((m)->Language.fromJSON(m)).forEach((l)->Arrays.stream(l.getMimeTypes()).forEach((mime)->languages.put(mime,l)));
	}
	private void addCommand(String name,Consumer<CodeEditor> action){
		commandRegistry.put(name,()->action.accept((CodeEditor)Main.INSTANCE.getCurrentNode()));
	}
	@Override
	public MenuRegistry getMenuRegistry(){
		return menuRegistry;
	}
	@Override
	public CommandRegistry getCommandRegistry(){
		return commandRegistry;
	}
	@Override
	public Object getRemark(Node node){
		CodeEditor editor=(CodeEditor)node;
		return Arrays.asList(editor.getArea().getAnchor(),editor.getArea().getCaretPosition());
	}
	@Override
	public KeymapRegistry getKeymapRegistry(){
		return keymapRegistry;
	}
	@Override
	public Node edit(TextObject data,Object remark){
		CodeEditor editor=(CodeEditor)edit(data);
		if(remark instanceof List){
			List<Number> pair=(List<Number>)remark;
			editor.getArea().selectRange(pair.get(0).intValue(),pair.get(1).intValue());
		}
		return editor;
	}
	@Override
	public Node edit(TextObject data){
		MetaLexer lex=null;
		Language language=languages.get(DataObjectRegistry.getMIME(data));
		TokenHighlighter highlighter=language!=null?language.getTokenHighlighter():null;
		CodeEditor codeEditor=new CodeEditor(null,highlighter);
		codeEditor.textProperty().bindBidirectional(data.getText());
		return codeEditor;
	}
	@Override
	public String getName(){
		return MessageRegistry.getString("CODE_EDITOR");
	}
	private static String encodeURL(String url){
		try{
			return URLEncoder.encode(url,"UTF-8");
		}catch(UnsupportedEncodingException ex){
			Logger.getGlobal().log(Level.SEVERE,null,ex);
			return url;
		}
	}
	private static String decodeURL(String url){
		try{
			return URLDecoder.decode(url,"UTF-8");
		}catch(UnsupportedEncodingException ex){
			Logger.getGlobal().log(Level.SEVERE,null,ex);
			return url;
		}
	}
}