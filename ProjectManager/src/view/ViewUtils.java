package view;

import javafx.scene.layout.Region;

public class ViewUtils {
	public static Region getVPlaceHolder(int height) {
		Region heightHolder = new Region();
		heightHolder.setPrefHeight(height);
		return heightHolder;
	}

	public static Region getHPlaceHolder(int width) {
		Region widthHolder = new Region();
		widthHolder.setPrefWidth(width);
		return widthHolder;
	}
}
