/*  Copyright (C) 2013 by Oduah Tobi
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *	to use, copy, modify, merge, publish, distribute, sub-license, and/or sell
 *	copies of the Software, and to permit persons to whom the Software is
 *	furnished to do so, subject to the following conditions:
 *	
 *	The above copyright notice and this permission notice shall be included in
 *	all copies or substantial portions of the Software.
 *	
 *	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *	IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *	FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *	AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *	LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *	OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *	THE SOFTWARE.
 */
package com.endow.main;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.UnsupportedLookAndFeelException;

import com.endow.filemod.FileTask;
import com.endow.imagemod.BuildImage;
import com.endow.zipmod.ZipTask;

public class MainUI extends WindowAdapter implements ActionListener, 
ItemListener, WindowListener, PropertyChangeListener {

	private JFrame frame;
	private JTextField outputText;
	private JRadioButton endowRadio, extractRadio;
	private JCheckBox imageCheck;
	private JLabel bufferLabel, progressStatic, progressLabel,
	imageFormat, nameLabel, mode;
	private JComboBox<String> bufferCombo;
	private JButton chooseImage, chooseFile, startButton,
	cancelButton, saveButton;
	private JMenuItem howToUse, furtherHelp;
	private JProgressBar progressBar;
	private ExecutorService executor;
	private File[] filesChosen = null;
	private File fileChosen = null;
	private File saveDirectory = null;
	private JTextField savePath;
	private File compressed = null;
	private boolean completed;
	private ZipTask newOne;
	private FileTask fileEndow;
	private BuildImage imageBuild;
	private final HelpDialog help = new HelpDialog(frame, this, "How to Use");
	private File imageFile = null;
	private File compressedOutput = null;
	private String fileName;
	private File tempFile0, tempFile1, tempFile2, tempFile3, tempFile4;
	private final String[] bufferStrings = {"5kB", "26kB", "70kB",
			"125kB", "256kB", "512kB"};
	private final int[] bufferFigures = {5120, 25600, 71680,
			128000, 256000, 512000}; 
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainUI window = new MainUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application. 
	 */
	public MainUI(){
		nimbusSet();
		initialize();
	}
	
	public void nimbusSet()
	{
		try {
            for (javax.swing.UIManager.LookAndFeelInfo info : 
            	javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | 
        		IllegalAccessException | UnsupportedLookAndFeelException ex) { }
	}
	
	public void windowClosing(WindowEvent ev)
	{
		if (imageFile != null)
			imageFile.delete();
		if (tempFile0 != null)
			tempFile0.delete();
		if (tempFile1 != null)
			tempFile1.delete();
		if (tempFile2 != null)
			tempFile2.delete();
		if (tempFile3 != null)
			tempFile3.delete();
		if (tempFile4 != null)
			tempFile4.delete();
	}
	
	public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getPropertyName().equals("progress")) {
            int progress = (Integer) evt.getNewValue();
            progressBar.setValue(progress);
        }
	}
	
	public void modButtons(int source, boolean status)
	{
		switch(source)
		{
		case 0:
			cancelButton.setEnabled(!status);
			extractRadio.setEnabled(status);
			imageCheck.setEnabled(status);
			chooseImage.setEnabled(status);
			chooseFile.setEnabled(status);
			saveButton.setEnabled(status);
			outputText.setEnabled(status);
			bufferCombo.setEnabled(status);
			break;
		case 1:
			cancelButton.setEnabled(!status);
			endowRadio.setEnabled(status);
			chooseFile.setEnabled(status);
			saveButton.setEnabled(status);
			bufferCombo.setEnabled(status);
			break;
		}
		
	}
	
	public void actionPerformed(ActionEvent ev)
	{
		if (ev.getSource() == chooseFile)
		{
			JFileChooser fileChooser = new JFileChooser();
			if (endowRadio.isSelected())
				fileChooser.setMultiSelectionEnabled(true);
			else
				fileChooser.setMultiSelectionEnabled(false);
			fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
			int backEnd = fileChooser.showOpenDialog(frame);
			if (backEnd == JFileChooser.CANCEL_OPTION)
				return;
			if (endowRadio.isSelected())
				filesChosen = fileChooser.getSelectedFiles();
			else
				fileChosen = fileChooser.getSelectedFile();
		}
		
		if (ev.getSource() == saveButton | ev.getSource() == savePath)
		{
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setMultiSelectionEnabled(false);
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int backEnd = fileChooser.showOpenDialog(frame);
			if (backEnd == JFileChooser.CANCEL_OPTION)
				return;
			saveDirectory = fileChooser.getSelectedFile();
			savePath.setText(saveDirectory.getAbsolutePath());
			savePath.setToolTipText(saveDirectory.getAbsolutePath());
		}
		
		if (ev.getSource() == chooseImage)
		{
			try
			{
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setMultiSelectionEnabled(false);
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				int backEnd = fileChooser.showOpenDialog(frame);
				if (backEnd == JFileChooser.CANCEL_OPTION)
					return;
				if (fileChooser.getSelectedFile() == null)
					return;
				if (imageFile != null)
				{
					imageFile.delete();
					imageFile = null;
				}
				File chosenFile = fileChooser.getSelectedFile();
				if (chosenFile.length() > 10000000)
				{
					JOptionPane.showMessageDialog(frame, "Image size too large. Please select"
							+ " an image below 10MB", "Image Error", JOptionPane.ERROR_MESSAGE);
					return;
				}
				if (chosenFile.getName().lastIndexOf('.') != -1)
					imageFormat.setText(chosenFile.getName().substring
							(chosenFile.getName().lastIndexOf('.')));
				else
					imageFormat.setText(".jpg");
				if (tempFile1 != null)
					tempFile1.delete();
				tempFile1 = File.createTempFile("endow", ".jpg");
				imageBuild = new BuildImage(chosenFile, tempFile1,
						bufferFigures[bufferCombo.getSelectedIndex()], 1, progressLabel);
				imageBuild.addPropertyChangeListener(MainUI.this);
				imageBuild.execute();
				try {
					imageFile = imageBuild.get();
					progressLabel.setText("");
				} catch (InterruptedException
						| ExecutionException | CancellationException  e) {
					e.printStackTrace();
					if (tempFile1 != null)
						tempFile1.delete();
					if (imageFile != null)
						imageFile.delete();
					return;
				}
			}
			catch(IOException except)
			{
				except.printStackTrace();
			}
		}
		
		if (ev.getSource() == cancelButton)
		{
			if (newOne != null)
				newOne.cancel(true);
			if (imageBuild != null)
				imageBuild.cancel(true);
			if (fileEndow != null)
				fileEndow.cancel(true);
			executor.shutdownNow();
			cancelButton.setEnabled(false);
			if (!startButton.isEnabled())
				startButton.setEnabled(true);
		}
		
		if (ev.getSource() == startButton)
		{
			startButton.setEnabled(false);
			executor = Executors.newCachedThreadPool();
			if (endowRadio.isSelected())
			{
				executor.execute(new Runnable()
				{
					public void run()
					{
						long t = System.currentTimeMillis();
						try
						{
							if (imageCheck.isSelected())
							{
								if (tempFile1 != null)
									tempFile1.delete();
								tempFile1 = File.createTempFile("endow", ".jpg");
								imageBuild = new BuildImage(MainUI.this, tempFile1,
										bufferFigures[bufferCombo.getSelectedIndex()], 0, progressLabel);
								imageBuild.addPropertyChangeListener(MainUI.this);
								imageBuild.execute();
								try {
									if (imageFile != null)
										imageFile.delete();
									imageFile = imageBuild.get();
									progressLabel.setText("");
								} catch (InterruptedException
										| ExecutionException | CancellationException  e) {
									e.printStackTrace();
									if (tempFile1 != null)
										tempFile1.delete();
									if (imageFile != null)
										imageFile.delete();
									return;
								}
							}
							if (imageFile != null & filesChosen != null & saveDirectory != null
									& outputText.getText().trim().matches("[^/*?><|\"\\\\]*"))
							{
								modButtons(0, false);
								if (tempFile0 != null)
									tempFile0.delete();
								tempFile0 = File.createTempFile("endow", ".zip");
								fileName = outputText.getText() + imageFormat.getText();
								newOne = new ZipTask(tempFile0, null, null, filesChosen,
										bufferFigures[bufferCombo.getSelectedIndex()], 0, progressLabel);
								newOne.addPropertyChangeListener(MainUI.this);	
								newOne.execute();
								try {
									if (compressed != null)
										compressed.delete();
									compressed = newOne.get();
								} catch (InterruptedException | ExecutionException
										| CancellationException e) {
									if (tempFile0 != null)
										tempFile0.delete();
									if (compressed != null)
										compressed.delete();
									return;
								}
								if (tempFile3 != null)
									tempFile3.delete();
								tempFile3 = constructIntercept();
								File[] finalFiles = {imageFile, tempFile3, compressed, tempFile3};
								fileEndow = new FileTask(finalFiles, saveDirectory, fileName,
										bufferFigures[bufferCombo.getSelectedIndex()], 0, progressLabel);								
								fileEndow.execute();
								newOne.addPropertyChangeListener(MainUI.this);
								fileEndow.addPropertyChangeListener(MainUI.this);
								try {
									fileEndow.get();
									completed = true;
								} catch (InterruptedException
										| ExecutionException | CancellationException  e) {
									if (tempFile0 != null)
										tempFile0.delete();
									if (tempFile3 != null)
										tempFile3.delete();
									if (compressed != null)
										compressed.delete();
									return;
								}
							}
							else
							{
								if (imageFile == null)
									JOptionPane.showMessageDialog(frame,"Please choose a valid image file",
											"Image Error", JOptionPane.ERROR_MESSAGE);
								else if (filesChosen == null)
									JOptionPane.showMessageDialog(frame, "Please choose valid files to endow image with.",
											"File Error", JOptionPane.ERROR_MESSAGE);
								else if (saveDirectory == null)
									JOptionPane.showMessageDialog(frame, 
											"Please choose valid location to save endowed image.", 
											"Save Error", JOptionPane.ERROR_MESSAGE);
								else if (!outputText.getText().trim().matches("[^/*?><|\"\\\\]*"))
									JOptionPane.showMessageDialog(frame, 
											"Please enter a valid name for the output file", 
											"Empty Text", JOptionPane.ERROR_MESSAGE);
							}
						}
						catch(FileNotFoundException except)
						{
							JOptionPane.showMessageDialog(frame, "Please choose a valid directory path", 
									"Write Error", JOptionPane.ERROR_MESSAGE);
						}
						catch (IOException except)
						{
							JOptionPane.showMessageDialog(frame, "Error while trying to endow file", 
									"Write Error", JOptionPane.ERROR_MESSAGE);
						}
						finally
						{
							if (!completed)
								progressLabel.setText("");
							if (compressed != null)
								compressed.delete();
							if (tempFile3 != null)
								tempFile3.delete();
							if (imageCheck.isSelected())
							{
								if (imageFile != null)
								{
									imageFile.delete();
								}
							}
							startButton.setEnabled(true);
							modButtons(0, true);
						}
						if (completed)
						{
							progressLabel.setText("Completed in: " + 
						(System.currentTimeMillis() - t) / 1000 + " seconds");
							completed = false;
						}
					}
				});
			}
			else
			{
				executor.execute(new Runnable()
				{
					long t = System.currentTimeMillis();
					public void run()
					{
						try
						{
							if (fileChosen != null & saveDirectory != null)
							{
								modButtons(1, false);
								if (tempFile2 != null)
								{
									System.out.println("tryint to delete - >" + tempFile2.delete());
								}
								tempFile2 = File.createTempFile("endow", ".zip");
								fileEndow = new FileTask(fileChosen, tempFile2,
										bufferFigures[bufferCombo.getSelectedIndex()], 1, progressLabel);
								fileEndow.execute();
								fileEndow.addPropertyChangeListener(MainUI.this);
								fileEndow.addPropertyChangeListener(MainUI.this);
								try {
									if (compressedOutput != null)
									{
										compressedOutput.delete();
									}
									compressedOutput = fileEndow.get();
									if (compressedOutput == null)
									{
										JOptionPane.showMessageDialog(frame, 
												"File not created by Endow", "Endow Error", JOptionPane.ERROR_MESSAGE);
										return;
									}
								} catch (InterruptedException
										| ExecutionException | CancellationException  e) {
									if (tempFile2 != null)
										tempFile2.delete();
									if (compressedOutput != null)
										compressedOutput.delete();
									return;
								}
								newOne = new ZipTask(saveDirectory, compressedOutput,
										bufferFigures[bufferCombo.getSelectedIndex()], 1, progressLabel);
								newOne.addPropertyChangeListener(MainUI.this);	
								newOne.execute();
								try {
									newOne.get();
									completed = true;
								} catch (InterruptedException | ExecutionException |
										CancellationException  e) {
									if (tempFile2 != null)
										tempFile2.delete();
									if (compressedOutput != null)
										compressedOutput.delete();
									return;
								}
							}
							else
							{
								if (fileChosen == null)
									JOptionPane.showMessageDialog(frame, 
											"Please choose a valid file to extract from.", 
											"File Error", JOptionPane.ERROR_MESSAGE);
								else if (saveDirectory == null)
									JOptionPane.showMessageDialog(frame,
											"Please choose valid location to save extracted file(s).",
											"Save Error", JOptionPane.ERROR_MESSAGE);
							}		
						}
						catch(IOException except)
						{
							JOptionPane.showMessageDialog(frame, 
									"Error while trying to extract file", "Write Error", JOptionPane.ERROR_MESSAGE);
						}
						finally
						{
							if (!completed)
								progressLabel.setText("");
							startButton.setEnabled(true);
							modButtons(1, true);
							if (compressedOutput != null)
								compressedOutput.delete();
							if (tempFile2 != null)
								tempFile2.delete();
						}
						if (completed)
						{
							progressLabel.setText("Completed in: " 
						+ (System.currentTimeMillis() - t) / 1000 + " seconds");
							completed = false;
						}
					}
				});
			}
		}
		
		
		
		if (ev.getSource() == howToUse)
		{
			help.setVisible(true);
			help.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		}
		
		if (ev.getSource() == furtherHelp)
		{
			JOptionPane.showMessageDialog(frame, "Send me a mail: oduaht@gmail.com", 
					"_where", JOptionPane.INFORMATION_MESSAGE);
		}
	}
	public void itemStateChanged(ItemEvent ev)
	{
		if (ev.getSource() == endowRadio)
		{
			imageCheck.setEnabled(true);
			imageCheck.setSelected(true);
			outputText.setEnabled(true);
			fileChosen = null; 
		}
		if (ev.getSource() == extractRadio)
		{
			imageCheck.setSelected(false);
			imageCheck.setEnabled(false);
			chooseImage.setEnabled(false);
			outputText.setEnabled(false);
			filesChosen = null;
		}
		if (ev.getSource() == imageCheck)
		{
			if (ev.getStateChange() == ItemEvent.DESELECTED)
			{
				chooseImage.setEnabled(true);
				imageFile = null;
				imageFormat.setText("");
			}
			else
			{
				if (imageFile != null)
					imageFile.delete();
				chooseImage.setEnabled(false);
				imageFormat.setText(".jpg");
			}
		}
	}
	
	public File constructIntercept() throws IOException
	{
		if (tempFile4 != null)
			tempFile4.delete();
		tempFile4 = File.createTempFile("intercept", ".zip");
		BufferedInputStream input = new BufferedInputStream(
				this.getClass().getResourceAsStream("/com/endow/resources/intercept.zip"));
		BufferedOutputStream output = new BufferedOutputStream(new FileOutputStream(tempFile4));
		byte[] buffer = new byte[bufferFigures[bufferCombo.getSelectedIndex()]];
		int k;
		while ((k = input.read(buffer)) != -1)
		{
			output.write(buffer, 0, k);
			output.flush();
		}
		output.close();
		input.close();
		return tempFile4;
	}
	
	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.getContentPane().setForeground(Color.BLACK);
		frame.setBounds(100, 100, 430, 383);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.getContentPane().setLayout(null);
		frame.addWindowListener(this);
		frame.setContentPane(new JLabel(
				new ImageIcon(getClass().getResource("/com/endow/resources/background.jpg"))));
		
		mode = new JLabel("Mode: ");
		mode.setForeground(Color.BLACK);
		mode.setFont(new Font("Segoe UI", Font.BOLD, 13));
		mode.setBounds(39, 22, 67, 23);
		frame.getContentPane().add(mode);
		
		endowRadio = new JRadioButton("Endow");
		endowRadio.setSelected(true);
		endowRadio.setFocusPainted(false);
		endowRadio.setOpaque(false);
		endowRadio.setForeground(Color.BLACK);
		endowRadio.setFont(new Font("Segoe UI", Font.BOLD, 13));
		endowRadio.setBounds(116, 22, 109, 23);
		endowRadio.addItemListener(this);
		frame.getContentPane().add(endowRadio);
		
		extractRadio = new JRadioButton("Extract");
		extractRadio.setFocusPainted(false);
		extractRadio.setForeground(Color.BLACK);
		extractRadio.setOpaque(false);
		extractRadio.setFont(new Font("Segoe UI", Font.BOLD, 13));
		extractRadio.setBounds(272, 22, 109, 23);
		extractRadio.addItemListener(this);
		frame.getContentPane().add(extractRadio);
		
		ButtonGroup radioButtons = new ButtonGroup();
		radioButtons.add(endowRadio);
		radioButtons.add(extractRadio);
		
		chooseFile = new JButton("Choose File (s)");
		chooseFile.setFocusPainted(false);
		chooseFile.setForeground(Color.BLACK);
		chooseFile.setOpaque(false);
		chooseFile.addActionListener(this);
		chooseFile.setFont(new Font("Segoe UI", Font.BOLD, 13));
		chooseFile.setBounds(39, 105, 130, 31);
		frame.getContentPane().add(chooseFile);
		
		imageCheck = new JCheckBox("Use accompanying image");
		imageCheck.setFocusPainted(false);
		imageCheck.setForeground(Color.BLACK);
		imageCheck.setOpaque(false);
		imageCheck.setFont(new Font("Segoe UI", Font.BOLD, 13));
		imageCheck.setBounds(39, 63, 206, 23);
		imageCheck.setSelected(true);
		imageCheck.addItemListener(this);
		frame.getContentPane().add(imageCheck);
		
		chooseImage = new JButton("Choose image");
		chooseImage.setFocusPainted(false);
		chooseImage.setFont(new Font("Segoe UI", Font.BOLD, 13));
		chooseImage.setForeground(Color.BLACK);
		chooseImage.setEnabled(false);
		chooseImage.setOpaque(false);
		chooseImage.setBounds(251, 61, 121, 31);
		chooseImage.addActionListener(this);
		frame.getContentPane().add(chooseImage);
		
		nameLabel = new JLabel("Output Filename: ");
		nameLabel.setForeground(Color.BLACK);
		nameLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
		nameLabel.setBounds(39, 155, 115, 17);
		frame.getContentPane().add(nameLabel);
		
		outputText = new JTextField();
		outputText.setFont(new Font("Tahoma", Font.PLAIN, 13));
		outputText.setBounds(168, 148, 158, 32);
		frame.getContentPane().add(outputText);
		outputText.setColumns(10);
		
		imageFormat = new JLabel(".jpg");
		imageFormat.setForeground(Color.BLACK);
		imageFormat.setFont(new Font("Segoe UI", Font.BOLD, 13));
		imageFormat.setBounds(327, 154, 59, 31);
		frame.getContentPane().add(imageFormat);
		
		progressStatic = new JLabel("Progress:");
		progressStatic.setForeground(Color.BLACK);
		progressStatic.setFont(new Font("Segoe UI", Font.BOLD, 13));
		progressStatic.setBounds(20, 238, 76, 23);
		frame.getContentPane().add(progressStatic);
		
		progressBar = new JProgressBar();
		progressBar.setForeground(Color.GREEN);
		progressBar.setBounds(84, 238, 265, 23);
		frame.getContentPane().add(progressBar);	
		
		startButton = new JButton("Start");
		startButton.setFocusPainted(false);
		startButton.setForeground(Color.BLACK);
		startButton.setOpaque(false);
		startButton.addActionListener(this);
		startButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		startButton.setBounds(103, 299, 89, 31);
		frame.getContentPane().add(startButton);
		
		cancelButton = new JButton("Cancel");
		cancelButton.setFocusPainted(false);
		cancelButton.setForeground(Color.BLACK);
		cancelButton.setOpaque(false);
		cancelButton.addActionListener(this);
		cancelButton.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		cancelButton.setBounds(228, 298, 89, 31);
		cancelButton.setEnabled(false);
		frame.getContentPane().add(cancelButton);
		
		bufferLabel = new JLabel("Buffer Size:");
		bufferLabel.setForeground(Color.BLACK);
		bufferLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
		bufferLabel.setBounds(74, 189, 76, 23);
		frame.getContentPane().add(bufferLabel);
		
		bufferCombo = new JComboBox(bufferStrings);
		bufferCombo.setFont(new Font("Segoe UI", Font.PLAIN, 14));
		bufferCombo.setOpaque(false);
		bufferCombo.setBounds(169, 191, 121, 23);
		bufferCombo.setSelectedIndex(2);
		frame.getContentPane().add(bufferCombo);
		
		savePath = new JTextField();
		savePath.setEditable(false);
		savePath.setFont(new Font("Segoe UI", Font.BOLD, 13));
		savePath.setText("Save to...");
		savePath.setBounds(190, 103, 115, 31);
		frame.getContentPane().add(savePath);
		savePath.addActionListener(this);
		savePath.setColumns(10);
		
		saveButton = new JButton("Save to");
		saveButton.setFont(new Font("Segoe UI", Font.BOLD, 13));
		saveButton.setBounds(302, 101, 95, 35);
		saveButton.addActionListener(this);
		frame.getContentPane().add(saveButton);
		
		progressLabel = new JLabel("");
		progressLabel.setForeground(Color.BLACK);
		progressLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
		progressLabel.setBounds(20, 265, 401, 28);
		frame.getContentPane().add(progressLabel);
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu mnAbout = new JMenu("About");
		mnAbout.setFont(new Font("Segoe UI", Font.BOLD, 13));
		menuBar.add(mnAbout);
		
		howToUse = new JMenuItem("How to Use");
		howToUse.setFont(new Font("Segoe UI", Font.BOLD, 12));
		howToUse.addActionListener(this);
		mnAbout.add(howToUse);
		
		furtherHelp = new JMenuItem("Further Help");
		furtherHelp.setFont(new Font("Segoe UI", Font.BOLD, 12));
		furtherHelp.addActionListener(this);
		mnAbout.add(furtherHelp);
	}
}
