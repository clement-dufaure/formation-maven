---
title: "Les plugins"
description: ""
lead: ""
draft: false
images: []
toc: true
weight: 307
---

En plus de tous les plugins chargés par défaut, il est possible de personnaliser leur configuration et d'en ajouter d'autres

## Déclaration d'un plugin

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



## Utilisation d'un plugin


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

RAPPEL des mappings par défaut : [http://maven.apache.org/guides/introduction/introduction-to-the-lifecycle.html#Built-in_Lifecycle_Bindings](http://maven.apache.org/guides/introduction/introduction-to-the-lifecycle.html#Built-in_Lifecycle_Bindings)





## Quelques plugins à ajouter dans son projet 

### Avoir un code bien formatté

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

### Tester et mesurer sa converture de test

**Maven surefire plugin** est intégré et exécuté d'office, il lance les tests unitaires selon une norme définie par le plugin (et personnalisable si besoin)

[https://maven.apache.org/surefire/maven-surefire-plugin/usage.html](https://maven.apache.org/surefire/maven-surefire-plugin/usage.html)

**Maven failsafe plugin** est à ajouter pour éxécuter des test d'intégration **au sens Maven**, c'est à dire des tests nécessitant un packaging de l'application.

Le plugin est très similaire au plugin surefire, les changements essentiels sont le nommage des classes par défaut et la position par défaut dans le cycke de vie.

[https://maven.apache.org/surefire/maven-failsafe-plugin/usage.html](https://maven.apache.org/surefire/maven-failsafe-plugin/usage.html)


Remarque : sans que ce soit nécéessairement une mauvaise pratique, il est parfois utile de sauter l'éxécution des tests (tests longs, découpage du pipeline en taches élémentaires, ...)

[Les plugins surfire et failsafe prévoient de sauter simplement les phases de tests.](https://maven.apache.org/surefire/maven-failsafe-plugin/examples/skipping-tests.html)

```sh
mvn package -DskipTests
```

**JaCoCo** va rajouter un agent java lors de l'éxécution des tests pour mesurer la couverture de test

[https://www.eclemma.org/jacoco/trunk/doc/maven.html](https://www.eclemma.org/jacoco/trunk/doc/maven.html)


### Déployer vers les outils de qualimétrie

**Sonar**

[https://docs.sonarqube.org/latest/analysis/scan/sonarscanner-for-maven/](https://docs.sonarqube.org/latest/analysis/scan/sonarscanner-for-maven/)

### Création avancée de livrable

**Maven Assembly plugin** : Définir un artifact personnalisé

[http://maven.apache.org/plugins/maven-assembly-plugin/](http://maven.apache.org/plugins/maven-assembly-plugin/)

Description xml du livrable, par exemple obtenir un zip contenant le jar + tous les jar de dépendances :

```xml
<assembly xmlns="http://maven.apache.org/ASSEMBLY/2.1.0" 
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" 
  xsi:schemaLocation="http://maven.apache.org/ASSEMBLY/2.1.0 http://maven.apache.org/xsd/assembly-2.1.0.xsd">

    <id>zip</id>

    <formats>
        <format>zip</format>
    </formats>
    <includeBaseDirectory>false</includeBaseDirectory>

    <fileSets>
        <fileSet>
            <directory>target</directory>
            <outputDirectory />
            <includes>
                <include>*.jar</include>
            </includes>
        </fileSet>
    </fileSets>

    <dependencySets>
        <dependencySet>
            <outputDirectory>/lib</outputDirectory>
        </dependencySet>
    </dependencySets>

</assembly>
```

A configurer à la phase package :
```xml
<plugin>
                <artifactId>maven-assembly-plugin</artifactId>
                <version>3.3.0</version>
                <configuration>
                    <descriptors>
                        <descriptor>src/assembly/zip.xml</descriptor>
                    </descriptors>
                    <appendAssemblyId>false</appendAssemblyId>
                    <finalName>mon-batch-${project.version}</finalName>
                </configuration>
                <executions>
                    <execution>
                        <phase>package</phase>
                        <goals>
                            <goal>single</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
```

### Exécutions avancées pour tests d'intégration


**Maven Exec plugin**
Excution d'executable (par exemple avant un test d'intégration)

[https://www.mojohaus.org/exec-maven-plugin/](https://www.mojohaus.org/exec-maven-plugin/)


**Cargo plugin**
Lancer un tomcat avec votre war (par exemple avant un test d'intégration)

[https://codehaus-cargo.github.io/cargo/Home.html](https://codehaus-cargo.github.io/cargo/Home.html)


### Sécurité des dépendances


**Dependency check**
Controler les CVE des librairies

[https://jeremylong.github.io/DependencyCheck/dependency-check-maven/](https://jeremylong.github.io/DependencyCheck/dependency-check-maven/)

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


### Divers

**Maven Versions plugin** : c'est celui qu'on a utilisé pour changer toutes les versions d'un projet multimodule d'un coup

[https://www.mojohaus.org/versions/versions-maven-plugin/](https://www.mojohaus.org/versions/versions-maven-plugin/)




