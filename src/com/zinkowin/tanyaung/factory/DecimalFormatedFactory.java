package com.zinkowin.tanyaung.factory;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class DecimalFormatedFactory<S> implements Callback<TableColumn<S, Integer>, TableCell<S, Integer>>{

	@Override
	public TableCell<S, Integer> call(TableColumn<S, Integer> param) {
		return new TableCell<S, Integer>(){
			@Override
			protected void updateItem(Integer item, boolean empty) {
				if(null == item || empty) {
					setText("");
				}
				else {
					setText(String.valueOf(item));
					setStyle("-fx-alignment: center;");
				}
			}
		};
		
		}

}
