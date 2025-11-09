# 🚢🎯 Battleship Server (Java) 🌈🔥

Benvenuto al **server Java** per un gioco di **Battaglia Navale** a due giocatori! 🌊⚓ Qui puoi sfidare un amico e vedere chi affonda tutte le navi 🛳️💣.

---

## 🌟 Caratteristiche principali 🌟

- Gioco per **2 giocatori** 👯
- Griglia di gioco **5x5** 🟦🟦🟦🟦🟦
- Ogni giocatore posiziona **3 navi** 🚢🚢🚢
- Turni alternati per attaccare il campo avversario ⚔️
- Risposta immediata per ogni attacco:  
  - `"HIT"` 💥  
  - `"MISS"` 💨  
  - `"INVALID"` ❌
- Controllo automatico della **vittoria** 🏆🎉

---

## 🛠️ Tecnologie utilizzate 🛠️

- **Java 17+** ☕
- **ServerSocket** per connessioni TCP 🌐
- **Socket** per la comunicazione client-server 📡
- **BufferedReader/PrintWriter** per invio/ricezione dei messaggi ✉️

---

## ⚡ Come funziona ⚡

1. Il server avvia un `ServerSocket` sulla porta **3000** 🔌
2. Attende la connessione di **2 giocatori** 👫
3. I giocatori ricevono:
   - `"WAIT"` ⏳ se devono aspettare l'altro giocatore
   - `"READY"` ✅ quando entrambi sono connessi
4. Ogni giocatore posiziona **3 navi** 🚢 inviando le coordinate X e Y 📍
5. Inizia la fase di **attacco** a turni 🔁:
   - Il server riceve le coordinate dell'attacco
   - Risponde con:
     - `"HIT"` 💥 se colpisce una nave
     - `"MISS"` 💨 se non colpisce nulla
     - `"INVALID"` ❌ se coordinate errate o già attaccate
6. Il gioco termina quando un giocatore colpisce tutte le navi avversarie 🏴‍☠️ e riceve `"WIN"` 🏆🎊

---

## 📂 Struttura del codice 📂

- `Main.java`  
  Contiene tutta la logica del server:
  - Connessione dei giocatori 🌐
  - Posizionamento delle navi 🚢
  - Turni di attacco ⚔️
  - Controllo della vittoria 🏆

- Funzioni principali:
  - `checkIfExist(int v)` ✅ – verifica che le coordinate siano valide (0–4)
  - `checkIfItAlreadyHitted(int x, int y, int[][] board)` ❌ – controlla se la cella è già stata attaccata
  - `printBoard(int[][] board)` 🖨️ – stampa lo stato della griglia
  - `checkWin(int[][] board)` 🏁 – verifica se tutte le navi avversarie sono state colpite

---

## 🚀 Come avviare il server 🚀

1. Compilare il progetto:

```bash
javac -d out src/com/server/Main.java
