/*******************************************************************************
 * Poor Man's CMS (pmcms) - A very basic CMS generating static html pages.
 * http://pmcms.sourceforge.net
 * Copyright (C) 2004-2013 by Thilo Schwarz
 * 
 * == BEGIN LICENSE ==
 * 
 * Licensed under the terms of any of the following licenses at your
 * choice:
 * 
 *  - GNU General Public License Version 2 or later (the "GPL")
 *    http://www.gnu.org/licenses/gpl.html
 * 
 *  - GNU Lesser General Public License Version 2.1 or later (the "LGPL")
 *    http://www.gnu.org/licenses/lgpl.html
 * 
 *  - Mozilla Public License Version 1.1 or later (the "MPL")
 *    http://www.mozilla.org/MPL/MPL-1.1.html
 * 
 * == END LICENSE ==
 ******************************************************************************/
package de.thischwa.pmcms.view.renderer;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Component;

import de.thischwa.pmcms.view.renderer.resource.IVirtualFile;


/**
 * Data collector for rendering.
 */
@Component
public class RenderData {
	private Set<File> ckResources;
	
	private Set<File> files;
	
	public RenderData() {
		ckResources = Collections.synchronizedSet(new HashSet<File>());
		files = Collections.synchronizedSet(new HashSet<File>());
	}
	
	public void clear() {
		ckResources.clear();
		files.clear();
	}
	
	public synchronized void addCKResource(final File file) {
		if(!ckResources.contains(file.getAbsoluteFile()))
			ckResources.add(file.getAbsoluteFile());
	}
	
	public void addCKResource(final IVirtualFile vf) {
		addCKResource(vf.getBaseFile());
	}
	
	public Set<File> getCkResources() {
		return ckResources;
	}
	
	public void addFile(final File file) {
		if(!file.isDirectory())
			files.add(file.getAbsoluteFile());
	}
	
	public Set<File> getFilesToCopy() {
		Set<File> all = new HashSet<File>(files);
		all.addAll(ckResources);
		return all;
	}
}
