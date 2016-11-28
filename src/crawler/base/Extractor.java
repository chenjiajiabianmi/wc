package crawler.base;

import crawler.entity.base.ExtractedElements;

/**
 * it's the base extractor your should extends from,
 * you will using css selector or regex to extract info from the 'html' content
 * in the return parameter of type ExtractedElements
 * you should maintain 
 * 1. info you would like to persist in DB, you will have to implement your own Bean just like Resource.class
 * and also extends from BaseModel.class, then store them in List<BaseModel>
 * 2. links you would like to fetch after current page, store them in List<SeedElement>
 * 3. resources like 'jpg' you would like to save in your local folder, store them in List<ResourceElement>
 * 
 * 
 * @author Hammer
 *
 */
public abstract class Extractor {
	
	public abstract ExtractedElements extractDataModel(String detailPageUrl, String html);
	
}
