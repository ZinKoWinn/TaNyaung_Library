package com.zinkowin.tanyaung.views.controllers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;

import com.zinkowin.tanyaung.models.User;
import com.zinkowin.tanyaung.services.UserService;
import com.zinkowin.tanyaung.utils.ApplicationException;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class LoginForm implements Initializable{
	@FXML
	private ImageView icon;
	@FXML
	private Label message;
	@FXML
	private TextField name;
	@FXML 
	private PasswordField password;
	private UserService user;
	
	public void login() {
		try {
			
			if(name.getText().isEmpty()) throw new ApplicationException("User name is empty! Please enter a user name.");
			if(password.getText().isEmpty()) throw new ApplicationException("User password is empty! Please enter a password.");
			
			User u = user.login(name.getText(), password.getText());
			
			if(null == u) throw new ApplicationException("User not found");
			if(!u.getName().equals(name.getText())) throw new ApplicationException("Please enter correct user name!");
			if(!u.getPassword().equals(password.getText())) throw new ApplicationException("Please enter correct password!");
			MainView.showView();
			close();
		} catch (Exception e) {
			e.printStackTrace();
			message.setText(e.getMessage());
		}
	}
	
	public void close() {
		name.getScene().getWindow().hide();
	}
	
	

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		user = UserService.getInstance();
		
		try {
			icon.setImage(new Image(new FileInputStream("resources/images/icon.png")));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
		
	}


