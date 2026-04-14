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
  )[Système en pair à pair de messagerie instantanée \ basée sur le style architecturale "object-based".]

  #text(size: 14pt)[ASAD - 2026]

  #text(size: 14pt)[Léna Beust, Kylian Manzini, Ileane Crocq, Samuel Roland]
]

#outline(title: "Table of contents")

#pagebreak()


== Conception

#figure(
  image("usecase.svg"),
  caption: [Diagramme de cas d'utilisations],
)

#figure(
  image("class.svg"),
  caption: [Diagramme de classe montrant l'application `App` qui gère une vue `ShellView` et un modèle `HumanEntity`. `HumanEntity` est le seul objet publié sous l'interface `Identity` sur le registre RMI. Toutes les interactions en pair à pair doivent ainsi passer par ce point de communication.],
)

#figure(
  image("requirements.svg"),
  caption: [Diagramme de requirements],
)

#figure(
  image("dfd.svg"),
  caption: [DFD TODO],
)

#figure(
  image("deployement.svg"),
  caption: [Diagramme de déploiement],
)

#figure(
  image("component.svg"),
  caption: [Diagramme de composants],
)


== Implémentation
TODO ?
