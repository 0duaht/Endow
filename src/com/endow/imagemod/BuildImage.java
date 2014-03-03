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
package com.endow.imagemod;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import com.endow.main.MainUI;

public class BuildImage extends SwingWorker<File, Void>{
	private MainUI main;
	private int bufferSize;
	private File fileSent, tempFile;
	private int method;
	private JLabel progressLabel;
	
	
	public BuildImage(MainUI mainUI, File tempFile, File fileSent, 
			int bufferSize, int method, JLabel progressLabel)
	{
		this.main = mainUI;
		this.tempFile = tempFile;
		this.fileSent = fileSent;
		this.bufferSize = bufferSize;
		this.method = method;
		this.progressLabel = progressLabel;
	}
	// constructor for method 0
	public BuildImage(MainUI mainUI, File tempFile, int bufferSize, 
			int method, JLabel progressLabel)
	{
		this(mainUI, tempFile, null, bufferSize, method, progressLabel);
	}
	
	// constructor for method 1
	public BuildImage(File fileSent, File tempFile, int bufferSize, int method, JLabel progressLabel)
	{
		this(null, tempFile, fileSent, bufferSize, method, progressLabel);
	}
	
	public File doInBackground() throws IOException
	{
		BufferedInputStream imageInput = null, imageInput1 = null;
		BufferedOutputStream output = null, output1 = null;
		switch(method)
		{
		case 0:
			try
			{
				File imageFile = tempFile;
				Random generate = new Random();
				int count = 1 + generate.nextInt(6);
				imageInput = new BufferedInputStream(main.getClass().
						getResourceAsStream("/com/endow/resources/image" + count + ".jpg"));
				output = new BufferedOutputStream(new FileOutputStream(imageFile));
				byte[] buffer = new byte[bufferSize];
				int k;
				progressLabel.setText("Extracting image file from archive...");
				while ((k = imageInput.read(buffer)) != -1)
				{
					output.write(buffer, 0, k);
					output.flush();
					if (isCancelled())
	                {
	                	setProgress(0);
	                	swingEdit("Operation stopped");
	                	return null;
	                }
				}
				output.close();
				imageInput.close();
				return imageFile;
			}
			finally
			{
				if (output != null)
					output.close();
				if (imageInput != null)
					imageInput.close();
			}
		case 1:
			try
			{
				File imageFile1 = tempFile;
				imageInput1 = new BufferedInputStream(new FileInputStream(fileSent));
				output1 = new BufferedOutputStream(new FileOutputStream(imageFile1));
				byte[] buffer1 = new byte[bufferSize];
				int k1;
				progressLabel.setText("Building image from location...");
				while ((k1 = imageInput1.read(buffer1)) != -1)
				{
					output1.write(buffer1, 0, k1);
					output1.flush();
					if (isCancelled())
	                {
	                	setProgress(0);
	                	swingEdit("Operation stopped");
	                	return null;
	                }
					setProgress((int)((double)imageFile1.length() / fileSent.length() * 100));
				}
				setProgress(0);
				output1.close();
				imageInput1.close();
				return imageFile1;
			}
			finally
			{
				if (output1 != null)
					output1.close();
				if (imageInput1 != null)
					imageInput1.close();
			}
		default:
			return null;
		}
	}
	public void swingEdit(final String message)
	{
		SwingUtilities.invokeLater(new Runnable()
    	{
    		public void run()
    		{
    			progressLabel.setText(message);
    		}
    	});
	}
}
