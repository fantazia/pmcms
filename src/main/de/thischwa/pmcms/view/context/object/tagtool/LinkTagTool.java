/*******************************************************************************
 * Poor Man's CMS (pmcms) - A very basic CMS generating static html pages.
 * http://poormans.sourceforge.net
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
package de.thischwa.pmcms.view.context.object.tagtool;

import java.io.File;
import java.io.IOException;


import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import de.thischwa.pmcms.exception.FatalException;
import de.thischwa.pmcms.livecycle.PojoHelper;
import de.thischwa.pmcms.tool.Link;
import de.thischwa.pmcms.tool.PathTool;
import de.thischwa.pmcms.view.ViewMode;
import de.thischwa.pmcms.view.context.IContextObjectCommon;
import de.thischwa.pmcms.view.context.IContextObjectNeedPojoHelper;
import de.thischwa.pmcms.view.context.IContextObjectNeedViewMode;
import de.thischwa.pmcms.view.renderer.RenderData;
import de.thischwa.pmcms.wysisygeditor.CKFileResource;

/**
 * Construct an a-tag. Mainly used by other context objects.
 * 
 * @version $Id: LinkTagTool.java 2227 2012-11-12 12:44:53Z th-schwarz $
 * @author <a href="mailto:th-schwarz@users.sourceforge.net">Thilo Schwarz</a>
 */
@Component(value="linktagtool")
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class LinkTagTool extends CommonXhtmlTagTool implements IContextObjectCommon, IContextObjectNeedPojoHelper, IContextObjectNeedViewMode {
	private static Logger logger = Logger.getLogger(LinkTagTool.class);
	private PojoHelper pojoHelper;
	private boolean isExportView = false;
	private boolean isExternalLink = false;
	
	@Autowired
	private Link link;	
	
	@Autowired
	private RenderData renderData;
	
	public LinkTagTool() {
		super("a");
	}

	@Override
	public void setViewMode(final ViewMode viewMode) {
		isExportView = viewMode.equals(ViewMode.EXPORT);
	}

	@Override
	public void setPojoHelper(PojoHelper pojoHelper) {
		this.pojoHelper = pojoHelper;
	}

	public LinkTagTool setHref(final String href) {
		link.init(href);
		isExternalLink = link.isExternal();
		String tempHref;
		if (isExternalLink)
			tempHref = href;
		else
			tempHref = href.replace(File.separatorChar, '/');
		return setAttribute("href", PathTool.encodePath(tempHref));
	}

	public LinkTagTool setAttribute(final String name, final String value) {
		super.setAttr(name, value);
		return this;
	}

	public LinkTagTool setTagValue(final String tagValue) {
		super.value(tagValue);
		return this;
	}

	/**
	 * Construct the a-tag.
	 */
	@Override
	public String toString() {
		String hrefString = super.getAttr("href");
		// 1. check, if the base attributes are set
		if (StringUtils.isBlank(hrefString))
			throw new IllegalArgumentException("'href' isn't set!");

		// 2. construct the tag for preview or export
		if (!isExternalLink) {
			CKFileResource cKFileResource = new CKFileResource(this.pojoHelper.getSite());
			cKFileResource.consructFromTagFromView(hrefString);
			if (isExportView) {
				try {
					File srcFile = cKFileResource.getFile();
					File destFile = cKFileResource.getExportFile();
					FileUtils.copyFile(srcFile, destFile);
					renderData.addCKResource(cKFileResource);
				} catch (IOException e) {
					logger.error("Error while copy [" + cKFileResource.getFile().getPath() + "] to [" + cKFileResource.getExportFile().getPath() + "]: " + e.getMessage(), e);
					throw new FatalException("Error while copy [" + cKFileResource.getFile().getPath() + "] to [" + cKFileResource.getExportFile().getPath() + "]: " + e.getMessage(), e);
				}
				this.setHref(cKFileResource.getTagSrcForExport(this.pojoHelper.getLevel()));
			} else
				this.setHref(cKFileResource.getTagSrcForPreview());
		}
		
		isExternalLink = false;
		return super.contructTag();
	}
}