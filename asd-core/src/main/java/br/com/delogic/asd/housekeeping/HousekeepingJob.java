package br.com.delogic.asd.housekeeping;

import java.io.File;
import java.util.Comparator;

public interface HousekeepingJob extends Runnable {

    Comparator<File> LAST_MODIFIED_DESC = new Comparator<File>() {
        @Override
        public int compare(File o1, File o2) {
            return o1.lastModified() < o2.lastModified() ? 1 : o1.lastModified() == o2.lastModified() ? 0 : -1;
        }
    };

}