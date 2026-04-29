### S1 — Communication Security

**Description**

Protect messages exchanged between two nodes against interception and modification.

**Acceptance Criteria**
- [ ] Messages are encrypted in transit
- [ ] Any modification is detectable
- [ ] Communication is refused if not secure

**Ideas of technologies**
- [ ] private and public keys

---

### S2 — User Authentication

**Description**

Implement an authentication system with simple profile management.

**Acceptance Criteria**
- [ ] A user must be authenticated to access the system (via a password ?)
- [ ] Profiles are persisted
- [ ] Sessions expire correctly when user quit

---

### D1 — User Availability

**Description**

Manage user availability using timeouts and timestamps, with fallback mechanisms in case of issues.

**Acceptance Criteria**
- [ ] Add an acknowledgement for messages when receiving
- [ ] The chat is closed when the acknowledgement is timedout

**Nice to have**
- [ ] A user is marked as online or offline (pas utile si juste ack ?)
- [ ] A user is marked offline after timeout  (pas utile si juste ack ?)
- [ ] Timestamps allow detection of delays
- [ ] A retry or fallback mechanism is present (?)

**Ideas of technology**
- [ ] Acknowlegment

---

### D2 — Conversation Persistence

**Description**

Save conversations to allow restoration after disconnection.

**Acceptance Criteria**
- [ ] Messages are persisted outside the session
- [ ] History is restored after reconnection
- [ ] No message is lost in case of system shutdown
- [ ] If user quit, the conversation is closed and no longer saved

---

### M1 — Message Prioritization

**Description**

Allow assigning priorities (high or low) to messages that impact their display.

**Acceptance Criteria**
- [ ] Priority messages are highlighted
- [ ] Display order respects priorities
- [ ] The system remains readable and consistent
- [ ] By default, the priority is low ?

**Nice to have**
- [ ] Having a TUI to higlight the priored messages

---

### M2 — Group Chats and Broadcast

**Description**

Support group discussions and broadcasts. We will implement first broadcast. If we have time/fun/nothing to do, we will implement the group chats.

**Acceptance Criteria**
- [ ] Ability to send a global message to all users
- [ ] (A user can create a group)
- [ ] (A user can join a group)
- [ ] (Messages are received by all members)

