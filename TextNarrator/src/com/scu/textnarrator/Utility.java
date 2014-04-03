package com.scu.textnarrator;

import java.text.DecimalFormat;

public class Utility {
	static String[] sizeUnits = new String[] { "B", "KB", "MB"};
    /**
     *
     * @param  filesize
     * @return B/KB/MB/GB format
     */
    public static String formattedFileSize(long filesize) {
        if(filesize <= 0) return "0";
        int digGroup = (int) (Math.log10(filesize)/Math.log10(1024));

        return new DecimalFormat("#,##0.00")
                .format(filesize/Math.pow(1024, digGroup)) + " " + sizeUnits[digGroup];
    }


}
