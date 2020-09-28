package com.zinkowin.tanyaung.views.controllers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import com.zinkowin.tanyaung.models.Member;
import com.zinkowin.tanyaung.services.MemberService;
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
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MemberEdit implements Initializable {
	@FXML
	private TextField name;
	@FXML
	private TextField cardnumber;
	@FXML
	private TextField occupation;
	@FXML
	private TextField phonenumber;
	@FXML
	private TextField address;
	@FXML
	private Label title;
	@FXML
	private DatePicker date;
	@FXML
	private ImageView header;
	
	private static Stage stage;
	private Consumer<Member> handler;
	private Member member;
	private MemberService memberService;

	static void showView(Member member, Consumer<Member> handler) {
		try {
			FXMLLoader loader = new FXMLLoader(
					MemberEdit.class.getResource("/com/zinkowin/tanyaung/views/MemberEdit.fxml"));
			Parent view = loader.load();

			stage = new Stage();
			MemberEdit control = loader.getController();
			control.handler = handler;
			control.setData(member);

			stage.setScene(new Scene(view));
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setResizable(false);
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void save() {

		boolean memberIsNull = member == null;

		try {
			if (memberIsNull) {
				member = new Member();
			}

			member.setName(name.getText());
			if (name.getText().isEmpty()) {
				throw new ApplicationException("Member's name is empty! \nPlease enter name.");
			}

			member.setCard_number(cardnumber.getText());
			if (cardnumber.getText().isEmpty()) {
				throw new ApplicationException("Member's card number is empty! \nPlease enter card number!");
			}

			member.setOccupation(occupation.getText());
			if (occupation.getText().isEmpty()) {
				throw new ApplicationException("Member's occupation is empty! \nPlease enter occupation.");
			}

			member.setPh_number(phonenumber.getText());
			if (phonenumber.getText().isEmpty()) {
				throw new ApplicationException("Member's phone number is empty! \nPlease enter phone number.");
			}

			member.setAddress(address.getText());
			if (address.getText().isEmpty()) {
				throw new ApplicationException("Member's address is empty! \nPlease enter address.");
			}
			member.setDate(date.getValue());
			if (null == date.getValue()) {
				throw new ApplicationException("Membership date is empty! \nPlease select date!");
			}

			if (memberIsNull)
				memberService.checkAndAdd(member);
			else
				memberService.update(member);

			handler.accept(member);
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
		cardnumber.setText("");
		occupation.setText("");
		phonenumber.setText("");
		address.setText("");
		date.setValue(null);

		name.setPromptText("Enter Name");
		cardnumber.setPromptText("Enter Card Number");
		occupation.setPromptText("Enter Occupation");
		phonenumber.setPromptText("Enter Phone Number");
		address.setPromptText("Enter Address");
		date.setPromptText("Select Date");

	}

	public void close() {
		title.getScene().getWindow().hide();
	}

	public void setData(Member member) throws FileNotFoundException {
		this.member = member;

		if (null != member) {
			name.setText(member.getName());
			cardnumber.setText(member.getCard_number());
			occupation.setText(member.getOccupation());
			phonenumber.setText(member.getPh_number());
			address.setText(member.getAddress());
			date.setValue(member.getDate());
			title.setText("EDIT MEMBER");
			stage.setTitle("Edit");
			stage.getIcons().add(new Image(new FileInputStream("resources/images/edit.png")));
		} else {
			title.setText("ADD");
			stage.setTitle("ADD");
			stage.getIcons().add(new Image(new FileInputStream("resources/images/add.png")));
		}

	}
	
	private void setIconImage() throws FileNotFoundException {
		header.setImage(new Image(new FileInputStream("resources/images/lib.png")));
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		memberService = MemberService.getInstance();
		try {
			setIconImage();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
