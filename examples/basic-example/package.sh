#!/bin/sh

# create artifacts using maven
mvn clean install -Dmaven.test.skip=true

# del example-dist
rm -fr demo-dist

# create example-dist
mkdir demo-dist
mkdir -p demo-dist/plugins
mkdir -p demo-dist/pluginConfig

# copy main program and config
cp basic-example-main/target/basic-example-main-*-exec.jar demo-dist
cp basic-example-main/src/main/resources/application-prod.yml demo-dist

# copy plugin and config
cp plugins/basic-example-plugin1/target/*-jar-with-dependencies.jar demo-dist/plugins
cp plugins/basic-example-plugin1/src/main/resources/plugin1.yml demo-dist/pluginConfig

cp plugins/basic-example-plugin2/target/*-jar-with-dependencies.jar demo-dist/plugins
cp plugins/basic-example-plugin2/src/main/resources/plugin2.yml demo-dist/pluginConfig

cd demo-dist

# run demo
mv basic-example-main-*-exec.jar basic-example-start.jar
mv application-prod.yml application.yml

java -jar -Dspring.profiles.active=prod basic-example-start.jar
