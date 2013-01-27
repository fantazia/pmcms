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

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * Helper to construct a common xhtml-tag.
 *
 * @version $Id: CommonXhtmlTagTool.java 2210 2012-06-17 13:01:49Z th-schwarz $
 * @author <a href="mailto:th-schwarz@users.sourceforge.net">Thilo Schwarz</a>
 */
public class CommonXhtmlTagTool {
	protected Map<String, String> attributes = new HashMap<String, String>();
	private String name = null;
	private String value = null;
	
	protected CommonXhtmlTagTool(final String tagName) {
		this.name = tagName;
	}
	
	protected void setName(final String name) {
		this.name = name;
	}
	
	protected void value(final String value) {
		this.value = value;
	}
	
	protected void setAttr(final String key, final String value) {
		attributes.put(key, value);
	}
	
	protected String getAttr(final String key) {
		return attributes.get(key);
	}
		
	/**
	 * Does the basic construction of the tag.
	 */
	protected String contructTag() {
		if (StringUtils.isBlank(name) || attributes.isEmpty())
			throw new IllegalArgumentException("Basic props are missing!");
		StringBuilder tag = new StringBuilder();
		tag.append("<");
		tag.append(name);
		for (String key : attributes.keySet()) {
			tag.append(' ').append(key).append("=")
				.append('\"').append(StringUtils.defaultString(attributes.get(key))).append('\"');
		}
		if (StringUtils.isBlank(value))
			tag.append(" />");
		else 
			tag.append(">").append(value).append('<').append('/').append(name).append('>');
		
		attributes.clear();
		return tag.toString();
	}
}