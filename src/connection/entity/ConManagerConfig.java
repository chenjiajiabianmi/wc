package connection.entity;

/**
 * setting for PooledConnectionManager
 * when you need to specific your own parameter for the connection pool
 * use a constructor with parameter
 * 
 * @author Hammer
 *
 */
public class ConManagerConfig {
	private int maxTotal;
	private int maxPerRoute;
	private int maxRoute;

	public int getMaxTotal() {
		return maxTotal;
	}

	public void setMaxTotal(int maxTotal) {
		this.maxTotal = maxTotal;
	}

	public int getMaxPerRoute() {
		return maxPerRoute;
	}

	public void setMaxPerRoute(int maxPerRoute) {
		this.maxPerRoute = maxPerRoute;
	}

	public int getMaxRoute() {
		return maxRoute;
	}

	public void setMaxRoute(int maxRoute) {
		this.maxRoute = maxRoute;
	}

}
