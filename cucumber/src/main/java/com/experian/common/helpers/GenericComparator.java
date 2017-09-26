package com.experian.common.helpers;

import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;

public class GenericComparator {

    public void doNotCheckCompare(String goldenFilePath, String targetFile) throws URISyntaxException, IOException {
        String dncStr = "@do_not_check@";
        CharSequence dnc = dncStr;

        InputStream isGoldenFile = this.getClass().getResourceAsStream(goldenFilePath);
        InputStream isTargetFile =  Files.newInputStream(Paths.get(targetFile));

        assertNotNull(isGoldenFile);
        assertNotNull(isTargetFile);

        Scanner scannerGolden = new Scanner(isGoldenFile);
        Scanner scannerTarget = new Scanner(isTargetFile);
        while(scannerGolden.hasNext()){
            String input = scannerGolden.nextLine();
            assertTrue(scannerTarget.hasNext());

            if(input.contains(dnc)){
                input = StringUtils.replace(input,dncStr, ".*(.*).*");
                assertTrue(scannerTarget.nextLine().matches(input));
            }else {
                int found = scannerTarget.nextLine().compareTo(input);
                assertTrue(found == 0);
            }
        }

        scannerGolden.close();
        scannerTarget.close();
    }

    public static void fileCompareWithIgnoreDNC(String goldenFilePath, String targetFile) throws IOException, URISyntaxException {
        GenericComparator comparator = new GenericComparator();
        comparator.doNotCheckCompare(goldenFilePath, targetFile);
    }
}
