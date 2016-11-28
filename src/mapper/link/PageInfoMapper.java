package mapper.link;

import java.util.List;

import mapper.link.base.BaseMapper;
import persistence.entity.PageInfo;
import persistence.entity.Resource;


public interface PageInfoMapper extends BaseMapper<PageInfo> {
	
	public List<PageInfo> selectByCode(PageInfo pageInfo);
	
	public int insert(PageInfo pageInfo);
	
}
