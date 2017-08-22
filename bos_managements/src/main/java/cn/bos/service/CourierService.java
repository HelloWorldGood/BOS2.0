package cn.bos.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import cn.bos.model.Courier;

public interface CourierService {

	void save(Courier courier);

	Page<Courier> findPageData(Pageable pageable);

	void delBatch(String[] idsarray);

	Page<Courier> findPageData(Specification<Courier> specification,
			Pageable pageable);

	List<Courier> findNoAssociation();

}
