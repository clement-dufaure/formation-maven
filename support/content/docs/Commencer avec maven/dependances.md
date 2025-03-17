---
title: "La gestion des dépendances"
description: ""
lead: ""
draft: false
images: []
toc: true
weight: 205
---

Les dépendances maven reposent sur un système de référentiel.
Par défaut elle sont recherchées sur maven central : https://repo.maven.apache.org/maven2

On peut dans ses configurations préciser d'autres référentiels pour aller chercher les dépendances, ou paramétrer un miroir.


## Définition d'une dépendances

Une dépendance consiste à aller charger un autre projet maven, on va donc retourver les éléments indispensables à la déclaration d'un projet : groupId, artifactId et version.

```xml
<dependency>
  <groupId>re.dufau</groupId>
  <artifactId>mon.autre.projet</artifactId>
  <version>1.2.3</version>
</dependency>
```

Il y a cependant d'autres paramètres requis mais ils ont des valeurs par défaut.

### Le type

Généralement, on va se servir d'un dépendance pour récupérer du code sous la forme de classes compilées dans un jar.

Le type est donc généralement `jar`, il s'agit de la valeur par défaut.

Cependant il peut être de type `pom` dans un cas spécifique présenté plus tard

### Le scope

Il permet de préciser l'usage de la dépendance, par défaut le scope est `compile`

- `compile` : la dépendance sert dans le code 'main' et doit être incluse au livrable
- `test` : la dépendance ne sert que pour l'éxécution des test (ex : jUnit), elle n'est pas utilisé pour la compilation de 'main' et ne sera pas incluse au livrable (les IDE ne la résolve donc pas dans 'main')
- `provided`: la dépendance sert dans le code 'main' mais ne doit pas être inclus au livrable car sera disponible par ailleurs au runtime (cas de certaines librairies tomcat par exemple)
- `runtime`: non nécessaire à la compilation de 'main' mais nécessaire à l'éxécution (cas de Class.forName())

## Les dépendances transitives

Un bibilothèque mise en dépendance peut également déclarer ses propres dépendances.
Elles seront alors automatiquement intégrées au projet.

`Si A --> B et B --> C&D Alors A --> B&C&D`

Il est possible d'exclure certaines dépendances venant avec la librairie 
```xml
<exclusions>
    <exclusion>
      <groupId>commons-logging</groupId>
      <artifactId>commons-logging</artifactId>
    </exclusion>
  </exclusions>
```

Cependant, le code peut utiliser une dépendance qu'on a pas explicitement déclaré, ce qui peut être dangereux en maintenance

### Sélection de version

Mais que se passe-t-il si la même dépendance est déclarées dans plusieurs dépendances, et même directement dans nos dépendances, le tout en plusieures versions différente ?

Revenons sur la déclaration de la dépendance

```xml
<version>1.0</version>
```

Il s'agit alors d'une simple indication de la version souhaitée, on n'aura pas la garantie d'avoir au final cette version si d'autres dépendances requierent des dépendances transitives plus stricte

Si nos dépendances et toutes les dépendances transitives utilisent cette syntaxe, la version retenue sera :
- 1 - notre version déclarée le cas échéant
- 2 - l'une des version transitive selon l'ordre de déclaration

Remarque : il n'y a pas détermination de la version "la plus récente" par exemple

### Connaitre les résolutions de version effectives

```sh
mvn dependency:tree -Dverbose
```

### Être précis sur les versions

Le système sera un peu plus complexe si certaines déclaration demande une version plus précise, ce qui peut parfois être cependant préférable pour s'assurer d'avoir par exemple au moins la premiere version ayant telle ou telle feature.


La déclaration se fait alors via un sytème d'intervalles

```xml
<version>[1.0]</version>
```

La 1.0 et rien d'autre

```xml
<version>[1.0)</version>
```

La 1.0 et toutes les version supérieures


```xml
<version>(,1.0),(1.0,)</version>
```

Tout sauf la 1.0

```xml
<version>[1.0,2.0)</version>
```

N'importe quelle version superieure ou égale à 1.0 et strictement inférieure à la 2.0


Maven cherchera un compromis entre version souhaitées mais il risque d'y avoir un blocage de compilation en cas de 2 demandes strictes irréconciliables.

par exemple :

```xml
<version>(,1.0],[2.0,)</version>
```

en meme temps que

```xml
<version>[1.5]</version>
```

## Documentation de référence

[https://maven.apache.org/guides/introduction/introduction-to-dependency-mechanism.html](https://maven.apache.org/guides/introduction/introduction-to-dependency-mechanism.html)

