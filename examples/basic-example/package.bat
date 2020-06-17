REM windows package

REM package
call mvn clean install -Dmaven.test.skip=true

REM del example-dist
rmdir demo-dist /s /q

REM create example-dist
mkdir demo-dist
mkdir demo-dist\plugins
mkdir demo-dist\pluginConfig

REM copy main program and config
xcopy basic-example-main\target\basic-example-main-*-exec.jar demo-dist /s /i
xcopy basic-example-main\src\main\resources\application-prod.yml demo-dist /s

REM copy plugin and config
xcopy plugins\basic-example-plugin1\target\*-jar-with-dependencies.jar demo-dist\plugins /s
xcopy plugins\basic-example-plugin1\src\main\resources\plugin1.yml demo-dist\pluginConfig /s

xcopy plugins\basic-example-plugin2\target\*-jar-with-dependencies.jar demo-dist\plugins /s
xcopy plugins\basic-example-plugin2\src\main\resources\plugin2.yml demo-dist\pluginConfig /s

cd demo-dist

REM run main
rename basic-example-main-*-exec.jar basic-example-start.jar
rename application-prod.yml application.yml

java -jar -Dspring.profiles.active=prod basic-example-start.jar
