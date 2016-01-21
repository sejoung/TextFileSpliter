package kr.co.killers.text;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TextFileSpliter {

	private static final Logger logger = LoggerFactory.getLogger(TextFileSpliter.class);

	private String fileName;
	private JFrame frame;
	private JTextField openFile;
	private JTextField saveDir;
	private JTextField textLineNumber;
	private JTextField spliteLineNumber;
	private Properties props;
	private int totalcnt;

	public static void main(String[] args) {

		TextFileSpliter textFile = new TextFileSpliter();
		textFile.init();
		textFile.go();
		
		logger.info("info");
		
		
	}

	public void init() {
		InputStream is = this.getClass().getClassLoader().getResourceAsStream("system.properties");

		props = new Properties();
		try {
			props.load(is);
		} catch (IOException e) {
			logger.error("Properties Load failed ", e);
		}
	}

	public void go() {

		this.frame = new JFrame(props.getProperty("top"));
		this.frame.setDefaultCloseOperation(3);
		JPanel panel = new JPanel();

		panel.setLayout(new BoxLayout(panel, 1));
		JLabel textFileLineNumber = new JLabel(props.getProperty("label"));
		JLabel signLineInput = new JLabel(props.getProperty("label1"));
		JButton openButton = new JButton(props.getProperty("label2"));
		JButton saveDirButton = new JButton(props.getProperty("btn"));
		JButton spliteButton = new JButton(props.getProperty("btn1"));
		JButton clearButton = new JButton(props.getProperty("btn2"));
		this.openFile = new JTextField(20);
		this.saveDir = new JTextField(20);
		this.textLineNumber = new JTextField(20);
		this.spliteLineNumber = new JTextField(20);
		this.openFile.setEnabled(false);
		this.saveDir.setEnabled(false);
		this.textLineNumber.setEnabled(false);

		openButton.addActionListener(new TextFileSpliter.openDirListener());
		saveDirButton.addActionListener(new TextFileSpliter.saveDirListener());
		spliteButton.addActionListener(new TextFileSpliter.spliteFileListener());
		clearButton.addActionListener(new TextFileSpliter.clearFileListener());

		panel.add(openButton);
		panel.add(this.openFile);
		panel.add(textFileLineNumber);
		panel.add(this.textLineNumber);
		panel.add(signLineInput);
		panel.add(this.spliteLineNumber);
		panel.add(saveDirButton);
		panel.add(this.saveDir);
		panel.add(clearButton);
		panel.add(spliteButton);

		this.frame.getContentPane().add("Center", panel);
		this.frame.setSize(500, 300);
		this.frame.setVisible(true);
	}

	public class openDirListener implements ActionListener {
		public openDirListener() {
		}

		public void actionPerformed(ActionEvent ev) {
			JFileChooser fileOpen = new JFileChooser();
			fileOpen.showOpenDialog(TextFileSpliter.this.frame);
			String path = fileOpen.getSelectedFile().getPath();
			TextFileSpliter.this.openFile.setText(path);
			TextFileSpliter.this.textFileLineCount(path);
			TextFileSpliter.this.fileName = TextFileSpliter.this.spliteFileName(fileOpen.getSelectedFile().getName());
			TextFileSpliter.this.spliteLineNumber.requestFocus();
		}
	}

	public String spliteFileName(String fn) {
		StringTokenizer parser = new StringTokenizer(fn, ".");
		return parser.nextToken();
	}

	public void textFileLineCount(String path) {
		int lineCount = 0;
		try {
			BufferedReader in = new BufferedReader(new FileReader(path));
			String line = null;
			while ((line = in.readLine()) != null) {
				lineCount++;
			}
			totalcnt = lineCount;
			this.textLineNumber.setText(Long.toString(lineCount));
			in.close();
		} catch (IOException e) {
			logger.error("textFileLineCount error ", e);
		}
	}

	public class saveDirListener implements ActionListener {
		public saveDirListener() {
		}

		public void actionPerformed(ActionEvent ev) {
			JFileChooser fileSave = new JFileChooser();
			fileSave.setFileSelectionMode(1);
			fileSave.showSaveDialog(TextFileSpliter.this.frame);
			String path = fileSave.getSelectedFile().getPath();
			TextFileSpliter.this.saveDir.setText(path);
		}
	}

	public class spliteFileListener implements ActionListener {
		public spliteFileListener() {
		}

		public void actionPerformed(ActionEvent ev) {
			int longSpliteLineNumber = Integer.parseInt(TextFileSpliter.this.spliteLineNumber.getText());

			int fileCount = 1;
			int lineCount = 0;
			String openPath = TextFileSpliter.this.openFile.getText();
			String savePath = TextFileSpliter.this.saveDir.getText();
			try {
				BufferedReader in = new BufferedReader(new FileReader(openPath));
				BufferedWriter writer = new BufferedWriter(new FileWriter(savePath + "\\" + TextFileSpliter.this.fileName + "-" + String.valueOf(fileCount) + ".txt"));
				String line = null;
				while ((line = in.readLine()) != null) {
					lineCount++;
					if (lineCount % longSpliteLineNumber == 0) {
						writer.write(line + "\n");
						writer.close();
						fileCount++;

						if (totalcnt > fileCount)
							writer = new BufferedWriter(new FileWriter(savePath + "\\" + TextFileSpliter.this.fileName + "-" + String.valueOf(fileCount) + ".txt"));

					} else {
						writer.write(line + "\n");
					}
				}
				in.close();
				writer.close();
			} catch (IOException e) {
				logger.error("spliteFileListener actionPerformed ", e);
			}
			TextFileSpliter.this.clear();
		}
	}

	public class clearFileListener implements ActionListener {
		public clearFileListener() {
			super();
		}

		public void actionPerformed(ActionEvent ev) {

			String openPath = TextFileSpliter.this.openFile.getText();
			String savePath = TextFileSpliter.this.saveDir.getText();
			Pattern pattern = Pattern.compile("[0-9]");
			try {
				BufferedReader in = new BufferedReader(new FileReader(openPath));
				String line = null;
				BufferedWriter writer = new BufferedWriter(new FileWriter(savePath + "\\" + TextFileSpliter.this.fileName + "-clear" + ".txt"));

				String exceptstext = props.getProperty("except");

				String[] excepts = exceptstext.split("|");

				while ((line = in.readLine()) != null) {

					line = line.trim();

					Matcher matcher = pattern.matcher(line);

					if (matcher.find()) {
						boolean exceptFlag = false;
						System.out.println(" line " + line);
						for (String except : excepts) {
							if (except.equals(line)) {
								exceptFlag = true;
								break;
							}
						}

						if (!exceptFlag) {
							writer.write(line + "\n");
						}
					} else {
						System.out.println("clear " + line);
					}

				}
				in.close();
				writer.close();
			} catch (IOException e) {
				System.out.println(e);
			}
			TextFileSpliter.this.clear();
		}

	}

	public void clear() {
		this.openFile.setText("");
		this.saveDir.setText("");
		this.textLineNumber.setText("");
		this.spliteLineNumber.setText("");
		this.fileName = "";
		this.totalcnt = 0;
	}
}
