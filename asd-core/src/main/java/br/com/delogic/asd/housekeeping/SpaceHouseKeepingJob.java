package br.com.delogic.asd.housekeeping;

import java.io.File;
import java.util.Collections;
import java.util.List;

import br.com.delogic.jfunk.Find;
import br.com.delogic.jfunk.When;

public class SpaceHouseKeepingJob extends HouseKeepingJobBase {

	protected Double thresholdGBs;

	public SpaceHouseKeepingJob(String directory, Double thresholdGBs) {
		super(directory);
		this.thresholdGBs = thresholdGBs;
	}

	@Override
	public List<File> filterFilesToBeDeleted(List<File> files) {
		Collections.sort(files, LAST_MODIFIED_DESC);

		return Find.all(files, new When<File>() {

			long thresholdBytes = (long) (thresholdGBs * 1024 * 1024 * 1024);

			@Override
			public boolean found(File e) {
				if (e.isFile()) {
					thresholdBytes -= e.length();
					return thresholdBytes < 0;
				}
				return false;
			}
		});
	}

}