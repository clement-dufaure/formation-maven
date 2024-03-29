---
title: "Comment builder un projet avec uniquement le JDK ?"
description: ""
lead: ""
draft: false
images: []
menu:
  docs:
    parent: "sans-maven"
toc: true
weight: 102
---

Qu'est ce que "builder un projet" ??

ou *De quoi va-t-on parler ?*

- Exécuter du code java
- Exécuter conjointement plusieurs classes ensemble
- Utiliser du code de biliothèques externes
- Changer la version d'un bibliothèque
- Préparer un déploiement : fournir un jar/war
- Lancer des tests unitaires ?


## Compiler son code

- Compilation d'une classes
```sh
javac ./src/re/dufau/demo/model/User.java -d ./build/classes
javac ./src/re/dufau/demo/dao/UserDao.java -d ./build/classes
```

1er problème : les classes s'appellent entre elles

- Compilation de plusieurs classes "ensemble"
```sh
echo src/re/dufau/demo/dao/UserDao.java > ./build/sources.txt
echo src/re/dufau/demo/model/User.java  >> ./build/sources.txt
javac @./build/sources.txt -d ./build/classes
```

- Compilation de toutes les classes
```sh
find src -name "*.java"  > ./build/sources.txt
javac @./build/sources.txt -d ./build/classes 
```

2e problème : le dev a utilisé des bibliothèques externes

-> Il faut télécharger les librairies nécessaires : création du classpath avec les jar externes requis
```sh
find src -name "*.java"  > ./build/sources.txt
javac @./build/sources.txt -d ./build/classes -cp "./classpath/cmain/*"
```

3e problème : les jars que l'on récupère ont eux aussi des dépendances...
Bienvenue aux dépendances transitives :

Il faut le savoir (doc de la lib) et les rajouter dans le classpath
Il faut faire attention au croisement des version, on ne pourra mettre qu'une version de chaque fichier .class au risque de conflits

Et si on doit changer la version, il faut tout recommencer... ajouter/retirer un fichier jar dans le classpath... et adapter les transitives


Remarque : on peut préciser la version de java dans laquelle est écrite java et la version de compilation des fichiers compilés
```sh
javac @./build/sources.txt -d ./build/classes -source 11 -target 11
```


## Création du livrable

"livrable" = moyen de tranfert standard

- le JAR = java archive
C'est un zip contenant les classes compilées (et des métadonnées dans un dossier META-INF)
Il permet l'échange de code (bibliothèque de dépendance).
Il peut être excutable : présence d'une méthode `main()`, lancement via `java -jar`

```
META-INF/
    MANIFEST.MF
re/
  dufau/
    demo/
      model/
        User.class
      ...
```

- le WAR = web application archive 
C'est un zip contenant les classes compilées et les fichiers web (jsp/html/css...). C'est un "bout" d'application, le reste de l'appli (dont le `main()`) se trouvant dans un conteneur d'application comme tomcat.
Le war n'est pas prévu pour s'éxécuter seul.

```
META-INF/
    MANIFEST.MF
WEB-INF/
    web.xml
    config-spring.xml
    static/
      css/
        style.css
      img/
        image.png
      ...
    classes/
        re/
          dufau/
            demo/
              model/
                User.class
              ...
    lib/
      dependance1.jar
      dependance2.jar
      ...
```

Pour créer ces livrables, on peut passer par la commande `jar`

## Faire fonctionner le projet dans l'IDE

Dans l'IDE, ca compile pas ?

- L'IDE compile à la volée (javac) pour nous dire si tout va bien
- Il faut donc qu'il connaisse le classpath, à paramétrer à la main...
- Si je transmet mon code l'autre dev doit tout reparamétrer...

## Lancer les tests

Logique similaire à l'éxécution de mon code

- On va devoir compiler les classes de tests
- Ces classes de tests vont dépendre
  - de mon code "runtime"
  - des dépendances de mon code "runtime"
  - de dépendances spécifiques à l'éxécution des tests

Ce qui donne :

```sh
javac ./test/re/dufau/demo/dao/UserDaoTest.java  -d ./build/test-classes/ -cp "./build/classes;./classpath/test/*"
java -jar ./lib/test/junit-platform-console-standalone-1.10.1.jar -cp "./classpath/main/;./classpath/test/;./build/test-classes/;./build/classes/" --select-class re.dufau.demo.dao.UserDaoTest
```

## Avez vous envie de lancer toutes ces commandes à chaque build ?

Si la réponse est non, on peut poursuivre.
Des outils cherchent à faciliter et automatiser ces étapes.

- Ant
- Maven
- Gradl

- Ant
  - Configuration XML
  - Les tâches précitées sont explicitées sont la forme d'un xml, tout doit être décrit
  - Approche impérative

- Maven
  - Configuration XML
  - La logique de maven repose sur des **conventions**, sous réserve d'avoir une architecture de code aux stardards maven, un fichier de configuration toujours au format xml assez minimal permet de réaliser les opération précitées.
  - Les principales opérations seront ainsi sous-entendues (non explicitées dans la conf).
  - Logique de dépendances avec référentiel
  - Approche déclarative

- Gradl
  - Approche impérative mais avec langage JVM (Groovy/Kotlin)
  - Repose également sur un système de référentiel pour les dépendances

Au delà des opérations essentielles précentées les outils vont permettre de réaliser une multiplicité de tâches.
Avec la présence quasi systèmatique de pipeline CI/CD sur nos applications, il faudra parfois choisir entre intégrer certaines tâches à travers l'outils de buils choisi, ou directement en opérations CI/CD




