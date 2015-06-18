package edu.byu.edge.test;

import edu.byu.edge.file.FileModifiedCallback;
import edu.byu.edge.file.FileResolver;
import edu.byu.edge.file.FileWatcher;
import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by wct5 on 6/18/15.
 */
@RunWith(JUnit4.class)
public class FileWatcherTest {
	private static final Logger LOG = Logger.getLogger(FileWatcherTest.class);

	@Test
	public void testFileWatcher() throws IOException, InterruptedException {
		final String tmpPath = System.getProperty("java.io.tmpdir");
		final File file = new File(tmpPath, "filewatchertext.txt");
		if (file.exists()) file.delete();
		final FileResolver resolver = new FileResolver("filewatchertext.txt", Arrays.asList(tmpPath));
		resolver.locateFile();
		assertFalse(resolver.hasFoundFile());

		file.createNewFile();

		resolver.locateFile();

		assertTrue(resolver.hasFoundFile());

		final AtomicInteger modifiedCount = new AtomicInteger(0);

		final FileWatcher watcher = new FileWatcher(resolver, new FileModifiedCallback() {
			@Override
			public void fileModified(final FileWatcher fw) {
				LOG.info("File modified");
				modifiedCount.incrementAndGet();
			}
		}, 1);
		watcher.beginWatching();
		Thread.sleep(2500);
		file.delete();
		file.createNewFile();
		Thread.sleep(2500);
		watcher.stopWatching();
		assertEquals(2, modifiedCount.get());

		file.delete();
	}
}
