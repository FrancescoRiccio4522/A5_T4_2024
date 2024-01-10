package com.g2.Model;

import java.time.*;

/*
GAME API T4
type Game struct {
	ID           int64      `json:"id"`
    Name         string     `json:"name"`
    Round        int        `json:"round"` // VALE
    Class        string     `json:"class"`
	Description  string     `json:"description"`
	Difficulty   string     `json:"difficulty"`
	CreatedAt    time.Time  `json:"createdAt"`
	UpdatedAt    time.Time  `json:"updatedAt"`
	StartedAt    *time.Time `json:"startedAt"`
	ClosedAt     *time.Time `json:"closedAt"`
	Players      []Player   `json:"players,omitempty"`
    Robot        Robot      `json:"robot,omitempty"`
}
*/

public class Game {
    private long id;
    private String name;
    private int round;
    private String testedClass;
    private String description;
    private String difficulty;
    private LocalDate createdAt;
    private LocalDate updateAt;
    private LocalDate startedAt;
    private LocalDate closedAt;
    private long playerId; // Adattare per il multi-player
    private String robot;

    public Game(int playerId, String description, String name, String difficulty) {
        this.playerId = playerId;
        this.description = description;
        this.name = name;
        this.difficulty = difficulty;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(long playerId) {
        this.playerId = playerId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public LocalDate getClosedAt() {
        return closedAt;
    }

    public void setClosedAt(LocalDate closedAt) {
        this.closedAt = closedAt;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public LocalDate getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(LocalDate updateAt) {
        this.updateAt = updateAt;
    }

    public LocalDate getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(LocalDate startedAt) {
        this.startedAt = startedAt;
    }

    public String getRobot() {
        return robot;
    }

    public void setRobot(String robot) {
        this.robot = robot;
    }

    public String getTestedClass() {
        return testedClass;
    }

    public void setTestedClass(String testedClass) {
        this.testedClass = testedClass;
    }
}
