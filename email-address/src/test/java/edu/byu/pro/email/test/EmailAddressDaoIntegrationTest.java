package edu.byu.pro.email.test;

import edu.byu.pro.email.EmailAddressDao;
import edu.byu.pro.email.ProEmailAddress;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Wyatt Taylor on 1/17/17.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:test-context.xml",
		"classpath:proEmailAddress/annotation-config.xml"
})
public class EmailAddressDaoIntegrationTest {

	private static final Logger LOG = LogManager.getLogger(EmailAddressDaoIntegrationTest.class);

	private EmailAddressDao emailAddressDao;

	@Autowired
	public void setEmailAddressDao(final EmailAddressDao emailAddressDao) {
		this.emailAddressDao = emailAddressDao;
	}

	@Test
	public void testEmailsMatch() {
		final ProEmailAddress rec = emailAddressDao.getEmailAddressRec("903201972");
		final String basicEmailAddress = emailAddressDao.getBasicEmailAddress("903201972");
		final String workEmailAddress = emailAddressDao.getWorkEmailAddress("903201972");
		LOG.debug(rec);
		Assert.assertEquals(rec.getEmailAddress(), basicEmailAddress);
		Assert.assertEquals(rec.getWorkEmailAddress(), workEmailAddress);
	}
}
