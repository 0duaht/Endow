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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.EmptyBorder;

public class HelpDialog extends JDialog {

	private final JPanel contentPanel = new JPanel();

	
	public void nimbusSet()
	{
		try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
        }	
	}

	/**
	 * Create the dialog.
	 */
	public HelpDialog(Frame frame, MainUI mainUI, String title) {
		super(frame, title);
		nimbusSet();
		setContentPane(new JLabel(new ImageIcon(
				mainUI.getClass().getResource("/com/endow/resources/image6.jpg"))));
		setBounds(100, 100, 479, 433);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setOpaque(false);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lbltheEndowMode = new JLabel(
				"The endow mode adds content behind images, songs, or any other file.");
		lbltheEndowMode.setForeground(Color.PINK);
		lbltheEndowMode.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		lbltheEndowMode.setBounds(10, 11, 427, 31);
		contentPanel.add(lbltheEndowMode);
		
		JLabel lblTheExtractMode = new JLabel(
				"The extract mode extracts content from files endowed with content.");
		lblTheExtractMode.setForeground(Color.PINK);
		lblTheExtractMode.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		lblTheExtractMode.setBounds(10, 43, 427, 31);
		contentPanel.add(lblTheExtractMode);
		
		JPanel panel = new JPanel();
		panel.setOpaque(false);
		panel.setBounds(10, 80, 217, 273);
		contentPanel.add(panel);
		panel.setLayout(null);
		
		JLabel lblEndowMode = new JLabel("Endow Mode");
		lblEndowMode.setForeground(Color.PINK);
		lblEndowMode.setBounds(62, 5, 100, 16);
		lblEndowMode.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		panel.add(lblEndowMode);
		
		JLabel lblNewLabel = new JLabel("- The app comes with some images");
		lblNewLabel.setForeground(Color.PINK);
		lblNewLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		lblNewLabel.setBounds(-1, 22, 224, 25);
		panel.add(lblNewLabel);
		
		JLabel lblImagesWhichAre = new JLabel("which are used if the option is ticked.");
		lblImagesWhichAre.setForeground(Color.PINK);
		lblImagesWhichAre.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		lblImagesWhichAre.setBounds(4, 35, 197, 25);
		panel.add(lblImagesWhichAre);
		
		JLabel lblHoweverToUse = new JLabel("- However, to use your own image or");
		lblHoweverToUse.setForeground(Color.PINK);
		lblHoweverToUse.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		lblHoweverToUse.setBounds(4, 49, 197, 25);
		panel.add(lblHoweverToUse);
		
		JLabel lblUntickTheOption = new JLabel("file, untick the options, and select");
		lblUntickTheOption.setForeground(Color.PINK);
		lblUntickTheOption.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		lblUntickTheOption.setBounds(5, 65, 199, 25);
		panel.add(lblUntickTheOption);
		
		JLabel lblAppropriateImageOr = new JLabel(" appropriate image or other file.");
		lblAppropriateImageOr.setForeground(Color.PINK);
		lblAppropriateImageOr.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		lblAppropriateImageOr.setBounds(2, 80, 197, 25);
		panel.add(lblAppropriateImageOr);
		
		JLabel lblFilesimagesThatCould = new JLabel("- Files/Images that could be used in");
		lblFilesimagesThatCould.setForeground(Color.PINK);
		lblFilesimagesThatCould.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		lblFilesimagesThatCould.setBounds(5, 95, 205, 25);
		panel.add(lblFilesimagesThatCould);
		
		JLabel lblSpoofingAreLimited = new JLabel("spoofing are limited in size to 10MB");
		lblSpoofingAreLimited.setForeground(Color.PINK);
		lblSpoofingAreLimited.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		lblSpoofingAreLimited.setBounds(5, 111, 196, 25);
		panel.add(lblSpoofingAreLimited);
		
		JLabel lblSelectFiles = new JLabel("- Select files that are to be hidden");
		lblSelectFiles.setForeground(Color.PINK);
		lblSelectFiles.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		lblSelectFiles.setBounds(4, 126, 205, 25);
		panel.add(lblSelectFiles);
		
		JLabel lblBehindTheImage = new JLabel(" behind the image");
		lblBehindTheImage.setForeground(Color.PINK);
		lblBehindTheImage.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		lblBehindTheImage.setBounds(3, 141, 204, 25);
		panel.add(lblBehindTheImage);
		
		JLabel lblInThis = new JLabel("- In this context, these files are not");
		lblInThis.setForeground(Color.PINK);
		lblInThis.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		lblInThis.setBounds(4, 155, 205, 25);
		panel.add(lblInThis);
		
		JLabel lblLimitedBySize = new JLabel("limited by size.");
		lblLimitedBySize.setForeground(Color.PINK);
		lblLimitedBySize.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		lblLimitedBySize.setBounds(6, 170, 206, 25);
		panel.add(lblLimitedBySize);
		
		JLabel lblSelectALocation = new JLabel("- Select a location to save endowed");
		lblSelectALocation.setForeground(Color.PINK);
		lblSelectALocation.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		lblSelectALocation.setBounds(2, 185, 203, 25);
		panel.add(lblSelectALocation);
		
		JLabel lblImagefileTo = new JLabel(" image/file to");
		lblImagefileTo.setForeground(Color.PINK);
		lblImagefileTo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		lblImagefileTo.setBounds(4, 198, 203, 29);
		panel.add(lblImagefileTo);
		
		JLabel lblEnterPreferred = new JLabel("- Enter preferred name for output");
		lblEnterPreferred.setForeground(Color.PINK);
		lblEnterPreferred.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		lblEnterPreferred.setBounds(4, 215, 203, 25);
		panel.add(lblEnterPreferred);
		
		JLabel lblThenStart = new JLabel("- Then start the process");
		lblThenStart.setForeground(Color.PINK);
		lblThenStart.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		lblThenStart.setBounds(4, 247, 205, 24);
		panel.add(lblThenStart);
		
		JLabel lblFile = new JLabel("file");
		lblFile.setForeground(Color.PINK);
		lblFile.setFont(new Font("Segoe UI", Font.BOLD, 11));
		lblFile.setBounds(7, 236, 46, 14);
		panel.add(lblFile);
		
		JPanel panel_1 = new JPanel();
		panel_1.setOpaque(false);
		panel_1.setLayout(null);
		panel_1.setBounds(236, 80, 217, 273);
		contentPanel.add(panel_1);
		
		JLabel lblExtractMode = new JLabel("Extract Mode");
		lblExtractMode.setForeground(Color.PINK);
		lblExtractMode.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		lblExtractMode.setBounds(69, 5, 95, 16);
		panel_1.add(lblExtractMode);
		
		JLabel lblThisMode = new JLabel("- This mode is used to extract content");
		lblThisMode.setForeground(Color.PINK);
		lblThisMode.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		lblThisMode.setBounds(4, 22, 205, 25);
		panel_1.add(lblThisMode);
		
		JLabel lblFromFilesCreated = new JLabel("from files created with Endow.");
		lblFromFilesCreated.setForeground(Color.PINK);
		lblFromFilesCreated.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		lblFromFilesCreated.setBounds(4, 35, 197, 25);
		panel_1.add(lblFromFilesCreated);
		
		JLabel lblChooseAppropriate = new JLabel("- Choose appropriate file");
		lblChooseAppropriate.setForeground(Color.PINK);
		lblChooseAppropriate.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		lblChooseAppropriate.setBounds(4, 49, 197, 25);
		panel_1.add(lblChooseAppropriate);
		
		JLabel lblThenSelect = new JLabel("- Select a location to extract content");
		lblThenSelect.setForeground(Color.PINK);
		lblThenSelect.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		lblThenSelect.setBounds(5, 65, 199, 25);
		panel_1.add(lblThenSelect);
		
		JLabel lblThenStartThe = new JLabel("- Then start the process.");
		lblThenStartThe.setForeground(Color.PINK);
		lblThenStartThe.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		lblThenStartThe.setBounds(4, 93, 197, 25);
		panel_1.add(lblThenStartThe);
		
		JLabel lblTo = new JLabel("to");
		lblTo.setForeground(Color.PINK);
		lblTo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		lblTo.setBounds(4, 80, 204, 25);
		panel_1.add(lblTo);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setOpaque(false);
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setForeground(Color.BLACK);
				okButton.setFont(new Font("Segoe UI", Font.BOLD, 11));
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				okButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent ev)
					{
						dispose();
					}
				});
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setForeground(Color.BLACK);
				cancelButton.setFont(new Font("Segoe UI", Font.BOLD, 11));
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
				cancelButton.addActionListener(new ActionListener(){
					public void actionPerformed(ActionEvent ev)
					{
						dispose();
					}
				});
			}
		}
	}
}
