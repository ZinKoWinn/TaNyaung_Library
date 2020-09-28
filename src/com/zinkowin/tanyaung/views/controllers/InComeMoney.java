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
import com.zinkowin.tanyaung.models.InCome;
import com.zinkowin.tanyaung.services.InComeService;
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

public class InComeMoney implements Initializable {

	@FXML
	private ComboBox<TimePeriod> date;
	@FXML
	private TableView<InCome> incomeTbView;
	@FXML
	private TableColumn<InCome, Integer> amountCol;
	@FXML
	private TableColumn<InCome, LocalDate> dateCol;
    @FXML
    private Label total;
	private InComeService incomeService;

	public void add() {
		InComeMoneyEdit.showView(null, this::handler);
	}

	public void search() {
		TimePeriod period = date.getValue();
		DateInterval interval = new DateInterval(period);
		incomeTbView.getItems().clear();
		List<InCome> incomeList = incomeService.findByParams(interval.startDate(), interval.endDate());
		incomeTbView.getItems().addAll(incomeList);
		findTotal();
	}

	public void clear() {
		date.setValue(TimePeriod.ALL);
	}

	public void handler(InCome i) {
		search();
	}

	private void createMenu() {
		MenuItem edit = new MenuItem("_Edit");
		edit.setAccelerator(new KeyCodeCombination(KeyCode.E, KeyCombination.ALT_DOWN));

		MenuItem delete = new MenuItem("_Delete");
		delete.setAccelerator(new KeyCodeCombination(KeyCode.D, KeyCombination.ALT_DOWN));

		incomeTbView.setContextMenu(new ContextMenu(edit, delete));
		edit.setOnAction(e -> edit());
		delete.setOnAction(d -> delete());
	}

	public void edit() {
		KoConfirm confirm = new KoConfirm();
		confirm.setTitle("CONFIRMATION");
		confirm.setContentText("Are you sure to edit this item?");
		confirm.show();

		confirm.setOnCancled(e -> confirm.close());

		confirm.setOnConfirmed(e -> {
			InCome i = incomeTbView.getSelectionModel().getSelectedItem();
			InComeMoneyEdit.showView(i, this::handler);
			confirm.close();
		});
	}

	public void delete() {

		KoConfirm confirm = new KoConfirm();
		confirm.setTitle("CONFIRMATION");
		confirm.setContentText("Are you sure to delete this item?");
		confirm.show();

		confirm.setOnCancled(e -> confirm.close());

		confirm.setOnConfirmed(e -> {
			InCome i = incomeTbView.getSelectionModel().getSelectedItem();
			incomeService.delete(i);
			search();
		});
	}

	private void doFormatFactory() {
		amountCol.setCellFactory(new PriceFormatedFactory<>());
		dateCol.setCellFactory(new DateFormatedFactory<>());
	}
	
	private void findTotal() {
		int incometotal = incomeTbView.getItems().stream().mapToInt(t->t.getIncomeAmount()).sum();
		total.setText("Total Income = "+ FormatToMmk.formatToMmk(incometotal));
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		incomeService = InComeService.getInstance();

		date.getItems().addAll(TimePeriod.values());
		date.getSelectionModel().select(TimePeriod.THIS_MONTH);
		date.getSelectionModel().selectedItemProperty().addListener((a, b, c) -> search());
		createMenu();
		doFormatFactory();
		search();

	}

}
