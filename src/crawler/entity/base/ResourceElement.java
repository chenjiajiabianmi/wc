package crawler.entity.base;


/**
 * resource need to save, like JPG, MP4, 
 * BUT YOU MUST MAKE SURE IT FITS THE RIGHT TYPE!!
 * like JPG target url returns a resource file with a type "image/jpeg" in the reponse
 * 
 * @author Hammer
 *
 */
public class ResourceElement {
	
	/**
	 * specify where the resource fetched from,
	 * there must be a specific initial web page for this resource
	 * 
	 * notice that this does not need to be UNIQUE
	 */
	private String originId;
	
	/**
	 * the type of file
	 * like "image/jpeg"
	 */
	private String resourceType;
	
	private String resourceName;
	
	private String url;


	public String getOriginId() {
		return originId;
	}

	public void setOriginId(String originId) {
		this.originId = originId;
	}

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	private String originalLocation;


	public String getOriginalLocation() {
		return originalLocation;
	}

	public void setOriginalLocation(String originalLocation) {
		this.originalLocation = originalLocation;
	}
	
	

}
