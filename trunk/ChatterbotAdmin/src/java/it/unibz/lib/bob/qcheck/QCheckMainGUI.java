package it.unibz.lib.bob.qcheck;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.Reader;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;

import antlr.ANTLRException;
import antlr.RecognitionException;
import antlr.TokenStreamException;
import antlr.debug.misc.ASTFrame;

import it.unibz.lib.bob.check.MyAST;
import it.unibz.lib.bob.check.BobHelper;
import it.unibz.lib.bob.check.Bob_Lexer;
import it.unibz.lib.bob.check.Bob_Parser;
import it.unibz.lib.bob.check.Bob_TreeParser;
import it.unibz.lib.bob.check.BobHelper.Pair;
import it.unibz.lib.bob.check.DialogueManager;

@SuppressWarnings("serial")
public class QCheckMainGUI extends JPanel implements ActionListener
{
  private static String macroFile = null;

  private static BobHelper sh = null;

  protected JTextArea questionTextArea;

  public JTextArea regexArea;

  public JScrollPane scrollPane;

  public JScrollPane scrollPane2;

  public JTextArea textArea, aa, ba;

  public static JTextArea abkInfoTextArea;

  protected JButton b1, b2;

  protected JRadioButton aca, acb;

  protected ButtonGroup group;

  protected JFileChooser macroFileChooser;

  protected ASTFrame astFrame = null;

  public QCheckMainGUI()
  {
    super(new GridLayout(5, 1));

    String acaString = "in macro file format";
    String acbString = "in TopicTree Format";

    // Create the radio buttons.
    aca = new JRadioButton(acaString);
    aca.setMnemonic(KeyEvent.VK_A);
    aca.setActionCommand("aca");
    aca.setSelected(false);

    acb = new JRadioButton(acbString);
    acb.setMnemonic(KeyEvent.VK_T);
    acb.setActionCommand("acb");
    acb.setSelected(true);

    // Group the radio buttons.
    group = new ButtonGroup();
    group.add(aca);
    group.add(acb);

    regexArea = new JTextArea(10, 80);
    regexArea.setLineWrap(true);
    regexArea.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
    scrollPane = new JScrollPane(regexArea);
    scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

    questionTextArea = new JTextArea(10, 80);
    questionTextArea.setLineWrap(true);
    questionTextArea.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
    scrollPane2 = new JScrollPane(questionTextArea);
    scrollPane2.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

    textArea = new JTextArea(
            "Enter regular expression and corresponding user question, then hit the 'check' button.");
    textArea.setEditable(false);

    abkInfoTextArea = new JTextArea("");
    abkInfoTextArea.setEditable(false);
    abkInfoTextArea.setBackground(Color.gray);

    aa = new JTextArea("Regular expression:");
    aa.setEditable(false);
    ba = new JTextArea("(User question:)");
    ba.setEditable(false);

    b1 = new JButton("Check!");
    b1.setVerticalTextPosition(AbstractButton.CENTER);
    b1.setHorizontalTextPosition(AbstractButton.LEADING);
    b1.setMnemonic(KeyEvent.VK_C);
    b1.setActionCommand("check");

    b1.addActionListener(this);

    b2 = new JButton("Reload macro file");
    b2.setVerticalTextPosition(AbstractButton.CENTER);
    b2.setHorizontalTextPosition(AbstractButton.LEADING);
    b2.setMnemonic(KeyEvent.VK_R);
    b2.setActionCommand("reload");

    b2.addActionListener(this);

    JPanel buttonbuffer = new JPanel();
    buttonbuffer.add(b1);

    buttonbuffer.add(b2);

    JPanel ac = new JPanel();
    ac.setLayout(new GridLayout(2, 1));
    ac.add(acb);
    ac.add(aca);

    JPanel a = new JPanel();
    a.setLayout(new GridLayout(1, 3));

    a.add(aa);

    a.add(scrollPane);
    a.add(ac);

    JPanel b = new JPanel();
    b.setLayout(new GridLayout(1, 3));
    b.add(ba);
    b.add(scrollPane2);
    b.add(new JLabel(""));

    JPanel c = new JPanel();
    c.setLayout(new GridLayout(1, 3));
    c.add(new JLabel(""));
    c.add(buttonbuffer);
    c.add(new JLabel(""));

    // Add Components to this panel.

    add(abkInfoTextArea);
    add(textArea);
    add(a);
    add(b);
    add(c);

  }

  // This method returns the selected radio button in a button group
  public static JRadioButton getSelection(ButtonGroup group)
  {
    for (Enumeration<AbstractButton> e = group.getElements(); e.hasMoreElements();)
    {
      JRadioButton b = (JRadioButton) e.nextElement();
      if (b.getModel() == group.getSelection())
      {
        return b;
      }
    }
    return null;
  }

  public void actionPerformed(ActionEvent evnt)
  {

    String command = evnt.getActionCommand();

    if (command.equals("check"))
    {

      String regex = regexArea.getText();

      if (regex.length() > 0)
      {

        String regexResolved = sh.replaceMacros(regex, "");
        System.out.println(regexResolved);

        Pair[] longestMacros = sh.get3CurrentPatternLongestMacros();

        if ("aca".equals(getSelection(group).getActionCommand()))
        {
          regexResolved = '"' + regexResolved + '"';
        }
        else
        {
          regexResolved = regexResolved.replaceAll("_", "\\\\b");
        }
        regexResolved = regexResolved.replaceAll("&backslash;", "\\\\");
        regexResolved = regexResolved.replaceAll("&dollar;", "\\$");
        regexResolved = regexResolved.replaceAll("&amp;", "&");
        regexResolved = regexResolved.replaceAll("&lt;", "<");
        regexResolved = regexResolved.replaceAll("&gt;", ">");

        String stringToMatch = questionTextArea.getText();
        // remove accents, as in new versions of BoB
        stringToMatch = DialogueManager.removeAccents(stringToMatch);
        // show replaced text in UI
        questionTextArea.setText(stringToMatch);

        Reader reader;

        Bob_Lexer lexer;
        Bob_Parser parser;
        Bob_TreeParser treeparser;
        try
        {
          reader = new StringReader(regexResolved);
          lexer = new Bob_Lexer(reader);
          parser = new Bob_Parser(lexer);
          parser.setASTNodeClass("it.unibz.lib.bob.check.MyAST");
          treeparser = new Bob_TreeParser();
          treeparser.setASTNodeClass("it.unibz.lib.bob.check.MyAST");

          parser.bExpression();
          MyAST tree = (MyAST) parser.getAST();

          // Fehler zuruecksetzen
          regexArea.setBackground(Color.WHITE);
          textArea.setText("Enter regular expression and corresponding user question, then hit the 'check' button.");

          if (astFrame != null)
          {
            astFrame.removeAll();
            astFrame.setVisible(false);
          }
          astFrame = new ASTFrame("Regular Expression", tree);
          astFrame.setVisible(true);

          if (treeparser.bExpression(tree, stringToMatch) == true)
          {
            questionTextArea.setBackground(Color.GREEN);
          }
          else
          {
            questionTextArea.setBackground(Color.ORANGE);
          }

        }
        catch (RecognitionException e)
        {
          textArea.setText("Error in boolean part of regular expression (i.e., outside of the double quotes): "
                  + e.toString());
          regexArea.setBackground(Color.RED);
        }
        catch (TokenStreamException e)
        {
          textArea.setText("Error in boolean part of regular expression (i.e., outside of the double quotes): "
                  + e.toString());
          regexArea.setBackground(Color.RED);
        }
        catch (ANTLRException e)
        {
          textArea.setText("Error in boolean part of regular expression (i.e., outside of the double quotes): "
                  + e.toString());
          regexArea.setBackground(Color.RED);
        }
        catch (Exception e)
        {
          if (e.getMessage().endsWith("Expression is too large."))
          {
            textArea.setText("Error in Perl regex part of regular expression (i.e., inside the double quotes): Pattern is too complex!\n"
                    + "\nThese are the 3 longest macros used in this pattern (to be removed by hand, or shortened considerably):\n"
                    + longestMacros[0].getMacro()
                    + " -> "
                    + longestMacros[0].getLength()
                    + "\n"
                    + longestMacros[1].getMacro()
                    + " -> "
                    + longestMacros[1].getLength()
                    + "\n"
                    + longestMacros[2].getMacro()
                    + " -> "
                    + longestMacros[2].getLength());
          }
          else
          {
            textArea.setText("Other error in Perl regex part of regular expression (i.e., inside the double quotes):\n"
                    + e.toString());
          }
          regexArea.setBackground(Color.RED);
        }

      }
    }

    if (command.equals("reload"))
    {
      loadMacroFile();
    }
  }

  /**
   * Create the GUI and show it. For thread safety, this method should be
   * invoked from the event-dispatching thread.
   */
  private void createAndShowGUI()
  {

    // Create and set up the window.
    JFrame frame = new JFrame("CheckExtendedRegex");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    // Create and set up the content pane.
    JComponent newContentPane = new QCheckMainGUI();

    newContentPane.setOpaque(true); // content panes must be opaque
    frame.setContentPane(newContentPane);

    frame.setSize(800, 600);

    // Display the window.
    // frame.pack();
    frame.setVisible(true);

    /**
     * File chooser for the abbreviation file
     */
    macroFileChooser = new JFileChooser();

    // macroFileChooser.setCurrentDirectory(new File("."));

    macroFileChooser.setDialogTitle("Choose Macro file");
    macroFileChooser.setFileFilter(new javax.swing.filechooser.FileFilter()
    {
      public boolean accept(File f)
      {
        return f.getName().toLowerCase().endsWith(".txt")
                || f.isDirectory();
      }

      public String getDescription()
      {
        return "macro files (*.txt)";
      }
    });

    int r = macroFileChooser.showOpenDialog(new JFrame());
    // exit app if no macro file was chosen
    if (r != JFileChooser.APPROVE_OPTION)
    {
      System.exit(0);
    }

    String name = macroFileChooser.getSelectedFile().getName();
    macroFile = macroFileChooser.getSelectedFile().getAbsolutePath();

    frame.setTitle("CheckExtendedRegex - " + name);

    loadMacroFile();

  }

  /**
   * (Re-)load the currently selected macro file (-> macroFileChooser )
   */
  private void loadMacroFile()
  {
    sh = new BobHelper(macroFile, "unknown_GUI_language");

    // just print warnings/errors to console for now:
    sh.checkAllRegexesInMacroMap();
    Pair[] longestMacros = sh.get3OverallLongestMacros();

    DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    Date date = new Date();
    String loadTime = dateFormat.format(date);

    abkInfoTextArea.setText("Macro file loaded at "
            + loadTime
            + ": "
            // + macroFileChooser.getSelectedFile().getCanonicalPath()
            + macroFile + "\nIts 3 longest macros:\n"
            + longestMacros[0].getMacro() + " -> "
            + longestMacros[0].getLength() + "\n"
            + longestMacros[1].getMacro() + " -> "
            + longestMacros[1].getLength() + "\n"
            + longestMacros[2].getMacro() + " -> "
            + longestMacros[2].getLength());
  }

  public static void main(String[] args)
  {

    // Schedule a job for the event-dispatching thread:
    // creating and showing this application's GUI.
    javax.swing.SwingUtilities.invokeLater(new Runnable()
    {
      public void run()
      {
        new QCheckMainGUI().createAndShowGUI();
        // abkInfoTextArea.setText(t)
      }
    });

  }
}
