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
  title: [ PoneyToPoney - chat en pair à pair objet-based],
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

== Démonstration

== Diagramme de classe
#figure(
  image("requirements.svg"),
  caption: [Diagramme de requirements],
)

== Diagramme de cas d'utilisations
#image("usecase.svg"),

== Diagramme séquence
#image("sequence.svg")

== Diagramme de classe
// On montre pas le premier, on le met juste ici en backup.
#figure(
  image("class.final.svg", width: 100%),
  caption: [Diagramme de classe complet de l'application PoneyToPoney. Les C signifie "Classe", les I sont des Interfaces],
).

#figure(
  image("class.simplified.svg", width: 100%),
  caption: [Diagramme de classe simplifié de l'application PoneyToPoney],
)


== Diagramme de déploiement


???o
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
