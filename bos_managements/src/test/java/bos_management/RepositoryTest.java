/*package bos_management;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import cn.bos.model.Standard;
import cn.bos.repository.StandardRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations="classpath:applicationContext.xml")
public class RepositoryTest {
	@Autowired
	private StandardRepository standardRepository;
	
	@Test
	@Transactional
	@Rollback(false)
	public void testSave(){
		Standard standard = new Standard();
		standard.setMaxWeight(100);
		standardRepository.save(standard);
	}
}
*/