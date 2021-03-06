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
package cc.fooledit.editor.msoffice;
import cc.fooledit.editor.msoffice.word.*;
import java.io.*;
import java.nio.file.*;
import javafx.application.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import org.apache.poi.hwpf.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class MsOfficeTest extends Application{
	@Override
	public void start(Stage primaryStage) throws Exception{
		HWPFDocumentCore document=new HWPFDocument(Files.newInputStream(new File("/home/kwong/sysu_learning/数学模型/MCM2013/A/CUMCM2013A.doc").toPath()));
		primaryStage.setScene(new Scene(new BorderPane(new DocViewer(document))));
		//XWPFDocument document=new XWPFDocument(Files.newInputStream(new File("/home/kwong/下载/2016112108225066.docx").toPath()));
		//primaryStage.setScene(new Scene(new BorderPane(new DocxViewer(document))));
		//HSLFSlideShow document=new HSLFSlideShow(Files.newInputStream(new File("/home/kwong/sysu_learning/中国近代法律文化史/第五章杨月楼奇案.ppt").toPath()));
		//primaryStage.setScene(new Scene(new BorderPane(new PptViewer(document))));
		//XMLSlideShow document=new XMLSlideShow(Files.newInputStream(new File("/home/kwong/sysu_learning/中国近代经济史/中国近代经济史_教学安排.pptx").toPath()));
		//primaryStage.setScene(new Scene(new BorderPane(new PptxViewer(document))));
		//Workbook document=WorkbookFactory.create(Files.newInputStream(new File("/home/kwong/下载/20151027114950964567.xls").toPath()));
		//primaryStage.setScene(new Scene(new BorderPane(new XlsViewer(document))));
		primaryStage.setMaximized(true);
		primaryStage.show();
	}
	public static void main(String[] args){
		launch(args);
	}
}
