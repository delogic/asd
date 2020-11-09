package br.com.delogic.asd.housekeeping;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.delogic.jfunk.Has;

public abstract class HouseKeepingJobBase implements HouseKeepingJob {

	protected final Logger log = LoggerFactory.getLogger(getClass());
	protected final String directoryPath;

	public HouseKeepingJobBase(String directory) {
		this.directoryPath = directory;
	}

	public void run() {
		List<File> files = listFilesFromDirectory();
		if (!Has.content(files)) {
			return;
		}

		List<File> filesToBeDeleted = filterFilesToBeDeleted(files);
		if (!Has.content(filesToBeDeleted)) {
			return;
		}

		for (File toBeDeleted : filesToBeDeleted) {

			if (!shouldDelete(toBeDeleted))
				continue;

			if (deleteFile(toBeDeleted)) {
				logDeletedFile(toBeDeleted);

			} else {
				logNotDeletedFile(toBeDeleted);

			}

		}
	}

	public void logDeletedFile(File toBeDeleted) {
		if (log.isDebugEnabled()) {
			log.debug("Deleted file:" + toBeDeleted.getName());
		}
	}

	public void logNotDeletedFile(File toBeDeleted) {
		if (log.isWarnEnabled()) {
			log.warn("File not deleted:" + toBeDeleted);
		}
	}

	public boolean deleteFile(File toBeDeleted) {
		try {
			return toBeDeleted.delete();
		} catch (Exception e) {
			log.error("Error deleting file:" + (toBeDeleted != null ? toBeDeleted.getName() : "arquivo nulo"), e);
			return false;
		}
	}

	public boolean shouldDelete(File toBeDeleted) {
		return true;
	}

	public abstract List<File> filterFilesToBeDeleted(List<File> files);

	public List<File> listFilesFromDirectory() {
		File directory = new File(directoryPath);
		File[] files = directory.listFiles();
		return Arrays.asList(files);
	}

}