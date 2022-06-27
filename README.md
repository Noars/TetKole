# TètKole

## Français (English version below)

### Outils utilisés

* JavaFx 17.0.2 (Azul community)
* Gradle 7.4.2
* Spotbugs 5.0.7
* Innosetup 6.2.0

### Comment lancer l'application avec un IDE en local

Pour ma part, j'utilise IntelliJ comme IDE.<br>
Pour lancer l'application, une fois que tout est en place et que gradle a fini de build, j'utilise le run de gradle.<br>
Pour ce faire, allez dans les tâches de gradle puis application et lancer la tâche "run".

### Branche master (défaut)

Cette branche contient la dernière version fonctionnel de TètKole.

### Branche develop

Cette branche permet de développer de nouvelle fonctionnalités ou corriger des bugs existant sans impacter la version fonctionnel.<br>
Une fois la branch Develop satisfaisante, on la merge dans la branche master.

### Branche website

Cette branche contient toute la partie site web de TètKole sur git.<br>
Le lien du site web est -> https://eschang.github.io/TetKole/

### Workflow

Actuellement ce dépôt contient 3 workflows :

* 1 pour l'intégration continue -> .github/ci.yml
* 1 pour automatiser les releases -> .github/release.yml
* 1 pour mettre a jour la page web si il y a eu des modification (gérer par git lui même)

### Fonctionnement de la release de TètKole

Lorsque l'on push dans la branche master, une release est automatiquement généré grâce au workflow release.yml qui se trouve dans le dossier ".github".<br>
Le numéro de la release est toujours la dernière version + 1. <br>
C'est à dire, si la dernière version est "TètKole 1.0.7", alors la prochaine release sera "TètKole 1.0.8".<br>
Pour changer les chiffres supérieurs il faut le faire a la main en éditant la dernière release.

### Générer une release de TètKole en local

Une fois le projet récupéré, il vous suffit de lancer la tâche gradle "generateWindowsInstaller" dans distribution.<br>
Cela va créer une release Windows et Linux dans le dossier build/distribution .

### Fonctionnement des recherches de bugs via Spotbugs

Pour rechercher des bugs via Spotbugs, ila faut lancer la tâche gradle "spotbugsMain" dans verification.<br>
Si jamais il trouve des erreurs, aller sur ce site -> https://spotbugs.readthedocs.io/en/stable/bugDescriptions.html <br>
Puis trouver à quoi correspond l'erreur remonter par Spotbugs.

### Les images utilisées

Toutes les images utilisées dans cette application sont libre de droit.<br>
90% sont des images que j'ai fait à la main sur paint 3D (pour avoir le fond transparent).

### Multilinguisme

L'application contient 2 langues actuellement :
- Français
- Anglais

Dans src/main/resources , il y a un dossier multilinguism.<br>
Si on veut rajouter un langue, il faut créer un nouveau properties.<br>
Par exemple, si on veut rajouter l'italien on crée un fichier "language_it_IT.properties" et dedans on met les traductions.<br>
Puis dans le fichier SettingsPane.java (qui se trouve dans src/main/java/application/ui/pane), faire les modifications nécessaire à son ajout.

## English

### Used tools

* JavaFx 17.0.2 (Azul community)
* Gradle 7.4.2
* Spotbugs 5.0.7
* Innosetup 6.2.0

### How to launch the application with a local IDE

For my part, I use IntelliJ as IDE.<br>
To launch the application, once everything is in place and gradle has finished building, I use the gradle run.<br>
To do this, go to the gradle tasks then application and launch the "run" task.

### Master branch (default)

This branch contains the last working version of TètKole.


### Develop branch

This branch allows you to develop new features or fix existing bugs without impacting the functional version.<br>
Once the Develop branch is satisfactory, we merge it into the master branch.


### Website branch

This branch contains all the website part of TètKole on git.<br>
The website link is -> https://eschang.github.io/TetKole/

### Workflow

Currently this repository contains 3 workflows:

* 1 for continuous integration -> .github/ci.yml
* 1 to automate releases -> .github/release.yml
* 1 to update the web page if there have been changes (managed by git itself)

### Operation of the TètKole release

When you push in the master branch, a release is automatically generated thanks to the release.yml workflow which is in the ".github" folder.<br>
The release number is always the latest version + 1. <br>
That is, if the latest version is "TètKole 1.0.7", then the next release will be "TètKole 1.0.8".<br>
To change the upper numbers it must be done by hand by editing the latest release.

### Generate a TètKole release locally

Once the project is recovered, you just need to launch the "generateWindowsInstaller" gradle task in distribution.<br>
This will create a Windows and Linux release in the build/distribution folder.

### How Spotbugs bug searches work

To search for bugs via Spotbugs, you have to run the gradle task "spotbugsMain" in verification.<br>
If it ever finds errors, go to this site -> https://spotbugs.readthedocs.io/en/stable/bugDescriptions.html <br>
Then find what the error reported by Spotbugs corresponds to.

### Images used

All images used in this app are copyright free.<br>
90% are images that I made by hand on 3D paint (to have the transparent background).

### Multilingualism

The application currently contains 2 languages:
- French
- English

In src/main/resources there is a multilingualism folder.<br>
If you want to add a language, you have to create a new properties.<br>
For example, if we want to add Italian, we create a "language_it_IT.properties" file and in it we put the translations.<br>
Then in the SettingsPane.java file (located in src/main/java/application/ui/pane), make the necessary changes to add it.