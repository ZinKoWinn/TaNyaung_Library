package com.zinkowin.tanyaung.views.controllers.custom;

import java.util.function.Consumer;

import com.zinkowin.tanyaung.models.Category;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.shape.SVGPath;

public class CategoryBox extends VBox {
	private static final double REQUIRED_WIDTH = 50.0;
	private static final double REQUIRED_HEIGHT = 40.0;

	public CategoryBox(Category c,Consumer<Category> listener) {
		getStyleClass().add("flowPane");
		SVGPath path = new SVGPath();

		path.setContent(
				"M17.484 14.344q1.219 0 2.531 0.234v1.5q-0.938-0.234-2.531-0.234-2.813 0-4.5 0.984v-1.688q1.781-0.797 4.5-0.797zM12.984 12.469q1.969-0.797 4.5-0.797 1.219 0 2.531 0.234v1.5q-0.938-0.234-2.531-0.234-2.813 0-4.5 0.984v-1.688zM17.484 10.5q-2.813 0-4.5 0.984v-1.641q1.875-0.844 4.5-0.844 1.219 0 2.531 0.234v1.547q-1.125-0.281-2.531-0.281zM21 18.516v-11.531q-1.547-0.469-3.516-0.469-3.047 0-5.484 1.5v11.484q2.438-1.5 5.484-1.5 1.828 0 3.516 0.516zM17.484 4.5q3.563 0 5.531 1.5v14.578q0 0.188-0.164 0.352t-0.352 0.164q-0.141 0-0.234-0.047-1.922-1.031-4.781-1.031-3.047 0-5.484 1.5-2.016-1.5-5.484-1.5-2.531 0-4.781 1.078-0.047 0-0.117 0.023t-0.117 0.023q-0.188 0-0.352-0.141t-0.164-0.328v-14.672q2.016-1.5 5.531-1.5 3.469 0 5.484 1.5 2.016-1.5 5.484-1.5z");
		path.getStyleClass().add("svg-path");
		resize(path, REQUIRED_WIDTH, REQUIRED_HEIGHT);

		Label catLabel = new Label(c.getName());
		catLabel.getStyleClass().add("cat-label");
		getChildren().addAll(path, catLabel);
		
//		setOnMouseClicked(e->{
//			if(e.getClickCount()>1) listener.accept(c);
//		});
		
		setOnContextMenuRequested(e->{
			listener.accept(c);
		});

	}

	private void resize(SVGPath svg, double width, double height) {

		double originalWidth = svg.prefWidth(-1);
		double originalHeight = svg.prefHeight(originalWidth);

		double scaleX = width / originalWidth;
		double scaleY = height / originalHeight;

		svg.setScaleX(scaleX);
		svg.setScaleY(scaleY);
	}
}