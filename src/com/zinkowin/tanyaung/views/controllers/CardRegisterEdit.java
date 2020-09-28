package com.zinkowin.tanyaung.views.controllers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import com.zinkowin.tanyaung.models.CardRegister;
import com.zinkowin.tanyaung.services.CardRegisterService;
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

public class CardRegisterEdit implements Initializable {
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
	private Consumer<CardRegister> handler;
	private CardRegisterService cardService;
	private CardRegister register;

	static void showView(CardRegister register, Consumer<CardRegister> handler) {
		try {
			FXMLLoader loader = new FXMLLoader(
					CardRegisterList.class.getResource("/com/zinkowin/tanyaung/views/CardRegisterEdit.fxml"));
			Parent view = loader.load();

			stage = new Stage();
			CardRegisterEdit control = loader.getController();
			control.handler = handler;
			control.setData(register);

			stage.setScene(new Scene(view));
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setResizable(false);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void save() {

		boolean cardIsNull = register == null;

		try {
			if (cardIsNull) {
				register = new CardRegister();

			}

			register.setName(name.getText());

			if (name.getText().isEmpty()) {
				throw new ApplicationException("Register's name is empty! \nPlease enter name.");
			}

			register.setCard_number(card_number.getText());

			if (card_number.getText().isEmpty()) {
				throw new ApplicationException("Register's card number is empty! \nPlease enter card number.");
			}

			register.setOccupation(occupation.getText());

			if (occupation.getText().isEmpty()) {
				throw new ApplicationException("Register's occupation is empty! \nPlease enter occupation.");
			}

			register.setPh_number(ph_no.getText());

			if (ph_no.getText().isEmpty()) {
				throw new ApplicationException("Register's phone number is empty! \nPlease enter phone number.");
			}

			register.setAddress(address.getText());

			if (address.getText().isEmpty()) {
				throw new ApplicationException("Register's address is empty! \nPlease enter address.");
			}
            
			if (price.getText().isEmpty()) {
				throw new ApplicationException("Registeration fees is empty! \nPlease enter fees.");
			}
			register.setPrice(Integer.parseInt(price.getText()));

			

			register.setDate(date.getValue());

			if (null == date.getValue()) {
				throw new ApplicationException("Registeration date is empty! \nPlease select date!");
			}

			if (cardIsNull) {
				cardService.checkAndAdd(register);

			} else {
				cardService.update(register);
			}

			handler.accept(register);
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

	public void setData(CardRegister register) throws FileNotFoundException {
		this.register = register;

		if (null != register) {
			name.setText(register.getName());
			card_number.setText(register.getCard_number());
			occupation.setText(register.getOccupation());
			ph_no.setText(register.getPh_number());
			address.setText(register.getAddress());
			price.setText(String.valueOf((register.getPrice())));
			date.setValue(register.getDate());
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
		cardService = CardRegisterService.getInstance();
		try {
			setIconImage();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
