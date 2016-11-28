package crawler.moeyo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import persistence.entity.PageInfo;
import persistence.entity.base.BaseModel;
import crawler.base.Extractor;
import crawler.entity.base.ExtractedElements;
import crawler.entity.base.SeedElement;


public class ListPageExtractor extends Extractor{
	
	private static final Logger logger = Logger.getLogger(ListPageExtractor.class);
	
	private final static Object syncLock = new Object();
	
	private static ListPageExtractor extractor;
	
	private ListPageExtractor() {
		
	}
	
	public static ListPageExtractor getExtractor() {
		if(extractor == null) {
			synchronized(syncLock){
				if(extractor == null) {
					extractor = new ListPageExtractor();
				}
			}
		}
		return extractor;
	}
	
	private static final String ARTICLE_ELEMENTS_FITURE = "div[class=article] div[class=title figures] a";
	
	private static final String ATRICLE_ELEMENTS_EVENT = "div[class=article] div[class=title event] a";
	
	private static final String ARTICLE_ELEMENTS_NEWS = "div[class=article] div[class=title news] a";
	
	private static final String ATTRIBUTE_KEY_HREF = "href";
	
	private static final String ATTRIBUTE_KEY_TITLE = "title";
	
	

	@Override
	public ExtractedElements extractDataModel(String detailPageUrl, String html) {
		Document doc = Jsoup.parse(html);
		Elements elements = doc.select(ARTICLE_ELEMENTS_FITURE);
		elements.addAll(doc.select(ATRICLE_ELEMENTS_EVENT));
		elements.addAll(doc.select(ARTICLE_ELEMENTS_NEWS));
		
		List<SeedElement> seeds = new ArrayList<SeedElement>();
		List<BaseModel> pageInfos = new ArrayList<BaseModel>();
		for(Element element: elements) {
			String targetUrl = element.attr(ATTRIBUTE_KEY_HREF);
			if(targetUrl == null) {
				logger.fatal("FATAL -- extract inside links encounter fatal error, selector match up failed");
				continue;
			}
			logger.info("seed link : <" + targetUrl + ">");
			String uid = getArticleId(targetUrl);
			if(uid == null) {
				logger.fatal("FATAL -- extract link unique id failed");
				continue;
			}
			// seems useless
			String title = element.attr(ATTRIBUTE_KEY_TITLE);
			
			SeedElement seed = new SeedElement();
			seed.setTargetUrl(targetUrl);
			seed.setDirectoryAppender(uid + "\\");
			seed.setExtractor(DetailPageExtractor.getExtractor());
			seeds.add(seed);
			
			PageInfo pageInfo = new PageInfo();
			pageInfo.setUniqueId(uid);
			pageInfo.setTitle(title);
			pageInfo.setUrl(targetUrl);
			Date now = new Date();
			pageInfo.setCreateDate(now);
			pageInfo.setModifyDate(now);
			pageInfos.add(pageInfo);
		}
		ExtractedElements baseElement = new ExtractedElements();
		baseElement.setSeeds(seeds);
		baseElement.setPersistInfos(pageInfos);
		return baseElement;
	}
	
	public String getArticleId(String url) {
		String[] tmp = url.split("/");
		return tmp[tmp.length - 1];
	}

}
