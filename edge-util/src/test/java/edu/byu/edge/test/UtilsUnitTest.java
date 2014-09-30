package edu.byu.edge.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Author: Wyatt Taylor (wyatt_taylor@byu.edu)
 * Date: 09/30/2014
 *
 * @author Wyatt Taylor (wyatt_taylor@byu.edu)
 * @since 09/30/2014
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
		Base31Test.class,
		Base62Test.class,
		StringUtilsTest.class
})
public class UtilsUnitTest {
}
