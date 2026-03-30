package fr.esgi.soheil.kevin.domain.model;

import java.time.LocalDateTime;

public class Review {

    private Long          id;
    private String        content;
    private Float         rating;
    private LocalDateTime submittedAt;
    private ReviewStatus  status = ReviewStatus.DRAFT;
    private Game          game;
    private Player        player;
    private Moderator     moderator;

    public Review() {}

    // ── Business methods — the core of the domain ─────────────

    public void publish() {
        if (this.status != ReviewStatus.DRAFT)
            throw new IllegalStateException(
                    "Only a DRAFT review can be published, current: " + this.status);
        this.status      = ReviewStatus.PUBLISHED;
        this.submittedAt = LocalDateTime.now();
    }

    public void approve(Moderator mod) {
        if (this.status != ReviewStatus.PUBLISHED)
            throw new IllegalStateException(
                    "Only a PUBLISHED review can be approved, current: " + this.status);
        this.moderator = mod;
        this.status    = ReviewStatus.APPROVED;
    }

    public void reject(Moderator mod) {
        if (this.status == ReviewStatus.APPROVED)
            throw new IllegalStateException("An already approved review cannot be rejected");
        this.moderator = mod;
        this.status    = ReviewStatus.REJECTED;
    }

    // ── Getters & Setters ──────────────────────────────────────
    public Long         getId()          { return id; }
    public void         setId(Long id)   { this.id = id; }
    public String       getContent()     { return content; }
    public void         setContent(String c)    { this.content = c; }
    public Float        getRating()      { return rating; }
    public void         setRating(Float r)     { this.rating = r; }
    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void         setSubmittedAt(LocalDateTime d) { this.submittedAt = d; }
    public ReviewStatus getStatus()      { return status; }
    public void         setStatus(ReviewStatus s)   { this.status = s; }
    public Game         getGame()        { return game; }
    public void         setGame(Game g)       { this.game = g; }
    public Player       getPlayer()      { return player; }
    public void         setPlayer(Player p)    { this.player = p; }
    public Moderator    getModerator()   { return moderator; }
    public void         setModerator(Moderator m) { this.moderator = m; }
}

