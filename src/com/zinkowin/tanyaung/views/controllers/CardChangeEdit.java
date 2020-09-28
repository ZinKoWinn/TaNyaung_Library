package com.zinkowin.tanyaung.views.controllers;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import com.zinkowin.tanyaung.models.CardChange;
import com.zinkowin.tanyaung.services.CardChangeService;
import com.zinkowin.tanyaung.utils.ApplicationException;
import com.zinkowin.tanyaung.views.controllers.custom.KoAlert;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CardChangeEdit implements Initializable {
	@FXML
	private Label title;
	@FXML
	private TextField name;
	@FXML
	private TextField card_number;
	@FXML
	private TextField occupation;
	@FXML
	private TextField ph_no;
	@FXML
	private TextField address;
	@FXML
	private TextField price;
	@FXML
	private DatePicker date;
	@FXML
	private ImageView header;
	
	private static Stage stage;
	private Consumer<CardChange> handler;
	private CardChangeService changeService;
	private CardChange change;
	


	static void showView(CardChange change, Consumer<CardChange> handler) {
		try {
			FXMLLoader loader = new FXMLLoader(CardChangeEdit.class.getResource("/com/zinkowin/tanyaung/views/CardChangeEdit.fxml"));
			Parent view = loader.load();

			stage = new Stage();
			CardChangeEdit control = loader.getController();
			control.handler = handler;
			control.setData(change);

			stage.setScene(new Scene(view));
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setResizable(false);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public void save() {

		boolean cardIsNull = change  == null;

		try {
			if (cardIsNull) {
				change = new CardChange();
				
			}

			change.setName(name.getText());
			
			if (name.getText().isEmpty()) {
				throw new ApplicationException("Member's name is empty! \nPlease enter member's name.");
			}

			change.setCard_number(card_number.getText());
			
			if (card_number.getText().isEmpty()) {
				throw new ApplicationException("Member card number is empty! \nPlease enter card number.");
			}

			change.setOccupation(occupation.getText());
		
			if (occupation.getText().isEmpty()) {
				throw new ApplicationException("Member's occupation is empty! \nPlease enter occupation!");
			}

			change.setPh_number(ph_no.getText());
			
			if (ph_no.getText().isEmpty()) {
				throw new ApplicationException("Member's phone number is empty! \nPlease enter phone number");
			}

			change.setAddress(address.getText());
			
			if (address.getText().isEmpty()) {
				throw new ApplicationException("Member's address is empty! \nPlease enter address.");
			}

			
			if (price.getText().isEmpty()) {
				throw new ApplicationException("Card change fee is empty! \nPlease enter fee");
			}
			change.setPrice(Integer.parseInt(price.getText()));
			
			change.setDate(date.getValue());
			
			if (null == date.getValue()) {
				throw new ApplicationException("Date is empty! \nPlease select date!");
			}
			

			if (cardIsNull) {
				changeService.add(change);
			
			}
			else {
				changeService.update(change);
			}
			
			handler.accept(change);
			close();

		} catch (Exception e) {
			KoAlert alert = new KoAlert();
			alert.setTitle("WARNING");
			alert.setContentText(e.getMessage());
			alert.show();
		}

	}

	public void close() {
		title.getScene().getWindow().hide();

	}

	public void clear() {
		name.setText("");
		card_number.setText("");
		occupation.setText("");
		ph_no.setText("");
		address.setText("");
		price.setText("");
		date.setValue(null);

		name.setPromptText("Enter Name");
		card_number.setPromptText("Enter Card Number");
		occupation.setPromptText("Enter Occupation");
		ph_no.setPromptText("Enter Phone Number");
		address.setPromptText("Enter Address");
		price.setPromptText("Enter Fees");
		date.setPromptText("Select Date");

	}

	public void setData(CardChange change) throws FileNotFoundException {
		this.change = change;

		if (null != change) {
			name.setText(change.getName());
			card_number.setText(change.getCard_number());
			occupation.setText(change.getOccupation());
			ph_no.setText(change.getPh_number());
			address.setText(change.getAddress());
			price.setText(String.valueOf((change.getPrice())));
			date.setValue(change.getDate());
			title.setText("EDIT MEMBER CARD REGISTERATION");
			stage.setTitle("Edit");
			stage.getIcons().add(new Image(new FileInputStream("resources/images/edit.png")));
		} else {
			title.setText("ADD");
			stage.setTitle("ADD");
			stage.getIcons().add(new Image(new FileInputStream("resources/images/add.png")));
		}

	}
	
	private void setIconImage() throws FileNotFoundException {
		header.setImage(new Image(new FileInputStream("resources/images/lib.png")));
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		changeService = CardChangeService.getInstance();
		try {
			setIconImage();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
