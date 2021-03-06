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
package cc.fooledit.editor.image;
import java.awt.image.*;
import java.io.*;
import javafx.application.*;
import javafx.embed.swing.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.stage.*;
import javax.imageio.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class ImageTest extends Application{
	@Override
	public void start(Stage primaryStage) throws Exception{
		BufferedImage image=ImageIO.read(new FileInputStream("/home/kwong/projects/unpaper-master/tests/goldenA1.pbm"));
		primaryStage.setScene(new Scene(new BorderPane(new ScrollPane(new ImageView(SwingFXUtils.toFXImage(image,null))))));
		primaryStage.setMaximized(true);
		primaryStage.show();
	}
	public static void main(String[] args){
		launch(args);
	}
}
