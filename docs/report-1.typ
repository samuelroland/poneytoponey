#set page(margin: 1.5cm)

#show raw.where(block: false): box.with(
  fill: luma(240),
  inset: (x: 3pt, y: 0pt),
  outset: (y: 3pt),
  radius: 2pt,
)
#show raw.where(block: true): block.with(
  inset: 10pt,
  radius: 2pt,
  stroke: 1pt + luma(200),
)

#set page(header: none, margin: 1.5cm)
#set text(font: "Cantarell", size: 12pt)

#align(center)[

  #text(size: 30pt)[PoneyToPoney]

  #text(
    size: 18pt,
  )[Système en pair à pair de messagerie instantanée \ basée sur le style architecturale "Object-based".]

  #text(size: 14pt)[ASAD - 2026]

  #text(size: 14pt)[Léna Beust, Kylian Manzini, Iléane Crocq, Samuel Roland]
]

#outline(title: "Table of contents")

#pagebreak()
= Introduction
Ce rapport fait état du projet PoneyToPoney réalisé dans le cadre du cours Advanced Software Architecture and Design délivré à l'HES-SO Master de Lausanne par Yassin Rekik.
Le projet PoneyToPoney, développé en langage Java, voue à réaliser une application de chat point à point au sein d'un groupe d'utilisateurs, il se veut basé sur une architecture du type "Object based" en utilisant l'API Java RMI. Le fonctionnement de l'application PoneyToPoney est le suivant : À tout moment un membre peut demander un chat avec un autre membre, même si celui-ci a déjà d'autres chats en cours. Un participant peut accepter ou refuser une demande de chat, comme il peut arrêter le chat quand il en a envie. Au bout
d'un timeout, un chat non accepté est considéré comme refusé.
= Labo 1
L'objectif de cette première partie était de concevoir une application avec l'architecture Object based imposée. En se basant sur la programmation orienté objet, une application d'échange de message en paire à paire a été imaginée et implémentée. La première partie de ce rapport présente ainsi les différents diagrammes qui ont permis de clarifier les attendus et contraintes du projet dans sa première implémentation.
== 1. *Diagramme de cas d'utilisation*

#figure(
  image("usecase.svg"),
  caption: [Diagramme de cas d'utilisations],
)
Les différents cas d'utilisation se définissent comme suis :

- *Avoir la liste des membres connectés* : l'utilisateur.trice aura la possibilité de mettre à jour la liste des membres connectés couramment au réseau.
- *Envoyer un message* : l'uilisateur.trice pourra envoyer des messages aux autres membres ayant accepté son invitation.
- *Recevoir un message* : L'utilisateur.trice visualisera les messages qu'il a reçu sur des conversations avec d'autres membres qu'il aura acceptés au préalable.
- *Fermer le chat* : L'utilisateur.trice aura la possibilité de clôturer une conversation préalablement ouverte, cette conversation sera alors supprimée et non consultable.
- *Consulter la liste et l'historique des conversations* : L'utilisateur.trice pourra visualiser les conversations courantes avec d'autres membres du réseau ainsi que les différents messages qui les composent.


== 2. *Diagramme DFD level 1*

#figure(
  image("dfd.svg"),
  caption: [Data Flow Diagram Level 1],
)
L'objectif de ce diagramme était de mettre en lumière le différents rôles des membres du réseau : pour initier la conversation, envoyer et recevoir des notifications d'acceptation ou de refus `(1)`, participer à la conversation `(2)`, et de fermeture de conversations `(3)`. Les structures de stockage pour renseigner des membres actuellement contactables `(D1)` et les sessions de conversations courantes `(D2)` ont également été représentées.

== 3. *Diagramme de composants*
#figure(
  image("component.svg"),
  caption: [Diagramme de composants],
)
Le diagramme de composants permet d'observer les principales interfaces de l'application. Les interfaces les plus importantes sont celles qui relient les sous-systèmes, notamment les interfaces gérant les interactions pair-à-pair et les échanges avec le serveur Directory.

== 4. *Diagramme de déploiement*
#figure(
  image("deployement.svg"),
  caption: [Diagramme de déploiement],
)
Ce diagramme permet de définir clairement les différentes machines à l'œuvre lors du fonctionnement de l'application. Celle-ci requiert le démarrage d'un serveur centralisé Directory, qui fait office d'annuaire en gérant la liste des utilisateurs du réseau. Cette liste est mise à disposition des clients afin de permettre l'établissement des conversations. Les clients communiquent ensuite directement entre eux en pair-à-pair via Java RMI.

== 5. *Diagramme de requirement*
#figure(
  image("requirements.svg"),
  caption: [Diagramme de requirements],
)
Ce diagramme permet de clarifier la fonctionnalité principalement attendue de notre application : envoyer des messages dans un réseau de membres. L'objectif de ce diagramme est aussi de découper cette fonctionnalité en sous-blocs pour définir les sous-fonctionnalités indispensables pour satisfaire les principales.

== 6. *Diagramme de classe*

#figure(
  image("class.simplified.svg", width: 100%),
  caption: [Diagramme de classe simplifié de l'application PoneyToPoney],
)

#figure(image("class.final.svg", width: 100%), caption: [Diagramme de classe complet de l'application PoneyToPoney.]).
Note: Les C signifient "Classe", les I sont des Interfaces. Les carrés rouges représentent la visibilité privée et les ronds vers la visibilité publique. Deux processus sont démarrés séparement, d'où la présence de deux `main` sur `App` et `DirectoryServer`.

Le diagramme de classe montre l'application `App` qui gère une vue `ShellView`, représentant le prompte de l'utilisateur.trice, et un modèle `HumanEntity`. `HumanEntity` est le seul objet publié sous l'interface `Identity` sur le registre RMI. Toutes les interactions en pair à pair doivent ainsi passer par ce point de communication. Les objets `HumanIdentity`, représentant les utilisateurs du réseau, contiennent le pseudo de l'utilisateur `username`, une liste de `Chat` courants contenant des `Message` envoyés par les utilisateurs réseaux, ainsi qu'une liste des participants connus `Knownparticipants`.

== 7. *Diagramme de séquence*

#figure(
  image("sequence.svg"),
  caption: [Diagramme de séquence],
)

Le groupe a choisi de créer un diagramme de séquence pour ce projet, bien qu'il ne figurait pas dans les attentes du rendu.
Ce choix a été motivé par le besoin d'avoir une idée claire des différents scénarios possibles et du fonctionnement du réseau, permettant à l'équipe PoneyToPoney de cerner d'éventuels problèmes d'implémentation. À cet effet, ce diagramme clarifie l'ordre des étapes ainsi que les flux de données qui transiteraient dans l'application.
Nous avons cerné 3 scénarios typiques de l'application PoneyToPoney qui sont représentés danc la `figure 8` :
- L'initiation de la conversation avec la connexion au réseau et la mise à jour des utilisateurs courants du réseau dans Directory.
- L'envoi d'une demande de chat et la réponse de la personne contactée comprennant deux possibilités : refus, ou acceptation.
- La fermeture d'une discussion par déconnexion imprévue ou commande.
Nous pouvons constater la manière dont est gérée la fin d'une conversation dans le bas de ce diagramme, le cas nominal doit être géré sans oublié les cas imprévus comme le départ soudain d'un membre de la conversation (suite à un problème technique de son ordinateur par exemple).

#pagebreak()

= Labo 2
