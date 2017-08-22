package cn.bos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import cn.bos.model.Courier;

@Repository
public interface CourierRepository extends JpaRepository<Courier, Integer>,JpaSpecificationExecutor<Courier> {

	@Query(value = "update Courier set deltag = '1' where id =? ")
	@Modifying
	void updateDelTag(Integer id);
	
}
