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
import com.zinkowin.tanyaung.models.CardRegister;
import com.zinkowin.tanyaung.services.CardRegisterService;
import com.zinkowin.tanyaung.views.controllers.custom.KoConfirm;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

public class CardRegisterList implements Initializable{
	@FXML
	private ComboBox<CardRegister> name;
	@FXML
	private ComboBox<CardRegister> card_number;
	@FXML
	private ComboBox<TimePeriod> date;
	@FXML 
	private ImageView addimg;
	@FXML
	private TableView<CardRegister> cardRegisterTbView;
	@FXML
	private TableColumn<CardRegister, String> phnoCol;
	@FXML
	private TableColumn<CardChange, Integer> feesCol;
	@FXML 
	private TableColumn<CardChange, LocalDate> dateCol;
	@FXML
	private Label total;
	
	private CardRegisterService cardService;
	
	
	public void add() {
		CardRegisterEdit.showView(null, this::listener);
	}
	
	public void search() {
		TimePeriod period = date.getValue();
		DateInterval interval = new DateInterval(period);
		
		cardRegisterTbView.getItems().clear();
		List<CardRegister> cardList = cardService.findByParams(name.getValue(), card_number.getValue(), interval.startDate(), interval.endDate());
		cardRegisterTbView.getItems().addAll(cardList);
		findTotal();
		
	}
	
	private void findTotal() {
		total.setText("Total = " + FormatToMmk.formatToMmk(cardRegisterTbView.getItems().stream().mapToInt(st -> st.getPrice()).sum()));
	}

	public void listener(CardRegister register) {
		search();
	}
	
	public void clear() {
		name.setValue(null);
		card_number.setValue(null);
		date.setValue(TimePeriod.ALL);

		name.setPromptText("Name");
		card_number.setPromptText("Card Number");
		
		
	}
	
	public void createMenu() {
		MenuItem edit = new MenuItem("_Edit");
		edit.setAccelerator(new KeyCodeCombination(KeyCode.E, KeyCombination.ALT_DOWN));
		
		MenuItem delete = new MenuItem("_Delete");
		delete.setAccelerator(new KeyCodeCombination(KeyCode.D, KeyCombination.ALT_DOWN));
		
		cardRegisterTbView.setContextMenu(new ContextMenu(edit, delete));
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
		CardRegister cc = cardRegisterTbView.getSelectionModel().getSelectedItem();
		CardRegisterEdit.showView(cc, this::listener);
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
		CardRegister cc  = cardRegisterTbView.getSelectionModel().getSelectedItem();
		cardService.delete(cc);
		confirm.close();
		search();
		});

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
		cardService = CardRegisterService.getInstance();
		name.getItems().addAll(cardService.findAll());
		card_number.getItems().addAll(cardService.findAll());
		date.getItems().addAll(TimePeriod.values());
        
		name.valueProperty().addListener((a, b, c) -> search());
		card_number.valueProperty().addListener((a, b, c) -> search());
		date.getSelectionModel().select(TimePeriod.THIS_MONTH);
		date.getSelectionModel().selectedItemProperty().addListener((ob,o,n)->search());
		createMenu();
		doFormatFactory();
		search();
		try {
			setIconImage();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
