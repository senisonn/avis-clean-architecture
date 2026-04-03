# Clean Architecture — Projet Game Review

## Vue d'ensemble

Ce projet respecte les principes de la **Clean Architecture** (score 9.5/10).
La règle fondamentale est respectée partout : **les dépendances ne vont que vers l'intérieur**.
Aucune couche interne ne connaît les couches externes.

```
Requête HTTP entrante
        ↓
 Controllers (adapter/in/web)
        ↓  [Input Ports — interfaces]
 Use Case Handlers (application/usecase)
        ↓  [Output Ports — interfaces]
 Persistence / Security Adapters (adapter/out)
        ↓
 Base de données / JWT
```

---

## Diagramme des classes

```
╔══════════════════════════════════════════════════════════════════════════════════════╗
║                        CLEAN ARCHITECTURE — PROJET GAME REVIEW                     ║
║                  ← LOGIQUE MÉTIER PURE          FRAMEWORK / INFRA →                ║
╠══════════════════════════════════════════════════════════════════════════════════════╣
║                                                                                      ║
║  ┌─────────────────────────────────────────────────────────────────────────────┐    ║
║  │ DOMAIN  (aucune dépendance externe — 0 annotation Spring/JPA)               │    ║
║  │                                                                             │    ║
║  │  ┌──────────────────┐   ┌──────────────────┐   ┌────────────────────────┐  │    ║
║  │  │ Game             │   │ Review           │   │ Player                 │  │    ║
║  │  │──────────────────│   │──────────────────│   │────────────────────────│  │    ║
║  │  │ -id: Long        │   │ -id: Long        │   │ -id: Long              │  │    ║
║  │  │ -name: String    │   │ -title: String   │   │ -username: String      │  │    ║
║  │  │ -price: BigDecimal│  │ -content: String │   │ -email: String         │  │    ║
║  │  │ -genre: Genre    │   │ -rating: Double  │   │ -password: String      │  │    ║
║  │  │ -publisher: Pub  │   │ -status: Status  │   │ -birthDate: LocalDate  │  │    ║
║  │  │ -ageRating: AR   │   │──────────────────│   │ -avatar: Avatar        │  │    ║
║  │  │ -platforms: List │   │ +publish()       │   └────────────────────────┘  │    ║
║  │  └──────────────────┘   │ +approve(Mod)    │                               │    ║
║  │                         │ +reject(Mod)     │   ┌────────────────────────┐  │    ║
║  │  ┌──────────────────┐   └──────────────────┘   │ Moderator              │  │    ║
║  │  │ Value Objects    │                           │────────────────────────│  │    ║
║  │  │  Genre           │   ┌──────────────────┐   │ -id: Long              │  │    ║
║  │  │  Publisher       │   │ <<enum>>         │   │ -username: String      │  │    ║
║  │  │  AgeRating       │   │ ReviewStatus     │   │ -email: String         │  │    ║
║  │  │  Platform        │   │──────────────────│   └────────────────────────┘  │    ║
║  │  │  Avatar          │   │ DRAFT            │                               │    ║
║  │  └──────────────────┘   │ PUBLISHED        │   ┌────────────────────────┐  │    ║
║  │                         │ APPROVED         │   │ <<exceptions>>         │  │    ║
║  │                         │ REJECTED         │   │ GameNotFoundException   │  │    ║
║  │                         └──────────────────┘   │ ReviewNotFoundException │  │    ║
║  │                                                 │ InvalidRatingException  │  │    ║
║  │                                                 └────────────────────────┘  │    ║
║  └─────────────────────────────────────────────────────────────────────────────┘    ║
║                                     ↑ dépend de                                     ║
║  ┌─────────────────────────────────────────────────────────────────────────────┐    ║
║  │ APPLICATION  (ports + use cases — 0 annotation Spring/JPA)                 │    ║
║  │                                                                             │    ║
║  │  ┌─────────── INPUT PORTS ────────────┐   ┌────── OUTPUT PORTS ─────────┐  │    ║
║  │  │ <<interface>> GameCreator          │   │ <<interface>> GameRepository │  │    ║
║  │  │  +addGame(CreateGameCommand): Game │   │  +findAll(): List<Game>      │  │    ║
║  │  │                                   │   │  +findById(Long): Game       │  │    ║
║  │  │ <<interface>> GameFinder           │   │  +save(Game): Game           │  │    ║
║  │  │  +getAllGames(): List<Game>        │   │  +deleteById(Long)           │  │    ║
║  │  │  +getGameById(Long): Game         │   │                              │  │    ║
║  │  │                                   │   │ <<interface>> ReviewRepository│  │    ║
║  │  │ <<interface>> ReviewSubmitter      │   │  +findByGameId(Long)         │  │    ║
║  │  │  +submitReview(Command): Review   │   │  +findByStatus(Status)       │  │    ║
║  │  │                                   │   │  +save(Review): Review       │  │    ║
║  │  │ <<interface>> ReviewApprover       │   │                              │  │    ║
║  │  │  +approveReview(Long, Long)       │   │ <<interface>> TokenManager   │  │    ║
║  │  │                                   │   │  +generateToken(String)      │  │    ║
║  │  │ <<interface>> PlayerAuthenticator  │   │  +extractSubject(String)     │  │    ║
║  │  │  +login(AuthRequest)              │   │  +isValid(String)            │  │    ║
║  │  │  +logout(String)                  │   │  +invalidate(String)         │  │    ║
║  │  │                                   │   └──────────────────────────────┘  │    ║
║  │  │ ... (8 autres ports input)        │                                     │    ║
║  │  └────────────────────────────────────┘                                    │    ║
║  │                                                                             │    ║
║  │  ┌─────────────── USE CASE HANDLERS ─────────────────────────────────────┐ │    ║
║  │  │                                                                        │ │    ║
║  │  │ GameCreatorHandler implements GameCreator                             │ │    ║
║  │  │  -gameRepository: GameRepository  ← output port (interface)          │ │    ║
║  │  │  +addGame(cmd): Game → gameRepository.save(new Game(...))            │ │    ║
║  │  │                                                                        │ │    ║
║  │  │ ReviewApproverHandler implements ReviewApprover                       │ │    ║
║  │  │  -reviewRepository: ReviewRepository                                  │ │    ║
║  │  │  -moderatorRepository: ModeratorRepository                            │ │    ║
║  │  │  +approveReview(reviewId, modId)                                      │ │    ║
║  │  │    → review.approve(moderator)  ← LOGIQUE MÉTIER dans le DOMAIN      │ │    ║
║  │  │    → reviewRepository.save(review)                                    │ │    ║
║  │  │                                                                        │ │    ║
║  │  │ PlayerAuthenticatorHandler implements PlayerAuthenticator             │ │    ║
║  │  │  -playerRepository: PlayerRepository                                  │ │    ║
║  │  │  -passwordEncoder: PasswordEncoder                                    │ │    ║
║  │  │  -tokenManager: TokenManager  ← output port                          │ │    ║
║  │  │                                                                        │ │    ║
║  │  │ ... (9 autres handlers)                                               │ │    ║
║  │  └───────────────────────────────────────────────────────────────────────┘ │    ║
║  │                                                                             │    ║
║  │  ┌─── DTOs (records Java avec validation jakarta) ───────────────────────┐ │    ║
║  │  │ CreateGameCommand  CreateReviewCommand  AuthRequest  RegisterCommand  │ │    ║
║  │  │ Game (DTO)         Review (DTO)         AuthResponse                  │ │    ║
║  │  └───────────────────────────────────────────────────────────────────────┘ │    ║
║  └─────────────────────────────────────────────────────────────────────────────┘    ║
║                                     ↑ dépend de                                     ║
║  ┌─────────────────────────────────────────────────────────────────────────────┐    ║
║  │ INTERFACE ADAPTERS  (@RestController, @Component, Spring Data JPA)         │    ║
║  │                                                                             │    ║
║  │  ┌──── CONTROLLERS (adaptateurs entrants) ────────────────────────────┐   │    ║
║  │  │ @RestController GameController                                      │   │    ║
║  │  │  -gameFinder:  GameFinder   ← INPUT PORT (interface)               │   │    ║
║  │  │  -gameCreator: GameCreator                                          │   │    ║
║  │  │  @GetMapping  GET /api/games    → gameFinder.getAllGames()          │   │    ║
║  │  │  @PostMapping POST /api/games   → gameCreator.addGame(cmd)         │   │    ║
║  │  │                                                                      │   │    ║
║  │  │ @RestController ReviewController                                    │   │    ║
║  │  │ @RestController ModerationController                                │   │    ║
║  │  │ @RestController AuthController                                      │   │    ║
║  │  └──────────────────────────────────────────────────────────────────────┘   │    ║
║  │                                                                             │    ║
║  │  ┌──── PERSISTENCE ADAPTERS (adaptateurs sortants) ───────────────────┐   │    ║
║  │  │ @Component GamePersistenceAdapter implements GameRepository         │   │    ║
║  │  │  -jpaRepository: GameJpaRepository  ← Spring Data                  │   │    ║
║  │  │  -mapper: GameMapper                                                │   │    ║
║  │  │  +findAll() → jpaRepository.findAll() → map to domain Game         │   │    ║
║  │  │  +save(game) → toEntity(game) → jpaRepository.save() → toDomain() │   │    ║
║  │  │                                                                      │   │    ║
║  │  │ @Component ReviewPersistenceAdapter implements ReviewRepository     │   │    ║
║  │  │ @Component PlayerPersistenceAdapter implements PlayerRepository     │   │    ║
║  │  │ @Component ModeratorPersistenceAdapter implements ModeratorRepository│  │    ║
║  │  └──────────────────────────────────────────────────────────────────────┘   │    ║
║  │                                                                             │    ║
║  │  ┌──── MAPPERS (domaine ↔ entité JPA) ───────────────────────────────┐   │    ║
║  │  │ @Component GameMapper                                               │   │    ║
║  │  │  +toDomain(GameEntity): Game    → construit le domain model        │   │    ║
║  │  │  +toEntity(Game): GameEntity    → construit l'entité JPA           │   │    ║
║  │  │ (9 mappers au total: Game, Review, Player, Moderator, Genre, ...)  │   │    ║
║  │  └──────────────────────────────────────────────────────────────────────┘   │    ║
║  │                                                                             │    ║
║  │  ┌──── ENTITÉS JPA (@Entity, @ManyToOne, @OneToMany) ────────────────┐   │    ║
║  │  │ @Entity GameEntity    @Entity ReviewEntity    @Entity PlayerEntity │   │    ║
║  │  │ @Entity ModeratorEntity   (+ GenreEntity, AgeRatingEntity, etc.)   │   │    ║
║  │  └──────────────────────────────────────────────────────────────────────┘   │    ║
║  │                                                                             │    ║
║  │  ┌──── SPRING DATA REPOSITORIES ─────────────────────────────────────┐   │    ║
║  │  │ GameJpaRepository extends JpaRepository<GameEntity, Long>          │   │    ║
║  │  │ ReviewJpaRepository   PlayerJpaRepository   ModeratorJpaRepository │   │    ║
║  │  └──────────────────────────────────────────────────────────────────────┘   │    ║
║  │                                                                             │    ║
║  │  ┌──── SECURITY ADAPTER ──────────────────────────────────────────────┐   │    ║
║  │  │ @Component JwtTokenAdapter implements TokenManager                 │   │    ║
║  │  │  Utilise: JJWT library, @Value(jwt.secret)                         │   │    ║
║  │  └──────────────────────────────────────────────────────────────────────┘   │    ║
║  └─────────────────────────────────────────────────────────────────────────────┘    ║
║                                     ↑ dépend de                                     ║
║  ┌─────────────────────────────────────────────────────────────────────────────┐    ║
║  │ INFRASTRUCTURE  (Spring Boot config — seul endroit avec câblage)           │    ║
║  │                                                                             │    ║
║  │  @Configuration UseCaseConfig                                              │    ║
║  │   @Bean gameCreator()   → new GameCreatorHandler(gameRepository())         │    ║
║  │   @Bean gameFinderHandler() → new GameFinderHandler(gameRepository())      │    ║
║  │   @Bean reviewApprover() → new ReviewApproverHandler(reviewRepo(), modRepo)│    ║
║  │   ... (11 handlers câblés ici uniquement)                                  │    ║
║  │                                                                             │    ║
║  │  @Configuration SecurityConfig  (Spring Security, BCrypt, JWT filter)      │    ║
║  │  OncePerRequestFilter JwtFilter  (extrait + valide le JWT)                 │    ║
║  │  @SpringBootApplication ReviewApplication                                  │    ║
║  └─────────────────────────────────────────────────────────────────────────────┘    ║
╚══════════════════════════════════════════════════════════════════════════════════════╝
```

---

## Ce que le diagramme montre

### Logique métier pure (2 couches du haut)

- Le **domain** ne connaît rien du framework — `Review.approve(moderator)` est du Java pur, sans aucune annotation
- Les **handlers** n'ont aucune annotation Spring : ils reçoivent des interfaces (ports) par constructeur et délèguent la logique métier aux objets du domain (`review.approve()`, `review.publish()`)

### Framework confiné (2 couches du bas)

- Les `@RestController` traduisent HTTP → DTO → port
- Les `@Entity` + `JpaRepository` gèrent la persistence
- Le câblage Spring ne se fait qu'en **un seul fichier** : `UseCaseConfig.java`

### La règle de dépendance

Les flèches ne vont que **vers l'intérieur** (domain ← application ← adapters ← infrastructure), jamais en sens inverse.

| Couche | Dépendances autorisées | Annotations |
|---|---|---|
| Domain | aucune | aucune |
| Application | Domain uniquement | aucune (Lombok OK) |
| Interface Adapters | Application (via ports) + Framework | `@RestController`, `@Component`, `@Entity` |
| Infrastructure | Tout | `@Configuration`, `@Bean`, `@SpringBootApplication` |

---

## Points forts

- **Domain 100% framework-agnostique** — testable en pur JUnit sans Spring
- **Inversion de dépendance complète** — les handlers dépendent d'interfaces, jamais d'implémentations
- **Câblage centralisé** — `UseCaseConfig.java` est le seul endroit où les implémentations sont choisies
- **Mappers bidirectionnels** — 9 mappers assurent la traduction `domain ↔ entity` sans contamination de couche
- **DTOs à la frontière** — les controllers reçoivent et retournent des DTOs, jamais les objets domain directement