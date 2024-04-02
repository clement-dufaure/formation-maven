# Formation maven

:arrow_forward: [Document](https://formation.dufau.re/formation-maven)


## 0. Récupérer le code source du TP

### 0.1. Cloner le dépot git

```bash
git clone https://github.com/clement-dufaure/formation-maven.git
```

### 0.2. Présentation du contenu

Les exercices s'effectuent sur build-me.

Les exercices sont séquentiels, chaque dossier correspond à la fois au corrigé de l'exercice précédent et au point de départ de l'exercice suivant.

### 0.3. Quelques prérequis

Une distribution java 17+

## 1

Transformer le projet java brut (0-sans-maven) en projet maven.

- Installer maven et le configurer si nécessaire 
- Réorganiser le projet selon l'arborescence usuelle maven
- Préparer un fichier pom

L'exercice est validé si `mvn package` finit en succès et que la log indique avoir compilé quelquechose !

Les dépendances requises :
En runtime :
- org.springframework
    - spring-webmvc
    - spring-aop
- org.slf4j
    - slf4j-api
    - slf4j-simple
En test :
- junit-jupiter-engine


Remarques :
- Sur l'arborescence:
    - Les source java -> src/main/java
    - Les source java dédiées aux test -> src/test/java
    - les ressources (fichiers non java utiles au build) -> src/main/resources
        - le fichier "properties" 
    - les resources liées à l'éxécution sur serveur web -> src/main/webapp
        - le dossier WEB-INF
- Le packaging sera ici le war
- Plusieurs build en échecs seront peut être nécessaire pour trouver toutes les dépendances nécessaires.
    - Il est possible d'avancer en étape en effectuant d'abord des `mvn compile` pour ne gérer que les dépendances principales. 
    - Puis `mvn test` pour ajouter les dépendances uniquement requise pour la phase de test : déclarer ces dépendances de tests en tant que tel : **on ne les veut pas dans le war**. Il faut utiliser `<scope>test</scope>`
- Penser à mettre le /target dans le gitignore


## 2

Modulariser le projet avec les modules suivant :
- model : les objets java métiers
- dao : la dao
- batch : pour le batch
- web : l'application web


- Créer de nouveaux fichiers pom dans les modules après avoir découpé l'arborescence en sous dossiers
- Transformer le module maintenant parent en packaging `pom` et y déclarer les modules
- Ajouter le pom parent dans les modules
- Faire en sorte que la commande `mvn package` continue à fonctionner comme avant !

Remarques :
- Il ne faut pas oublier les nouvelles dépendences intermodules: (web -> core -> model)
- On peut commencer à identifier les dépendences réellement nécessaires à chaque module et ainsi découper la liste initiale de dépendance 

## 3

Améliorer la gestion des dépendances.
- Transformer la liste de dépendances initiale en DependencyManagement (puis en properties eventuellement)
- Reporter sans version les dépendances nécessaires à chaque module (en se rappelant qu'un dépendance ne doit pas être copié entre deux modules si un dépend de l'autre)

Remarques :
- On peut vérifier que l'on a choisi les versions les plus récentes avec `mvn versions:display-dependency-updates`
- On peut vérifier la présence de CVE avec des plugins tels que dependency-check

## 4

Gérer le cas du livrable batch qui doit contenir les dépendances nécessaires.
Utiliser le plugin assembly pour créer un zip avec le format suivant : 
```
mon-batch-<version>.zip
    |- mon-batch.jar
    |- un dossier lib avec les dépendances
```


Utiliser pour cela le plugin assembly avec la conf suivante :

```xml
    <formats>
        <format>zip</format>
    </formats>
    <includeBaseDirectory>false</includeBaseDirectory>

    <fileSets>
        <dependencySets>
            <dependencySet>
                <outputDirectory>/lib</outputDirectory>
            </dependencySet>
        </dependencySets>
    <fileSets>
```

