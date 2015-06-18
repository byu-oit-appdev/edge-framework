package edu.byu.edge.file;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by wct5 on 6/17/15.
 */
public class FileResolver {

	public static final List<String> DEFAULT_SEARCH_PATHS = Collections.unmodifiableList(Arrays.asList("/opt/prod", "/opt/prod/config", "/byu/prdusr", "/home/prdusr"));

	protected String fileName;
	protected List<String> pathsToSearch;
	protected File foundFile;

	/**
	 *
	 * @param fileName
	 */
	public FileResolver(final String fileName) {
		this(fileName, DEFAULT_SEARCH_PATHS);
	}

	/**
	 *
	 * @param fileName
	 * @param pathsToSearch
	 */
	public FileResolver(final String fileName, final List<String> pathsToSearch) {
		this.fileName = fileName;
		this.pathsToSearch = pathsToSearch;
		if (fileName == null || fileName.isEmpty()) throw new IllegalStateException("No file specified.");
		if (pathsToSearch == null || pathsToSearch.isEmpty()) throw new IllegalStateException("No paths to search provided.");
	}

	/**
	 *
	 * @return
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 *
	 * @param fileName
	 */
	public void setFileName(final String fileName) {
		this.fileName = fileName;
	}

	/**
	 *
	 * @return
	 */
	public List<String> getPathsToSearch() {
		return pathsToSearch;
	}

	/**
	 *
	 * @param pathsToSearch
	 */
	public void setPathsToSearch(final List<String> pathsToSearch) {
		this.pathsToSearch = pathsToSearch;
	}

	/**
	 *
	 */
	public void locateFile() {
		this.foundFile = _doLocateFile();
	}

	//good candidate for java 8 streams
	private File _doLocateFile() {
		for (final String path : pathsToSearch) {
			final File dir = new File(path);
			if (dir.exists() && dir.isDirectory()) {
				final File file = new File(dir, fileName);
				if (file.exists() && file.isFile()) {
					return file;
				}
			}
		}
		return null;
	}

	public boolean hasFoundFile() {
		return this.foundFile != null;
	}

	public File getFoundFile() {
		return foundFile;
	}
}
