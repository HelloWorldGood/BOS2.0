package cn.bos.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import cn.bos.model.FixedArea;
import cn.bos.model.Standard;

public interface FixedAreaService {

	Page<FixedArea> findPageData(Specification<FixedArea> specification,
			Pageable pageable);

	void save(FixedArea model);

	void delBatch(String[] idsarray);

	void associationCourierToFixedArea(FixedArea model, Integer courierId,
			Integer takeTimeId);

}
