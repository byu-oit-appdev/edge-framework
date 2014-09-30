package edu.byu.edge.test;

import edu.byu.edge.util.Base62;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Author: Wyatt Taylor (wyatt_taylor@byu.edu)
 * Date: 09/30/2014
 *
 * @author Wyatt Taylor (wyatt_taylor@byu.edu)
 * @since 09/30/2014
 */
@RunWith(JUnit4.class)
public class Base62Test {

	private static final Logger LOG = Logger.getLogger(Base62Test.class);

	@Test
	public void testBasic() {
		Assert.assertEquals("0", Base62.encode(0));
		Assert.assertEquals("1", Base62.encode(1));
		Assert.assertEquals("2", Base62.encode(2));
		Assert.assertEquals("3", Base62.encode(3));
		Assert.assertEquals("4", Base62.encode(4));
		Assert.assertEquals("5", Base62.encode(5));
		Assert.assertEquals("6", Base62.encode(6));
		Assert.assertEquals("7", Base62.encode(7));
		Assert.assertEquals("8", Base62.encode(8));
		Assert.assertEquals("9", Base62.encode(9));
		Assert.assertEquals("a", Base62.encode(10));
		Assert.assertEquals("b", Base62.encode(11));
		Assert.assertEquals("c", Base62.encode(12));
		Assert.assertEquals("d", Base62.encode(13));
		Assert.assertEquals("e", Base62.encode(14));
		Assert.assertEquals("f", Base62.encode(15));
		Assert.assertEquals("g", Base62.encode(16));
		Assert.assertEquals("h", Base62.encode(17));
		Assert.assertEquals("i", Base62.encode(18));
		Assert.assertEquals("j", Base62.encode(19));
		Assert.assertEquals("k", Base62.encode(20));
		Assert.assertEquals("l", Base62.encode(21));
		Assert.assertEquals("m", Base62.encode(22));
		Assert.assertEquals("n", Base62.encode(23));
		Assert.assertEquals("o", Base62.encode(24));
		Assert.assertEquals("p", Base62.encode(25));
		Assert.assertEquals("q", Base62.encode(26));
		Assert.assertEquals("r", Base62.encode(27));
		Assert.assertEquals("s", Base62.encode(28));
		Assert.assertEquals("t", Base62.encode(29));
		Assert.assertEquals("u", Base62.encode(30));
		Assert.assertEquals("v", Base62.encode(31));
		Assert.assertEquals("w", Base62.encode(32));
		Assert.assertEquals("x", Base62.encode(33));
		Assert.assertEquals("y", Base62.encode(34));
		Assert.assertEquals("z", Base62.encode(35));
		Assert.assertEquals("A", Base62.encode(36));
		Assert.assertEquals("B", Base62.encode(37));
		Assert.assertEquals("C", Base62.encode(38));
		Assert.assertEquals("D", Base62.encode(39));
		Assert.assertEquals("E", Base62.encode(40));
		Assert.assertEquals("F", Base62.encode(41));
		Assert.assertEquals("G", Base62.encode(42));
		Assert.assertEquals("H", Base62.encode(43));
		Assert.assertEquals("I", Base62.encode(44));
		Assert.assertEquals("J", Base62.encode(45));
		Assert.assertEquals("K", Base62.encode(46));
		Assert.assertEquals("L", Base62.encode(47));
		Assert.assertEquals("M", Base62.encode(48));
		Assert.assertEquals("N", Base62.encode(49));
		Assert.assertEquals("O", Base62.encode(50));
		Assert.assertEquals("P", Base62.encode(51));
		Assert.assertEquals("Q", Base62.encode(52));
		Assert.assertEquals("R", Base62.encode(53));
		Assert.assertEquals("S", Base62.encode(54));
		Assert.assertEquals("T", Base62.encode(55));
		Assert.assertEquals("U", Base62.encode(56));
		Assert.assertEquals("V", Base62.encode(57));
		Assert.assertEquals("W", Base62.encode(58));
		Assert.assertEquals("X", Base62.encode(59));
		Assert.assertEquals("Y", Base62.encode(60));
		Assert.assertEquals("Z", Base62.encode(61));
		Assert.assertEquals("10", Base62.encode(62));
	}

	@Test
	public void testSymmetry() {
		final long a = System.currentTimeMillis();
		for (int i = 0; i < 0x1b4d89f; i++) {
			Assert.assertEquals(i, Base62.decode(Base62.encode(i)));
		}
		final long b = System.currentTimeMillis();
		LOG.info("Base62 run: " + (b - a));
	}
}
