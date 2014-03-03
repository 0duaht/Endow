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
package com.endow.zipmod;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

public class ZipTask extends SwingWorker<File, Object>{
	private File[] args;
	private int bufferSize;
	private File tempFile, directory, zipFileName;
	private JLabel progressLabel;
	int method;
	
	public ZipTask(File tempFile, File directory, File zipFileName, 
			File[] args, int bufferSize, int method, JLabel progressLabel)
	{
		this.args = args;
		this.bufferSize = bufferSize;
		this.tempFile = tempFile;
		this.progressLabel = progressLabel;
		this.method = method;
		this.directory = directory;
		this.zipFileName = zipFileName;
	}
	
	// method 0
	public ZipTask(File tempFile, File[] args, int bufferSize, int method, JLabel progressLabel)
	{
		this(tempFile, null, null, args, bufferSize, method, progressLabel);
	}
	
	// method 1
	public ZipTask(File directory, File zipFileName, int bufferSize,
			int method, JLabel progressLabel)
	{
		this(null, directory, zipFileName, null, bufferSize, method, progressLabel);
	}
	
	public File doInBackground() throws IOException
	{
		switch(method)
		{
		case 0:
			ZipOutputStream output = null;
			BufferedInputStream input = null;
			try
			{
				File compressedFile = tempFile;
				output = new ZipOutputStream(new FileOutputStream(compressedFile));
				long pd = 0;
				for (File file : args)
					pd += file.length();
				for (int fileIndex = 0; fileIndex < args.length; fileIndex++)
				{
					File currentFile = args[fileIndex];
					output.putNextEntry(new ZipEntry(currentFile.getName()));
					input = new BufferedInputStream(new FileInputStream(currentFile));
		            byte[] buffer = new byte[bufferSize];
		            int k;
		            String nameLabel = currentFile.getName();
		            StringBuilder nameGen = new StringBuilder(150);
		            if (nameLabel.length() > 30)
		            {
		            	nameGen.append(String.format("%.26s", nameLabel));
		            	nameGen.append("(...)");
		            	if (nameLabel.lastIndexOf('.') != -1)
		            		nameGen.append(nameLabel.substring(nameLabel.lastIndexOf('.')));
		            	nameLabel = new String(nameGen);
		            }
		            progressLabel.setText("Caching :*: " + nameLabel + " :*: to temporary archive");
		            while ((k = input.read(buffer)) != -1)
		            {
		                output.write(buffer, 0, k);
		                output.flush();
		                if (isCancelled())
		                {
		                	setProgress(0);
		                	swingEdit("Operation stopped.");
		                	output.close();
		                	input.close();
		                	return null;
		                }
		                if (((double)compressedFile.length()/pd * 80) < 100)
		                	setProgress((int)((double)compressedFile.length()/pd * 80));
		                else
		                	setProgress(100);
		            }
		            input.close();
		            input = null;
				}
				return compressedFile;
			}
			finally
			{
				if (output != null)
					output.close();
				if (input != null)
					input.close();
			}
		case 1:
			BufferedInputStream input1 = null;
			BufferedOutputStream output1 = null;
			try
			{
				ZipFile zip = new ZipFile(zipFileName);
				Enumeration zipEntries = zip.entries();
				int constantStatic = 100/zip.size();
				int constant = 0;
				if (!directory.exists())
					directory.mkdirs();
				while(zipEntries.hasMoreElements())
				{
					ZipEntry zipEntry = (ZipEntry)zipEntries.nextElement();
					File finalDestination = new File(directory, zipEntry.getName());
					input1 = new BufferedInputStream(zip.getInputStream(zipEntry));
					output1 = new BufferedOutputStream(new FileOutputStream(finalDestination));
		            byte[] buffer = new byte[bufferSize];
		            int k;
		            if (isCancelled())
	                {
	                	setProgress(0);
	                	swingEdit("Operation stopped.");
	                	zip.close();
	                	output1.close();
	                	input1.close();
	                	return null;
	                }
		            String nameLabel = zipEntry.getName();
		            StringBuilder nameGen = new StringBuilder(150);
		            if (nameLabel.length() > 30)
		            {
		            	nameGen.append(String.format("%.26s", nameLabel));
		            	nameGen.append("(...)");
		            	if (nameLabel.lastIndexOf('.') != -1)
		            		nameGen.append(nameLabel.substring(nameLabel.lastIndexOf('.')));
		            	nameLabel = new String(nameGen);
		            }
		            swingEdit("Writing " + nameLabel + " to directory");
		            while ((k = input1.read(buffer)) != -1)
		            {
		                output1.write(buffer, 0, k);
		                output1.flush();
		                if (isCancelled())
		                {
		                	setProgress(0);
		                	swingEdit("Operation stopped.");
		                	zip.close();
		                	output1.close();
		                	input1.close();
		                	return null;
		                }
		                setProgress((int)((double)(finalDestination.length())/zipEntry.getSize() 
		                		* constantStatic + constant));
		            }
		            constant += constantStatic;
		            output1.close();
		            input1.close();
				}
				setProgress(0);
				zip.close();
			}
			finally
			{
				if (input1 != null)
					input1.close();
				if (output1 != null)
					output1.close();
			}
			return null;
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
