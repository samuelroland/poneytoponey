# poneytoponey
> Object-based architecture project for MSE ASAD at HES-SO

A peer-to-peer chat system, with simple direct conversations (2 persons). The interface will be a simple interactive CLI.

## Important files
- [docs/brainstorm.md](docs/brainstorm.md)

## Usage
Start an RMI registry with port 7000.
```sh
cd project
just rmi
```

Then you can start one client with
```sh
cd project
just client
```

If you don't have gradle installed, you can use the gradle wrapper `gradlew`:
```sh
cd project
./gradlew run
```

**WARNING**: if you get an some class not found, this because the RMI registry doesn't know where to find the classes like here:
```
RemoteException occurred in server thread; nested exception is: 
        java.rmi.UnmarshalException: error unmarshalling arguments; nested exception is: 
        java.lang.ClassNotFoundException: poneytoponey.Identity (no security manager: RMI class loader disabled)
```
The solution is in the `justfile`, we have configured `CLASSPATH=app/bin/main/ rmiregistry 7000` to make sure it can find them.

## Demo
Here is how you can run a demo with 4 persons joining the network. We have to bypass the school Wifi restrictions to do peer-to-peer communications.
1. Choose a person to be the leader of the demo. This person will host the RMI Registry and manage network.
1. As a leader, create a Wifi Hotspot and ask the 3 others to join it. The leader should share its IP address in private LAN on the hotspot to the others.
1. Also plug your phone with USB tethering mode to share cellular data to access internet, like an Ethernet cable would give access to Internet.
1. Everyone build and run the Docker image
    ```
    cd project
    docker build --network host -t poneytoponey-demo .
    docker run -it -p 7000:7000 -v .:/project poneytoponey-demo bash
    ```
1. The leader runs `just rmi &` to start the RMI registry in background, and runs `just client` then (localhost by default).
1. The other members first start a tmate session by typing `tmate` and share a SSH read-only link to the leader.
1. Then, they run `just client <ip of the leader> 7000` to connect to the same Java RMI server on port 7000.
1. The leader can them open 3 other terminal panes with the links of tmate sessions to see everything on one screen, that can be shared on the beamer !

## Game usage
Once started, the game acts like a small shell, with a few commands to do the chat actions. This defines the available commands of our shell.
- `join alice`: join the network with username `alice` (we suppose all clients are using a unique username)
- `users`: list participants in the network
- `chat jamy`: create a new chat with `jamy` if there is no existing chat and only if user `jamy` is in the network. If chat exist, just switch to it.
- `approve jamy`: approve the chat asked by another user `jamy`
- `refuse jamy`: explicitly refuse the chat asked by another user `jamy`
- `chats`: list non-closed chats
- `send hey there`: send a message in current chat with text `hey there`
- `close`: close the current chat

## TODOs
- classe chat et message: Léna
- shell view: Kylian
- HumanIdentity part 1 -> username -> createChat Samuel
- HumanIdentity part 2 -> approveChat -> closeChat Ileane
