package com.optum.jmeter;
import com.optum.constants.ApplicationConstants;
import com.optum.jarvis.JmeterUtilEngine;
import com.typesafe.config.ConfigFactory;

public class Engine extends JmeterUtilEngine {


    public static void main(String[] args) {

        targetFileHardCode = ApplicationConstants.TARGET_FILE;
        csvFileHardCode = ApplicationConstants.CSV_FILE;
        osNameHardCode = ApplicationConstants.OS_NAME;
        aggregateResultHardCode = ApplicationConstants.AGGREGATE_RESULT;
        csvFileTypeHardCode = ApplicationConstants.CSV_FILE_TYPE;
        binHardCode = ApplicationConstants.BIN;
        resultsHardCode = ApplicationConstants.RESULTS;
        projectRootHardCode = ApplicationConstants.PROJECT_ROOT;
        targetHardCode = ApplicationConstants.TARGET;
        reportHardCode = ApplicationConstants.REPORT;
        windowsHardCode = ApplicationConstants.WINDOWS;
        jmeterHomeHardCode = ApplicationConstants.JMETER_HOME;
        jarServer = System.getProperty("jarvisServer");
        appConfig = ConfigFactory.load();
        inJar = Engine.class.getResource("Engine.class").toString();
        jmeterJarvisInit(args);
        is = Engine.class.getClassLoader().getResourceAsStream(simNameRun);
        jmeterJarvisExecute(is);
    }

}
