zuss-mojo
=========

maven mojo to compile .zuss files to css

usage:

pom.xml:
	<plugin>
                <artifactId>zuss-maven-plugin</artifactId>
                <version>0.0.1</version>
                <executions>
                     <execution>
                            <phase>generate-resources</phase>
                            <goals>
                                <goal>compile-css</goal>
                            </goals>
                     </execution>
                </executions>
		<configuration>
				<inputDirectory>${basedir}/src/main/webapp/javascript/common/zuss</inputDirectory>
				<outputDirectory>${basedir}/src/main/webapp/javascript/common/compiled-css</outputDirectory>
		</configuration>
	</plugin>


command line:
		~/niceproject] $ mvn -e zuss:compile-css

