package com.zinkowin.tanyaung.factory;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class TextFormatedFactory<S> implements Callback<TableColumn<S, String>, TableCell<S, String>>{

	@Override
	public TableCell<S, String> call(TableColumn<S, String> param) {
		return new TableCell<S, String>(){
			@Override
			protected void updateItem(String item, boolean empty) {
				if(null == item || empty) {
					setText("");
				}
				else {
					setText(item);
					if(item.isEmpty())
						setText("UnKnown");
					setStyle("-fx-alignment: center;");
					
				}
			}
		};
	}

}
