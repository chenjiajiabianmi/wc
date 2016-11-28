package crawler.entity.base;

import java.util.List;

import persistence.entity.base.BaseModel;


/**
 * when extracting a HTML content,
 * could save content in instance (C extends ExtractElements) when you have specific data need to persist
 * 
 * @author Hammer
 *
 */
public class ExtractedElements {
	
	/**
	 * the URL direct to HTML content 
	 */
	private String targetUrl;
	
	private boolean shouldBeDiscard = false;

	public String getTargetUrl() {
		return targetUrl;
	}

	public void setTargetUrl(String targetUrl) {
		this.targetUrl = targetUrl;
	}

	public boolean isShouldBeDiscard() {
		return shouldBeDiscard;
	}

	public void setShouldBeDiscard(boolean shouldBeDiscard) {
		this.shouldBeDiscard = shouldBeDiscard;
	}
	
	/**
	 * the resources extracted from current page,
	 * which you would like to save as local file
	 */
	private List<ResourceElement> resources;
	
	private List<SeedElement> seeds;

	public List<ResourceElement> getResources() {
		return resources;
	}

	public void setResources(List<ResourceElement> resources) {
		this.resources = resources;
	}

	public List<SeedElement> getSeeds() {
		return seeds;
	}

	public void setSeeds(List<SeedElement> seeds) {
		this.seeds = seeds;
	}
	
	/**
	 * information beans (C extends BaseModel) which need to be persist
	 */
	private List<BaseModel> persistInfos;

	public List<BaseModel> getPersistInfos() {
		return persistInfos;
	}

	public void setPersistInfos(List<BaseModel> persistInfos) {
		this.persistInfos = persistInfos;
	}
	
	
	
	
}
