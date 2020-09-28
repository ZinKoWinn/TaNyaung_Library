package com.zinkowin.tanyaung.views.controllers;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import com.zinkowin.tanyaung.factory.DateFormatedFactory;
import com.zinkowin.tanyaung.factory.DateInterval;
import com.zinkowin.tanyaung.factory.FormatToMmk;
import com.zinkowin.tanyaung.factory.PriceFormatedFactory;
import com.zinkowin.tanyaung.factory.TimePeriod;
import com.zinkowin.tanyaung.models.OutCome;
import com.zinkowin.tanyaung.services.OutComeService;
import com.zinkowin.tanyaung.views.controllers.custom.KoConfirm;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

public class OutComeMoney implements Initializable {

	@FXML
	private ComboBox<TimePeriod> date;
	@FXML
	private TableView<OutCome> outcomeTbView;
	@FXML
	private TableColumn<OutCome, Integer> expensesCol;
	@FXML
	private TableColumn<OutCome, LocalDate> dateCol;
	@FXML
    private Label total;
	private OutComeService outService;

	public void add() {
		OutComeMoneyEdit.showView(null, this::handler);
	}

	public void search() {
		TimePeriod period = date.getValue();
		DateInterval interval = new DateInterval(period);
		outcomeTbView.getItems().clear();
		List<OutCome> outcomeList = outService.findByParams(interval.startDate(), interval.endDate());
		outcomeTbView.getItems().addAll(outcomeList);
		 findTotal();
	}

	public void clear() {
		date.setValue(TimePeriod.ALL);
	}

	public void handler(OutCome o) {
		search();
	}
	
	public void createMenu() {
		MenuItem edit = new MenuItem("_Edit");
		edit.setAccelerator(new KeyCodeCombination(KeyCode.E, KeyCombination.ALT_DOWN));
		
		MenuItem delete = new MenuItem("_Delete");
		delete.setAccelerator(new KeyCodeCombination(KeyCode.D, KeyCombination.ALT_DOWN));
		
		outcomeTbView.setContextMenu(new ContextMenu(edit,delete));
		edit.setOnAction(e->edit());
		delete.setOnAction(e->delete());
	}

	private void edit() {
		KoConfirm confirm = new KoConfirm();
		confirm.setTitle("CONFIRMATION");
		confirm.setContentText("Are you sure to edit this item?");
		confirm.show();
		
		confirm.setOnCancled(e->confirm.close());
		
		confirm.setOnConfirmed(e->{
			OutCome o = outcomeTbView.getSelectionModel().getSelectedItem();
			OutComeMoneyEdit.showView(o, this::handler);
			confirm.close();
		});
	}
	
	private void delete() {
		KoConfirm confirm = new KoConfirm();
		confirm.setTitle("CONFIRMATION");
		confirm.setContentText("Are you sure to delete this item?");
		confirm.show();
		
		confirm.setOnCancled(e->confirm.close());
		
		confirm.setOnConfirmed(e->{
			OutCome o = outcomeTbView.getSelectionModel().getSelectedItem();
			outService.delete(o);
			confirm.close();
			search();
		});
	}
	
	private void doFormatFactory() {
		expensesCol.setCellFactory(new PriceFormatedFactory<>());
		dateCol.setCellFactory(new DateFormatedFactory<>());
	}
	
	private void findTotal() {
		int outcomeTotal = outcomeTbView.getItems().stream().mapToInt(o->o.getExpenses()).sum();
		total.setText("Total Expenses = "+ FormatToMmk.formatToMmk(outcomeTotal));
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		outService = OutComeService.getInstance();
		date.getItems().addAll(TimePeriod.values());
		date.getSelectionModel().select(TimePeriod.THIS_MONTH);
		date.getSelectionModel().selectedItemProperty().addListener((a, b, c) -> search());
		createMenu();
	    doFormatFactory();
	    search();
	    

	}

}
