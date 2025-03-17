---
title: "Compléments"
description: ""
lead: ""
draft: false
images: []
toc: true
weight: 402
---

## Nouveau projet Maven

- Pour un projet spring boot, le spring initilizr [https://start.spring.io/](https://start.spring.io/) crée tout ce qu'il faut si on lui demande un projet maven

- Mais il existe un plugin pour générer un projet plus générique : `mvn archetype:generate`


## Déployer sur un repository maven (privé)

Si notre projet peut servir de dépendance à un autre projet, il peut être nécessaire de le rendre facilement accessible aux autres projet.

(On ne détaille pas ici le mode de livraison sur maven central)

Il y a conventionnellement deux types dépôts :
- release : les version immuables, elles ne doivent JAMAIS être réécrite, les systèmes les considèrent en cache infini en cas de miroir
- snapshots : les versions en cours. A l'opposé elles sont censées être volatiles. Le numéro de version **doit** se terminer par `-SNAPSHOT`

On peut déclarer dans les configurations maven les urls des dépôts release et snapshots.
Ils y seront envoyés lors de la phase deploy.
Ces conf sont surchargeable en ligne de commande : `-DaltDeployementRepository=<id>::default::<url>`

Remarque, pour les dépôt non maven (dépôt brut du jar ou war), il y a le plugin wagon, il est cependant devenu plus maintenable de définir ses déploiement non maven directement dans son script CI/CD.


## Les profils

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
```


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

## Créer son propre plugin

[https://maven.apache.org/guides/plugin/guide-java-plugin-development.html](https://maven.apache.org/guides/plugin/guide-java-plugin-development.html)


## Options avancées de jvm

Il est possible de définir des options à rajouter au démarrage de la jvm par maven

- Fichier .mvn/jvm.config

```
--add-opens java.base/java.lang=ALL-UNNAMED
```



