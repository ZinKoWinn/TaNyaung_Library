package com.zinkowin.tanyaung.views.controllers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import com.zinkowin.tanyaung.models.MoneyDonator;
import com.zinkowin.tanyaung.services.MoneyDonatorService;
import com.zinkowin.tanyaung.utils.ApplicationException;
import com.zinkowin.tanyaung.views.controllers.custom.KoAlert;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MoneyDonatorEdit implements Initializable {

	@FXML
	private Label title;
	@FXML
	private TextField name;
	@FXML
	private TextField address;
	@FXML
	private TextField donation;
	@FXML
	private DatePicker date;
	@FXML
	private ComboBox<String> remark;
	private MoneyDonator m;
	private static Stage stage;
	private Consumer<MoneyDonator> handler;
	private MoneyDonatorService donatorService;

	public static void showView(MoneyDonator m, Consumer<MoneyDonator> handler) {
		try {
			FXMLLoader loader = new FXMLLoader(
					MoneyDonatorEdit.class.getResource("/com/zinkowin/tanyaung/views/MoneyDonatorEdit.fxml"));
			Parent view = loader.load();
			
			stage = new Stage();
			MoneyDonatorEdit controller = loader.getController();
			controller.handler = handler;
			controller.loadData(m);
			stage.setScene(new Scene(view));
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setResizable(false);
			stage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void save() {
		boolean donatorIsNull = null == m;
		try {
			if (donatorIsNull)
				m = new MoneyDonator();

			if (name.getText().isEmpty())
				throw new ApplicationException("Donator's name is empty! \nPlease enter name.");
			m.setName(name.getText());

			if (address.getText().isEmpty())
				throw new ApplicationException("Donator's address is empty! \nPlease enter address.");
			m.setAddress(address.getText());

			if (donation.getText().isEmpty())
				throw new ApplicationException("Donation (Cash) is empty! \nPlease enter a donation (Cash)");
			int d = Integer.valueOf(donation.getText());
			m.setDonation(d);

			if (date.getValue() == null)
				throw new ApplicationException("Donation date is empty! \nPlease select donation date.");
			m.setDonate_date(date.getValue());

			if (remark.getValue() == null)
				throw new ApplicationException("Donation type is empty! \nPlease select donation type.");
			m.setRemark(remark.getValue());

			if (donatorIsNull)
				donatorService.add(m);
			else
				donatorService.update(m);
			handler.accept(m);
			close();
		} catch (Exception e) {
			KoAlert alert = new KoAlert();
			alert.setTitle("WARNING");
			alert.setContentText(e.getMessage());
			alert.show();
		}

	}

	public void clear() {
		name.setText("");
		address.setText("");
		donation.setText("");
		date.setValue(null);
		remark.setValue(null);

		name.setPromptText("Enter name");
		address.setPromptText("Enter address");
		donation.setPromptText("Enter donation (cash)");
		date.setPromptText("Select donation date");
		remark.setPromptText("Select donation type");

	}

	public void close() {
		title.getScene().getWindow().hide();

	}

	public void loadData(MoneyDonator m) throws FileNotFoundException {
		this.m = m;
		if (m != null) {
		   name.setText(m.getName());
		   address.setText(m.getAddress());
		   donation.setText(String.valueOf(m.getDonation()));
		   date.setValue(m.getDonate_date());
		   remark.setValue(m.getRemark());
			title.setText("EDIT");
			stage.setTitle("MoneyDonator Edit");
			stage.getIcons().add(new Image(new FileInputStream("resources/images/edit.png")));
		} else {
			title.setText("ADD");
			//stage.setTitle("MoneyDonator Add");
			stage.getIcons().add(new Image(new FileInputStream("resources/images/add.png")));
		}

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		donatorService = MoneyDonatorService.getInstance();
		remark.getItems().addAll("Monthly Donator","Non Monthly Donator");

	}

}
