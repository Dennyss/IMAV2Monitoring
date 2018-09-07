#!/bin/bash
JAVA_HOME=/share/java/jdk1.8.0_172

$JAVA_HOME/bin/java -jar -Dlogging.config=/share/vsb/IMAV2Monitoring/log4j2.xml /share/vsb/IMAV2Monitoring/ima2-monitoring-1.0.jar com.sprint.ima.Application --spring.config.location=file:/share/vsb/IMAV2Monitoring/application.properties &