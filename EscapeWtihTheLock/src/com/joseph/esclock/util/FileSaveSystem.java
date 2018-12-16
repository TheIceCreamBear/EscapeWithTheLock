package com.joseph.esclock.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * The class responsible for ALL file IO
 * @author Joseph
 *
 */
public class FileSaveSystem {
	/**
	 * the file object of the file that contains prefrences
	 */
	private static File prefrencesFile;
	
	/**
	 * the file that holds a blank game
	 */
	private static File newGameFile;
	
	/**
	 * scanner for the preferences file
	 */
	private static Scanner prefrencesScanner;
	
	/**
	 * writer for the prefrences file
	 */
	private static PrintWriter prefrencesWriter;
	
	/**
	 * string representation of the file that was last saved/loaded
	 */
	private static String continueLocation;
	
	private static final String saveVersionString = "SaveSystemVersion:0.2";
	
	/**
	 * initializes the File system
	 */
	public static void init() {
		prefrencesFile = new File(System.getProperty("user.home") + "/TheDarknessBeyond/prefrences.dat");
		if (!prefrencesFile.exists()) {
			try {
				prefrencesFile.getParentFile().mkdirs();
				prefrencesFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try {
			prefrencesScanner = new Scanner(prefrencesFile);
			if (prefrencesScanner.hasNext()) {
				continueLocation = prefrencesScanner.nextLine();
			} else {
				continueLocation = System.getProperty("user.home") + "/TheDarknessBeyond/saves/default.tdbSave";
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * to be called after all object have been initialized. Finds the last saved game
	 * and loads it.
	 */
	public static void postInit() {
		
	}
	
	/**
	 * 
	 * @return - and array with all the possilbe save games to load
	 */
	public static File[] getPossibleLoadableFiles() {
		File tmp = new File(System.getProperty("user.home") + "/TheDarknessBeyond/saves/tmp.tmp");
		if (!tmp.exists()) {
			try {
				tmp.getParentFile().mkdirs();
				tmp.createNewFile();
				tmp.delete();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		File[] f = new File(System.getProperty("user.home") + "/TheDarknessBeyond/saves/").listFiles();
		if (f == null) {
			f = new File[0];
		}
		File[] f1 = new File[f.length + 1];
		for (int i = 0; i < f1.length; i++) {
			if (i == 0) {
				f1[i] = newGameFile;
			} else {
				f1[i] = f[i - 1];
			}
		}
		return f1;
	}
	
	/**
	 * continues the game from the last loaded/saved game
	 * @throws Exception
	 */
	public static void contineGame() throws Exception {
		loadGame(new File(continueLocation));
	}
	
	/**
	 * Loads the game form the given file
	 * @param f - the file
	 * @throws Exception
	 */
	public static void loadGame(File f) throws Exception {
		if (f == null || f.getAbsolutePath().lastIndexOf('.') == -1 || f.isDirectory()) {
			throw new RuntimeException("Error: INVALID SAVE LOCATION");
		}
		String path = f.getAbsolutePath();
		if (!path.substring(path.indexOf('.')).equals(".tdbSave")) {
			throw new RuntimeException("Error: INVALID SAVE FILE TYPE");
		}
		
		if (!f.exists()) {
			f.getParentFile().mkdirs();
			f.createNewFile();
		}
		
		markNewContinueLocation(f);
		
		// Init reading object
		Scanner scan = new Scanner(f);
		
		// Version Check
		String s = scan.nextLine();
		if (!s.equals(saveVersionString)) {
			scan.close();
			throw new RuntimeException("AHHHHHHHHHH: BAD FILE SAVE VERSION");
		}
		
		scan.nextLine(); // blank
		
		// StorageManager Resources Loading
		String label = scan.nextLine();
		if (!label.equals("STORAGE: RESOURCES")) {
			scan.close();
			throw new RuntimeException("Error: BAD FILE FORMAT");
		}
		
		
		scan.close();
	}
	
	/**
	 * save the game into the autosave file
	 * @throws Exception
	 */
	public static void autoSaveGame() throws Exception {
		saveGame(new File(System.getProperty("user.home") + "/TheDarknessBeyond/saves/autoSave.tdbSave"));
	}
	
	/**
	 * save the game in the save location with the given name
	 * @param name - the name
	 * @throws Exception
	 */
	public static void saveGame(String name) throws Exception {
		saveGame(new File(System.getProperty("user.home") + "/TheDarknessBeyond/saves/" + name + ".tdbSave"));
	}
	
	
	/**
	 * does the actual saving of the game
	 * @param f - the file
	 * @throws Exception
	 */
	private static void saveGame(File f) throws Exception {
		if (f == null || f.getAbsolutePath().lastIndexOf('.') == -1 || f.isDirectory()) {
			throw new RuntimeException("Error: INVALID SAVE LOCATION");
		}
		String path = f.getAbsolutePath();
		if (!path.substring(path.indexOf('.')).equals(".tdbSave")) {
			throw new RuntimeException("Error: INVALID SAVE FILE TYPE");
		}
		
		if (!f.exists()) {
			try {
				f.getParentFile().mkdirs();
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if (!path.contains("autoSave")) {
			markNewContinueLocation(f);
		}
		
		// Init writing object
		PrintWriter pw = new PrintWriter(new FileWriter(f), true);
		pw.println(saveVersionString);
		
		pw.println();
		
		pw.flush();
		pw.close();
	}
	
	/**
	 * marks the given file as the last edited file
	 * @param f - the file
	 * @throws IOException
	 */
	private static void markNewContinueLocation(File f) throws IOException {
		if (!f.exists()) {
			throw new IllegalArgumentException("Some programmer called this method on a non existant file. Report this bug and wait for an update.");
		}
		
		continueLocation = f.getAbsolutePath();
		prefrencesFile.delete();
		prefrencesFile.createNewFile();
		prefrencesWriter = new PrintWriter(new FileWriter(prefrencesFile), true);
		prefrencesWriter.println(continueLocation);
		prefrencesScanner.close();
		prefrencesScanner = new Scanner(prefrencesFile);
	}
}