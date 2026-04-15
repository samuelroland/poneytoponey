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
== 1. *Diagramme de cas d'utilisation*

#figure(
  image("usecase.svg"),
  caption: [Diagramme de cas d'utilisations],
)

== 2. *Diagramme DFD level 1*

#figure(
  image("dfd.png"),
  caption: [Data Flow Diagram Level 1],
)
L'objectif de ce diagramme était de mettre en lumière le différents rôles des membres du réseau : pour initier la conversation, participer à la conversation, envoyer et recevoir des notifications d'acceptation, de refus et de fermeture de conversations.

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

== 6. *Diagramme de classe*

#figure(
  image("class.svg", width : 80%),
  caption: [Diagramme de classe de l'application PoneyToPoney],
)
 Le diagramme de classe montre l'application `App` qui gère une vue `ShellView` et un modèle `HumanEntity`. `HumanEntity` est le seul objet publié sous l'interface `Identity` sur le registre RMI. Toutes les interactions en pair à pair doivent ainsi passer par ce point de communication.

== 7. *Diagramme de séquence*

#figure(
  image("sequence.svg"),
  caption: [Diagramme de séquence],
)

#pagebreak()

= Labo 2 
