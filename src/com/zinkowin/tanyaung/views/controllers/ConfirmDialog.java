package com.zinkowin.tanyaung.views.controllers;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class ConfirmDialog implements Initializable {
	@FXML
	private Label title;
	@FXML
	private Label contentText;
	@FXML
	private Button cancle;
	@FXML
	private Button confirm;

	public void setTitle(String titleType) {
		title.setText(titleType);
	}

	public void setContentText(String contentTexts) {
		contentText.setText(contentTexts);
	}

	public void setOnCancled(EventHandler<ActionEvent> handler) {
		cancle.setOnAction(handler);
	}

	public void setOnConfirmed(EventHandler<ActionEvent> handler) {
		confirm.setOnAction(handler);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		cancle.setOnAction(event -> title.getScene().getWindow().hide());

	}

}
