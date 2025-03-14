---
title: "Compléments"
description: ""
lead: ""
draft: false
images: []
toc: true
weight: 302
---

## Nouveau projet Maven

- utiliser un standard pour créer un projet maven

`mvn archetype:generate`

- Pour un projet spring boot, le spring initilizr https://start.spring.io/ crée tout ce qu'il faut si on lui demande un projet maven



## Déployer sur un repository maven (privé)

Si notre projet peut servir de dépendance à un autre projet, il peut être nécessaire de le rendre facilement accessible aux autres projet.

(On ne détaille pas ici le mode de livraison sur maven central)

Il y a conventionnellement deux types dépôts :
- release : les version immuables, elles ne doivent JAMAIS être réécrite, les systèmes les considèrent en cache infini en cas de miroir
- snapshots : les versions en cours. A l'opposé elles sont censées être volatiles. Le numéro de version **doit** se terminer par `-SNAPSHOT`

On peut déclarer dans les configurations maven les urls des dépôts release et snapshots.
Ils y seront envoyés lors de la phase deploy.
Ces conf sont surchargeable en ligne de commande : `-DaltDeployementRepository=<id>::default::<url>`

Remarque, pour les dépôt non maven (déôt brut du jar ou war), il y a le plugin wagon, il est cependant devenu plus maintenable de définir ses déploiement non maven directement dans son script CI/CD.


## Bonnes pratiques


On ne commit rien d'autre que
```
projet
 |--src/
 |--pom.xml
```
- POM le plus petit possible
- Version de dépendences centralisées (DependecyManagement + properties)


## Créer son propre plugin

https://maven.apache.org/guides/plugin/guide-java-plugin-development.html


## Options avancées de jvm

Il est possible de définir des options à rajouter au démarrage de la jvm par maven

- Fichier .mvn/jvm.config

```
--add-opens java.base/java.lang=ALL-UNNAMED
```



