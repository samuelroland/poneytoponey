#!/bin/fish -i
# This is a script to generate a class diagram, in full and in simplified forms.
# https://github.com/samuelroland/ctp
ctp java project/app/src/main/ docs/class.final.puml
ctp java project/app/src/main/ docs/class.simplified.puml
