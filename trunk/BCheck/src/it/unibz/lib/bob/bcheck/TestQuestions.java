package it.unibz.lib.bob.bcheck;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.ButtonGroup;
import javax.swing.ButtonModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class TestQuestions {

	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			;
		}

		CheckBoB_Model model = new CheckBoB_Model();
		CheckBoB_View view = new CheckBoB_View("CheckBoB");
		new CheckBoB_Controller(model, view);

		TestQuestions_View view2 = new TestQuestions_View("Test Questions",
				view);
		new TestQuestions_Controller(model, view2);

	}

}

class TestQuestions_Controller implements ActionListener {
	// private String ttText, aeText, adText, aiText;
	CheckBoB_Model model;
	TestQuestions_View view;

	public TestQuestions_Controller(CheckBoB_Model model,
			TestQuestions_View view) {
		this.model = model;
		this.view = view;

		// Give the model a start situation as per what the GUI will show.
		view.isNullWS();
		// Add listeners from this class to the buttons of the view.
		view.buttonActionListeners(this);
	}

	// methods to deal with the interactions performed on the View.
	public void actionPerformed(ActionEvent event) {
		String cmd = event.getActionCommand();

		if (cmd.equals("Test")) {
			ButtonModel selected = view.group.getSelection();
			view.setLang(selected.getActionCommand());
			view.resTextArea.setText(null);

			// TODO: set this somewhere else!!!
			boolean trainingMode = false;

			String idfname = null;
			if (view.MLbox.isSelected()) {
				idfname = view.getIDFName();
			}

			String idfnameEN = null;
			String idfnameDE = null;
			String idfnameIT = null;

			if (view.getLang().toUpperCase().equals("EN")) {
				idfnameEN = idfname;
			} else if (view.getLang().toUpperCase().equals("DE")) {
				idfnameDE = idfname;
			} else if (view.getLang().toUpperCase().equals("IT")) {
				idfnameIT = idfname;
			}

			model
					.testQuestions(view, view.getQName(), view.getLang(), view
							.getTTName(), view.getAEName(), view.getADName(),
							view.getAIName(), trainingMode, idfnameEN,
							idfnameDE, idfnameIT);
		}

	}
}

class TestQuestions_View extends JFrame {

	class QFileInHandler implements ActionListener {

		public void actionPerformed(ActionEvent ae) {
			JFileChooser chooser = new JFileChooser(parentView.curDir);

			chooser.setMultiSelectionEnabled(false);
			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			chooser.setSelectedFile(qFile); // set default for output
			ExampleFileFilter filter = new ExampleFileFilter();

			filter.addExtension("xls");
			filter.setDescription("Test questions + Topic IDs");
			chooser.setFileFilter(filter);
			chooser.addChoosableFileFilter(filter);
			int option = chooser.showOpenDialog(TestQuestions_View.this);
			if (option == JFileChooser.APPROVE_OPTION) {
				qFile = chooser.getSelectedFile();
				try {
					qName = qFile.getCanonicalPath();
				} catch (IOException e) {
				}
				;
				qField.setText(qFile.getName()); // show on GUI
				qField.setToolTipText(qFile.getAbsolutePath());
				isNullWS(); // and adjust runtime button(s)
			} else {/* add cancelled message here */
			}
		}
	}

	class IDFFileHandler implements ActionListener {

		public void actionPerformed(ActionEvent ae) {
			JFileChooser chooser = new JFileChooser(parentView.curDir);

			chooser.setMultiSelectionEnabled(false);
			chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			chooser.setSelectedFile(IDFFile); // set default for output
			ExampleFileFilter filter = new ExampleFileFilter();

			filter.addExtension("txt_sm");
			filter.setDescription("IDF training data");

			chooser.setFileFilter(filter);
			chooser.addChoosableFileFilter(filter);
			int option = chooser.showOpenDialog(TestQuestions_View.this);
			if (option == JFileChooser.APPROVE_OPTION) {
				IDFFile = chooser.getSelectedFile();
				try {
					IDFName = IDFFile.getCanonicalPath();
				} catch (IOException e) {
				}
				;
				IDFField.setText(IDFFile.getName()); // show on GUI
				IDFField.setToolTipText(IDFFile.getAbsolutePath());
				isNullWS(); // and adjust runtime button(s)
			} else {/* add cancelled message here */
			}
		}
	}

	class MLBoxHandler implements ActionListener {
		public void actionPerformed(ActionEvent ae) {
			isNullWS(); // adjust runtime button(s)
		}
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static String eString = "en";
	static String dString = "de";

	static String iString = "it";

	// declare area components
	JLabel qLabel = new JLabel("Test Questions File: ");

	JLabel IDFLabel = new JLabel("Machine Learning: Text Corpus File: ");

	JTextField qField = new JTextField(20);

	JTextField IDFField = new JTextField(20);

	JCheckBox MLbox = new JCheckBox("", true);

	ImageIcon iconOpen = new ImageIcon(getClass()
			.getResource("/icons/open.gif"));
	ImageIcon iconCheck = new ImageIcon(getClass().getResource(
			"/icons/Check.gif"));
	JButton qBrow = new JButton(iconOpen);

	JButton IDFBrow = new JButton(iconOpen);

	JPanel qPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

	JPanel IDFPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

	JLabel langLabel = new JLabel("Question Language: ");

	JRadioButton eButton = new JRadioButton(eString);

	JRadioButton dButton = new JRadioButton(dString);

	JRadioButton iButton = new JRadioButton(iString);

	// Group the radio buttons.
	ButtonGroup group = new ButtonGroup();
	JPanel langPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
	JButton testButton = new JButton("Test", iconCheck);
	JPanel settingsZone = new JPanel(new GridLayout(4, 1, 1, 1));
	JPanel resultsZone = new JPanel(new GridLayout(1, 1, 1, 1));

	// declare results area components
	JTextArea resTextArea = new JTextArea("", 10, 30);
	JScrollPane resScrollPane = new JScrollPane(resTextArea);

	// main is split into 2 grid regions
	JPanel main = new JPanel(new BorderLayout());

	private String qName = "";

	private String IDFName = "resources/ukwac/UKWAC-1.txt_sm";

	File qFile = new File(qName);

	File IDFFile = new File(IDFName);

	private String lang = "en";
	// group.getSelection().getSelectedObjects()[0] .toString();

	private CheckBoB_View parentView;

	public TestQuestions_View(String title, CheckBoB_View parentView) {

		super(title);
		this.parentView = parentView;
		setBounds(490, 250, 600, 500);
		// setResizable(false);

		qField.setEnabled(false);
		qField.setToolTipText(qFile.getAbsolutePath());

		qField.setText(qName);

		IDFField.setEnabled(false);
		IDFField.setToolTipText(IDFFile.getAbsolutePath());

		IDFField.setText(IDFName);

		eButton.setActionCommand(eString);
		eButton.setSelected(true);
		dButton.setActionCommand(dString);
		iButton.setActionCommand(iString);

		group.add(eButton);
		group.add(dButton);
		group.add(iButton);

		JPanel radioPanel = new JPanel(new GridLayout(1, 0));
		radioPanel.add(eButton);
		radioPanel.add(dButton);
		radioPanel.add(iButton);

		resTextArea.setTabSize(4);
		resTextArea.setLineWrap(true);
		resTextArea.setWrapStyleWord(true);
		resTextArea.setEditable(false);

		qLabel.setForeground(Color.black);
		qPanel.add(qLabel);
		qPanel.add(qField);
		qPanel.add(qBrow);

		IDFLabel.setForeground(Color.black);
		IDFPanel.add(MLbox);
		IDFPanel.add(IDFLabel);
		IDFPanel.add(IDFField);
		IDFPanel.add(IDFBrow);

		langLabel.setForeground(Color.black);
		langPanel.add(langLabel);
		langPanel.add(radioPanel);

		settingsZone.add(qPanel);
		settingsZone.add(langPanel);
		settingsZone.add(IDFPanel);
		settingsZone.add(testButton);

		settingsZone.setBorder(new TitledBorder("Test Settings"));

		resultsZone.add(resScrollPane);
		resultsZone.setBorder(new TitledBorder("Test Results"));

		// complete the panel assembly
		main.add("North", settingsZone);
		main.add("Center", resultsZone);
		this.getContentPane().add("Center", main);

		qBrow.addActionListener(new QFileInHandler());
		IDFBrow.addActionListener(new IDFFileHandler());
		MLbox.addActionListener(new MLBoxHandler());

		// set up output stream redirection
		System.setOut(new PrintStream(new JTextAreaOutputStream(resTextArea)));

		// Windows-Listener
		addWindowListener(new WindowClosingAdapter(false));
		setVisible(true);

	}

	public void buttonActionListeners(ActionListener al) {
		testButton.addActionListener(al);
		testButton.setActionCommand("Test");

	}

	public String getADName() {
		return parentView.getADName();
	}

	public String getAEName() {
		return parentView.getAEName();
	}

	public String getAIName() {
		return parentView.getAIName();
	}

	public String getLang() {
		return lang;
	}

	public String getQName() {
		return qName;
	}

	public String getIDFName() {
		return IDFName;
	}

	// view object field accessors
	public String getTTName() {
		return parentView.getTTName();
	}

	public boolean isNullWS() {

		boolean flag = parentView.isNullWS();

		qField.setBackground(Color.white);
		IDFField.setBackground(Color.white);

		if (!qFile.canRead()) {
			flag = false;
			qField.setBackground(Color.yellow);
		}

		if (!IDFFile.canRead() && MLbox.isSelected()) {
			flag = false;
			IDFField.setBackground(Color.yellow);
		}

		testButton.setEnabled(flag);
		return flag;

	}

	public void setLang(String lang) {
		this.lang = lang;

	}

}

class TestQuestionSpreadSheet {
	InputStream myxls;
	HSSFWorkbook wb;

	Vector<String> questions = new Vector<String>();
	Vector<String> ids = new Vector<String>();

	/**
	 * 
	 * @param file
	 * 
	 * @throws FileNotFoundException
	 *             ...
	 * @throws IOException
	 *             ...
	 */
	public TestQuestionSpreadSheet(File file) {
		try {
			myxls = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			wb = new HSSFWorkbook(myxls);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@SuppressWarnings("unchecked")
	public void loadSheet() {
		HSSFSheet sheet = wb.getSheetAt(0);

		int rown = 1;
		int goodrows = 0;
		System.out.println("Loading Test Questions and correct Answer IDs:");
		for (Iterator<HSSFRow> rit = sheet.rowIterator(); rit.hasNext(); rown++) {
			if (rown == 1) {
				rit.next(); // skip title row
				continue;
			}
			HSSFRow row = rit.next();
			HSSFCell qCell = row.getCell(0);
			HSSFCell idCell = row.getCell(1);
			if (qCell != null && idCell != null
					&& qCell.getCellType() == HSSFCell.CELL_TYPE_STRING
					&& idCell.getCellType() == HSSFCell.CELL_TYPE_STRING
					&& qCell.getRichStringCellValue().length() > 0
					&& idCell.getRichStringCellValue().length() > 0) {
				goodrows++;
				questions.add(qCell.getRichStringCellValue().getString());
				ids.add(idCell.getRichStringCellValue().getString());
				if (goodrows > 0 && goodrows <= 3) {
					System.out.println(" " + rown + ": '"
							+ questions.get(goodrows - 1) + "' - "
							+ ids.get(goodrows - 1));
				}
			}
		}
		System.out.println(" ...");
		// don't count title row
		System.out.println("Read " + goodrows + " entries.");
	}
}
