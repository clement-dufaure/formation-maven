---
title: "Le fichier POM"
description: ""
lead: ""
draft: false
images: []
menu:
  docs:
    parent: "commencer-avec-maven"
toc: true
weight: 204
---

[Référence](http://maven.apache.org/pom.html)

# Le minimum

```xml
<project xmlns="http://maven.apache.org/POM/4.0.0"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
                      http://maven.apache.org/xsd/maven-4.0.0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <groupId>re.dufau</groupId>
  <artifactId>my-app</artifactId>
  <version>1</version>
  <packaging>jar</packaging>
</project>
```

Les coordonnées maven pour assurer l'unicité d'un artifact globalement :
  - `groupId` : identifiant de l'organisation, de l'équipe réalisatrice, etc
  - `artifactId` : le nom du projet/de l'application (ou du module dans le cas de projets multimodule)
  - `version` : la version de l'artifact produit

Le principe de convention cache l'existence d'un super POM contenant toutes les valeurs par défaut dont :
- la déclaration de tous le chemins présentés dans l'architecture par défaut (src/main/java, src/main/resource, target, ...)
- les plugins par défaut et leur mappings dans le cycle de vie

On peut voir notre POM effectif avec :

```sh
mvn help:effective-pom
```

# Le type d'artifact à construire

```xml
  <packaging>jar</packaging>
```

```xml
  <packaging>war</packaging>
```

Permet à maven de lier les phases de build à certains plugins, et notemment la phase de packaging.

[Voir les associations](http://maven.apache.org/ref/3.6.3/maven-core/default-bindings.html)

# Déclaration des dépendances

```xml
<dependencies>
  <dependancy>
  ...
  </dependency>
</dependencies>
```

[Vers la partie dédiée](/docs/commencer-avec-maven/dependances/)

# Properties

Permettent de variabiliser dépendances, plugin ou généralement toute autre configuration de maven. Elles peuvent être personnalisées pour centraliser certaines configurations comme les versions.

## La version de java

Quelquesoit le jdk que l'on a installé, on peut souhaiter préciser la source dans laquelle est écrite le code et la version dans laquelle on va le compiler.
Par exemple, j'ai un jdk 21 mais je sais que mon environnement d'éxécution sera en 17.

Il faut non seulement que je compile mon code en 17 (la jre 17 de l'éxécution refusera un code compilé en 21), ce qui signifie également que je ne dois écrire que des éléments de code existant en java 17 (même si mon jdk connait la syntaxe 21, il doit les considérer comme des erreurs).

Ces deux éléments correspondent aux deux options de javac :
```sh
javac ... -source 17 -target 17
```

On va préciser ces élement à maven via les properties (qui seront injectées dans le plugin compiler)

```xml
<properties>
<maven.compiler.source>17</maven.compiler.source>
<maven.compiler.target>17</maven.compiler.target>
</properties>
```

ou comme génralement, on met la même version dans les deux :

```xml
<properties>
    <maven.compiler.release>17</maven.compiler.release>
</properties>
```

ou si le projet est en Spring Boot :
```xml
<properties>
    <java.version>17</java.version>
</properties>
```

Attention, par défaut maven compile en une vieille version de java (<=1.8)

## L'encodage

Il s'agit d'indiquer à maven quel est l'encodage des fichier qu'il va lire, et dans quel encodage il doit écrire les fichier non compilés (ressourecs) :

```xml
<properties>
  <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
  <project.reporting.outputEncoding>UTF-8</project.reporting.outputEncoding>
</properties>
```

## Properties personnalisées

- accessible depuis le reste du pom (`${prop}`)
- surchargeable par un argument en ligne de commande `-Dkey=value`

```xml
<properties>
  <ma-lib.version>1.2.3</ma-lib.version>
  <ma-propriete>something</ma-propriete>
</properties>

...

<dependencies>
  <dependency>
    <groupId>ma</groupId>
    <artifactId>lib</artifactId>
    <version>${ma-lib.version}</version>
  </dependency>
</dependencies>

```

Certaines propriétés sont autmatiques telles que `${project.version}` donnant la valeur de la balise version du projet.


Les properties peuvent être injectées dans les ressources, c'est le filtering

https://maven.apache.org/plugins/maven-resources-plugin/examples/filter.html

```xml
      <resource>
        <directory>src/main/resources</directory>
        <filtering>true</filtering>
      </resource>
```

**Warning: Do not filter files with binary content like images! This will most likely result in corrupt output.**

# Les paramètre de build

L'ensemble des paramètre dont une grande partie devrait rester implicite

```xml
<build>
  <directory>${project.basedir}/target</directory>
  <outputDirectory>${project.build.directory}/classes</outputDirectory>
  <finalName>${project.artifactId}-${project.version}</finalName>
  <testOutputDirectory>${project.build.directory}/test-classes</testOutputDirectory>
  <sourceDirectory>${project.basedir}/src/main/java</sourceDirectory>
  <scriptSourceDirectory>src/main/scripts</scriptSourceDirectory>
  <testSourceDirectory>${project.basedir}/src/test/java</testSourceDirectory>
  <resources>
    <resource>
      <directory>${project.basedir}/src/main/resources</directory>
    </resource>
  </resources>
  <testResources>
    <testResource>
      <directory>${project.basedir}/src/test/resources</directory>
    </testResource>
  </testResources>
  <plugins>...</plugins>
</build>
```

Généralement, on va surtout personnaliser uniquement cette dernière balise de plugin pour ajouter des plugins et leurs goals dans le cycle de vie.

[Vers la partie dédiée](/docs/commencer-avec-maven/plugins/)


# Les profils

Il est possible de cumuler plusieurs configurations de build concurrentes via les profils, il sont ensuite appelable via la ligne de commande maven `-Pnomprofil`

```xml
<profiles>
  <profile>
    <id>nomprofil</id>
    <properties>
      <ma.conf>maConfDeQf</ma.conf>
    </properties>
    <build>
    ...
    </build>
  </profile>
</profiles>


Ils peuvent également s'activer sous conditions

```xml
<profiles>
  <profile>
    <id>linux</id>
    <activation>
      <jdk>1.5</jdk>
      <os>
        <family>!windows</family>
      </os>
    </activation>
    <properties>
      <linux>true</linux>
    </properties>
  </profile>
</profiles>
```


