/*
 * Copyright (c) 2011, Ken Gilmer
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 * 
 * Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer 
 * in the documentation and/or other materials provided with the distribution. Neither the name of the Ken Gilmer nor the names 
 * of its contributors may be used to endorse or promote products derived from this software without specific prior written permission.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. 
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY,
 * OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; 
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING 
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.sprinkles.functions;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import org.sprinkles.Fn.Function;

/**
 * A function to return files and directories.
 * @author kgilmer
 *
 */
public class ReturnFilesFunction implements Function<File, Collection<File>> {
	/**
	 * Get all files (not directories).  Assumes input is a File or Collection of Files.
	 */
	public static Function<File, Collection<File>> GET_FILES_FN = new ReturnFilesFunction(true, false, null, null);
	/**
	 * Get all directories.  Assumes input is a File or Collection of Files.
	 */
	public static Function<File, Collection<File>> GET_DIRS_FN = new ReturnFilesFunction(false, true, null, null);
	/**
	 * Get all files and directories.  Assumes input is a File or Collection of Files.
	 */
	public static Function<File, Collection<File>> GET_FILES_AND_DIRS_FN = new ReturnFilesFunction(true, true, null, null);
	
	private final boolean incFile;
	private final boolean incDir;
	private final FileFilter ffilter;
	private final FilenameFilter fnfilter;

	public ReturnFilesFunction(boolean file, boolean dir, FileFilter ffilter, FilenameFilter fnfilter) {
		this.incFile = file;
		this.incDir = dir;
		this.ffilter = ffilter;
		this.fnfilter = fnfilter;
	}
	
	@Override
	public Collection<File> apply(File f) {		
		Collection<File> c = new ArrayList<File>();
		
		this.fileToCollection(f, c);
		
		return c;
	}
	
	private void fileToCollection(File f, Collection<File> container) {
		if ((f.isFile() && incFile) || (f.isDirectory() && incDir)) {
			container.add(f);
		} 

		File[] oa = null;
		
		if (ffilter != null) {
			oa = f.listFiles(ffilter);
		} else if (fnfilter != null) {
			oa = f.listFiles(fnfilter);
		} else {
			oa = f.listFiles();
		}
		
		if (oa != null && oa.length > 0) {
			for (File cf: Arrays.asList(oa)) {
				fileToCollection(cf, container);
			}
		}
	}
}