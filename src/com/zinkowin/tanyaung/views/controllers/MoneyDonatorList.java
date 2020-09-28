package com.zinkowin.tanyaung.views.controllers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import com.zinkowin.tanyaung.factory.DateFormatedFactory;
import com.zinkowin.tanyaung.factory.DateInterval;
import com.zinkowin.tanyaung.factory.PriceFormatedFactory;
import com.zinkowin.tanyaung.factory.TextFormatedFactory;
import com.zinkowin.tanyaung.factory.TimePeriod;
import com.zinkowin.tanyaung.models.MoneyDonator;
import com.zinkowin.tanyaung.services.MoneyDonatorService;
import com.zinkowin.tanyaung.views.controllers.custom.KoConfirm;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MoneyDonatorList implements Initializable {
	@FXML
	private ComboBox<MoneyDonator> name;
	@FXML
	private ComboBox<String> remark;
	@FXML
	private ComboBox<TimePeriod> date;
	@FXML
	private ImageView addimg;
	@FXML
	private TableView<MoneyDonator> moneyDonatorTbView;
	@FXML
	private TableColumn<MoneyDonator, Integer> donateCol;
	@FXML
	private TableColumn<MoneyDonator, LocalDate> dateCol;
    @FXML
    private TableColumn<MoneyDonator, String> remarkCol;
	private MoneyDonatorService moneyDonatorService;

	public void add() {
		MoneyDonatorEdit.showView(null, this::handler);

	}

	public void search() {
		TimePeriod period = date.getValue();
		DateInterval interval = new DateInterval(period);
		moneyDonatorTbView.getItems().clear();
		List<MoneyDonator> donatorList = moneyDonatorService.findByParams(name.getValue(), remark.getValue(),
				interval.startDate(), interval.endDate());
		moneyDonatorTbView.getItems().addAll(donatorList);
	}

	public void clear() {
		name.setValue(null);
		remark.setValue(null);
		date.setValue(TimePeriod.ALL);

		name.setPromptText("Name");
		remark.setPromptText("Remark");
	}

	public void createMenu() {
		MenuItem edit = new MenuItem("_Edit");
		MenuItem delete = new MenuItem("_Delete");

		ContextMenu menu = new ContextMenu(edit, delete);
		moneyDonatorTbView.setContextMenu(menu);
		edit.setOnAction(e -> edit());
		delete.setOnAction(e -> delete());

	}

	private void edit() {
		KoConfirm confirm = new KoConfirm();
		confirm.setTitle("CONFIRMATION");
		confirm.setContentText("Are you sure to edit this item?");
		confirm.show();

		confirm.setOnCancled(e -> confirm.close());

		confirm.setOnConfirmed(e -> {
			MoneyDonator m = moneyDonatorTbView.getSelectionModel().getSelectedItem();
			MoneyDonatorEdit.showView(m, this::handler);
			confirm.close();
		});
	}

	private void delete() {

		KoConfirm confirm = new KoConfirm();
		confirm.setTitle("CONFIRMATION");
		confirm.setContentText("Are you sure to delete this item?");
		confirm.show();

		confirm.setOnCancled(e -> confirm.close());

		confirm.setOnConfirmed(e -> {
			MoneyDonator m = moneyDonatorTbView.getSelectionModel().getSelectedItem();
			moneyDonatorService.delete(m);
			confirm.close();
			search();
		});

	}

	public void doFormatFactory() {
		donateCol.setCellFactory(new PriceFormatedFactory<>());
		dateCol.setCellFactory(new DateFormatedFactory<>());
		remarkCol.setCellFactory(new TextFormatedFactory<>());
	}
	
	public void handler(MoneyDonator m) {
		search();
	}
	private void setIconImage() throws FileNotFoundException {
		addimg.setImage(new Image(new FileInputStream("resources/images/memberAdd.png")));
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		moneyDonatorService = MoneyDonatorService.getInstance();
		name.getItems().addAll(moneyDonatorService.findAll());
		remark.getItems().addAll("Monthly Donator", "Non Monthly Donator");

		name.valueProperty().addListener((a, b, c) -> search());
		remark.valueProperty().addListener((ob, o, n) -> search());

		date.getItems().addAll(TimePeriod.values());
		date.getSelectionModel().select(TimePeriod.THIS_MONTH);
		date.getSelectionModel().selectedItemProperty().addListener((ob, o, n) -> search());

		createMenu();
		doFormatFactory();
		try {
			setIconImage();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		search();
	}

}
