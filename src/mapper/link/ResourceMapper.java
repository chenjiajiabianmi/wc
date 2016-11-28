package mapper.link;

import java.util.List;

import persistence.entity.Resource;

import mapper.link.base.BaseMapper;


public interface ResourceMapper extends BaseMapper<Resource> {
	
	public List<Resource> selectByCodeAndName(Resource resource);
	
	public int insert(Resource resource);
	
}
