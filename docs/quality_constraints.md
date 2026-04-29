### S1 — Communication Security

**Description**

Protect messages exchanged between two nodes against interception and modification.

**Acceptance Criteria**
- [ ] Messages are encrypted in transit
- [ ] Any modification is detectable
- [ ] Communication is refused if not secure

---

### S2 — User Authentication

**Description**

Implement an authentication system with simple profile management.

**Acceptance Criteria**
- [ ] A user must be authenticated to access the system
- [ ] Profiles are persisted
- [ ] Sessions expire correctly

---

### D1 — User Availability

**Description**

Manage user availability using timeouts and timestamps, with fallback mechanisms in case of issues.

**Acceptance Criteria**
- [ ] Add an acknowledgement for messages
- [ ] The chat is closed when the acknowledgement is timedout

**Nice to have**
- [ ] A user is marked as online or offline
- [ ] A user is marked offline after timeout
- [ ] Timestamps allow detection of delays
- [ ] A retry or fallback mechanism is present

---

### D2 — Conversation Persistence

**Description**

Save conversations to allow restoration after disconnection.

**Acceptance Criteria**
- [ ] Messages are persisted outside the session
- [ ] History is restored after reconnection
- [ ] No message is lost in case of system shutdown

---

### M1 — Message Prioritization

**Description**

Allow assigning priorities to messages that impact their display.

**Acceptance Criteria**
- [ ] Priority messages are highlighted
- [ ] Display order respects priorities
- [ ] The system remains readable and consistent

---

### M2 — Group Chats and Broadcast

**Description**

Support group discussions and broadcasts.

**Acceptance Criteria**
- [ ] A user can create a group
- [ ] A user can join a group
- [ ] Messages are received by all members
- [ ] Ability to send a global message to all users
