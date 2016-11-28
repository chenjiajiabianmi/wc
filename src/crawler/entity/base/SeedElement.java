package crawler.entity.base;

import crawler.base.Extractor;


/**
 * a seed element is for create another web page fetch job:
 * targetUrl should match up with a specific extractor
 * 
 * @author Hammer
 *
 */
public class SeedElement {
	
	private String targetUrl;
	
	private Extractor extractor;
	
	/**
	 * if you need to save resource in the target page
	 * you need to specify the 'inside directory' base on the 'base directory'
	 * like we have a base 'C:\\temp\\' plus a inside directory 'folder\\'
	 * we make a new 'C:\\temp\\folder\\' to save resource in target page
	 * 
	 * notice that folder should better be unique
	 */
	private String directoryAppender;

	public String getTargetUrl() {
		return targetUrl;
	}

	public void setTargetUrl(String targetUrl) {
		this.targetUrl = targetUrl;
	}

	public Extractor getExtractor() {
		return extractor;
	}

	public void setExtractor(Extractor extractor) {
		this.extractor = extractor;
	}

	public String getDirectoryAppender() {
		return directoryAppender;
	}

	public void setDirectoryAppender(String directoryAppender) {
		this.directoryAppender = directoryAppender;
	}

	
	
	

}
