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

  #text(size: 14pt)[Léna Beust, Kylian Manzini, Ileane Crocq, Samuel Roland]
]

#outline(title: "Table of contents")

#pagebreak()

= Labo 1
L'objectif de cette première partie était de concevoir une application avec une architecture imposée. En se basant sur la programmation orienté objet, une application d'échange de message en paire à paire a été imaginée et implémentée. Ce rapport présente ainsi les différents diagrammes qui ont permis de clarifier les attendus et contraintes du projet.
== 1. *Diagramme de cas d'utilisation*

#figure(
  image("usecase.svg"),
  caption: [Diagramme de cas d'utilisations],
)
Les différents cas d'utilisation se définissent comme suis :

- *Avoir la liste des membres connectés* : l'utilisateur.trice aura la possibilité de mettre à jour la liste des membres connectés couramment au réseau.
- *Envoyer un message* : l'uilisateur.trice pourra envoyer des messages aux autres membres ayant accepté son invitation.
- *Recevoir un message* : L'utilisateur.trice visualisera les messages qu'il a reçu sur des conversations acceptées avec d'autre membres au préalable.
- *Fermer le chat* : L'utilisateur.trice aura la possibilité de clôturer une conversation préalablement ouverte, cette conversation sera alors supprimée et non consultable.
- *Consulter la liste et l'historique des conversations* : L'utilisateur.trice pourra visualiser les conversations, donc les échanges de messages, en cours avec d'autres membres du réseau.


== 2. *Diagramme DFD level 1*

#figure(
  image("dfd.png"),
  caption: [Data Flow Diagram Level 1],
)
L'objectif de ce diagramme était de mettre en lumière le différents rôles des membres du réseau : pour initier la conversation, participer à la conversation, envoyer et recevoir des notifications d'acceptation, de refus et de fermeture de conversations. Les structures de stockage pour renseigner des membres actuellements contactable et les sessions de conversations disponibles ont également été représentées.

== 3. *Diagramme de composant*
#figure(
  image("component.svg"),
  caption: [Diagramme de composants],
)

== 4. *Diagramme de déploiement*
#figure(
  image("deployement.svg"),
  caption: [Diagramme de déploiement],
)

== 5. *Diagramme de requirement*
#figure(
  image("requirements.svg"),
  caption: [Diagramme de requirements],
)
Ce diagramme permet de clarifier la fonctionnalité principalement attendue de notre application : envoyer des messages dans un réseau de membres. L'objectif de ce diagramme est aussi de découper cette fonctionnalité en sous-blocs pour préciser les fonctionnalités qui précisent et permettent de satisfont les fonctionnalité principale.

== 6. *Diagramme de classe*

#figure(
  image("class.svg", width: 80%),
  caption: [Diagramme de classe de l'application PoneyToPoney],
)
Le diagramme de classe montre l'application `App` qui gère une vue `ShellView`, représentant le prompte de l'utilisateur.trice, et un modèle `HumanEntity`. `HumanEntity` est le seul objet publié sous l'interface `Identity` sur le registre RMI. Toutes les interactions en pair à pair doivent ainsi passer par ce point de communication.

== 7. *Diagramme de séquence*

#figure(
  image("sequence.svg"),
  caption: [Diagramme de séquence],
)

Le groupe a choisi de créer un diagramme de séquence de ce projet, bien qu'il ne soit pas dans les attentes du rendu.
Ce choix a été motivé par le besoin d'avoir une idée claire de l'utilisation du réseau et des différentes étapes des discussions. Nous avions besoin de clarifier l'ordre des étapes et les flux de données qui transiterait dans notre application. Ce diagramme permet donc de mettre en avant l'initiation de la conversation avec la connection au réseau, l'envoie d'une demande de chat et la réponse de la personne contacté (refus, acceptation ou absence de réponses). On peut donc facilement voir les scénarios envisageables. On peut également voir dans le bas de ce diagramme, comment est gérée la fin d'une conversation : par l'envoie d'une commande ou une déconnexion imprévue. Le cas nominal doit être géré sans oublié les cas imprévus comme le départ soudain d'un membre de la conversation (suite à un problème technique sur son ordinateur par exemple).

#pagebreak()

= Labo 2
