---
title: "Les modules et les héritages"
description: ""
lead: ""
draft: false
images: []
toc: true
weight: 207
---

- Un POM peut hériter d'un autre (avec le type pom).
- Un projet peut définir plusieurs **modules** qui seront liés entre eux

# Découper en modules

On va découper un projet initial en plusieurs projet maven indépendant du point de vue du format (ils ont chacun leur pom, leur arborescence de source, ...).
Admettons l'architecture après découpage :

```
|
|--api
    |--pom.xml
    `--src ...
|--core
    |--pom.xml
    `--src ...
`--batch
    |--pom.xml
    `--src ...
```

Pour présenter cet ensemble comme un prjet maven multi module, on va rajouter un pom dans le dossier racine. Ce pom sera le **pom parent** du projet.
Il va devoir au mimimum contenir les conf suivantes :
- passer au type `pom`
- lister les dossiers correspondants aux modules

```xml
<packaging>pom</packaging>

<modules>
  <module>api</module>
  <module>core</module>
  <module>batch</module>
</modules>
```

Attention, il s'agit du nom des fichiers et non pas des artifactId (qui pourront donc être différent du nom des dossiers).

Du point de vue des modules, il va falloir déclarer l'héritage de ce pom parent.

```xml
<!-- Héritage du POM général-->
	<parent>
		<groupId>group.id</groupId>
		<artifactId>pom-parent</artifactId>
		<version>version</version>
	</parent>
```

**Attention** : en découpant en module, on rajoute des dépendances entre les modules qui n'existaient a fortiori pas avant découpage.
L'ordre de ces dépendance impactera l'ordre d'exécution des modules maven.

# Le pom parent

Au delà du découpage en module, le pom parent permet de mutualiser des configurations. Tout projet maven récupèra :
- les propriétés
- les dépendences et gestion des dépendances
- les plugins et gestion des plugins

Il faut éviter de déclarer toutes les dépendances du projet dans le pom parent, car des modules se retourveront avec des dépendances non nécessaire "unitairement" au module.

Cependant on sohaite quand même concentrer le maximum de configuration dans le pom parent, qui peut servir d'index des dépendances et plugins utilisés avec leur versions notamment.

## Le dependencyManagement

```xml
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>test</groupId>
                <artifactId>a</artifactId>
                <version>1.2</version>
            </dependency>
        ...
        <dependencies>
    </dependencyManagement>   
```
On s'en sert pour paramétrer globalement la version des dépendances sans charger la dépendance dans le projet parent

La déclaration sera alors minimale dans l'utilisation dans les projets fils :

```xml
 <dependencies>
        <dependency>
            <groupId>test</groupId>
            <artifactId>a</artifactId>
        </dependency>
      ...
  <dependencies>
```

L'IDE va généralement alerter si on surcharge la version.

Ce principe de management s'applique à toutes les conf de la dépendance telles que le scope.


## Mettre à jour toutes les références à la version des modules

Plugin "version" :
```
mvn versions:set -DnewVersion=1.2.3 
```


# Des pom parents externes : le cas de Spring Boot

```xml
<!-- Héritage Spring-->
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>2.4.0</version>
</parent>
```

Comme pour les dépendances, il existe un pluginManagement pour préconfigurer des plugin sans les éxécuter :

```xml
    <pluginManagement>
      <plugins>
        <plugin>
        ...
```


# Héritage multiple ?

- Sous module qui a besoin spécifiquement d'un autre parent ?
- Besoin de plusieurs dependencyManagment ?

=> on va récupérer uniquement ce qui nous interesse dans l'héritage

- On peut récupérer et "combiner" plusieurs dependencyManagement

- on passe par un BOM (Bill Of Materials)

```xml
    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>test</groupId>
                <artifactId>test-BOM</artifactId>
                <version>0.0.1-SNAPSHOT</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>
```

