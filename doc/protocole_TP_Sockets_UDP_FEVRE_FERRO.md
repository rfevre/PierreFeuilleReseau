# DA2I - Programmation répartie Sockets UDP
## Par FERRO Thomas et FEVRE Rémy

---

## Protocole utilisé entre le client et le serveur :

- ### Création partie :
  - #### Client :
    - `CREATE:PSEUDO:ID_PARTIE:NB_JOUEURS:NB_MANCHES`
      - `PSEUDO` : Le pseudonyme du joueur (ex : 'DuckDuckGoGoGadgeto').
      - `ID_PARTIE` : L'identifiant de la partie (ex : 'Partie de la mort').
      - `NB_JOUEURS` : Le nombre de joueurs (ex : 2).
      - `NB_MANCHES` : Le nombre de manches (ex : 3).
  - #### Serveur :
    - `OK/KO`
      - `OK` : Partie créée avec succès.
      - `KO` : Erreur à la création de la partie.

- ### Connexion :
  - #### Client :  
    - `JOIN:PSEUDO:ID_PARTIE:`
      - `PSEUDO` : L'identifiant de la partie (ex : 'Partie de la mort').
      - `ID_PARTIE` : L'identifiant de la partie (ex : 'Partie de la mort').
  - #### Serveur :
    - A la réception d'une connexion d'un joueur :
      - `WAIT/READY/ERROR`
        - `WAIT` : Connexion réussie, en attente des autres joueurs.
        - `READY` : Tous les joueurs ont rejoint.
        - `ERROR` : Erreur au moment de rejoindre la partie (id ou nom incorrect, salle pleine, etc...).
    - Au démarrage d'une partie :
      - `GO` : Lancement de la partie.

- ### En partie :
  - #### Client :
    - `CHOIX:PSEUDO:ID_PARTIE`
      - `CHOIX` : Le choix du joueur (`ROCK/PAPER/SCISSORS`).
      - `PSEUDO` : Le pseudonyme du joueur (ex : 'DuckDuckGoGoGadgeto').
      - `ID_PARTIE` : L'identifiant de la partie (ex : 'Partie de la mort').
  - #### Serveur :
    - A la réception d'un choix du joueur :
      - `WAIT/ERROR`
        - `WAIT` : Choix reçu, en attente des choix des autres joueurs.
        - `ERROR` : Erreur à la reception ou partie annulée.
    - Quand tous les joueurs ont envoyés leur choix :
      - `CHOIX_J1:CHOIX_J2:SCORE_J1:SCORE_J2:NB_MANCHES`
        - `CHOIX_J1` : Choix du joueur qui a ouvert le serveur.
        - `CHOIX_J2` : Choix du joueur qui a rejoint le serveur.
        - `SCORE_J1` : Score du joueur qui a ouvert le serveur.
        - `SCORE_J2` : Score du joueur qui a rejoint le serveur.
        - `NB_MANCHES` : Le nombre de manches (ex : 3).

- ### Fin de partie / Déconnexion :
  - #### Client :
    - Se déconnecter d'une partie :
      - `DISCONNECT:PSEUDO:ID_PARTIE`
        - `PSEUDO` : Pseudonyme du joueur cherchant à se deconnecter.
        - `ID_PARTIE` : L'identifiant de la partie (ex : 'Partie de la mort').
    - Fermer une partie :
      - `CLOSE:PSEUDO:ID_PARTIE`
        - `PSEUDO` : Pseudonyme du joueur cherchant à fermer la partie (doit être le joueur qui à ouvert le serveur).
        - `ID_PARTIE` : L'identifiant de la partie (ex : 'Partie de la mort').
  - #### Serveur :
    - A la fermeture d'une partie (score atteint ou requête de fermeture par un joueur) :
      - `ID_PARTIE:KO:GAGNANT:POINTS`
        - `ID_PARTIE` : L'identifiant de la partie (ex : 'Partie de la mort').
        - `GAGNANT` : Le joueur qui a le plus de points.
        - `POINTS` : Le nombre de points du gagnant.
