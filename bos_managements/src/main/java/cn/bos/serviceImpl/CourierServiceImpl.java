package cn.bos.serviceImpl;

import java.util.List;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bos.model.Courier;
import cn.bos.repository.CourierRepository;
import cn.bos.service.CourierService;

@Service
@Transactional
public class CourierServiceImpl implements CourierService{

	@Autowired
	private CourierRepository courierRepository;

	@Override
	public void save(Courier courier) {
		courierRepository.save(courier);
	}

	@Override
	public Page<Courier> findPageData(Pageable pageable) {
		// TODO Auto-generated method stub
		return courierRepository.findAll(pageable);
	}

	@Override
	public void delBatch(String[] idsarray) {
		for(String ids: idsarray){
			Integer id = Integer.parseInt(ids);
			//courierRepository.delete(id);
			courierRepository.updateDelTag(id);
		}
	}

	@Override
	public Page<Courier> findPageData(Specification<Courier> specification,
			Pageable pageable) {
		// TODO Auto-generated method stub
		return courierRepository.findAll(specification, pageable);
	}

	@Override
	public List<Courier> findNoAssociation() {
		// 封装Specification
		Specification<Courier> specification = new Specification<Courier>() {
					@Override
					public Predicate toPredicate(Root<Courier> root,
							CriteriaQuery<?> query, CriteriaBuilder cb) {
						// TODO Auto-generated method stub
						Predicate p = cb.isEmpty(root.get("fixedAreas").as(Set.class));
						return p;
					}
		};
		return courierRepository.findAll(specification);
	}
}
