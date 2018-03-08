/*
 * Created on 19-Jun-2004
 *
 * Holds the ravel and unravel options for OOCalc.
 */
package org.republic;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 * @author sfg
 * 
 * This tool will create an OOCalc file from content.xml and a template dir.
 * Later it will unravel an OOCalc file to content.xml for reading into other
 * classes.
 *  
 *  This works fine for OO v1.x.
 *  It is superceded for OO v2.x 
 */
public class OoCalcTool {

	static final int BUFFER = 2048;

	private Logger logger = Logger.getLogger(this.getClass().getName());

	public void createSxcFile(String templateDirStr, String contentXMLFile,
			String finalSxcFileName) {
		try {
			// Refresh the temporary make area for the sxc.
			// To do this, copy the contents of the temporary directory
			// over the top of the tmp/mksxc directory.
			File tmpMkSxcDir = new File("./tmp/mksxc/");
			// Wipe the dir, then re-create it from template.
			tmpMkSxcDir.delete();
			tmpMkSxcDir.mkdirs();
			File templateDir = new File(templateDirStr);
			copyDirectory(templateDir, tmpMkSxcDir);
			// Now copy in the new content.xml.
			logger.info("Copying: " + contentXMLFile + " to " + tmpMkSxcDir
					+ "content.xml");
			copyFile(new File(contentXMLFile), new File(tmpMkSxcDir
					+ File.separator + "content.xml"));
			BufferedInputStream origin = null;
			FileOutputStream dest = new
			// Out to the new final name!
			FileOutputStream(finalSxcFileName);
			ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(
					dest));
			out.setMethod(ZipOutputStream.DEFLATED);
			// get a list of files from current directory
			// Add them to the zip archive.
			addToZip(tmpMkSxcDir, dest, out, origin);
			out.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("Created Output file:" + finalSxcFileName);
	}

	public void copyDirectory(File srcDir, File dstDir) throws IOException {
		if (srcDir.isDirectory()) {
			if (!dstDir.exists()) {
				dstDir.mkdir();
			}

			String[] children = srcDir.list();
			for (int i = 0; i < children.length; i++) {
				copyDirectory(new File(srcDir, children[i]), new File(dstDir,
						children[i]));
			}
		} else {
			copyFile(srcDir, dstDir);
		}
	}

	public void copyFile(File src, File dst) throws IOException {
		InputStream in = new FileInputStream(src);
		OutputStream out = new FileOutputStream(dst);

		// Transfer bytes from in to out
		byte[] buf = new byte[1024];
		int len;
		while ((len = in.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		in.close();
		out.close();
	}

	public File extractContentToData(String oocalcFileName,
			String workingDirName, String finalDataFileName) {
		File finalDataFile = new File(finalDataFileName);
		logger.info("we are going to create: "+ finalDataFile.getAbsolutePath());
		// 1. Check dir paths.
		File ooCalcFile = new File(oocalcFileName);
		if (!ooCalcFile.exists()) {
			System.err.println("ooCalc file does not exist:" + oocalcFileName);
			return finalDataFile;
		}
		// Make the work Dir by wiping previous work dir.
		File workDir = new File(workingDirName);
		if (workDir.exists())
			workDir.delete();
		workDir.mkdirs();
		if (!workDir.exists()) {
			System.err.println("Unable to create new workDirs with name :"
					+ workingDirName);
			return finalDataFile;
		}
		//
		try
		{
			finalDataFile.createNewFile();
		}
		catch (Exception e)
		{
			logger.severe("Could not create required output data file: "
					+finalDataFile.getAbsolutePath()
					+ ", reason:"+e
					);
			return finalDataFile;
		}
		
		// 2. unzip the oocalcfile into the workdir
		String outFilename = ""; 
	    try {
	        // Open the ZIP file
	        //String inFilename = "infile.zip";
	        ZipInputStream in = new ZipInputStream(new FileInputStream(oocalcFileName));
	    
	        // Get the content.xml entry
	        ZipEntry entry = in.getNextEntry();
	        while (!entry.getName().equalsIgnoreCase("content.xml"))
	        {
	        	logger.info("Looking in ZIP at:" + entry.getName());
	        	entry = in.getNextEntry();
	        }
	        
	        // Open the output file
	        outFilename = workingDirName+ File.separator+"content.xml";
	        OutputStream out = new FileOutputStream(outFilename);
	        // Transfer bytes from the ZIP file to the output file
	        byte[] buf = new byte[1024];
	        int len;
	        while ((len = in.read(buf)) > 0) {
	            out.write(buf, 0, len);
	        }
	    
	        // Close the streams
	        out.close();
	        in.close();
	    } catch (IOException e) {
	    	logger.severe("Unable to read OOCALC file for content"+e);
	    	return finalDataFile;
	    }
		// @TODO THE MISSING BIT - HOW TO UNZIP!!

		// Now convert the content.xml into a data.xml file with XSLT
		//File contentXmlFile = new File(workDir+"content.xml");
		File contentXmlFile = new File(outFilename);
		if (!contentXmlFile.exists()) {
			System.err
					.println("unzip of ooCalc file failed - unable to find content.xml:"
							+ oocalcFileName
							+ " in location: "
							+ contentXmlFile.getAbsolutePath());
			return finalDataFile;
		}
		// Call xslt..
		ParseFile parseFile = new ParseFile();
		// Some sort of resource lesson to learn here. 
		// xsl/blah is in the JAR but not accessible from this point..
		logger.info("About to transform to content");
		//
		
		InputStream is = null;
		//if (oocalcFileName.toLowerCase().endsWith("sxc"))
			is = ParseFile.class.getClassLoader().getResourceAsStream(CommonNames.CONTENT2DATASXC);
		//else
			//is = ParseFile.class.getClassLoader().getResourceAsStream(CommonNames.CONTENT2DATAODS);
		//mapping.loadMapping(new InputSource(is));

		//parseFile.transformToContent(is, contentXmlFile.getAbsolutePath(), finalDataFile.getAbsolutePath());
		logger.info("transformed to "+ finalDataFile.getAbsolutePath());
		//    	transFormFromContent(contentXmlFile, dataXmlFile);
		if (!finalDataFile.exists()) {
			System.err.println("Create Data File failed");
			return finalDataFile;
		}
		// End.
		return finalDataFile;
	}

	public void addToZip(File dirFile, FileOutputStream dest,
			ZipOutputStream out, BufferedInputStream origin) {
		try {

			byte data[] = new byte[BUFFER];
			File f = dirFile;
			String files[] = f.list();

			for (int i = 0; i < files.length; i++) {
				System.out
						.println("Adding To Zip: " + dirFile.getAbsolutePath()
								+ File.separator + files[i]);
				File addFile = new File(dirFile.getAbsolutePath()
						+ File.separator + files[i]);
				//System.out.println("Test File " +addFile.getName() + "dir: "
				// +addFile.isDirectory());
				if (!addFile.isDirectory()) {
					FileInputStream fi = new FileInputStream(addFile);
					origin = new BufferedInputStream(fi, BUFFER);
					ZipEntry entry = new ZipEntry(files[i]);
					out.putNextEntry(entry);
					int count;
					while ((count = origin.read(data, 0, BUFFER)) != -1) {
						out.write(data, 0, count);
					}
					origin.close();
				}
			}
		} catch (Exception e) {
			logger.severe("Exception writing ZIP: " + e);
		}
	}
}