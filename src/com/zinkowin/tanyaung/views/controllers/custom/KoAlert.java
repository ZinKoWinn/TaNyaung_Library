package com.zinkowin.tanyaung.views.controllers.custom;

import com.zinkowin.tanyaung.views.controllers.AlertDialog;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class KoAlert {
	private Stage stage;
	private AlertDialog alertDialog;

	public KoAlert() {
		loadView();
	}

	public void setTitle(String titleType) {
		alertDialog.setTitle(titleType); 
	}

	public void setContentText(String contentTexts) {
		alertDialog.setContentText(contentTexts);
	}

	public KoAlert(String title, String contentText) {
		loadView();
		alertDialog.setTitle(title);
		alertDialog.setContentText(contentText);
	}

	public KoAlert(Stage stage) {
		loadView();
		stage.initOwner(stage);
	}

	public void loadView() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/zinkowin/tanyaung/views/AlertDialog.fxml"));
			Parent view = loader.load();
			alertDialog = loader.getController();
			stage = new Stage();
			stage.setScene(new Scene(view));
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.initStyle(StageStyle.UNDECORATED);
			stage.setResizable(false);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void show() {
		stage.show();
	}

}
