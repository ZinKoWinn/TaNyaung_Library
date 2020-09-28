package com.zinkowin.tanyaung.views.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class AlertDialog {
	@FXML
	private Label title;
	@FXML
	private Label contentText;

	public void setTitle(String titletype) {
		title.setText(titletype);
	}

	public void setContentText(String contentTexts) {
		contentText.setText(contentTexts);
	}

	public void ok() {
		title.getScene().getWindow().hide();

	}

}
