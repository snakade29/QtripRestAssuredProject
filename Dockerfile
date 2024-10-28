FROM maven:3.8.2-jdk-11

COPY . /home/apiframework/
COPY index.html home/apiframework/index.html
COPY pom.xml home/apiframework/pom.xml
COPY testng.xml home/apiframework/testng.xml

WORKDIR home/apiframework

ENTRYPOINT mvn clean test