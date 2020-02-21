package Glavni;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;


@SuppressWarnings("unused")
public class Datoteka {

	private String linija, path, zone, construction;
	private File datoteka;
	private boolean fileCheck;
	

	public String getLinija() {
		return linija;
	}

	public void setLinija(String linija) {
		this.linija = linija;
	}

	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public String getConstruction() {
		return construction;
	}

	public void setConstruction(String construction) {
		this.construction = construction;
	}

	public boolean isFileCheck() {
		return fileCheck;
	}

	public void setFileCheck(boolean fileCheck) {
		this.fileCheck = fileCheck;
	}
	
	
	public File getDatoteka() {
		return datoteka;
	}

	public void setDatoteka(File datoteka) {
		this.datoteka = datoteka;
	}

	public Datoteka(String linija) {
		super();
		this.linija = linija;
	}

	public Datoteka() {	
	}

}

