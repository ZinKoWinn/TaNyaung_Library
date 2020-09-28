package com.zinkowin.tanyaung.factory;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class BorrowCellFactory<S> implements Callback<TableColumn<S,String>, TableCell<S,String>> {

	@Override
	public TableCell<S, String> call(TableColumn<S, String> column) {
		
		return new TableCell<S, String>(){
			@Override
			protected void updateItem(String item, boolean empty) {
				if(null == item || empty) {
					setText("");
					setGraphic(null);
				}else {
					setText(item);
					if(item.contains("Rented")) {
						setStyle(" -fx-text-fill : #ff3d00;; -fx-alignment: center; -fx-font-family: Century; -fx-font-weight: bold; -fx-font-size: 13.0px");
					}else if(item.contains("Returned")) {
						setStyle(" -fx-text-fill : #087f23; -fx-alignment: center; -fx-font-family: Century; -fx-font-weight: bold; -fx-font-size: 13.0px");
					}else if(item.contains("Over Date")) {
						setStyle(" -fx-text-fill : #2962ff; -fx-alignment: center; -fx-font-family: Century; -fx-font-weight: bold; -fx-font-size: 13.0px");
					}else {
						setStyle(null);
				}
			}
			}
		};
	}

}
