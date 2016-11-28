package persistence.entity;


import persistence.entity.base.BaseModel;

/**
 * save file info like 'JPG'
 * 1. originId -- JPG links must base on some web page, the web page must have a unique info to create a unique id
 * 2. resourceType -- it's not only for JPG, if you like to save MP4, AVI, this column will maintain the type
 * 3. orginalLocation -- if you would like to record a specific local location for the resource
 * 4. resourceName -- usually trim from the JPG link URL, like 'http://cdn.example.com/2015/0326/05/009.jpg' to 009.jpg
 * 5. url -- the resource URL, like 'http://cdn.example.com/2015/0326/05/009.jpg'
 * 
 * you should always maintain this by yourself: originId + resourceName SHOULD BE UNIQUE!!!
 * 
 * @author Hammer
 *
 */
public class Resource extends BaseModel{

	private String originId;
	
	private String resourceType;
	
	private String originalLocation;
	
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

	public void setResourceType(String string) {
		this.resourceType = string;
	}

	public String getOriginalLocation() {
		return originalLocation;
	}

	public void setOriginalLocation(String originalLocation) {
		this.originalLocation = originalLocation;
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

	public String toString() {
		return "<" + originId + ", " + resourceName + ">";
	}
}
