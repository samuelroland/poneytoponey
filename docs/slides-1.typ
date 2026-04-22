#import "@preview/diatypst:0.9.1": *

#set text(font: "Noto Sans", lang: "fr")
#show raw: set text(font: "Noto Sans Mono")
#show raw.where(block: true): set par(leading: 2.5mm)

// Display inline code in a small box with light gray backround that retains the correct baseline.
#show raw.where(block: false): box.with(
  fill: luma(240),
  inset: (x: 3pt, y: 0pt),
  outset: (y: 3pt),
  radius: 2pt,
)

// See docs of diatypst on https://typst.app/universe/package/diatypst
#show: slides.with(
  title: [ PoneyToPoney - chat en pair à pair Objet-Based],
  subtitle: "ASAD 2026 - Présentation partie 1",
  date: "22/04/2026",
  authors: "Léna Beust, Kylian Manzini, Iléane Crocq, Samuel Roland",

  // Optional Styling (for more and explanation of options take a look at the typst universe)
  ratio: 16 / 9,
  theme: "normal",
  layout: "small",
  title-color: blue.darken(54%),
  toc: false,
  count: "number",
  footer: false,
)

// Make sure all slides content is centered
#show: it => align(
  center + horizon,
  it,
)

== Java RMI
#align(left + top, [Architecture imposée : *Object-based*])

#image("schemas/rmi.png", height: 89%)

== Java RMI

#align(left + top, [

  - applications Java distribuées
  - méthodes distantes de Java
  - utilisation de sérialisation pour marshaller et démarshaller les informations échangées
  - pas de types tronqués : possibilité de faire du vrai polymorphisme orienté objet
])

#align(left + bottom, [Documentation : https://docs.oracle.com/javase/8/docs/technotes/guides/rmi/index.html])



== Diagramme de cas d'utilisations
#image("usecase.svg")
== Diagramme de requirements
#image("requirements.svg")

== Diagramme de séquences
#image("sequence.svg", height: 112%)

== Diagramme DFD level 1
#image("dfd.pdf", width: 63%)

== Diagramme de classe
// On montre pas le premier, on le met juste ici en backup.
#image("class.final.svg", width: 75%)

#image("class.simplified.svg", width: 100%)



== Diagramme de composants

#image("component.pdf", height: 106%)

== Diagramme de déploiement

#image("deployement.pdf")

== Démonstration

#image("assets/demo.png")

