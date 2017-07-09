import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.StringTokenizer;
import javax.swing.*;
import javax.swing.text.*;

public class CoffeeText extends JFrame {
	
	private static final long serialVersionUID = 1L;
	
	private JTextArea area = new JTextArea(30,70); 
	private JFileChooser dialog = new JFileChooser(System.getProperty("user.dir"));
	private String currentFile = "Untitled";
	private boolean changed = false; 
	JLabel bottom = new JLabel();
	
	public CoffeeText() {
		JScrollPane scroll = new JScrollPane(area,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		add(scroll, BorderLayout.CENTER);
		
		JMenuBar JMB = new JMenuBar();
		setJMenuBar(JMB);
		JMenu file = new JMenu("File");
		JMenu edit = new JMenu("Edit");
		JMenu mode = new JMenu("Mode");
		JMenu about = new JMenu("About");
		
		JMB.add(file); 
		JMB.add(edit);
		JMB.add(mode);
		JMB.add(about);
		
		file.add(newFile);
		file.add(openFile);
		file.add(saveFile);
		file.add(quitFile);
		file.add(saveFileAs);
		
		for (int i = 0; i < 4; i++) {
			file.getItem(i);
		}
		
		edit.add(cut);
		edit.add(copy);
		edit.add(paste);
		
		edit.getItem(0).setText("Cut");
		edit.getItem(1).setText("Copy");
		edit.getItem(2).setText("Paste");
		
		mode.add(writerMode);
		mode.add(programmerMode);
		
		about.add(openAbout);
		
		saveFile.setEnabled(false);
		saveFileAs.setEnabled(false);
		
		add(bottom, BorderLayout.PAGE_END);
		bottom.setPreferredSize(new Dimension(this.getWidth(), 25));
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		
		area.addKeyListener(kl);
		area.addKeyListener(k2);
		
		Font myFont = new Font("Consolas", Font.PLAIN, 18);
		area.setFont(myFont);	
		
		setTitle(currentFile);
		setVisible(true);			
	}
	
	private KeyListener kl = new KeyAdapter() {
		public void keyPressed(KeyEvent e) {
			changed = true;
			saveFile.setEnabled(true);
			saveFileAs.setEnabled(true);
		}
	};
	
	private KeyListener k2 = new KeyAdapter() {
		public void keyPressed(KeyEvent e) {
			StringTokenizer st = new StringTokenizer(area.getText());
			bottom.setText(st.countTokens() + " words");
		}
	};
	
	Action openFile = new AbstractAction("Open") {
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			saveOld();
			if (dialog.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				readInFile(dialog.getSelectedFile().getAbsolutePath());
			}
			saveFileAs.setEnabled(true);
		}
	};
	
	Action saveFile = new AbstractAction("Save") {
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			if (!currentFile.equals("Untitled")) {
				saveFile(currentFile);
			}
			else {
				saveFileAs();
			}
		}
	};
	
	Action saveFileAs = new AbstractAction("Save as...") {
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			saveFileAs();
		}
	};
	
	Action quitFile = new AbstractAction("Quit") {
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			saveOld();
			System.exit(0);
		}
	};
	
	Action newFile = new AbstractAction("New") {
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			saveOld();
			area.setText("");
			currentFile = "Untitled";
			setTitle(currentFile);
			changed = false;
			saveFile.setEnabled(false);
			saveFileAs.setEnabled(false);
		}
	};
	
	Action writerMode = new AbstractAction("Writer") {
		private static final long serialVersionUID = 1L;
		
		public void actionPerformed(ActionEvent e) {
			area.setBackground(Color.WHITE);
			area.setForeground(Color.BLACK);
			area.setCaretColor(Color.BLACK);
		}
		
	};
	
	Action programmerMode = new AbstractAction("Programmer") {
		private static final long serialVersionUID = 1L;
		Color background = new Color(51, 51, 51);
		
		public void actionPerformed(ActionEvent e) {
			area.setBackground(background);
			area.setForeground(Color.WHITE);
			area.setCaretColor(Color.WHITE);
		}
	};
	
	Action openAbout = new AbstractAction("About CoffeeText") {
		private static final long serialVersionUID = 1L;

		public void actionPerformed(ActionEvent e) {
			area.setText("CoffeeText is an open source text editor made and maintained \n"
					+ "by Saadiq Shaik. Anyone is free to use and distribute it for any \n"
					+ "purpose.");
		}
	};
	
	ActionMap am = area.getActionMap();
	Action cut = am.get(DefaultEditorKit.cutAction), copy = am.get(DefaultEditorKit.copyAction), 
			paste = am.get(DefaultEditorKit.pasteAction);
	
	private void saveFileAs() {
		if (dialog.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
			saveFile(dialog.getSelectedFile().getAbsolutePath());
		}
	}
	
	private void saveOld() {
		if (changed) {
			if (JOptionPane.showConfirmDialog(this, 
					"Would you like to save "+ currentFile +" ?",
					"Save",JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
						saveFile(currentFile);
			}
		}
	}
	
	private void readInFile(String fileName) {
		try {
			FileReader r = new FileReader(fileName);
			area.read(r,null);
			r.close();
			currentFile = fileName;
			setTitle(currentFile);
			changed = false;
		}
		
		catch (IOException e) {
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(this,"Editor can't find the file called " 
			+ fileName);
		}
	}
	
	private void saveFile(String fileName) {
		try {
			FileWriter w = new FileWriter(fileName);
			
			area.write(w);
			w.close();
			
			currentFile = fileName;
			setTitle(currentFile);
			
			changed = false;
			saveFile.setEnabled(false);
		}
		
		catch (IOException e) { }
	}
	
	public static void main(String[] args) {
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new CoffeeText();
			}
		});
	}	
}