package it.unibz.lib.bob.bbcheck;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;

import it.unibz.lib.bob.check.DialogueManager;
import it.unibz.lib.bob.check.TopicTree;
import it.unibz.lib.bob.check.QAFeatures;
import it.unibz.lib.bob.check.QAMatchingBob; //import chatterbot.taskstructure.ResultFileWriter;

import com.mallardsoft.tuple.Quadruple;
import com.mallardsoft.tuple.Tuple;

public class CheckBoB
{
  private static final long serialVersionUID = 1L;

  public static void main(String[] args)
  {
    try
    {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    }
    catch (Exception e)
    {
      ;
    }
    CheckBoB_Model model = new CheckBoB_Model();
    CheckBoB_View view = new CheckBoB_View("CheckBoB");
    new CheckBoB_Controller(model, view);

  }
}

class CheckBoB_Controller implements ActionListener
{
  // private String ttText, aeText, adText, aiText;
  CheckBoB_Model model;

  CheckBoB_View view;

  public CheckBoB_Controller(CheckBoB_Model model, CheckBoB_View view)
  {
    this.model = model;
    this.view = view;
    // Give the model a start situation as per what the GUI will show.
    view.isNullWS();
    // Add listeners from this class to the buttons of the view.
    view.buttonActionListeners(this);
  }

  // methods to deal with the interactions performed on the View.
  public void actionPerformed(ActionEvent event)
  {
    String cmd = event.getActionCommand();

    if (cmd.equals("checkTT"))
    {
      model.checkTT(view.getTTName(), view.getAEName(), view.getADName(),
              view.getAIName());
    }
    else if (cmd.equals("testBoB"))
    {
      TestQuestions_View view2 = new TestQuestions_View("Test Questions",
              view);
      new TestQuestions_Controller(model, view2);

    }

  }
}

class CheckBoB_Model implements Runnable
{
  private String questionsFile, lang, tt, ae, ad, ai, idfEN, idfDE, idfIT;

  private JFrame frame;

  private boolean trainingMode = false;

  public CheckBoB_Model()
  {
    // nop
  }

  public void checkTT(String tt, String ae, String ad, String ai)
  {
    String msg = "TT file: " + tt + "\nAE File: " + ae;
    JOptionPane.showMessageDialog(null, msg, "Message Dialog",
            JOptionPane.PLAIN_MESSAGE);
  }

  public void testQuestions(JFrame frame, String q, String lang, String tt,
          String ae, String ad, String ai, boolean trainingMode,
          String idfEN, String idfDE, String idfIT)
  {
    execAsyncron(frame, q, lang, tt, ae, ad, ai, trainingMode, idfEN,
            idfDE, idfIT);
  }

  /*
   * By using DialogueManager's topicIDsAreEquivalent(), this method now
   * honors name-trace equivalences
   *
   * @param q
   * @param lang
   * @param tt
   * @param ae
   * @param ad
   * @param ai
   * @param trainingMode
   * @param idfEN
   * @param idfDE
   * @param idfIT
   *
   * private void _testQuestions(String q, String lang, String tt, String ae,
   *        String ad, String ai, boolean trainingMode, String idfEN,
   *        String idfDE, String idfIT)
   *
   */
  public void run()
  {
    // super.testQuestions(q, lang, tt, ae, ad, ai);
    // _testQuestions(questionsFile, lang, tt, ae, ad, ai, trainingMode,
    //  idfEN, idfDE, idfIT);
  }

  private void execAsyncron(JFrame frame, String q, String lang, String tt,
          String ae, String ad, String ai, boolean trainingMode,
          String idfEN, String idfDE, String idfIT)
  {
    Thread t = new Thread(this);
    this.frame = frame;
    this.questionsFile = q;
    this.lang = lang;
    this.tt = tt;
    this.ae = ae;
    this.ad = ad;
    this.ai = ai;
    this.trainingMode = trainingMode;
    this.idfEN = idfEN;
    this.idfDE = idfDE;
    this.idfIT = idfIT;
    t.start();
  }
}

class CheckBoB_View extends JFrame
{
  class ENAbbrevFileInHandler implements ActionListener
  {
    public void actionPerformed(ActionEvent ae)
    {
      JFileChooser chooser = new JFileChooser(curDir);
      chooser.setMultiSelectionEnabled(false);
      chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
      chooser.setSelectedFile(aeFile); // set default for output
      ExampleFileFilter filter = new ExampleFileFilter();
      filter.addExtension("txt");
      filter.setDescription("Abbreviations file");
      chooser.setFileFilter(filter);
      chooser.addChoosableFileFilter(filter);
      int option = chooser.showOpenDialog(CheckBoB_View.this);
      if (option == JFileChooser.APPROVE_OPTION)
      {
        aeFile = chooser.getSelectedFile();
        try
        {
          aeName = aeFile.getCanonicalPath();
          // remember the directory of the last selected file
          if (aeFile.getParentFile() != null)
          {
            curDir = aeFile.getParentFile().getCanonicalPath();
          }

        }
        catch (IOException e)
        {
        }
        ;
        aeField.setText(aeFile.getName()); // show on GUI
        aeField.setToolTipText(aeFile.getAbsolutePath());
        isNullWS(); // and adjust runtime button(s)
      }
      else
      {/* add cancelled message here */

      }
    }
  }

  class DEAbbrevFileInHandler implements ActionListener
  {
    public void actionPerformed(ActionEvent ae)
    {
      JFileChooser chooser = new JFileChooser(curDir);
      chooser.setMultiSelectionEnabled(false);
      chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
      chooser.setSelectedFile(adFile); // set default for output
      ExampleFileFilter filter = new ExampleFileFilter();
      filter.addExtension("txt");
      filter.setDescription("Abbreviations file");
      chooser.setFileFilter(filter);
      chooser.addChoosableFileFilter(filter);
      int option = chooser.showOpenDialog(CheckBoB_View.this);
      if (option == JFileChooser.APPROVE_OPTION)
      {
        adFile = chooser.getSelectedFile();
        try
        {
          adName = adFile.getCanonicalPath();
          // remember the directory of the last selected file
          if (adFile.getParentFile() != null)
          {
            curDir = adFile.getParentFile().getCanonicalPath();
          }

        }
        catch (IOException e)
        {
        }
        ;
        adField.setText(adFile.getName()); // show on GUI
        adField.setToolTipText(adFile.getAbsolutePath());
        isNullWS(); // and adjust runtime button(s)
      }
      else
      {/* add cancelled message here */

      }
    }
  }

  class ITAbbrevFileInHandler implements ActionListener
  {
    public void actionPerformed(ActionEvent ae)
    {
      JFileChooser chooser = new JFileChooser(curDir);
      chooser.setMultiSelectionEnabled(false);
      chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
      chooser.setSelectedFile(aiFile); // set default for output
      ExampleFileFilter filter = new ExampleFileFilter();
      filter.addExtension("txt");
      filter.setDescription("Abbreviations file");
      chooser.setFileFilter(filter);
      chooser.addChoosableFileFilter(filter);
      int option = chooser.showOpenDialog(CheckBoB_View.this);
      if (option == JFileChooser.APPROVE_OPTION)
      {
        aiFile = chooser.getSelectedFile();
        try
        {
          aiName = aiFile.getCanonicalPath();
          // remember the directory of the last selected file
          if (aiFile.getParentFile() != null)
          {
            curDir = aiFile.getParentFile().getCanonicalPath();
          }

        }
        catch (IOException e)
        {
        }
        ;
        aiField.setText(aiFile.getName()); // show on GUI
        aiField.setToolTipText(aiFile.getAbsolutePath());
        isNullWS(); // and adjust runtime button(s)
      }
      else
      {/* add cancelled message here */

      }
    }
  }

  class TTFileInHandler implements ActionListener
  {
    public void actionPerformed(ActionEvent ae)
    {
      JFileChooser chooser = new JFileChooser(curDir);

      chooser.setMultiSelectionEnabled(false);
      chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
      chooser.setSelectedFile(ttFile); // set default for output
      ExampleFileFilter filter = new ExampleFileFilter();
      filter.addExtension("xml");
      filter.setDescription("Topictree File");
      chooser.setFileFilter(filter);
      chooser.addChoosableFileFilter(filter);
      int option = chooser.showOpenDialog(CheckBoB_View.this);
      if (option == JFileChooser.APPROVE_OPTION)
      {
        ttFile = chooser.getSelectedFile();
        try
        {
          ttName = ttFile.getCanonicalPath();
          // remember the directory of the last selected file
          if (ttFile.getParentFile() != null)
          {
            curDir = ttFile.getParentFile().getCanonicalPath();
          }

        }
        catch (IOException e)
        {
        }
        ;
        ttField.setText(ttFile.getName()); // show on GUI
        ttField.setToolTipText(ttFile.getAbsolutePath());
        isNullWS(); // and adjust runtime button(s)
      }
      else
      {/* add cancelled message here */

      }
    }
  }
  /**
   *
   */
  private static final long serialVersionUID = 1L;

  /**
   * class FileOutHandler implements ActionListener { public void
   * actionPerformed(ActionEvent ae) { JFileChooser chooser = new
   * JFileChooser(curDir); chooser.setMultiSelectionEnabled(false);
   * chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
   * chooser.setSelectedFile(reportFile); // default output int option =
   * chooser.showSaveDialog(Fgui_View.this); if (option ==
   * JFileChooser.APPROVE_OPTION) { reportFile = chooser.getSelectedFile();
   * try { reportName = reportFile.getCanonicalPath(); } catch (IOException e)
   * { } ; reportField.setText(reportFile.getName()); // show it on GUI } else
   * } } }
   */
  String curDir = System.getProperty("user.dir") + File.separator;

  private String ttName = "topictree.xml",
          aeName = "bob_macros_EN.txt",
          adName = "bob_macros_DE.txt",
          aiName = "bob_macros_IT.txt";

  File ttFile = new File(ttName);

  File aeFile = new File(aeName);

  File adFile = new File(adName);

  File aiFile = new File(aiName);

  // declare file area components
  JLabel ttLabel = new JLabel("Topictree: ");

  JTextField ttField = new JTextField(20);

  ImageIcon iconOpen = new ImageIcon(getClass().getResource("/icons/open.gif"));

  ImageIcon iconEnter = new ImageIcon(getClass().getResource(
          "/icons/Enter.gif"));

  JButton ttBrow = new JButton(iconOpen);

  JPanel ttPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

  JLabel aeLabel = new JLabel("Abbreviations File EN: ");

  JTextField aeField = new JTextField(20);

  JButton aeBrow = new JButton(iconOpen);

  JPanel aePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

  JLabel adLabel = new JLabel("Abbreviations File DE: ");

  JTextField adField = new JTextField(20);

  JButton adBrow = new JButton(iconOpen);

  JPanel adPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

  JLabel aiLabel = new JLabel("Abbreviations File IT: ");

  JTextField aiField = new JTextField(20);

  JButton aiBrow = new JButton(iconOpen);

  JPanel aiPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

  JPanel fileZone = new JPanel(new GridLayout(4, 1, 1, 1));
  // declare button components

  JButton checkTTButton = new JButton(
          "Check TopicTree and Abbreviations Files...", iconEnter);

  JButton testBoBButton = new JButton(
          "Test BoB with Questions from .xls File...", iconEnter);

  JPanel buttons = new JPanel(new GridLayout(2, 1, 1, 1));
  // main is split into 2 grid regions

  JPanel main = new JPanel(new BorderLayout());

  public CheckBoB_View(String title)
  {
    super(title);
    setBounds(180, 150, 500, 400);
    setResizable(false);

    // construct files zone
    ttField.setEnabled(false);
    ttField.setToolTipText(ttFile.getAbsolutePath());
    aeField.setEnabled(false);
    aeField.setToolTipText(aeFile.getAbsolutePath());
    adField.setEnabled(false);
    adField.setToolTipText(adFile.getAbsolutePath());
    aiField.setEnabled(false);
    aiField.setToolTipText(aiFile.getAbsolutePath());

    ttField.setText(ttName);
    aeField.setText(aeName);
    adField.setText(adName);
    aiField.setText(aiName);

    ttLabel.setForeground(Color.black);
    ttPanel.add(ttLabel);
    ttPanel.add(ttField);
    ttPanel.add(ttBrow);

    aeLabel.setForeground(Color.black);
    aePanel.add(aeLabel);
    aePanel.add(aeField);
    aePanel.add(aeBrow);

    adLabel.setForeground(Color.black);
    adPanel.add(adLabel);
    adPanel.add(adField);
    adPanel.add(adBrow);

    aiLabel.setForeground(Color.black);
    aiPanel.add(aiLabel);
    aiPanel.add(aiField);
    aiPanel.add(aiBrow);

    fileZone.add(ttPanel);
    fileZone.add(aePanel);
    fileZone.add(adPanel);
    fileZone.add(aiPanel);

    fileZone.setBorder(new TitledBorder("General File Settings"));

    // construct controls zone
    buttons.add(checkTTButton);
    buttons.add(testBoBButton);
    buttons.setBorder(new TitledBorder("Actions"));

    // complete the panel assembly
    main.add("Center", fileZone);
    main.add("South", buttons);
    this.getContentPane().add("Center", main);

    ttBrow.addActionListener(new TTFileInHandler());
    aeBrow.addActionListener(new ENAbbrevFileInHandler());
    adBrow.addActionListener(new DEAbbrevFileInHandler());
    aiBrow.addActionListener(new ITAbbrevFileInHandler());

    // Windows-Listener
    addWindowListener(new WindowClosingAdapter(true));
    setVisible(true);
  }

  public void buttonActionListeners(ActionListener al)
  {
    checkTTButton.addActionListener(al);
    checkTTButton.setActionCommand("checkTT");
    testBoBButton.addActionListener(al);
    testBoBButton.setActionCommand("testBoB");
  }

  public String getADName()
  {
    return adName;
  }

  public String getAEName()
  {
    return aeName;
  }

  public String getAIName()
  {
    return aiName;
  }

  // view object field accessors
  public String getTTName()
  {
    return ttName;
  }

  public boolean isNullWS()
  {
    boolean flag = true;
    ttField.setBackground(Color.white);
    aeField.setBackground(Color.white);
    adField.setBackground(Color.white);
    aiField.setBackground(Color.white);

    if (!ttFile.canRead())
    {
      flag = false;
      ttField.setBackground(Color.yellow);
    }
    if (!aeFile.canRead())
    {
      flag = false;
      aeField.setBackground(Color.yellow);
    }
    if (!adFile.canRead())
    {
      flag = false;
      adField.setBackground(Color.yellow);
    }
    if (!aiFile.canRead())
    {
      flag = false;
      aiField.setBackground(Color.yellow);
    }

    // TODO: fix checkTT part, re-enable the button
    // checkTTButton.setEnabled(flag);
    checkTTButton.setEnabled(false);
    testBoBButton.setEnabled(flag);
    return flag;
  }

  public void setADName(String s)
  {
    adName = s;
  }

  public void setAEName(String s)
  {
    aeName = s;
  }

  public void setAIName(String s)
  {
    aiName = s;
  }

  public void setTTName(String s)
  {
    ttName = s;
  }
}
