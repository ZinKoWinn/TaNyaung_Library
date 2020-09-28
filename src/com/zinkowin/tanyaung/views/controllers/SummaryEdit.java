package com.zinkowin.tanyaung.views.controllers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import com.zinkowin.tanyaung.models.Summary;
import com.zinkowin.tanyaung.services.SummaryService;
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

public class SummaryEdit implements Initializable {
	
	@FXML
	private Label title;
	@FXML
	private TextField remark;
	@FXML
	private TextField income;
	@FXML
	private TextField outcome;
	@FXML
	private DatePicker date;

	private static Stage stage;
	private Summary s;
	private Consumer<Summary> handler;
	private SummaryService sumService;

	public static void showView(Summary s, Consumer<Summary> handler) {
		try {
			FXMLLoader loader = new FXMLLoader(
					SummaryEdit.class.getResource("/com/zinkowin/tanyaung/views/SummaryEdit.fxml"));
			Parent view = loader.load();
			stage = new Stage();
			SummaryEdit controller = loader.getController();
			controller.handler = handler;
			controller.loadData(s);
			stage.setScene(new Scene(view));
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setResizable(false);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void save() {
		boolean summaryIsNull = null == s;
		try {
			if (summaryIsNull)
				s = new Summary();

			if (income.getText().isEmpty())
				throw new ApplicationException("Amount of income is empty! \nPlease enter amount of income.");
			int i = Integer.valueOf(income.getText());
			s.setIncome(i);

			if (outcome.getText().isEmpty())
				throw new ApplicationException("Amount of outcome is empty! \nPlease enter amount of outcome.");
			int o = Integer.valueOf(outcome.getText());
			s.setOutcome(o);

			if (date.getValue() == null)
				throw new ApplicationException("Date is empty! \nPlease select a date.");
			s.setSumdate(date.getValue());
			
			if (remark.getText().isEmpty()) s.setRemark("");
			else s.setRemark(remark.getText());

			int r = i - o;
			s.setRemain(r);

			if (summaryIsNull)
				sumService.add(s);
			else
				sumService.update(s);

			handler.accept(s);
			close();

		} catch (Exception e) {
			KoAlert alert = new KoAlert();
			alert.setTitle("WARNING");
			alert.setContentText(e.getMessage());
			alert.show();
		}
	}

	public void clear() {
		remark.setText("");
		income.setText("");
		outcome.setText("");
		date.setValue(null);

		remark.setPromptText("Enter Content");
		income.setPromptText("Enter amount of income");
		outcome.setPromptText("Enter amount of outcome");
		date.setPromptText("Select Date");
	}

	public void close() {
		title.getScene().getWindow().hide();
	}

	public void loadData(Summary s) throws FileNotFoundException {
		this.s = s;
		if (null != s) {
			remark.setText(s.getRemark());
			income.setText(String.valueOf(s.getIncome()));
			outcome.setText(String.valueOf(s.getOutcome()));
			date.setValue(s.getSumdate());
			title.setText("EDIT");
			stage.setTitle("Summary Edit");
			stage.getIcons().add(new Image(new FileInputStream("resources/images/edit.png")));
		} else {
			title.setText("ADD");
			stage.setTitle("Summary Add");
			stage.getIcons().add(new Image(new FileInputStream("resources/images/add.png")));
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		sumService = SummaryService.getInstance();

	}

}
