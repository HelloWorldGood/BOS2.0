package cn.bos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import cn.bos.model.FixedArea;


@Repository
public interface FixedAreaRepository extends JpaRepository<FixedArea, Integer>,JpaSpecificationExecutor<FixedArea> {
	@Query(value="delete from FixedArea where id =?")
	@Modifying
	void delBatch(String id);
}
