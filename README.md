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
gradle run
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
