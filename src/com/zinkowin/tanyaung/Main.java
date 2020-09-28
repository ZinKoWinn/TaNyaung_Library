package com.zinkowin.tanyaung;
	
import com.zinkowin.tanyaung.views.controllers.LoginForm;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class Main extends Application {
	@Override
	public void start(Stage stage) {
		try {
			Parent root = FXMLLoader.load(LoginForm.class.getResource("/com/zinkowin/tanyaung/views/LoginForm.fxml"));
			Scene scene = new Scene(root);
			stage.setScene(scene);
		    stage.setResizable(false);
		    stage.centerOnScreen();
			stage.initStyle(StageStyle.UNDECORATED);
			stage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
