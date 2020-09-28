package com.zinkowin.tanyaung.views.controllers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import com.zinkowin.tanyaung.models.OutCome;
import com.zinkowin.tanyaung.services.OutComeService;
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

public class OutComeMoneyEdit implements Initializable {
	@FXML
	private Label title;
	@FXML
	private TextField content;
	@FXML
	private TextField expenses;
	@FXML
	private DatePicker date;
	@FXML
	private TextField remark;
	private OutComeService outService;
	private static Stage stage;
	private OutCome o;
	private Consumer<OutCome> handler;

	public static void showView(OutCome o, Consumer<OutCome> handler) {
		try {
			FXMLLoader loader = new FXMLLoader(
					OutComeMoneyEdit.class.getResource("/com/zinkowin/tanyaung/views/OutComeMoneyEdit.fxml"));
			Parent view = loader.load();
			stage = new Stage();
			OutComeMoneyEdit controller = loader.getController();
			controller.handler = handler;
			controller.loadData(o);
			stage.setScene(new Scene(view));
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setResizable(false);
			stage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void save() {
		boolean outcomeIsNull = null == o;
		try {
			if (outcomeIsNull)
				o = new OutCome();

			if (content.getText().isEmpty())
				throw new ApplicationException("Content is empty! \nPlease enter a content");
			o.setContent(content.getText());

			if (expenses.getText().isEmpty())
				throw new ApplicationException("Expenses is empty! \nPlease enter expenses");
			int e = Integer.valueOf(expenses.getText());
			o.setExpenses(e);

			if (date.getValue() == null)
				throw new ApplicationException("Expenses date is empty! \nPlease select date.");
			o.setOutcomeDate(date.getValue());

			if (remark.getText().isEmpty())
				o.setRemark("");
			else
				o.setRemark(remark.getText());

			if (outcomeIsNull)
				outService.add(o);
			else
				outService.update(o);
			handler.accept(o);
			close();
		} catch (Exception e) {
			KoAlert alert = new KoAlert();
			alert.setTitle("WARNING");
			alert.setContentText(e.getMessage());
			alert.show();

		}

	}

	public void clear() {
		content.setText("");
		expenses.setText("");
		date.setValue(null);
		remark.setText("");

		content.setPromptText("Enter Content");
		expenses.setPromptText("Enter Expenses");
		date.setPromptText("Select Date");
		remark.setPromptText("Enter Remark");

	}

	public void close() {
		title.getScene().getWindow().hide();

	}

	public void loadData(OutCome o) throws FileNotFoundException {
		this.o = o;

		if (null != o) {
			content.setText(o.getContent());
			expenses.setText(String.valueOf(o.getExpenses()));
			date.setValue(o.getOutcomeDate());
			remark.setText(o.getRemark());
			title.setText("Edit");
			stage.setTitle("OutCome Eidt");
			stage.getIcons().add(new Image(new FileInputStream("resources/images/edit.png")));
		} else {
			title.setText("ADD");
			stage.setTitle("OutCome Add");
			stage.getIcons().add(new Image(new FileInputStream("resources/images/add.png")));
		}

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		outService = OutComeService.getInstance();

	}

}
