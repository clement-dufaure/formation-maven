---
title: "Les plugins"
description: ""
lead: ""
draft: false
images: []
menu:
  docs:
    parent: "commencer-avec-maven"
toc: true
weight: 206
---

En maven, toutes les exécution sont des goals contenues dans des plugins, les mapping phase->goals des plugins essentiels étant définis par défaut.

# Déclaration d'un plugin

Exemple : le plugin de compilation


par défaut dans le "super pom" :
```xml
<plugin>
        <artifactId>maven-compiler-plugin</artifactId>
        <version>3.1</version>
        <executions>
          <execution>
            <id>default-compile</id>
            <phase>compile</phase>
            <goals>
              <goal>compile</goal>
            </goals>
          </execution>
          <execution>
            <id>default-testCompile</id>
            <phase>test-compile</phase>
            <goals>
              <goal>testCompile</goal>
            </goals>
          </execution>
        </executions>
      </plugin>
```

On le configure de façon "cachée" via la propriété `<maven.compiler.release>17</maven.compiler.release>` mais on peut également à la place surcharger la déclaration du plugin via :


```xml
<build>
  <plugins>
    <plugin>
      <groupId>org.apache.maven.plugins</groupId>
      <artifactId>maven-compiler-plugin</artifactId>
      <version>3.8.0</version>
      <configuration>
        <release>17<release>
      </configuration>
    </plugin>
  </plugins>
</build>
```

Ce qui donnera dans le pom effectif

```xml
<build>
  <plugins>
    <plugin>
      <groupId>org.apache.maven.plugins</groupId>
      <artifactId>maven-compiler-plugin</artifactId>
      <version>3.8.0</version>
      <configuration>
        <release>17</release>
      </configuration>
      <executions>
        <execution>
          <id>compile</id>
          <phase>compile</phase>
          <goals>
            <goal>compile</goal>
          </goals>
        </execution>
        ...
      </executions>
    </plugin>
  </plugins>
</build>
```



# Utilisation d'un plugin


- Directement par le goal voulu

`mvn compile:compile`

- En le liant à une des phase du cycle de vie par execution

```xml
      <executions>
        <execution>
          <id>compile</id>
          <phase>compile</phase>
          <goals>
            <goal>compile</goal>
          </goals>
        </execution>
        ...
      </executions>
```

RAPPEL des mappings par défaut : http://maven.apache.org/guides/introduction/introduction-to-the-lifecycle.html#Built-in_Lifecycle_Bindings)


# Quelques plugins

**Spotless plugin** : formatter son code

https://github.com/diffplug/spotless

exemple de déclaration :

```xml
<plugin>
  <groupId>com.diffplug.spotless</groupId>
  <artifactId>spotless-maven-plugin</artifactId>
  <version>${spotless.version}</version>
  <configuration>
    <formats>
      <format>
        <indent>
          <spaces>true</spaces>
          <spacesPerTab>4</spacesPerTab>
        </indent>
      </format>
    </formats>
    <java>
      <googleJavaFormat>
        <version>1.8</version>
        <style>AOSP</style>
        <reflowLongStrings>true</reflowLongStrings>
      </googleJavaFormat>
    </java>
  </configuration>
  <executions>
  <execution>
    <goals>
      <goal>check</goal>
    </goals>
    <phase>validate</phase>
  </execution>
</executions>
</plugin>
```

ou d'exécution ponctuelle :
```sh
mvn spotless:apply
```

**Maven surefire plugin** : intégré d'office, parfois à paramétrer
Lancer les tests unitaire

https://maven.apache.org/surefire/maven-surefire-plugin/usage.html

**Maven failsafe plugin**
Lancer les tests d'intégration

https://maven.apache.org/surefire/maven-failsafe-plugin/usage.html


**JaCoCo**
Mesurer la couverture de test

https://www.eclemma.org/jacoco/trunk/doc/maven.html

**Sonar**

https://docs.sonarqube.org/latest/analysis/scan/sonarscanner-for-maven/


**Maven Assembly plugin**
Agréger différents fichiers (construits ou non) sous forme de package -> création de livrable pour le CEI.

http://maven.apache.org/plugins/maven-assembly-plugin/


**Maven Exec plugin**
Excution d'executable (par exemple avant un test d'intégration)

https://www.mojohaus.org/exec-maven-plugin/


**Maven Build Helper plugin**
Ensemble d'aide à la construction (récupération ip locale, ajout de source au classpath)

https://www.mojohaus.org/build-helper-maven-plugin/plugin-info.html


**Maven Versions plugin**
Gérer les Versions

https://www.mojohaus.org/versions-maven-plugin/


**Cargo plugin**
Lancer un tomcat avec votre war (par exemple avant un test d'intégration)

https://codehaus-cargo.github.io/cargo/Home.html


**Dependency check**
Controler les CVE des librairies

https://jeremylong.github.io/DependencyCheck/dependency-check-maven/

--

Exemple de déclaration :

```xml
    <project>
        ...
        <build>
            ...
            <plugins>
                ...
                <plugin>
                  <groupId>org.owasp</groupId>
                  <artifactId>dependency-check-maven</artifactId>
                  <version>6.1.5</version>
                  <executions>
                      <execution>
                          <goals>
                              <goal>check</goal>
                          </goals>
                      </execution>
                  </executions>
                </plugin>
                ...
            </plugins>
            ...
        </build>
        ...
    </project>
```

Ca marche parce que par défaut le goal "check" de dependency check s'éxécute à la phase "verify"





