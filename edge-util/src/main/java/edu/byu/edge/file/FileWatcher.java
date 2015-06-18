package edu.byu.edge.file;

import java.io.File;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by wct5 on 6/17/15.
 */
public class FileWatcher {

	public static final int DEFAULT_CHECK_INTERVAL = 60;

	protected FileModifiedCallback callback;
	protected FileResolver fileResolver;
	protected int checkIntervalSeconds;

	protected String distinguisherString = "";
	protected int distinguisherInt = 0;

	protected long lastModified = 0;
	protected long size = 0;
	protected final AtomicBoolean watch = new AtomicBoolean(false);
	protected FileWatcherTask task;

	public FileWatcher(final FileResolver fileResolver, final FileModifiedCallback callback) {
		this(fileResolver, callback, DEFAULT_CHECK_INTERVAL);
	}

	public FileWatcher(final FileResolver fileResolver, final FileModifiedCallback callback, final int checkIntervalSeconds) {
		this.fileResolver = fileResolver;
		this.callback = callback;
		this.checkIntervalSeconds = checkIntervalSeconds;
	}

	public String getDistinguisherString() {
		return distinguisherString;
	}

	public void setDistinguisherString(final String distinguisherString) {
		this.distinguisherString = distinguisherString;
	}

	public int getDistinguisherInt() {
		return distinguisherInt;
	}

	public void setDistinguisherInt(final int distinguisherInt) {
		this.distinguisherInt = distinguisherInt;
	}

	public File getFile() {
		return fileResolver.getFoundFile();
	}

	public void beginWatching() {
		if (task != null) return;
		this.watch.set(true);
		this.task = new FileWatcherTask(this);
		this.task.start();
	}

	protected void checkFile() {
		if (fileResolver.hasFoundFile()) {
			final File file = fileResolver.getFoundFile();
			final long lm = file.lastModified();
			final long fl = file.length();
			if (lm != lastModified || fl != size) {
				lastModified = lm;
				size = fl;
				callback.fileModified(this);
			}
		}
	}

	public void stopWatching() {
		if (task == null) return;
		this.watch.set(false);
		try {
			task.interrupt();
		} catch (final Throwable ignore) {
		}
	}

	private static class FileWatcherTask extends Thread {
		private FileWatcher fw;

		public FileWatcherTask(final FileWatcher fw) {
			super("FileWatcher-" + fw.distinguisherString + fw.distinguisherInt);
			this.fw = fw;
			super.setDaemon(true);
		}

		@Override
		public void run() {
			while (fw.watch.get()) {
				final long a = System.currentTimeMillis();
				try {
					fw.checkFile();
				} catch (final Throwable ignore) {
				}
				final long b = System.currentTimeMillis();
				try {
					sleep(fw.checkIntervalSeconds * 1000 - (b - a));
				} catch (final Throwable ignore) {
				}
			}
		}
	}
}
