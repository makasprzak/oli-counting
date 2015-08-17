set START_DIR=%CD%
cd ../../..
mvn exec:java
cd %START_DIR%
