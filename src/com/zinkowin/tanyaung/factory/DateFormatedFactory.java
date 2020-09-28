package com.zinkowin.tanyaung.factory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

public class DateFormatedFactory<S> implements Callback<TableColumn<S, LocalDate>, TableCell<S, LocalDate>>{
 
	@Override
	public TableCell<S, LocalDate> call(TableColumn<S, LocalDate> param) {
		// TODO Auto-generated method stub
		return new TableCell<S, LocalDate>(){
			@Override
			protected void updateItem(LocalDate item, boolean empty) {
				if(null == item || empty) setText("");
				else setText(item.format(DateTimeFormatter.ofPattern("yyyy-MMMM-dd (E)")));
				setStyle("-fx-alignment: center;");
			}
		};
	}

}
