## About
This is a Java implementation to fetch data from network elements via snmp. This crawler provides you the spring bean lifecycle management as built-in. Also you can add any network element which supports snmp. Switch and Olt crawlers were added to the project as samples.

## Tech Stack
Tech|Version
---|---
Maven|3.2.5
JDK|1.7
Spring|4.3.3.RELEASE
Snmp4j|2.5.0
Apache DBCP2|2.1.1
MySQL|5.1.39
Logback|1.1.7
JUnit|4.12

## Installation
* Run the following `git` command to clone: `git clone https://github.com/kahramani/snmp-crawler.git`
* Run the following `maven` command to install dependencies: `mvn install`
* Open project in your editor/ide and follow the `TODO`s to configure your specifications
* Run the following `maven` plugin `assembly` command to build project jar with dependencies: `mvn assembly:assembly`
* Run with: `java -jar snmp-crawler-1.0.0-jar-with-dependencies.jar&`

## License
GNU GPLv3
