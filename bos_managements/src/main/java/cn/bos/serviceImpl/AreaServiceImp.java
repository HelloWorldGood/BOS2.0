package cn.bos.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bos.model.Area;
import cn.bos.model.Courier;
import cn.bos.repository.AreaRepository;
import cn.bos.service.AreaService;

@Service
@Transactional
public class AreaServiceImp  implements AreaService{

	@Autowired
	private AreaRepository areaRepository;
	@Override
	public void saveBatch(List<Area> areas) {
		areaRepository.save(areas);
	}
	@Override
	public Page<Area> findPageData(Specification<Area> specification,
			Pageable pageable) {
		// TODO Auto-generated method stub
		return areaRepository.findAll(specification, pageable);
	}
	@Override
	public void save(Area area) {
		// TODO Auto-generated method stub
		areaRepository.save(area);
	}
	@Override
	public void delBatch(String[] idsarray) {
		for(String ids : idsarray){
			areaRepository.delBatch(ids);
		}
	}
}
