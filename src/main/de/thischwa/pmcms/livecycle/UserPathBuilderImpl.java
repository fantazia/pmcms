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
 *  - GNU Lesser General Public License Version 2.1 or later (the "LGPL")
 *    http://www.gnu.org/licenses/lgpl.html
 * 
 *  - Mozilla Public License Version 1.1 or later (the "MPL")
 *    http://www.mozilla.org/MPL/MPL-1.1.html
 * 
 * == END LICENSE ==
 ******************************************************************************/
package de.thischwa.pmcms.livecycle;

import javax.servlet.ServletContext;

import de.thischwa.c5c.requestcycle.Context;
import de.thischwa.c5c.requestcycle.BackendPathBuilder;
import de.thischwa.pmcms.conf.InitializationManager;
import de.thischwa.pmcms.conf.PropertiesManager;
import de.thischwa.pmcms.model.domain.PoPathInfo;
import de.thischwa.pmcms.model.domain.pojo.Site;
import de.thischwa.pmcms.tool.PathTool;

/**
 * Implementation of the C5Connector's UserBathBuilder to build the correct user path for different {@link Site}s.
 */
public class UserPathBuilderImpl implements BackendPathBuilder {

	@Override
	public String getBackendPath(String urlPath, Context ctx, ServletContext servletContext) {
		Site site = getSite();
		if(site == null)
			return null;
		PropertiesManager pm =  InitializationManager.getBean(PropertiesManager.class);
		String resourceFolder = pm.getSiteProperty("pmcms.site.dir.resources.other");
		String path = String.format("%s/%s%s", PathTool.getURLFromFile(PoPathInfo.getSiteDirectory(site).getPath(), false),
				resourceFolder, urlPath);
		
		return path;
	}	
	
	private Site getSite() {
		SiteHolder siteHolder = InitializationManager.getBean(SiteHolder.class);
		Site site = siteHolder.getSite();
		return site;
	}

}
