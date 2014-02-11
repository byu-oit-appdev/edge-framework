package edu.byu.util;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: Wyatt Taylor (wyatt_taylor@byu.edu)
 * Date: 02/10/2014
 *
 * @author Wyatt Taylor (wyatt_taylor@byu.edu)
 * @since 02/10/2014
 */
@RunWith(JUnit4.class)
public class QueryUtilsUnitTest {

	private static final Logger LOG = Logger.getLogger(QueryUtilsUnitTest.class);

	private final SelectableQueryExecutor<Integer, Integer> selectableQueryExecutor = new DummySelectableQueryExecutor();
	private final UpdatableQueryExecutor<Integer> updatableQueryExecutor = new DummyUpdatableQueryExecutor();

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidInput1() {
		LOG.info("Testing invalid 1.");
		QueryUtils.doQueryWithParameterList(null, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidInput2() {
		LOG.info("Testing invalid 2.");
		QueryUtils.doUpdateQueryWithParameterList(null, null);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidInput3() {
		LOG.info("Testing invalid 3.");
		final ArrayList<Integer> list = new ArrayList<Integer>(2);
		list.add(1);
		QueryUtils.doQueryWithParameterList(list, selectableQueryExecutor, 0);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidInput4() {
		LOG.info("Testing invalid 4.");
		final ArrayList<Integer> list = new ArrayList<Integer>(2);
		list.add(1);
		QueryUtils.doUpdateQueryWithParameterList(list, updatableQueryExecutor, 0);
	}

	@Test
	public void testIllogicalInput1() {
		LOG.info("Testing illogical 1.");
		Assert.assertArrayEquals("Both bounds should be zero.", QueryUtils.determineParamBounds(0, 1), new int[] {0, 0});
	}

	@Test
	public void testBounds1() {
		LOG.info("Testing bounds 1.");
		doTestBounds(1, 12);
	}

	@Test
	public void testBounds2() {
		LOG.info("Testing bounds 2.");
		doTestBounds(2, 24);
	}

	@Test
	public void testBounds3() {
		LOG.info("Testing bounds 3.");
		doTestBounds(3, 36);
	}

	@Test
	public void testBounds4() {
		LOG.info("Testing bounds 4.");
		doTestBounds(4, 48);
	}

	@Test
	public void testBounds5() {
		LOG.info("Testing bounds 5.");
		doTestBounds(5, 21);
	}

	@Test
	public void testBounds6() {
		LOG.info("Testing bounds 6.");
		doTestBounds(10, 120);
	}

	@Test
	public void testBounds7() {
		LOG.info("Testing bounds 7.");
		doTestBounds(50, 600);
	}

	@Test
	public void testBounds8() {
		LOG.info("Testing bounds 8.");
		doTestBounds(101, 15000);
	}

	@Test
	public void testBounds9() {
		LOG.info("Testing bounds 9.");
		doTestBounds(1000, 350111);
	}

	private void doTestBounds(final int maxItems, final int maxSize) {
		for (int size = 0; size < maxSize; size++) {
			final int[] vals = QueryUtils.determineParamBounds(size, maxItems);
			int total = 0;
			for (int i = 0; i < vals.length - 1; i++) {
				final int n = vals[i + 1] - vals[i];
				Assert.assertFalse(String.format("Too many actual params in sublist. (size = %d, index = %d, max = %d, n = %d)", size, i, maxItems, n), n > maxItems);
				total += n;
			}
			Assert.assertEquals(String.format("Wrong number of actual params.(size = %d)", size), size, vals[vals.length - 1]);
			Assert.assertEquals(String.format("Wrong number of total params. (size = %d, total = %d)", size, total), size, total);
			if (size % 10000 == 9999) {
				if (LOG.isDebugEnabled()) LOG.debug(String.format("Processing size of %d.", size));
			}
		}
	}


	private static class DummySelectableQueryExecutor implements SelectableQueryExecutor<Integer, Integer> {
		@Override
		public List<Integer> doQuery(final List<Integer> list) {
			return list;
		}
	}

	private static class DummyUpdatableQueryExecutor implements UpdatableQueryExecutor<Integer> {
		@Override
		public int doQuery(final List<Integer> list) {
			return list.size();
		}
	}
}
