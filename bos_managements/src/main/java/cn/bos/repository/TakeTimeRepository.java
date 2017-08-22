package cn.bos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cn.bos.model.TakeTime;
@Repository
public interface TakeTimeRepository extends JpaRepository<TakeTime,Integer>{

}
