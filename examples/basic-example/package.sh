#!/bin/sh

# create artifacts using maven
mvn clean install -Dmaven.test.skip=true

# del example-dist
rmdir demo-dist /s /q

# create example-dist
mkdir -p demo-dist
mkdir demo-dist\plugins
mkdir demo-dist\pluginConfig

# copy main program and config
cp basic-example-main\target\basic-example-main-*-exec.jar demo-dist /s /i
cp basic-example-main\src\main\resources\application-prod.yml demo-dist /s

# copy plugin and config
cp plugins\basic-example-plugin1\target\*-jar-with-dependencies.jar demo-dist\plugins /s
cp plugins\basic-example-plugin1\src\main\resources\plugin1.yml demo-dist\pluginConfig /s

cp plugins\basic-example-plugin2\target\*-jar-with-dependencies.jar demo-dist\plugins /s
cp plugins\basic-example-plugin2\src\main\resources\plugin2.yml demo-dist\pluginConfig /s

cd demo-dist

# run demo
rename basic-example-main-*-exec.jar basic-example-start.jar
rename application-prod.yml application.yml

java -jar -Dspring.profiles.active=prod basic-example-start.jar
