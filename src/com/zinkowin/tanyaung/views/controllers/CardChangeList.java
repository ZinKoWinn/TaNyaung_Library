package com.zinkowin.tanyaung.views.controllers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import com.zinkowin.tanyaung.factory.DateFormatedFactory;
import com.zinkowin.tanyaung.factory.DateInterval;
import com.zinkowin.tanyaung.factory.FormatToMmk;
import com.zinkowin.tanyaung.factory.PriceFormatedFactory;
import com.zinkowin.tanyaung.factory.TextFormatedFactory;
import com.zinkowin.tanyaung.factory.TimePeriod;
import com.zinkowin.tanyaung.models.CardChange;
import com.zinkowin.tanyaung.services.CardChangeService;
import com.zinkowin.tanyaung.views.controllers.custom.KoConfirm;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

public class CardChangeList implements Initializable {
	@FXML
	private TextField name;
	@FXML
	private ComboBox<CardChange> card_number;
	@FXML
	private ComboBox<TimePeriod> date;
	@FXML
	private ImageView addimg;
	@FXML
	private TableView<CardChange> cardChangeTbView;
	@FXML
	private TableColumn<CardChange, String> phnoCol;
	@FXML
	private TableColumn<CardChange, Integer> feesCol;
	@FXML
	private TableColumn<CardChange, LocalDate> dateCol;

	@FXML
	private Label total;
	
	private CardChangeService changeService;

	public void add() {
		CardChangeEdit.showView(null, this::listener);
	}

	public void search() {
		TimePeriod period = date.getValue();
		DateInterval interval = new DateInterval(period);

		cardChangeTbView.getItems().clear();
		List<CardChange> cardList = changeService.findByParams(name.getText(), card_number.getValue(), interval.startDate(), interval.endDate());
		cardChangeTbView.getItems().addAll(cardList);
		findTotal();

	}

	private void findTotal() {
		total.setText("Total = " + FormatToMmk.formatToMmk(cardChangeTbView.getItems().stream().mapToInt(st -> st.getPrice()).sum()));
	}

	public void listener(CardChange change) {
		search();
	}

	public void createMenu() {
		MenuItem edit = new MenuItem("_Edit");
		edit.setAccelerator(new KeyCodeCombination(KeyCode.E, KeyCombination.ALT_DOWN));

		MenuItem delete = new MenuItem("_Delete");
		delete.setAccelerator(new KeyCodeCombination(KeyCode.D, KeyCombination.ALT_DOWN));

		cardChangeTbView.setContextMenu(new ContextMenu(edit, delete));
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
			CardChange b = cardChangeTbView.getSelectionModel().getSelectedItem();
			CardChangeEdit.showView(b, this::listener);
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
			CardChange b = cardChangeTbView.getSelectionModel().getSelectedItem();
			changeService.delete(b);
			confirm.close();
			search();
		});

	}

	public void clear() {
		name.setText("");
		card_number.setValue(null);
		date.setValue(TimePeriod.ALL);

		name.setPromptText("Enter Name");
		card_number.setPromptText("Card Number");
	

	}

	private void doFormatFactory() {
		phnoCol.setCellFactory(new TextFormatedFactory<>());
		feesCol.setCellFactory(new PriceFormatedFactory<>());
		dateCol.setCellFactory(new DateFormatedFactory<>());
	}
	
	private void setIconImage() throws FileNotFoundException {
		addimg.setImage(new Image(new FileInputStream("resources/images/memberAdd.png")));
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		changeService = CardChangeService.getInstance();

		card_number.getItems().addAll(changeService.findAll());
		date.getItems().addAll(TimePeriod.values());

		name.textProperty().addListener((a, b, c) -> search());
		card_number.valueProperty().addListener((a, b, c) -> search());
		date.getSelectionModel().select(TimePeriod.THIS_MONTH);
		date.getSelectionModel().selectedItemProperty().addListener((ob,o,n)->search());
	
		search();
		createMenu();
		doFormatFactory();
		try {
			setIconImage();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
