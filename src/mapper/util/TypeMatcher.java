package mapper.util;

import java.util.HashMap;
import java.util.Map;

import mapper.link.PageInfoMapper;
import mapper.link.ResourceMapper;
import persistence.entity.PageInfo;
import persistence.entity.Resource;

/**
 * when you create new type need to persist, you need to make add a key-value to current map.
 * 
 * @author Hammer
 *
 */
public class TypeMatcher {
	
	public static final Map<Object, Object> relationMap = new HashMap<Object, Object>();
	
	static {
		relationMap.put(Resource.class, ResourceMapper.class);
		relationMap.put(PageInfo.class, PageInfoMapper.class);
	}
	

}
