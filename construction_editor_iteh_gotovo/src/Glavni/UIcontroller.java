package Glavni;

import java.awt.Component;
import java.awt.MenuItem;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.JFileChooser;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;



import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

@SuppressWarnings("unused")
public class UIcontroller{

	@FXML
	private Text tekst;
	
	@FXML
	private TextField dateTextField;

	@FXML
	private TextField constructionTextField;
	
	@FXML
	private TextField zonaTextField;

	@FXML
	private Button searchButton;

	@FXML
	private Button replaceButton;

	@FXML
	private TableView<Datoteka> tableViewGlavni;

	@FXML
	TableColumn<Datoteka, String> stringColumn;

	private static ObservableList<Datoteka> observableListaStringova;
	private String tekstIzTekstFieldaKonstrukcija, tekstIzTekstFieldaZona, tekstIzTekstFieldaDatum;
	private List<Datoteka> listaStringova, listaUcitanihStringova;
	public Datoteka datoteka = new Datoteka();

	
	public void initialize() {
		boolean temp = false;
		String datum = new SimpleDateFormat("dd.M.yyyy").format(Calendar.getInstance().getTime());
		datoteka.setFileCheck(temp);
		dateTextField.setPromptText(datum);
		tableViewGlavni.setVisible(false);
		stringColumn.setSortable(false);
		Tooltip tooltipOpen = new Tooltip("Click here to choose a file.");
		searchButton.setTooltip(tooltipOpen);
		
		Tooltip tooltipReplace = new Tooltip("Click here to rename files.");
		replaceButton.setTooltip(tooltipReplace);
		
		Tooltip tooltipConstruction = new Tooltip("Type in construction here.");
		constructionTextField.setTooltip(tooltipConstruction);	
		
		Tooltip tooltipZone = new Tooltip("Type in zone here.");
		zonaTextField.setTooltip(tooltipZone);	
		
		Tooltip tooltipDate = new Tooltip("Type in date here, otherwise present day will be displayed.");
		dateTextField.setTooltip(tooltipDate);	
		}

	@FXML
	public void promjeni() {
		// The path of the file to open.
		boolean temp = datoteka.isFileCheck();
		if (!temp) {
			Alert alert = new Alert(AlertType.INFORMATION);
			Main.addDialogIconTo(alert);
			alert.setTitle("Information Dialog");
			alert.setHeaderText(null);
			alert.setContentText("Chose a file first!");
			alert.showAndWait();
		} else {
			try {
				ucitajText();
				// Loop za izmjenu texta
				List<String> fileContent = new ArrayList<>();
				List<String> provjera = new ArrayList<>();
				List<File> TBLFiles = findPaths();
				List<Path> listaPutanja = new ArrayList<>();
				for (int j = 0; j < TBLFiles.size(); j++) {
					Path putanja = Paths.get(TBLFiles.get(j).getPath());
					listaPutanja.add(putanja);
					System.out.println(listaPutanja.size() + " ovaj");
					fileContent = new ArrayList<>(Files.readAllLines(listaPutanja.get(j), StandardCharsets.UTF_8));
					// Araylist fajla koji nam sluzi da bi usporedili jesmo promjenili ili ne
					provjera = new ArrayList<>(Files.readAllLines(listaPutanja.get(j), StandardCharsets.UTF_8));
				for (int i = 0; i < fileContent.size() / 2; i++) {
					if(tekstIzTekstFieldaKonstrukcija.length() == 4 && tekstIzTekstFieldaKonstrukcija.matches("[0-9]+")) {
							// razlomljen drugi redak na 2 string i string na 3 dijela za rename
							String tabcons =  "\t" + "\t" + "\t" + ";costruzione";
							String prviSubredak2 = "00";
							String drugiRedprviDio = prviSubredak2 + tekstIzTekstFieldaKonstrukcija + tekstIzTekstFieldaZona;
							String cijelidrugiredak = drugiRedprviDio + tabcons;
							
							// razlomljen treci redak na 2 string i string na 2 dijela za rename
							String drugiSubredak3 = fileContent.get(3).substring(2, fileContent.get(3).length());
							String zamjena = tekstIzTekstFieldaKonstrukcija.substring(tekstIzTekstFieldaKonstrukcija.length() - 2);
							String cijelitreciredak = zamjena + drugiSubredak3;
							fileContent.set(2, cijelidrugiredak);
							fileContent.set(3, cijelitreciredak); 
							datoteka.setZone(tekstIzTekstFieldaZona);
							datoteka.setConstruction(tekstIzTekstFieldaKonstrukcija);
							
							//promjeni datum				
							String drugiDioRedaDatum =  "\t" + "\t" + "\t" +  ";data creazione";
							String renameOsmiRedMan = tekstIzTekstFieldaDatum + drugiDioRedaDatum;
							boolean provjeraDatuma = ProvjeraDatuma(tekstIzTekstFieldaDatum);
							// Upisi preimenovane strringove u array
							if(provjeraDatuma)
							fileContent.set(8, renameOsmiRedMan);	
							else {
								Alert alert = new Alert(AlertType.WARNING);
								Main.addDialogIconTo(alert);
								alert.setTitle("Warning!");
								alert.setHeaderText("Wrong input!");
								alert.setContentText("Choose a valid date or leave this field empty.");
								alert.showAndWait();
								break;
							}
					}				
				}
				Files.write(listaPutanja.get(j), fileContent, StandardCharsets.UTF_8);
				}
				// Usporeduje dva arraya u koji smo spremili file i govori dali smo izmjenili
				// file ili ne
				if (provjera.equals(fileContent) || !(tekstIzTekstFieldaKonstrukcija.length() == 4) || !(tekstIzTekstFieldaKonstrukcija.matches("[0-9]+"))) {
					System.out.println("Nisi promjenio unos");
					Alert alert = new Alert(AlertType.WARNING);
					Main.addDialogIconTo(alert);
					alert.setTitle("Warning!");
					alert.setHeaderText("Wrong input!");
					alert.setContentText("Choose a different construction.");
					alert.showAndWait();
				}
				// Pise u file
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			if(tekstIzTekstFieldaKonstrukcija.length() == 4 && tekstIzTekstFieldaKonstrukcija.matches("[0-9]+")) {
				renameFileova();
				renameOtvoreniFile();
			}
		}
		PlaceHolderUpdate();
		PostaviTekstNaTablicu();
		refreshajTablicu();
	}

	@FXML
	public void refreshajTablicu() {
		listaUcitanihStringova = dohvatiStringoveIzDatoteke();
		stringColumn.setCellValueFactory(new PropertyValueFactory<>("linija"));
		listaStringova = listaUcitanihStringova;
		observableListaStringova = FXCollections.observableArrayList(listaStringova);
		tableViewGlavni.setItems(observableListaStringova);
	}

	// Renamo ime file-ova
	public void renameFileova() {
		//rename file-ove u dat folderu
		File absolutePath = datoteka.getDatoteka();
		String absolutePathString = pretvoriFileuFolder(absolutePath);
		File dir = new File(absolutePathString);
		File[] filesInDir = null;
		filesInDir = dir.listFiles();
		File tempFile;
		List<File> fileovi = new ArrayList<>();
		for (int i = 0; i < filesInDir.length; i++) {
			fileovi.add(filesInDir[i]);
			String str = fileovi.get(i).getName();
			String drugapolovica = str.substring(2, str.length());
			String novistr = tekstIzTekstFieldaKonstrukcija.substring(tekstIzTekstFieldaKonstrukcija.length() - 2);
			String newPath = absolutePathString + "\\" + novistr + drugapolovica;
			tempFile = new File(newPath);
			boolean provjera = fileovi.get(i).renameTo(tempFile);
			
		}
		
		//rename file-ove u dgn folderu
		String drugiFolder = pretvoriFileuMapu(absolutePathString);
		drugiFolder += "\\DGN";
		File dir2 = new File(drugiFolder);
		File[] filesInDir2 = null;
		filesInDir2 = dir2.listFiles();
		File tempFile2;
		List<File> fileovi2 = new ArrayList<>();
		for (int i = 0; i < filesInDir2.length; i++) {
			fileovi2.add(filesInDir2[i]);
			String str = fileovi2.get(i).getName();
			String drugapolovica = str.substring(2, str.length());
			String novistr = tekstIzTekstFieldaKonstrukcija.substring(tekstIzTekstFieldaKonstrukcija.length() - 2);
			String newPath = drugiFolder + "\\" + novistr + drugapolovica;
			tempFile2 = new File(newPath);
			boolean provjera = fileovi2.get(i).renameTo(tempFile2);
			System.out.println(str + " changed to " + novistr + drugapolovica);
			System.out.println(provjera);
		}
		fileovi.clear();
		fileovi2.clear();
	}

	private void renameOtvoreniFile() {
		File orginalIme = datoteka.getDatoteka();
		File absolutePath = datoteka.getDatoteka();
		Path startiPath = Paths.get(absolutePath.toString());
		String absolutePathString = pretvoriFileuFolder(absolutePath);
		String str = orginalIme.getName();
		String drugapolovica = str.substring(2, str.length());
		String novistr = tekstIzTekstFieldaKonstrukcija.substring(tekstIzTekstFieldaKonstrukcija.length() - 2);
		String novoIme = absolutePathString + "\\" + novistr + drugapolovica;
		File promjenjenoIme = new File(novoIme);
		datoteka.setDatoteka(promjenjenoIme);
		datoteka.setPath(promjenjenoIme.toString());
	}

	// Cita stringove iz file-ova
	public List<Datoteka> dohvatiStringoveIzDatoteke() {
		List<Datoteka> listaLinija = new ArrayList<>();
		String drugaDatoteka = datoteka.getPath();
		try (BufferedReader in = new BufferedReader(new FileReader(drugaDatoteka))) {
			String redak;
			Datoteka file = null;
			while ((redak = in.readLine()) != null) {
				file = new Datoteka(redak);
				listaLinija.add(file);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return listaLinija;
	}

	// Ucita text iz text field-a i pretvorri u string sa metodom ispod
	public void ucitajText() {
		tekstIzTekstFieldaKonstrukcija = ucitajVrijednostIzTextConstruction(constructionTextField);
		tekstIzTekstFieldaDatum = ucitajVrijednostIzTextFieldaDatum(dateTextField);
		tekstIzTekstFieldaZona = ucitajVrijednostIzTextZona(zonaTextField);
		if (constructionTextField.getLength() == 0 && zonaTextField.getLength() == 0)
			provjeriUnos();
	}

	// Pretvara unos iz text fielda-a u string
	private String ucitajVrijednostIzTextConstruction(TextField tF) {
		String unosKonstrukcije = datoteka.getConstruction();
		if (tF.getText().trim().isEmpty() == false)
			return tF.getText();
		else
			return unosKonstrukcije;
	}
	
	// Pretvara unos iz text fielda-a u string
	private String ucitajVrijednostIzTextZona(TextField tF) {
		String unosZone = datoteka.getZone();
		if (tF.getText().trim().isEmpty() == false)
			return tF.getText();
		else
			return unosZone;
	}
	
	private String ucitajVrijednostIzTextFieldaDatum(TextField tF) {
		String datum = new SimpleDateFormat("dd.M.yyyy").format(Calendar.getInstance().getTime());
		if (tF.getText().trim().isEmpty() == false)
			return tF.getText();
		else
			return datum;
	}
	
	private List<File> findPaths() {
		File[] tbl = null;
		List<File> putanje = new ArrayList<>();
		// datoteka = searchFile();
		// path = pretvoriFileuFolder(datoteka);
		// String folder = path;
		String dirStr = pretvoriFileuFolder(datoteka.getDatoteka());
		File dir = new File(dirStr);
		
		if (dir.isDirectory()) {
			tbl = dir.listFiles(new FilenameFilter() {

				public boolean accept(File folder, String name) {
					return name.toLowerCase().endsWith(".tbl");
				}

			});
			// Pretvara File[] u String array
			for (int i = 0; i < tbl.length; i++) {
				putanje.add(tbl[i]);
				System.out.println(putanje.get(i));				
		}
			// Returna String array
			return putanje;
		} else
			return null;
	}

	// File koji izaberemo, od njega dobijemo folder u kojem se nalazi,da bi mogli
	// staviti u folder
	private String pretvoriFileuFolder(File file) {
		String putanja = file.toString();
		int kraj = putanja.lastIndexOf("\\");
		String rezultat = putanja.substring(0, kraj);
		return rezultat;
	}

	private String pretvoriFileuMapu(String file) {
		String putanja = file.toString();
		int kraj = putanja.lastIndexOf("\\");
		String rezultat = putanja.substring(0, kraj);
		return rezultat;
	}

	@FXML
	private void OpenMenu() throws ClassNotFoundException, InstantiationException, IllegalAccessException,
			UnsupportedLookAndFeelException {
		UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		int returnVal = 0;
		FileChooser fileChooser = new FileChooser();
		File fileC = fileChooser.showOpenDialog(null);
		System.out.println(fileC);
		// In response to a button click:
		if (fileC.exists()) {
			tekst.setVisible(false);
			tableViewGlavni.setVisible(true);
			datoteka.setDatoteka(fileC);
			datoteka.setPath(fileC.toString());
			refreshajTablicu();
			PostaviTekstNaTablicu();
			datoteka.setFileCheck(true);
			PrviPlaceHolder();
		}
	}
	/*
	public void DragDrop() {
		
		tekst.setOnDragDetected(new EventHandler<MouseEvent>() {
		    public void handle(MouseEvent event) {

		        Dragboard db = tekst.startDragAndDrop(TransferMode.ANY);
		        

		        ClipboardContent content = new ClipboardContent();
		        content.putString(tekst.getText());
		        db.setContent(content);
		        
		        event.consume();
		    }
		});
	}
	*/
	
	/*@FXML
	public void DragAndDropFile() {
		
		tableViewGlavni.setOnDragOver(new EventHandler<DragEvent>() {
            @Override
            public void handle(DragEvent event) {
                Dragboard db = event.getDragboard();
                if (db.hasFiles()) {
                    event.acceptTransferModes(TransferMode.COPY);
                } else {
                    event.consume();
                }
            }
        });
		
			tableViewGlavni.setOnDragDropped(new EventHandler<DragEvent>() {
	            @Override
	            public void handle(DragEvent event) {
	                Dragboard db = event.getDragboard();
	                File file = db.getFiles().get(0);
	                event.setDropCompleted(true);
	                event.consume();
	        		if (file.exists()) {
	        			tekst.setVisible(false);
	        			tableViewGlavni.setVisible(true);
	        			datoteka.setDatoteka(file);
	        			datoteka.setPath(file.toString());
	        			refreshajTablicu();
	        			PostaviTekstNaTablicu();
	        			datoteka.setFileCheck(true);
	        			PrviPlaceHolder();
	        		}
	            } 
	        });  
			tekst.setOnDragOver(new EventHandler<DragEvent>() {
	            @Override
	            public void handle(DragEvent event) {
	                Dragboard db = event.getDragboard();
	                if (db.hasFiles()) {
	                    event.acceptTransferModes(TransferMode.COPY);
	                } else {
	                    event.consume();
	                }
	            }
	        });
			tekst.setOnDragDropped(new EventHandler<DragEvent>() {
	            @Override
	            public void handle(DragEvent event) {
	                Dragboard db = event.getDragboard();
	                List<File> fileovi = db.getFiles();
	                File file = fileovi.get(0);
	                event.setDropCompleted(true);
	                event.consume();
	        		if (file.exists()) {
	        			tekst.setVisible(false);
	        			tableViewGlavni.setVisible(true);
	        			datoteka.setDatoteka(file);
	        			datoteka.setPath(file.toString());
	        			refreshajTablicu();
	        			PostaviTekstNaTablicu();
	        			datoteka.setFileCheck(true);
	        			PrviPlaceHolder();
	        		}
	            } 
	        }); 
	}*/
	
	private void PostaviTekstNaTablicu() {
		File putanjaFile = datoteka.getDatoteka();
		String putanja = putanjaFile.getName();
		stringColumn.setText(putanja);
	}
	
	private void PlaceHolderUpdate() {
		File tempath = datoteka.getDatoteka();
		Path putanjaFile = Paths.get(tempath.toString());
		try {
			List<String> fileContent = new ArrayList<>(Files.readAllLines(putanjaFile, StandardCharsets.UTF_8));
			String konstrukcija = fileContent.get(2).substring(2, 6);
			int kraj = fileContent.get(2).lastIndexOf("\t");
			String zona = fileContent.get(2).substring(6, kraj);
			constructionTextField.setPromptText(konstrukcija);
			zonaTextField.setPromptText(zona);
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	private void PrviPlaceHolder() {
		File tempath = datoteka.getDatoteka();
		Path putanjaFile = Paths.get(tempath.toString());
		try {
			List<String> fileContent = new ArrayList<>(Files.readAllLines(putanjaFile, StandardCharsets.UTF_8));
			String konstrukcija = fileContent.get(2).substring(2, 6);
			int kraj = fileContent.get(2).lastIndexOf("\t");
			String zona = fileContent.get(2).substring(6, kraj);
			constructionTextField.setPromptText(konstrukcija);
			zonaTextField.setPromptText(zona);
			datoteka.setZone(zona);
			datoteka.setConstruction(konstrukcija);
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	@FXML
	private void PritisnutEnter(KeyEvent keyEvent) {
	    if (keyEvent.getCode() == KeyCode.ENTER) {
	        promjeni();
	    }
	}

    public static boolean ProvjeraDatuma(String Datum) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.M.yyyy");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(Datum.trim());
        } catch (ParseException pe) {
        	pe.printStackTrace();
            return false;
        }
        return true;
    }

	public void provjeriUnos() {
		Alert alert = new Alert(AlertType.WARNING);
		Main.addDialogIconTo(alert);
		alert.setTitle("Warning!");
		alert.setHeaderText("No input found!");
		alert.setContentText("Type in corrent information.");
		alert.showAndWait();
	}
	
	@FXML
	public void aboutinfo() {
		Alert alert = new Alert(AlertType.INFORMATION);
		Main.addDialogIconTo(alert);
		alert.setTitle("Information about software");
		alert.setHeaderText("Software made by:");
		alert.setContentText("Mladen Sekusak @email: mladen.sekusak93@gmail.com" + "\n" + "Vjekoslav Krainovic @email:"
				+ " vjekokr@hotmail.com" + "\n" + "Version: 1.01" + "\n" + "Date: 17.3.2019" + "\n" + "Copyright © 2019 Construction Editor");
		alert.showAndWait();
	}

}