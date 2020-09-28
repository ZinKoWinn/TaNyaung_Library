package com.zinkowin.tanyaung.views.controllers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import com.zinkowin.tanyaung.models.InCome;
import com.zinkowin.tanyaung.services.InComeService;
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
import javafx.stage.Modality;
import javafx.stage.Stage;

public class InComeMoneyEdit implements Initializable {

	@FXML
	private Label title;
	@FXML
	private TextField type;
	@FXML
	private TextField amount;
	@FXML
	private DatePicker date;
	@FXML
	private TextField remark;

	private static Stage stage;
	private InCome i;
	private Consumer<InCome> handler;
	private InComeService incomeService;

	public static void showView(InCome i, Consumer<InCome> handler) {
		try {
			FXMLLoader loader = new FXMLLoader(
					InComeMoneyEdit.class.getResource("/com/zinkowin/tanyaung/views/InComeMoneyEdit.fxml"));
			Parent view = loader.load();
			stage = new Stage();
			InComeMoneyEdit controller = loader.getController();
			controller.handler = handler;
			controller.loadData(i);
			stage.setScene(new Scene(view));
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setResizable(false);
			stage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void save() {
		boolean incomeIsNull = null == i;
		try {
			if (incomeIsNull)
				i = new InCome();

			if (type.getText().isEmpty())
				throw new ApplicationException(
						"Type of cash received is empty! \nPlease enter a type of cash received.");
			i.setIncomeType(type.getText());

			if (amount.getText().isEmpty())
				throw new ApplicationException(
						"Amount of cash received is empty! \nPlease enter amount of cash received.");
			int a = Integer.valueOf(amount.getText());
			i.setIncomeAmount(a);

			if (date.getValue() == null)
				throw new ApplicationException("Date of cash received is empty! \nPlease select a date.");
			i.setIncomeDate(date.getValue());

			if (remark.getText().isEmpty())
				i.setRemark("");
			else
				i.setRemark(remark.getText());

			if (incomeIsNull)
				incomeService.add(i);
			else
				incomeService.update(i);

			handler.accept(i);
			close();

		} catch (Exception e) {
			KoAlert alert = new KoAlert();
			alert.setTitle("WARNING");
			alert.setContentText(e.getMessage());
			alert.show();
		}
	}

	public void clear() {
		type.setText("");
		amount.setText("");
		date.setValue(null);
		remark.setText("");

		type.setPromptText("Enter type of cash received");
		amount.setPromptText("Enter amount of cash");
		date.setPromptText("Select date");
		remark.setPromptText("Enter remark");

	}

	public void close() {
		title.getScene().getWindow().hide();
	}

	public void loadData(InCome i) throws FileNotFoundException {
		this.i = i;
		if (null != i) {
			type.setText(i.getIncomeType());
			amount.setText(String.valueOf(i.getIncomeAmount()));
			date.setValue(i.getIncomeDate());
			remark.setText(i.getRemark());
			title.setText("EDIT");
			stage.setTitle("InCome Money Edit");
			stage.getIcons().add(new Image(new FileInputStream("resources/images/edit.png")));
		} else {
			title.setText("ADD");
			stage.setTitle("InCome Money Add");
			stage.getIcons().add(new Image(new FileInputStream("resources/images/add.png")));
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		incomeService = InComeService.getInstance();

	}

}
