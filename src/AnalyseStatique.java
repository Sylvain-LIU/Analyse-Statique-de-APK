import java.io.*;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Enumeration;
import java.util.Scanner;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

public class AnalyseStatique {

	// Buffering character-input stream
	public static BufferedReader bufread;
	private static final int BUFFER = 1024;

	private static String[] Widgetstrings = { "TextView", // TextView //1
			"Button", // Button- //2
			"ToggleButton", // Switch button- //3
			"CheckBox", // Checkbox- //4
			"RadioButton", // Single box- //5
			"CheckedTextView", // Select the text //6
			"Spinner", // Drop-down list- //7
			"SeekBar", // Fine tuning //8
			"QuickContactBadge", // Contacts //9
			"RadioGroup", // Radio button group- //10
			"RatingBar", // Bar Rating- //11
			"Switch", // Switch- /12
			"EditText", // Edit box- //13
			"AutoCompleteTextView", // Auto-complete text edit box- //14
			"MultiAutoCompleteTextView", // Multi-line auto-complet text edit
											// box- //15
			"ImageView", // Picture view //16
			"ImageButton", // Picture button //17
			"Gallery", // Gallery //18
			"MediaController", // Media controller //19
			"VideoView", // Video view //20
			"TimePicker", // Time picker- //21
			"DatePicker", // Date picker- //22
			"CalendarView", // Clendar view- //23
			"Chronometer", // Chronometer //24
			"ImageSwitcher", // Picture swotcher //25
			"AnalogClock", // Analog clock //26
			"DigtalClock", // Digtal clock //27
			"TextClock" // Text clock //28
	};

	// Write String to File
	public void WriteStringToFile(String filePath, String s) throws IOException {
		try {

			FileWriter fileWriter = new FileWriter(filePath, true);
			BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
			bufferedWriter.write(s);
			bufferedWriter.close();

		} catch (FileNotFoundException e) {

			e.printStackTrace();

		}

	}

	// Judgment Method
	// If the first parameter S1 contains S2?
	public static boolean isContain(String S1, String S2) {

		return S1.contains(S2);

	}

	/**
	 * Unzip the apk file
	 * 
	 * @param fileName
	 *            To unzip the file name, including path, such as:
	 *            "c: \\ test.zip"
	 * @param filePath
	 *            After extracting the file storage path such as: "c: \\ temp"
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public static void unZip(String fileName, String filePath) throws Exception {
		ZipFile zipFile = new ZipFile(fileName);
		Enumeration emu = zipFile.getEntries();

		while (emu.hasMoreElements()) {
			ZipEntry entry = (ZipEntry) emu.nextElement();
			if (entry.isDirectory()) {
				new File(filePath + entry.getName()).mkdirs();
				continue;
			}
			BufferedInputStream bis = new BufferedInputStream(zipFile.getInputStream(entry));

			File file = new File(filePath + entry.getName());
			File parent = file.getParentFile();
			if (parent != null && (!parent.exists())) {
				parent.mkdirs();
			}
			FileOutputStream fos = new FileOutputStream(file);
			BufferedOutputStream bos = new BufferedOutputStream(fos, BUFFER);

			byte[] buf = new byte[BUFFER];
			int len = 0;
			while ((len = bis.read(buf, 0, BUFFER)) != -1) {
				fos.write(buf, 0, len);
			}
			bos.flush();
			bos.close();
			bis.close();
		}
		zipFile.close();
	}

	// Delete Folder
	// @param folderPath:Folder full absolute path
	public static void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); // Delete all the contents inside the folder
			String filePath = folderPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			myFilePath.delete(); // Delete empty folder
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Delete all the files in the specified folder
	// @param path:Folder full absolute path
	public static boolean delAllFile(String path) {
		boolean flag = false;
		File file = new File(path);
		if (!file.exists()) {
			return flag;
		}
		if (!file.isDirectory()) {
			return flag;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);// Delete files inside the
														// folder
				delFolder(path + "/" + tempList[i]);// Then delete the empty
													// folder
				flag = true;
			}
		}
		return flag;
	}

	public static void main(String[] args) throws Exception {

		StringBuffer buffer0 = new StringBuffer();
		StringBuffer buffer1 = new StringBuffer();
		StringBuffer buffer2 = new StringBuffer();
		StringBuffer buffer3 = new StringBuffer();

		String separator = File.separator;
		String filename_widget = "WidgetsList.txt";

		// Input the path of your application(apk)
		Scanner scanner = new Scanner(System.in);
		System.out.println("Input the path of your application(apk):");
		String srcPath = scanner.nextLine();
		scanner.close();

		File srcFile = new File(srcPath);
		String parentPath = srcFile.getParent(); // Source Files Directory
		String fileName = srcFile.getName(); // Source file name

		// Source file name without .apk
		String fileNameNoApk = fileName.substring(0, fileName.length() - 4);

		// Output source file name
		System.out.println(fileName);

		// Extraction path
		String sourcePath = buffer0.append(parentPath).append(separator + "Temp" + separator).toString();

		// Extraction
		unZip(srcPath, sourcePath);

		// Determining whether the widget has been written to the file.
		int Index_widget[] = {

				0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0

		};

		// Static Analysis Temporary Files
		String filePath1 = buffer1.append(parentPath).append(separator + "Temp" + separator + "TEMP.txt").toString();

		// File of widgets list
		String filePath2 = buffer2.append(parentPath).append(separator + fileNameNoApk + separator + filename_widget)
				.toString();

		// Save directory widgets list
		String filePath3 = buffer2.append(parentPath).append(separator + fileNameNoApk + separator).toString();
		// New folder to save the results
		File file = new File(filePath3, filename_widget);
		file.mkdirs();

		// Static analysis layout file directory
		String sourceRead = buffer3.append(parentPath)
				.append(separator + "Temp" + separator + "res" + separator + "layout").toString();

		File fileDir = new File(sourceRead);

		File[] textFiles = fileDir.listFiles();

		for (int j = 0; j < textFiles.length; j++) {
			if (textFiles[j].isFile() && textFiles[j].getName().endsWith(".xml")) {

				String filePath = textFiles[j].getAbsolutePath();

				BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)));

				String str;

				while ((str = br.readLine()) != null) {

					// Remove all spaces and carriage returns inside the file
					// layout.
					String s1 = str.replaceAll("\\W", "");

					// Output byte code we have analyzed
					// System.out.println(s1);

					// The contents of static analysis written to a temporary
					// file.
					new AnalyseStatique().WriteStringToFile(filePath1, s1);
					// Recognition the widget in the application's layout file.
					for (int i = 0; i < 28; i++) {
						if (isContain(s1, Widgetstrings[i]) && (Index_widget[i] < 1)) {
							new AnalyseStatique().WriteStringToFile(filePath2, Widgetstrings[i] + "\r\n");
							Index_widget[i] += 1;
						}
					}
				}
				br.close();
			}
		}

		System.out.println("We analysed a total of " + textFiles.length + " layout files" + "\r\n");

		// Delete the temporary folder
		delFolder(sourcePath);
		System.out.println("Widgets list is stored in the path:" + "\r\n" + filePath2);

	}
}