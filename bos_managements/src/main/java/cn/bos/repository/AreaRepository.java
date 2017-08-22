package cn.bos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import cn.bos.model.Area;
@Repository
public interface AreaRepository extends JpaRepository<Area,String>,JpaSpecificationExecutor<Area>{
	
	@Query(value="delete from Area where id =?")
	@Modifying
	void delBatch(String id);
}
