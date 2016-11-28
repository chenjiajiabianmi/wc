package mapper.link.base;

import persistence.entity.base.BaseModel;



public interface BaseMapper<T extends BaseModel> {
	
	public int insertWhetherDuplicate(T t);
}
