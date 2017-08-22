package cn.bos.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import cn.bos.model.Area;
import cn.bos.model.Courier;

public interface AreaService {

	void saveBatch(List<Area> areas);

	Page<Area> findPageData(Specification<Area> specification,
			Pageable pageable);

	void save(Area area);

	void delBatch(String[] idsarray);

}
