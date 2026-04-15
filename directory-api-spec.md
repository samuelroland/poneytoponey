# User-IP Directory API

Simple HTTP service mapping `username -> IP`.

* plain text only
* no JSON
* HTTP status codes indicate result

---

## Endpoints

### GET /list

Returns all entries.

200 OK

```
alice / 10.10.12.12
bob / 10.10.12.13
```

204 No Content
(empty directory)

---

### POST /register

Body:

```
alice
```

Registers the username with the sender IP.

201 Created

```
alice / 10.10.12.12
```

400 Bad Request
(empty username)

409 Conflict
(username or IP already exists)

---

### POST /remove

Removes the entry matching the sender IP.

200 OK

```
alice / 10.10.12.12
```

404 Not Found
(no entry for this IP)

---

## Format

Each line:

```
username / ip
```
