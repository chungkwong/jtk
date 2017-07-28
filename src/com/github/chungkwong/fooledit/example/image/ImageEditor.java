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
package com.github.chungkwong.fooledit.example.image;
import com.github.chungkwong.fooledit.*;
import com.github.chungkwong.fooledit.api.*;
import com.github.chungkwong.fooledit.control.*;
import com.github.chungkwong.fooledit.model.*;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;
import javafx.application.*;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.*;
import javafx.scene.canvas.*;
import javafx.scene.control.*;
import javafx.scene.effect.*;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.stage.*;
/**
 *
 * @author Chan Chung Kwong <1m02math@126.com>
 */
public class ImageEditor  extends Application implements DataEditor<ImageObject>{
	@Override
	public Node edit(ImageObject data){
		Canvas canvas=data.getCanvas();
		canvas.setCursor(Cursor.CROSSHAIR);
		ImageContext context=new ImageContext(canvas);

		return new BorderPane(new ScrollPane(new StackPane(canvas,context.preview)),getEffectBar(context),getPathBar(context),getPropertiesBar(context),null);
	}
	private Node getPathBar(ImageContext context){
		Canvas canvas=context.canvas;
		GraphicsContext g2d=canvas.getGraphicsContext2D();
		GraphicsContext pg2d=context.preview.getGraphicsContext2D();
		Button start=new Button(MessageRegistry.getString("START"));
		start.setOnAction((e)->{
			g2d.beginPath();
			pg2d.beginPath();
		});
		Button close=new Button(MessageRegistry.getString("CLOSE"));
		close.setOnAction((e)->{
			g2d.closePath();
		});
		Button draw=new Button(MessageRegistry.getString("DRAW"));
		draw.setOnAction((e)->{
			g2d.stroke();
			clearPreview(context);
		});
		Button fill=new Button(MessageRegistry.getString("FILL"));
		fill.setOnAction((e)->{
			g2d.fill();
			clearPreview(context);
		});
		VBox bar=new VBox(start,close,draw,fill);
		bar.getChildren().add(new Separator(Orientation.VERTICAL));
		ToggleGroup elements=new ToggleGroup();
		for(Element shape:Element.values()){
			ToggleButton button=new ToggleButton(MessageRegistry.getString(shape.name()));
			button.setUserData(shape);
			elements.getToggles().add(button);
			bar.getChildren().add(button);
		}
		elements.selectedToggleProperty().addListener((e,o,n)->context.recorded=0);
		context.preview.setOnMouseClicked((e)->{
			configure(context);
			context.preview.getGraphicsContext2D().fillOval(e.getX()-2,e.getY()-2,4,4);
			((Element)elements.getSelectedToggle().getUserData()).make(e,context);
		});
		return bar;
	}
	private enum Element{
		MOVE((e,c)->{
			c.getGraphics().moveTo(e.getX(),e.getY());
			c.preview.getGraphicsContext2D().moveTo(e.getX(),e.getY());
		}),
		LINE((e,c)->{
			c.getGraphics().lineTo(e.getX(),e.getY());
			c.preview.getGraphicsContext2D().lineTo(e.getX(),e.getY());
			c.preview.getGraphicsContext2D().stroke();
		}),
		RECT(keepOrMake((e,c)->{
			c.getGraphics().rect(c.lastx,c.lasty,e.getX()-c.lastx,e.getY()-c.lasty);
			c.preview.getGraphicsContext2D().rect(c.lastx,c.lasty,e.getX()-c.lastx,e.getY()-c.lasty);
			c.preview.getGraphicsContext2D().stroke();
		},1)),
		QUADRATIC(keepOrMake((e,c)->{
			c.getGraphics().quadraticCurveTo(c.lastx,c.lasty,e.getX(),e.getY());
			c.preview.getGraphicsContext2D().quadraticCurveTo(c.lastx,c.lasty,e.getX(),e.getY());
			c.preview.getGraphicsContext2D().stroke();
		},1)),
		ARC(keepOrMake((e,c)->{
			double angle=getAngle(c.lastlastx,c.lastlasty,c.lastx,c.lasty,e.getX(),e.getY());
			c.getGraphics().arcTo(c.lastx,c.lasty,e.getX(),e.getY(),angle);
			c.preview.getGraphicsContext2D().arcTo(c.lastx,c.lasty,e.getX(),e.getY(),angle);
			c.preview.getGraphicsContext2D().stroke();
		},1)),
		BEZIER(keepOrMake((e,c)->{
			c.getGraphics().bezierCurveTo(c.lastlastx,c.lastlasty,c.lastx,c.lasty,e.getX(),e.getY());
			c.preview.getGraphicsContext2D().bezierCurveTo(c.lastx,c.lasty,c.lastx,c.lasty,e.getX(),e.getY());
			c.preview.getGraphicsContext2D().stroke();
		},2)),
		TEXT((e,c)->{
			Main.INSTANCE.getMiniBuffer().setMode((text)->{
				c.getGraphics().strokeText(text,e.getX(),e.getY());
				clearPreview(c);
				Main.INSTANCE.getMiniBuffer().restore();
			},null,"",null,null);
		});
		private final BiConsumer<MouseEvent,ImageContext> drawer;
		private Element(BiConsumer<MouseEvent,ImageContext> drawer){
			this.drawer=drawer;
		}
		void make(MouseEvent e,ImageContext c){
			drawer.accept(e,c);
		}
		static BiConsumer<MouseEvent,ImageContext> keepOrMake(BiConsumer<MouseEvent,ImageContext> maker,int needPoints){
			return (e,c)->{
				if(c.recorded>=needPoints){
					maker.accept(e,c);
					c.recorded=0;
				}else{
					++c.recorded;
				}
				c.lastlastx=c.lastx;
				c.lastlasty=c.lasty;
				c.lastx=e.getX();
				c.lasty=e.getY();
			};
		}
		static double getAngle(double x0,double y0,double x1,double y1,double x2,double y2){
			double a=Math.hypot(x0-x1,y0-y1);
			double b=Math.hypot(x0-x1,y0-y1);
			double c=Math.hypot(x0-x2,y0-y2);
			return Math.acos((a*a+b*b-c*c)/(2*a*b));
		}
	}
	private Node getEffectBar(ImageContext context){
		Canvas canvas=context.canvas;
		ComboBox<ImageEffect> effectChooser=new ComboBox<>();
		effectChooser.getItems().setAll(ImageEffect.values());
		effectChooser.getSelectionModel().selectFirst();
		effectChooser.getSelectionModel().selectedItemProperty().addListener((e,o,n)->canvas.setEffect(n.getEffect()));
		Spinner<Double> scaleChooser=new Spinner<>(0.0,4.0,1.0);
		scaleChooser.valueProperty().addListener((e,o,n)->{
			canvas.setTranslateX(canvas.getWidth()*(n-1)/2);canvas.setScaleX(n);
			canvas.setTranslateY(canvas.getHeight()*(n-1)/2);canvas.setScaleY(n);});
		Spinner<Double> rotateChooser=new Spinner<>(-180.0,180.0,0.0);
		rotateChooser.valueProperty().addListener((e,o,n)->{canvas.setRotate(n);});
		return new HBox(effectChooser,scaleChooser,rotateChooser);
	}
	private enum ImageEffect{
		NONE(()->null),
		BLOOM(()->new Bloom()),
		SHADOW(()->new Shadow()),
		GLOW(()->new Glow()),
		BLUR(()->new GaussianBlur()),
		TONE(()->new SepiaTone());
		private final Supplier<Effect> supplier;
		private ImageEffect(Supplier<Effect> supplier){
			this.supplier=supplier;
		}
		Effect getEffect(){
			return supplier.get();
		}
		public String toString(){
			return MessageRegistry.getString(name());
		}
	}
	private Node getPropertiesBar(ImageContext context){
		TabPane tabs=new TabPane(new Tab(MessageRegistry.getString("STROKE"),getStrokePropertiesBar(context)),
				new Tab(MessageRegistry.getString("FILL"),getFillPropertiesBar(context)),
				new Tab(MessageRegistry.getString("TEXT"),getTextPropertiesBar(context)));
		tabs.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
		return tabs;
	}
	private Node getStrokePropertiesBar(ImageContext context){
		Canvas canvas=context.canvas;
		GraphicsContext g2d=canvas.getGraphicsContext2D();
		context.joinChooser.setConverter(new EnumStringConvertor<>(StrokeLineJoin.class));
		context.joinChooser.getItems().setAll(StrokeLineJoin.values());
		context.joinChooser.getSelectionModel().select(g2d.getLineJoin());
		context.capChooser.setConverter(new EnumStringConvertor<>(StrokeLineCap.class));
		context.capChooser.getItems().setAll(StrokeLineCap.values());
		context.capChooser.getSelectionModel().select(g2d.getLineCap());
		Label dashLabel=new Label(MessageRegistry.getString("DASH"));
		context.dashChooser.setText(fromDashArray(g2d.getLineDashes()));
		Label thickLabel=new Label(MessageRegistry.getString("THICK"));
		context.thickChooser.setText(Double.toString(g2d.getLineWidth()));
		return new HBox(context.joinChooser,context.capChooser,
				dashLabel,context.dashChooser,thickLabel,context.thickChooser,
				context.strokeChooser);
	}
	private Node getFillPropertiesBar(ImageContext context){
		context.fillRuleChooser.setConverter(new EnumStringConvertor<>(FillRule.class));
		context.fillRuleChooser.getItems().setAll(FillRule.values());
		context.fillRuleChooser.getSelectionModel().select(FillRule.NON_ZERO);
		context.alphaChooser.setEditable(true);
		return new HBox(context.fillRuleChooser,context.alphaChooser,context.fillChooser);
	}
	private Node getTextPropertiesBar(ImageContext context){
		return context.fontChooser;
	}
	private static double[] toDashArray(String str){
		return Arrays.stream(str.split(":")).mapToDouble((s)->Double.parseDouble(s)).toArray();
	}
	private static String fromDashArray(double[] array){
		return array==null?"1":Arrays.stream(array).mapToObj((d)->Double.toString(d)).collect(Collectors.joining(":"));
	}
	private void configure(ImageContext context){
		GraphicsContext g2d=context.canvas.getGraphicsContext2D();
		g2d.setLineJoin(context.joinChooser.getValue());
		g2d.setLineCap(context.capChooser.getValue());
		g2d.setLineDashes(toDashArray(context.dashChooser.getText()));
		g2d.setLineWidth(Double.parseDouble(context.thickChooser.getText()));
		g2d.setStroke(context.strokeChooser.getPaint());
		g2d.setFill(context.fillChooser.getPaint());
		g2d.setFillRule(context.fillRuleChooser.getValue());
		g2d.setFont(context.fontChooser.getFont());
		g2d.setFontSmoothingType(context.fontChooser.getFontSmoothingType());
		g2d.setTextAlign(context.fontChooser.getTextAlignment());
		g2d.setTextBaseline(context.fontChooser.getTextBaseline());
		g2d.setGlobalAlpha(((Number)context.alphaChooser.getValue()).doubleValue());
	}
	@Override
	public String getName(){
		return MessageRegistry.getString("IMAGE_EDITOR");
	}
	@Override
	public void start(Stage stage) throws Exception{
		stage.setScene(new Scene(new BorderPane(new ImageEditor().edit(new ImageObject(new WritableImage(200,200))))));
		stage.show();
	}
	private static void clearPreview(ImageContext c){
		c.preview.getGraphicsContext2D().clearRect(0,0,c.canvas.getWidth(),c.canvas.getHeight());
		c.recorded=0;
	}
	public static void main(String[] args){
		launch(args);
	}
	private class ImageContext{
		private final FontChooser fontChooser=new FontChooser();
		private final ComboBox<StrokeLineJoin> joinChooser=new ComboBox<>();
		private final ComboBox<StrokeLineCap> capChooser=new ComboBox<>();
		private final ComboBox<FillRule> fillRuleChooser=new ComboBox<>();
		private final Spinner alphaChooser=new Spinner(0.0,1.0,1.0,0.1);
		private final TextField dashChooser=new TextField();
		private final TextField thickChooser=new TextField();
		private final PaintChooser strokeChooser=new PaintChooser(Color.BLACK);
		private final PaintChooser fillChooser=new PaintChooser(Color.WHITE);
		private double lastx=Double.NaN,lasty=Double.NaN;
		private double lastlastx=Double.NaN,lastlasty=Double.NaN;
		private int recorded=0;
		private final Canvas canvas,preview;
		public ImageContext(Canvas canvas){
			this.canvas=canvas;
			preview=new Canvas(canvas.getWidth(),canvas.getHeight());
			preview.translateXProperty().bind(canvas.translateXProperty());
			preview.translateYProperty().bind(canvas.translateYProperty());
			preview.scaleXProperty().bind(canvas.scaleXProperty());
			preview.scaleYProperty().bind(canvas.scaleYProperty());
		}
		GraphicsContext getGraphics(){
			return canvas.getGraphicsContext2D();
		}
	}
}