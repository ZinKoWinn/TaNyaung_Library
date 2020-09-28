package com.zinkowin.tanyaung.views.controllers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import com.zinkowin.tanyaung.factory.DateFormatedFactory;
import com.zinkowin.tanyaung.factory.DateInterval;
import com.zinkowin.tanyaung.factory.TextFormatedFactory;
import com.zinkowin.tanyaung.factory.TimePeriod;
import com.zinkowin.tanyaung.models.Member;
import com.zinkowin.tanyaung.services.MemberService;
import com.zinkowin.tanyaung.views.controllers.custom.KoConfirm;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;

public class MemberList implements Initializable {
	@FXML
	private ComboBox<Member> card_number;
	@FXML
	private TextField name;
	@FXML
	private ImageView addimg;
	@FXML
	private TableView<Member> memberTbView;
	@FXML
	private TableColumn<Member, String> phnoCol;
	@FXML
	private TableColumn<Member, LocalDate> dateCol;
	@FXML
	private ComboBox<TimePeriod> date;
	private MemberService memberService;

	public void add() {

		MemberEdit.showView(null, this::listener);

	}

	public void search() {
		TimePeriod period = date.getValue();
		DateInterval interval = new DateInterval(period);
		memberTbView.getItems().clear();
		List<Member> memberList = memberService.findByParams(name.getText(), card_number.getValue(), interval.startDate(), interval.endDate());
		memberTbView.getItems().addAll(memberList);
	}

	public void clear() {
		name.setText("");
		card_number.setValue(null);
		date.setValue(TimePeriod.ALL);

		name.setPromptText("Enter Name");
		card_number.setPromptText("Select Card Number");
		date.setPromptText("Select Date");
	}

	public void listener(Member member) {
		// listener.accept(book);
		search();
	}

	public void createMenu() {
		MenuItem edit = new MenuItem("_Edit");
		edit.setAccelerator(new KeyCodeCombination(KeyCode.E, KeyCombination.ALT_DOWN));

		MenuItem delete = new MenuItem("_Delete");
		delete.setAccelerator(new KeyCodeCombination(KeyCode.D, KeyCombination.ALT_DOWN));

		memberTbView.setContextMenu(new ContextMenu(edit, delete));
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
			Member m = memberTbView.getSelectionModel().getSelectedItem();
			MemberEdit.showView(m, this::listener);
			confirm.close();
		});

	}

	public void delete() {
		KoConfirm confirm = new KoConfirm();
		confirm.setTitle("CONFIRMATION");
		confirm.setContentText("Are you sure to edit this item?");
		confirm.show();

		confirm.setOnCancled(e -> confirm.close());

		confirm.setOnConfirmed(e -> {
			Member m = memberTbView.getSelectionModel().getSelectedItem();
			memberService.delete(m);
			confirm.close();
			search();
		});

	}

	private void doFormatFactory() {
		phnoCol.setCellFactory(new TextFormatedFactory<>());
		dateCol.setCellFactory(new DateFormatedFactory<>());
	}
	
	private void setIconImage() throws FileNotFoundException {
		addimg.setImage(new Image(new FileInputStream("resources/images/memberAdd.png")));
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		memberService = MemberService.getInstance();
		card_number.getItems().addAll(memberService.findAll());
		date.getItems().addAll(TimePeriod.values());
        date.getSelectionModel().select(TimePeriod.THIS_MONTH);
		
		date.getSelectionModel().selectedItemProperty().addListener((a,b,c)->search());
		name.textProperty().addListener((a, b, c) -> search());
		card_number.valueProperty().addListener((a, b, c) -> search());
		
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
