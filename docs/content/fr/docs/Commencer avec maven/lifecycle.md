---
title: "Bases de fonctionnement de maven"
description: ""
lead: ""
draft: false
images: []
menu:
  docs:
    parent: "commencer-avec-maven"
toc: true
weight: 203
---

# Les plugins et goals

Dans maven, tout est plugin.
Les plugins contiennent le code des exécutions exposés comme "goals".
Une exécution se déclenche avec la syntaxe :
```sh
mvn nom-du-plugin:version:nom-du-goal
```

Si le nom du plugin correspond à la syntaxe maven-PPP-plugin, si une version est défini dans le pom (on voir ça plus tard), on peut plus simplement l'appeler :
```sh
mvn PPP:nom-du-goal
```

Par exemple, la compilation du code se fait avec le plugin `maven-compiler-plugin`, il possède deux goals `compile` pour compiler le code principal et `testCompile` pour compiler le code de test. On va appeller ces deux goals :
```sh
mvn compiler:compile
mvn compiler:testCompile
``` 

Cependant la plupart du temps on souhaite enchaîner logiquement plusieurs goals.

# Le cycle de vie du build

Maven décrit plusieurs enchaînements de phase décrivant les étapes fines de contruction d'un projet.

[Lifecycle Reference](https://maven.apache.org/guides/introduction/introduction-to-the-lifecycle.html#lifecycle-reference)

```sh
mvn nom-de-la-phase
```
lancera l'ensemble des phases préalables à la phase demandée en plus de celle-ci.

On peut alors associer des goals à certaines phases.
Par défaut, un certain nombre de goals sont déjà associé à certaines phases. 

[Built-in_Lifecycle_Bindings](http://maven.apache.org/guides/introduction/introduction-to-the-lifecycle.html#Built-in_Lifecycle_Bindings)

Même si on peut théoriquement appeler toutes les phases, on appelle généralement les phases suivantes :

-  validate - Valide la configuration du projet.
-  compile - Compile le code source
-  test - Test le projet
-  package - Crée un fichier livrable
-  verify - Vérifie le projet (tests d'intégration)
-  install - Installe le livrable dans le dépot local
-  deploy - Déploie le livrable dans le dépot distant

# Configuration et conventions

Maven fonctionne par **conventions**.
Suivre ces conventions permet de limiter le nombre de configurations à expliciter.

## Architecture standard d'un projet Maven

```
projet-maven
|-- pom.xml : fichier de configuration
|-- src
|   |-- main
|   |   |-- java : le code java principal
|   |   |   `-- re
|   |   |       `-- dufau
|   |   |           `-- App.java
|   |   |-- resources : les fichiers non java devant être ajoutés au livrable
|   |   `-- webapp : les ressources WEB dans le cas d'un war
|   `-- test
|       |-- java : le code java de test
|       |   `-- re
|       |       `-- dufau
|       |           `-- AppTest.java
|       `-- resources : les fichiers non java nécessaires à l'éxécution des tests
|
`-- target : le répertoire de travail de maven
    |-- classes
    |   `-- re
    |       `-- dufau
    |           `-- App.class
    [...]
    |-- surefire-reports
    |   |-- TEST-re.dufau.AppTest.xml
    |   `-- re.dufau.AppTest.txt
    `-- test-classes
        `-- re
            `-- dufau
                `-- AppTest.class
```

:warning: on ne veut pas partager le dossier target => à ajouter au .gitignore


## Le fichier de configuration : le POM

Même avec les conventions respectés, sa présence est nécessaire pour que maven considère le projet comme projet maven.



