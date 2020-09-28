package com.zinkowin.tanyaung.views.controllers.custom;

import com.zinkowin.tanyaung.views.controllers.ConfirmDialog;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class KoConfirm {
	private ConfirmDialog confirmDialog;
	private Stage stage;

	public KoConfirm() {
		loadView();
	}

	public void setTitle(String titleType) {
		confirmDialog.setTitle(titleType);
	}

	public void setContentText(String contentTexts) {
		confirmDialog.setContentText(contentTexts);
	}
	
	public void setOnCancled(EventHandler<ActionEvent> handler) {
		confirmDialog.setOnCancled(handler);
	}
	
	public void setOnConfirmed(EventHandler<ActionEvent> handler) {
		confirmDialog.setOnConfirmed(handler);
	}

	public KoConfirm(String title, String contentText) {
		loadView();
		confirmDialog.setTitle(title);
		confirmDialog.setContentText(contentText);
	}

	public KoConfirm(Stage stage) {
		loadView();
		stage.initOwner(stage);

	}

	public void loadView() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zinkowin/tanyaung/views/ConfirmDialog.fxml"));
			Parent view = loader.load();
			confirmDialog = loader.getController();
			stage = new Stage();
			stage.setScene(new Scene(view));
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.initStyle(StageStyle.UNDECORATED);
			stage.setResizable(false);
			stage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void show() {
		stage.show();
	}
	
	public void close() {
		stage.close();
	}

}
