# poneytoponey
> Object-based architecture project for MSE ASAD at HES-SO

A peer-to-peer chat system, with simple direct conversations (2 persons). The interface will be a simple interactive CLI.

## Important files
- [docs/brainstorm.md](docs/brainstorm.md)

## Installation
TODO: setup `pocs` folder with a POC with Java RMI and Gradle to experiment and test the network setup ...

TODO: setup gradle project

## Usage

Just run
```sh
cd project
gradle run
```
If you don't have gradle installed, you can use the gradle wrapper `gradlew`:
```sh
cd project
./gradlew run
```

## Game usage
Once started, the game acts like a small shell, with a few commands to do the chat actions. This defines the available commands of our shell.
- `join alice`: join the network with username `alice`
- `participants`: list participants in the network
- `chat jamy`: create a new chat with `jamy` if there is no existing chat and only if user `jamy` is in the network. If chat exist, just switch to it.
- `chats`: list non-closed chats
- `send hey there`: send a message in current chat with text `hey there`
- `close`: list non-closed chats

