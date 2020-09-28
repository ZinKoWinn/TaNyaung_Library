package com.zinkowin.tanyaung.factory;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class PriceFormatedFactory<S> implements Callback<TableColumn<S, Integer>, TableCell<S, Integer>> {

	@Override
	public TableCell<S, Integer> call(TableColumn<S, Integer> column) {
		// TODO Auto-generated method stub
		return new TableCell<S, Integer>(){
			@Override
			protected void updateItem(Integer item, boolean empty) {
				if(null == item || empty) {
					setText("");
				}
				else {
					setText(FormatToMmk.formatToMmk(item));
					setStyle("-fx-alignment: center;");
				}
			}
		};
	}
	
	

}
