package crawler.moeyo;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import crawler.base.Extractor;
import crawler.entity.base.ExtractedElements;
import crawler.entity.base.ResourceElement;

public class DetailPageExtractor extends Extractor{
	
	private static final Logger logger = Logger.getLogger(DetailPageExtractor.class);
	
	private final static Object syncLock = new Object();
	
	private static DetailPageExtractor extractor;
	
	private DetailPageExtractor() {
		
	}
	
	public static DetailPageExtractor getExtractor() {
		if(extractor == null) {
			synchronized(syncLock){
				if(extractor == null) {
					extractor = new DetailPageExtractor();
				}
			}
		}
		return extractor;
	}
	
	private static final String Resource_Type = "image/jpeg";
	
	private static final String HTTP_Header = "http://";
	
	private static final String JPG_RESOURCE_LINK_MATCHER = "div[itemprop=articleBody] p a[class=colorbox]";

	private static final String PAGE_TITLE_MATCHER = "h1[class=title figures_title]";
	
	private static final String TIME_STAMP_MATCHER = "div[class=main_wrap] time";
	
	private static final String ATTRIBUTE_KEY_HREF = "href";
	
	private static final String ATTRIBUTE_KEY_TIME_STAMP = "datetime";
	
	@Override
	public ExtractedElements extractDataModel(String detailPageUrl, String html) {
		if(html == null) {
			logger.fatal("FATAL -- detail page content fetch failed : " + detailPageUrl);
			return null;
		}
		Document doc = Jsoup.parse(html);
		String pageUid = getUniqueId(detailPageUrl);
		Elements elements = doc.select(JPG_RESOURCE_LINK_MATCHER);
		List<ResourceElement> resources = new ArrayList<ResourceElement>();
		for(Element element: elements) {
			String url = element.attr(ATTRIBUTE_KEY_HREF);
			ResourceElement resource = new ResourceElement();
			resource.setUrl(url);
			resource.setOriginalLocation(detailPageUrl);
			resource.setResourceName(createFileName(url));
			resource.setOriginId(pageUid);
			resource.setResourceType(Resource_Type);
			resources.add(resource);
		}
		ExtractedElements extractedElements = new ExtractedElements();
		extractedElements.setResources(resources);
		return extractedElements;
	}
	
	public String getUniqueId(String url) {
		String[] tmp = url.split("/");
		return tmp[tmp.length - 1];
	}
	
	public static String createFileName(String url) {
		String tmp = url.replace(HTTP_Header, "");
		return tmp.replace("/", "#");
	}
	
	public static void main(String args[]) {
		String tmp = "http://cdn.moeyo.com/2016/0217/03/001.jpg";
		logger.info(createFileName(tmp));
	}

}
