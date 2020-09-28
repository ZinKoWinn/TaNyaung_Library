package com.zinkowin.tanyaung.views.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import com.zinkowin.tanyaung.models.Category;
import com.zinkowin.tanyaung.services.CategoryService;
import com.zinkowin.tanyaung.utils.ApplicationException;
import com.zinkowin.tanyaung.views.controllers.custom.CategoryBox;
import com.zinkowin.tanyaung.views.controllers.custom.KoAlert;
import com.zinkowin.tanyaung.views.controllers.custom.KoConfirm;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.FlowPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class CategoryList implements Initializable {
	@FXML
	private TextField name;
	@FXML
	private ImageView addimg;
	@FXML
	private ImageView uploadimg;
	@FXML
	private FlowPane catBox;
	private CategoryService catService;

	public void add() {
		try {
			Category c = new Category();
			if(name.getText().isEmpty()) throw new ApplicationException("Category name is empty! \n Please enter a name.");
			else
			c.setName(name.getText());
			catService.checkAndAdd(c);
			search();

		} catch (Exception e) {
			KoAlert alert = new KoAlert();
			alert.setTitle("WARNING");
			alert.setContentText(e.getMessage());
			alert.show();
		}

	}

	public void upload() {
		try {
			FileChooser chooser = new FileChooser();
			chooser.setInitialDirectory(new File(System.getProperty("user.home"), "Desktop"));
			chooser.setSelectedExtensionFilter(new ExtensionFilter("Category", "*.txt", "*.csv", "*.tsv"));
			File file = chooser.showOpenDialog(name.getScene().getWindow());
			if(file == null) throw new ApplicationException("File is empty! Please choose a file. \n (eg, extension with  .txt, .csv, .tsv)");
			catService.upload(file);
			search();
			
		} catch (Exception e) {
			KoAlert alert = new KoAlert();
			alert.setTitle("WARNING");
			alert.setContentText(e.getMessage());
			alert.show();
		}
		
	}

	public void search() {
		catBox.getChildren().clear();
		List<Category> list = catService.findByName(name.getText());
		list.stream().map(c->new CategoryBox(c,this::doDelete)).forEach(catBox.getChildren()::addAll);

	}
	
	public  void doDelete(Category c) {
		KoConfirm confirm = new KoConfirm();
		confirm.setTitle("CONFIRMATION");
		confirm.setContentText("Are you sure to delete this category?");
		confirm.show();
		
		confirm.setOnCancled(e->confirm.close());
		
		confirm.setOnConfirmed(e-> {
			catService.delete(c);
			confirm.close();
			search();
		});
		
		
	}
	public void clear() {
		name.setText("");
		name.setPromptText("Enter name");
	}
	
	private void setIconImage() throws FileNotFoundException {
		addimg.setImage(new Image(new FileInputStream("resources/images/add.png")));
		uploadimg.setImage(new Image(new FileInputStream("resources/images/upload.png")));
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		catService = CategoryService.getInstance();
		search();
		name.textProperty().addListener((a, b, c) -> search());
		name.setOnKeyPressed(p -> {
			if (p.getCode().equals(KeyCode.ENTER)) {
				add();
			}
		});
		
		try {
			setIconImage();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	

}
