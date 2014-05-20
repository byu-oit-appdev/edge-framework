package edu.byu.merge.bl.impl;

import com.google.common.base.Predicate;
import com.google.common.collect.Maps;
import edu.byu.callObject.*;
import edu.byu.callObject.spring.AtswasdDaoSupport;
import edu.byu.merge.bl.MergeController;
import edu.byu.merge.bl.MergeReportGenerator;
import edu.byu.merge.bl.Merger;
import edu.byu.pro.merge.da.PersonMergeDao;
import edu.byu.pro.merge.domain.PersonMerge;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Author: Wyatt Taylor (wyatt_taylor@byu.edu)
 * Date: 05/07/2014
 *
 * @author Wyatt Taylor (wyatt_taylor@byu.edu)
 * @since 05/07/2014
 */
public class MergeControllerImpl implements MergeController, InitializingBean {

	private static final Logger LOG = Logger.getLogger(MergeControllerImpl.class);

	private static final String WASD_USER = "               ";

	private static final String WASD_LIB = "person";

	private static final String WASD_METHOD = "DelPersonMerge";

	private static final String WASD_VER = "0001";

	private String mergeSubscriber;
	private boolean emailReport;
	private String fromAddress;
	private String[] recipients;
	private String emailSubject;
	private MergeReportGenerator reportGenerator;

	private JavaMailSender mailSender;
	private Merger merger;
	private PersonMergeDao personMergeDao;
	private AtswasdDaoSupport atswasdDao;

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.hasText(mergeSubscriber, "A merge subscriber must be specified.");
		if (emailReport) {
			Assert.notNull(mailSender, "A valid mail sender is required.");
			Assert.hasText(fromAddress, "A valid from email address must be provided.");
			Assert.hasText(emailSubject, "A valid email subject must be provided.");
			Assert.notEmpty(recipients, "At least one recipient is required.");
			Assert.notNull(reportGenerator, "A report generator must be provided.");
		}
	}

	@Override
	public void doMerges() {
		final List<PersonMerge> list = personMergeDao.findAllMergesForSubscriber(mergeSubscriber);
		if (LOG.isDebugEnabled()) LOG.debug("Found " + list.size() + " merges.");
		final Map<PersonMerge, Map<String, Integer>> resultList = new TreeMap<PersonMerge, Map<String, Integer>>();
		for (final PersonMerge pm : list) {
			try {
				resultList.put(pm, merger.doMerge(pm));
			} catch (final Exception ignore) {
			}
		}
		if (emailReport && resultList.size() > 0) {
			sendResultEmail(reportGenerator.apply(resultList));
		}
		for (final PersonMerge m : Maps.filterEntries(resultList, new ErrorFreeMergesFilter()).keySet()) {
			try {
				final WasdReply reply = atswasdDao.callFcsObject(WASD_VER, WASD_USER, WASD_LIB, WASD_METHOD, createWasdData(m));
			} catch (final Exception e) {
				LOG.error("Error deleting person merge " + m + ".", e);
			}
		}
	}

	private String createWasdData(final PersonMerge m) {
		return String.format("%-9s%-10s%-23s", m.getOldPersonId(), m.getSubscriber(), "").replaceAll(" ", "\0");
	}

	private void sendResultEmail(final String body) {
		try {
			final MimeMessageHelper helper = new MimeMessageHelper(mailSender.createMimeMessage());
			for (final String r : recipients) {
				helper.addTo(r);
			}
			helper.setSubject(emailSubject);
			helper.setFrom(fromAddress);
			helper.setText(body, true);
			mailSender.send(helper.getMimeMessage());
		} catch (final Exception e) {
			LOG.error("Error sending result email.", e);
		}
	}

	public void setMergeSubscriber(final String mergeSubscriber) {
		this.mergeSubscriber = mergeSubscriber;
	}

	public void setEmailReport(final boolean emailReport) {
		this.emailReport = emailReport;
	}

	public void setFromAddress(final String fromAddress) {
		this.fromAddress = fromAddress;
	}

	public void setRecipients(final String... recipients) {
		this.recipients = recipients;
	}

	public void setEmailSubject(final String emailSubject) {
		this.emailSubject = emailSubject;
	}

	public void setReportGenerator(final MergeReportGenerator reportGenerator) {
		this.reportGenerator = reportGenerator;
	}

	public void setMailSender(final JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	public void setMerger(final Merger merger) {
		this.merger = merger;
	}

	public void setPersonMergeDao(final PersonMergeDao personMergeDao) {
		this.personMergeDao = personMergeDao;
	}

	public void setAtswasdDao(final AtswasdDaoSupport atswasdDao) {
		this.atswasdDao = atswasdDao;
	}

	private static class ErrorFreeMergesFilter implements Predicate<Map.Entry<PersonMerge, Map<String, Integer>>> {
		@Override
		public boolean apply(final Map.Entry<PersonMerge, Map<String, Integer>> input) {
			return !input.getValue().values().contains(-1);
		}
	}

}
