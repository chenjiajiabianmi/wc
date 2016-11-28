package engine;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * when you need to implement anything to control the 'core task queue'
 * you can implement your synchronized tools here
 * 
 * @author Hammer
 *
 */
public class Zookeeper {

	private AtomicInteger poolCounter = null;
	
	private static Zookeeper myZookeeper = null;
	
	private Zookeeper() {
		poolCounter = new AtomicInteger(0);
	}
	
	
	public static Zookeeper getInstance() {
		if (myZookeeper == null) {
			synchronized(Zookeeper.class) {
				if(myZookeeper == null) {
					myZookeeper = new Zookeeper();
					
				}
			}
		}
		return myZookeeper;
	}
	
	public Integer getCounter() {
		return poolCounter.get();
	}
	
	public Integer increaseCounter() {
		return poolCounter.incrementAndGet();
	}
	
	public Integer decreaseCounter() {
		return poolCounter.decrementAndGet();
	}
	
	
}
