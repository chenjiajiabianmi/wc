package engine.entity;

/**
 * use for batch jobs with link form like:
 * http://www.example.com/article/20161112/page/1
 * 
 * the transformation will change the link to a batch job like
 * http://www.example.com/article/20161112/page/1
 * http://www.example.com/article/20161112/page/2
 * http://www.example.com/article/20161112/page/3
 * http://www.example.com/article/20161112/page/4
 * ...
 * 
 * @author Hammer
 *
 */
public class Source {

	private String directoryBase;
	
	private String targetUrl;
	
	private String name;
	
	private int totalPageNum;

	public String getDirectoryBase() {
		return directoryBase;
	}

	public void setDirectoryBase(String directoryBase) {
		this.directoryBase = directoryBase;
	}

	public String getTargetUrl() {
		return targetUrl;
	}

	public void setTargetUrl(String targetUrl) {
		this.targetUrl = targetUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getTotalPageNum() {
		return totalPageNum;
	}

	public void setTotalPageNum(int totalPageNum) {
		this.totalPageNum = totalPageNum;
	}
	
	
}
