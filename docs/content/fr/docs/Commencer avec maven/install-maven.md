---
title: "Installer et exécuter maven"
description: ""
lead: ""
draft: false
images: []
menu:
  docs:
    parent: "commencer-avec-maven"
toc: true
weight: 202
---

# L'exécutable maven

## Installation de maven
Maven est un programme écrit en java, l'environnement java doit être correctement paramétré (soit via la variable d'environnement JAVA_HOME, soit en ayant correctement configuré java dans le path)

- Télécharger et extraire l'archive sur le site de maven (https://maven.apache.org/download.cgi).
- Ajouter le dossier `/bin` au path pour une exécution en ligne de commande

```sh
mvn -v
```

## Le maven wrapper
Les projets peuvent intégrer directement l'exécutable maven dans leurs sources. Cela permet de s'assurer de la présence de maven et de la version d'éxécution de maven.

```sh
./mvnw -v
```

# Configuration générale 

- Propre à votre environnement de travail, autrement dit, commune à tout les projets
- Dans le répertoire d'installation : /conf/settings.xml -> Eviter de modifier celui la, il est propre uniquement à cette installation
- Il vaut mieux le surcharger par le fichier settings.xml dans `%USERPROFILE%/.m2` ou `$HOME/.m2`
- Il sera alors commun à toutes les installations et également aux wrappers


# Execution

```
mvn --help

usage: mvn [options] [<goal(s)>] [<phase(s)>]
```

- goals : actions spécifiques unitaires, ils sont inclus dans des plugins maven
- phase : correspond aux étapes du cycle de vie du build de l'application

