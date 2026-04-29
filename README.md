# Avis — Plateforme de reviews de jeux vidéo

Application full-stack construite sur une **architecture hexagonale** (Ports & Adapters), permettant à des joueurs de soumettre des reviews et à des modérateurs de les approuver ou rejeter.

---

## Stack technique

| Couche | Technologie |
|---|---|
| Backend | Java 25 · Spring Boot 4 · Spring Security · JPA/Hibernate |
| Base de données | H2 in-memory (dev) |
| Authentification | JWT (JJWT 0.12) · BCrypt |
| Frontend | React 18 · TypeScript · Vite · TailwindCSS · React Query |
| Documentation API | Swagger UI (SpringDoc OpenAPI 3) |

---

## Architecture hexagonale

```
adapter/in/web          →  Controllers HTTP (Adapter IN)
application/port/in     →  Interfaces Use Case (Port IN)
application/usecase     →  Handlers métier (Use Cases)
domain/model            →  Entités et logique métier pure
domain/exception        →  Exceptions domaine
application/port/out    →  Interfaces repositories & services (Port OUT)
adapter/out/persistence →  Adapters JPA (Adapter OUT)
adapter/out/security    →  Adapters BCrypt & JWT (Adapter OUT)
infrastructure          →  Configuration Spring (câblage, sécurité, seed)
```

> Les dépendances pointent toujours vers le domaine. Le domaine ne connaît ni Spring, ni JPA, ni HTTP.

---

## Lancer le projet

### Backend

```bash
./mvnw spring-boot:run
```

L'API démarre sur `http://localhost:8080`.  
La base est réinitialisée à chaque démarrage et peuplée automatiquement via `seed.json`.

### Frontend

```bash
cd frontend
npm install
npm run dev
```

Le frontend démarre sur `http://localhost:5173`.

---

## Accès utiles

| URL | Description |
|---|---|
| `http://localhost:8080/swagger-ui.html` | Documentation interactive de l'API |
| `http://localhost:8080/h2-console` | Console H2 (dev uniquement) |

**Connexion H2 :**
- JDBC URL : `jdbc:h2:mem:avisdb`
- Username : `sa`
- Password : *(vide)*

---

## Endpoints principaux

### Authentification — `/api/auth`

| Méthode | Route | Description | Auth |
|---|---|---|---|
| POST | `/register` | Inscription joueur | Public |
| POST | `/login` | Connexion joueur | Public |
| POST | `/logout` | Déconnexion | Bearer |
| POST | `/moderator/login` | Connexion modérateur | Public |
| POST | `/moderator/logout` | Déconnexion modérateur | Bearer |

### Jeux — `/api/games`

| Méthode | Route | Description | Auth |
|---|---|---|---|
| GET | `/` | Liste tous les jeux | Public |
| GET | `/{id}` | Détail d'un jeu | Public |
| POST | `/` | Créer un jeu | Bearer |
| PUT | `/{id}` | Mettre à jour un jeu | Bearer |
| DELETE | `/{id}` | Supprimer un jeu | Bearer |

### Reviews — `/api/reviews`

| Méthode | Route | Description | Auth |
|---|---|---|---|
| GET | `/game/{gameId}` | Reviews d'un jeu | Public |
| GET | `/player/{playerId}` | Reviews d'un joueur | Public |
| POST | `/` | Soumettre une review | Bearer |
| DELETE | `/{reviewId}/player/{playerId}` | Supprimer sa review | Bearer |

### Modération — `/api/moderation`

| Méthode | Route | Description | Auth |
|---|---|---|---|
| GET | `/pending` | Reviews en attente | MODERATOR |
| PATCH | `/{id}/approve/moderator/{modId}` | Approuver | MODERATOR |
| PATCH | `/{id}/reject/moderator/{modId}` | Rejeter | MODERATOR |

---

## Cycle de vie d'une Review

```
DRAFT  ──publish()──▶  PUBLISHED  ──approve(mod)──▶  APPROVED
                    └──reject(mod)──▶  REJECTED
```

- `DRAFT` → état initial à la création
- `PUBLISHED` → après soumission par le joueur
- `APPROVED` / `REJECTED` → après décision du modérateur
- Une review `APPROVED` ne peut plus être rejetée

---

## Configuration

| Propriété | Valeur par défaut | Description |
|---|---|---|
| `jwt.secret` | `change-me-in-production-at-least-32-chars` | Clé HMAC-SHA256 — **à changer en prod** |
| `jwt.expiration-ms` | `86400000` | Durée du token (24h) |
| `app.seed.enabled` | `true` | Charge `seed.json` au démarrage |

---

## Tests

```bash
./mvnw test
```

Les tests unitaires couvrent :
- La logique métier du domaine (`ReviewTest`) — transitions d'état sans Spring
- Les use case handlers (`ReviewSubmitterHandlerTest`, `ReviewApproverHandlerTest`, etc.) — ports mockés avec Mockito
- L'authentification joueur (`PlayerAuthenticatorHandlerTest`)