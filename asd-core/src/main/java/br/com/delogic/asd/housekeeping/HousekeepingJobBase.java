package br.com.delogic.asd.housekeeping;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.delogic.jfunk.Has;

public abstract class HousekeepingJobBase implements HousekeepingJob {

	protected final Logger log = LoggerFactory.getLogger(getClass());
	protected final String directoryPath;
	protected boolean dryRun = false;

	public HousekeepingJobBase(String directory) {
		this(directory, false);
	}
	
	public HousekeepingJobBase(String directory, boolean dryRun) {
        this.directoryPath = directory;
        this.dryRun = dryRun;
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
			log.debug("Deleted file:" + toBeDeleted.getName() + ", Last Modified:" + toBeDeleted.lastModified());
		}
	}

	public void logNotDeletedFile(File toBeDeleted) {
		if (log.isWarnEnabled()) {
			log.warn("File not deleted:" + toBeDeleted);
		}
	}

	public boolean deleteFile(File toBeDeleted) {
	    //if testing
	    if (dryRun) {
	        return true;
	    }
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