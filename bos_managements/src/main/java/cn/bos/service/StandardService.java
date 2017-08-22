package cn.bos.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.bos.model.Standard;

public interface StandardService {

	void save(Standard standard);

	List<Standard> findAll();

	Page<Standard> findPageData(Pageable pageable);
	//批量删除收派标准
	void delBatch(String[] idsarray);

}
