package cn.bos.serviceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.bos.model.TakeTime;
import cn.bos.repository.TakeTimeRepository;
import cn.bos.service.TakeTimeService;

@Service
@Transactional
public class TakeTimeServiceImpl implements TakeTimeService{

	@Autowired
	private TakeTimeRepository takeTimeRepository;
	
	@Override
	public List<TakeTime> findAll() {
		// TODO Auto-generated method stub
		return takeTimeRepository.findAll();
	}

}
