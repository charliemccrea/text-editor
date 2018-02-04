// Author: Charlie McCrea

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class TextEditor extends JFrame implements ActionListener
{
	private static final long serialVersionUID = 1L;
	private File file;
	private FileNameExtensionFilter filter;
	private FileReader fReader;
	private BufferedReader bReader;
	private BufferedWriter bWriter;
	private JFileChooser fc;
	private JFrame frame;
	private JMenuBar menuBar;
	private JMenu menuFile;
	private JMenu menuEdit;
	private JMenu menuInsert;
	private JMenu menuGreek;
	private JMenu menuMath;
	private JMenuItem mOpen;
	private JMenuItem mSave;
	private JMenuItem mSaveAs;
	private JMenuItem mClose;
	private JMenuItem mExit;
	private JTextArea textArea;
	private JTextField textField;
	private JScrollPane scrollPane;

	private char mathSymbols[] = {'\u2264', '\u2265', '\u2200', '\u2201', '\u2202',
								  '\u2203', '\u2204', '\u2205', '\u2206', '\u2207',
								  '\u2208', '\u2209', '\u220a', '\u2282', '\u2283',
								  '\u2284', '\u2285', '\u2286', '\u2287'};
	private JMenuItem mathItems[] = new JMenuItem[mathSymbols.length];

	private char greekSymbols[] = {'\u03b1', '\u03b2', '\u03b3', '\u03b4', '\u03b5',
								   '\u03b6', '\u03b7', '\u03b8', '\u03b9', '\u03c0',
								   '\u03c1', '\u03c2', '\u03c3', '\u03c4', '\u03c5',
								   '\u03c6', '\u03c7', '\u03c8', '\u03c9', '\u0391',
								   '\u0392', '\u0393', '\u0394', '\u0395', '\u0396',
								   '\u0397', '\u0398', '\u0399', '\u03a0', '\u03a1',
								   '\u03a3', '\u03a4', '\u03a5', '\u03a6', '\u03a7',
								   '\u03a8', '\u03a9'};
	private JMenuItem greekItems[] = new JMenuItem[greekSymbols.length];

	public TextEditor()
	{
		// Initialize JFrame
		frame = new JFrame("TextEditor by mccreacc");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container contentPane = frame.getContentPane();

		// Initialize MenuBar
		menuBar = new JMenuBar();
		menuFile = new JMenu("File");
		menuEdit = new JMenu("Edit");
		menuBar.add(menuFile);
		menuBar.add(menuEdit);

		// Initialize MenuBar Items
		mOpen = new JMenuItem("Open");
		mSave = new JMenuItem("Save");
		mSaveAs = new JMenuItem("SaveAs");
		mClose = new JMenuItem("Close");
		mExit = new JMenuItem("Exit");
		menuInsert = new JMenu("Insert Symbol");
		menuGreek = new JMenu("Greek");
		menuMath = new JMenu("Math");
		menuFile.add(mOpen);
		menuFile.add(mSave);
		menuFile.add(mSaveAs);
		menuFile.add(mClose);
		menuFile.add(mExit);
		menuEdit.add(menuInsert);
		menuInsert.add(menuGreek);
		menuInsert.add(menuMath);

		// Initialize File Chooser
		fc = new JFileChooser();
		filter = new FileNameExtensionFilter(".txt Files", "txt", "text");
		fc.setFileFilter(filter);
		fc.setSelectedFile(new File(".txt"));

		// Initialize Editable Text Area
		textArea = new JTextArea(30, 80);
		scrollPane = new JScrollPane(textArea);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

		// Initialize Text Field
		textField = new JTextField();
		textField.setEditable(false);

		// Initialize Math symbols
		for (int i = 0; i < mathSymbols.length; i++)
		{
			mathItems[i] = new JMenuItem(Character.toString(mathSymbols[i]));
			menuMath.add(mathItems[i]);
			mathItems[i].addActionListener(this);
		}

		// Initialize Greek symbols
		for (int i = 0; i < greekSymbols.length; i++)
		{
			greekItems[i] = new JMenuItem(Character.toString(greekSymbols[i]));
			menuGreek.add(greekItems[i]);
			greekItems[i].addActionListener(this);
		}

		// Add action listeners
		mOpen.addActionListener(this);
		mSave.addActionListener(this);
		mSaveAs.addActionListener(this);
		mClose.addActionListener(this);
		mExit.addActionListener(this);

		// Add components to the content pane
		contentPane.add(menuBar, BorderLayout.NORTH);
		contentPane.add(scrollPane, BorderLayout.CENTER);
		contentPane.add(textField, BorderLayout.SOUTH);
	}

	public void actionPerformed(ActionEvent e)
	{
		// User selects "Open" from menu
		if(e.getSource() == mOpen && fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
		{
			file = fc.getSelectedFile();
			textField.setText(fc.getSelectedFile().getAbsolutePath());
			try
			{
				fReader = new FileReader(file);
				bReader = new BufferedReader(fReader);
				textArea.read(bReader, null);
				bReader.close();
				textArea.requestFocus();
			}
			catch (FileNotFoundException fnfe)
			{
				JOptionPane.showMessageDialog(frame, "Error: File not found.");
			}
			catch (IOException ioe)
			{
				JOptionPane.showMessageDialog(frame, "Error: Failed to read file.");
			}
		}

		// User selects "Save" from menu
		if(e.getSource() == mSave)
		{
			try
			{
				bWriter = new BufferedWriter(new FileWriter(file));
				textArea.write(bWriter);
				bWriter.close();
			}
			catch (IOException ioe)
			{
				JOptionPane.showMessageDialog(frame, "Error: Failed to write to file.");
			}
			catch (NullPointerException npe)
			{
				JOptionPane.showMessageDialog(frame, "Error: No open file.");
			}
		}

		// User selects "SaveAs" from menu
		if(e.getSource() == mSaveAs && fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION)
		{
			file = fc.getSelectedFile();
			textField.setText(fc.getSelectedFile().getAbsolutePath());
			try
			{
				bWriter = new BufferedWriter(new FileWriter(file));
				textArea.write(bWriter);
				bWriter.close();
			}
			catch (IOException ioe)
			{
				JOptionPane.showMessageDialog(frame, "Error: Failed to write to file.");
			}
		}

		// User selects "Close" from menu
		if(e.getSource() == mClose)
		{
			file = null;
			textArea.setText("");
			textField.setText("");
		}

		// User selects "Exit" from menu
		if(e.getSource() == mExit)
		{
			frame.setVisible(false);
			frame.dispose();
			System.exit(0);
		}

		// User selects a math symbol from menu
		for(int i = 0; i < mathSymbols.length; i++)
		{
			if(e.getSource() == mathItems[i])
			{
				textArea.append(Character.toString(mathSymbols[i]));
			}
		}

		// User selects a greek symbol from menu
		for(int i = 0; i < greekSymbols.length; i++)
		{
			if(e.getSource() == greekItems[i])
			{
				textArea.append(Character.toString(greekSymbols[i]));
			}
		}
	}

	private void display()
	{
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
	}

	public static void main(String[] args)
	{
		new TextEditor().display();
	}
}
