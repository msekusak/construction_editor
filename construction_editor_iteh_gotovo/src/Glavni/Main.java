package Glavni;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;


@SuppressWarnings("unused")
public class Main extends Application{
	
	
	
	@Override
	public void start(Stage primaryStage) {
		try {
			BorderPane root = (BorderPane)FXMLLoader.load(getClass().getResource("ui.fxml"));
			Scene scene = new Scene(root,650, 600);
			primaryStage.setTitle("   Construction Editor");
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.getIcons().add(new Image("/icons/Folder-icon.png"));
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void addDialogIconTo(Alert alert) {
	    // Add custom Image to Dialog's title bar
	    final Image APPLICATION_ICON = new Image("/icons/Folder-icon.png");
	    Stage dialogStage = (Stage) alert.getDialogPane().getScene().getWindow();
	    dialogStage.getIcons().add(APPLICATION_ICON);

	    // Add custom ImageView to Dialog's header pane.        
	    /* final ImageView DIALOG_HEADER_ICON = new ImageView("file:Folder-icon.png");
	    DIALOG_HEADER_ICON.setFitHeight(48); // Set size to API recommendation.
	    DIALOG_HEADER_ICON.setFitWidth(48);
	    alert.getDialogPane().setGraphic(DIALOG_HEADER_ICON);   */                    
	    }
	
	
	public static void main(String[] args) {
			launch(args);

	}
}