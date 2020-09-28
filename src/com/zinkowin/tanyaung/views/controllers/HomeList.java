package com.zinkowin.tanyaung.views.controllers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import com.zinkowin.tanyaung.factory.BorrowCellFactory;
import com.zinkowin.tanyaung.factory.DateFormatedFactory;
import com.zinkowin.tanyaung.factory.DateInterval;
import com.zinkowin.tanyaung.factory.TimePeriod;
import com.zinkowin.tanyaung.models.CardRegister;
import com.zinkowin.tanyaung.models.Home;
import com.zinkowin.tanyaung.models.Member;
import com.zinkowin.tanyaung.services.CardRegisterService;
import com.zinkowin.tanyaung.services.HomeService;
import com.zinkowin.tanyaung.services.MemberService;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

public class HomeList implements Initializable {
	@FXML
	private ComboBox<Member> name;
	@FXML
	private ComboBox<CardRegister> memberNo;
	@FXML
	private ImageView addimg;
	@FXML
	private ComboBox<String> find;
	@FXML
	private ComboBox<TimePeriod> date;
	@FXML
	private TableView<Home> homeTbView;
	@FXML
	private TableColumn<Home, LocalDate> fromCol;
	@FXML
	private TableColumn<Home, LocalDate> toCol;
	@FXML
	private TableColumn<Home, String> remarkCol;
  
	private HomeService homeService;
	private MemberService memberService;
	private CardRegisterService cardService;

	
	public void add() {
		HomeEdit.showView(null, this::listener);
	}

	public void search() {
		TimePeriod period = date.getValue();
		DateInterval interval = new DateInterval(period);
		homeTbView.getItems().clear();
		List<Home> homeList = homeService.findByParams(name.getValue(), memberNo.getValue(),interval.startDate(),interval.endDate(),find.getValue());
		homeTbView.getItems().addAll(homeList);

	}

	public void clear() {
		name.setValue(null);
		memberNo.setValue(null);
		date.setValue(TimePeriod.ALL);
        find.setValue(null);
        
		name.setPromptText("Name");
		memberNo.setPromptText("Card Number");
		date.setPromptText("Find by date");
		find.setPromptText("Find By Remark");
		

	}

	public void listener(Home home) {
		search();
	}

	public void createMenu() {
		MenuItem edit = new MenuItem("_Edit");
		edit.setAccelerator(new KeyCodeCombination(KeyCode.E, KeyCombination.ALT_DOWN));

		MenuItem delete = new MenuItem("_Delete");
		delete.setAccelerator(new KeyCodeCombination(KeyCode.D, KeyCombination.ALT_DOWN));

		homeTbView.setContextMenu(new ContextMenu(edit, delete));
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
			Home h = homeTbView.getSelectionModel().getSelectedItem();
			HomeEdit.showView(h, this::listener);
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
			Home h = homeTbView.getSelectionModel().getSelectedItem();
			homeService.delete(h);
			confirm.close();
			search();
		});

	}

	private void doFormatFactory() {
		fromCol.setCellFactory(new DateFormatedFactory<>());
		toCol.setCellFactory(new DateFormatedFactory<>());
		remarkCol.setCellFactory(new BorrowCellFactory<>());
	}
	
	private void setIconImage() throws FileNotFoundException {
		addimg.setImage(new Image(new FileInputStream("resources/images/add-friend.png")));
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		homeService = HomeService.getInstance();
		memberService = MemberService.getInstance();
		cardService = CardRegisterService.getInstance();
		date.getItems().addAll(TimePeriod.values());
		date.getSelectionModel().select(TimePeriod.THIS_WEEK);
		
		date.getSelectionModel().selectedItemProperty().addListener((a,b,c)->search());

		name.getItems().addAll(memberService.findAll());
		memberNo.getItems().addAll(cardService.findAll());
		find.getItems().addAll("Rented","Returned","Over Date");

		name.valueProperty().addListener((ob, o, n) -> search());
		memberNo.valueProperty().addListener((ob, o, n) -> search());
		find.valueProperty().addListener((ob, o, n) -> search());
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