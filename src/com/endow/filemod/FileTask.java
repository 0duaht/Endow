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
package com.endow.filemod;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

public class FileTask extends SwingWorker<File, Void>{
	private File[] args;
	private File directory, inputFile, tempFile;
	private String fileName;
	private int bufferSize, method;
	private final String marker;
	private JLabel progressLabel;
	private final byte[] markerByte = {10, 109, 97, 114, 107, 101, 114, 10};

	public FileTask(File[] args, File directory, File inputFile, 
			File tempFile, String fileName, int bufferSize, int method, 
			JLabel progressLabel) throws UnsupportedEncodingException
	{
		this.args = args;
		this.directory = directory;
		this.inputFile = inputFile;
		this.tempFile = tempFile;
		this.fileName = fileName;
		this.bufferSize = bufferSize;
		this.method = method;
		this.progressLabel = progressLabel; 
		marker = new String(markerByte, "UTF-8");
	}
	
	// method 0
	public FileTask(File[] args, File directory, String fileName,
			int bufferSize, int method, JLabel progressLabel) throws UnsupportedEncodingException
	{
		this(args, directory, null, null, fileName, bufferSize, method, progressLabel);
	}
	
	// method 1
	public FileTask(File inputFile, File tempFile, int bufferSize,
			int method, JLabel progressLabel) throws UnsupportedEncodingException
	{
		this(null, null, inputFile, tempFile, null, bufferSize, method, progressLabel);
	}
	
	
	public File doInBackground() throws IOException
	{
		switch(method)
		{
		case 0:
			BufferedOutputStream output = null;
	        BufferedInputStream input = null;
	        File outputFile;
			try
			{
				if (!directory.exists())
					directory.mkdirs();
				outputFile = new File(directory, fileName);
		        byte[] buffer;
		        long pd = 0;
		        for (File file : args)
		        {
		        //	System.out.println(file.getAbsolutePath());
		        	pd += file.length();
		        }
		        for (int m = 0; m < args.length; m++)
		        {
		            int k;
		            if (m == 0)
		            	output = new BufferedOutputStream(new FileOutputStream(outputFile));
		            else
		            	output = new BufferedOutputStream(new FileOutputStream(outputFile, true));
		            input = new BufferedInputStream(new FileInputStream(args[m]));
		            buffer = new byte[bufferSize];
		            if (m == 0)
		            	swingEdit("Adding image file...");
		            else
		            	swingEdit("Endowing image with content...");
		            while ((k = input.read(buffer)) != -1)
		            {
		                output.write(buffer, 0, k);
		                output.flush();
		                if (isCancelled())
		                {
		                	setProgress(0);
		                	swingEdit("Operation stopped");
		                	output.close();
		                	input.close();
		                	System.out.println("killing this file");
		                	return null;
		                }
		                setProgress((int)((double)outputFile.length() / pd * 20) + 80);
		            }
		            buffer = null;
		            setProgress(0);
		            if (m == 1)
		            {
		            	output.write(marker.getBytes());
		            	output.flush();
		            }
		            output.close();
		            input.close();
		        }
		        return null;
	        }
			finally
			{
				if (output != null)
					output.close();
				if (input != null)
					input.close();
			}
		case 1:
			BufferedOutputStream output1 = null;
	    	BufferedInputStream inputNew = null;
	    	BufferedInputStream input1 = null;
			try
			{
				byte[] methodBuffer;	
		    	input1 = new BufferedInputStream(new FileInputStream(inputFile));
		    	int k = 0;
		    	StringBuilder me4 = new StringBuilder(11000000);
		    	methodBuffer = new byte[bufferSize];
		    	String bufferString;
		    	boolean found = false;
		    	swingEdit("Searching for Endow signature");
		    	while ((k = input1.read(methodBuffer)) != -1)
		    	{
		    		bufferString = new String(methodBuffer);
		    		me4.append(bufferString);
		    		int i = 0;
		    		if (isCancelled())
	                {
	                	setProgress(0);
	                	swingEdit("Operation stopped");
	                	input1.close();
	                }
		    		if ((i = me4.indexOf(marker)) != -1)
		    		{
		    			k = (i + marker.length());
		    			found = true;
		    			break;
		    		}
		    		if (me4.length() > (me4.capacity() - bufferSize))
		    			break;
		    	}
		    	bufferString = null;
		    	me4 = null;
		    	methodBuffer = null;
		    	input1.close();
		    	if (!found)
		    		return null;
		    	output1 = new BufferedOutputStream(new FileOutputStream(tempFile));
		    	inputNew = new BufferedInputStream(new FileInputStream(inputFile));
		    	inputNew.skip(k);
		    	methodBuffer = new byte[bufferSize];
		    	while ((k = inputNew.read(methodBuffer)) != -1)
		    	{
		    		if (isCancelled())
	                {
	                	setProgress(0);
	                	swingEdit("Operation stopped");
	                	output1.close();
	                	inputNew.close();
	                	return null;
	                }
		    		if (k < bufferSize)
		    			output1.write(methodBuffer, 0, k - 162);
		    		else
		    			output1.write(methodBuffer, 0, k);
		    		output1.flush();
		    	}
		    	methodBuffer = null;
		    	output1.close();
		    	inputNew.close();
		    	return tempFile;
			}
			finally
			{
				if (output1 != null)
					output1.close();
				if (inputNew != null)
					inputNew.close();
				if (input1 != null)
					input1.close();
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
