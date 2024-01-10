package game

import (
	"strconv"
	"time"

	"github.com/alarmfox/game-repository/model"
)

type Game struct {
	ID          int64      `json:"id"`
	Name        string     `json:"name"`
	Round       int        `json:"round"` 
	Class       string     `json:"class"`
	Description string     `json:"description"`
	Difficulty  string     `json:"difficulty"`
	CreatedAt   time.Time  `json:"createdAt"`
	UpdatedAt   time.Time  `json:"updatedAt"`
	StartedAt   *time.Time `json:"startedAt"`
	ClosedAt    *time.Time `json:"closedAt"`
	Players     []Player   `json:"players,omitempty"`
	Robot       int64      `json:"robot,omitempty"`
}

type Player struct {
	ID        int64  `json:"id"`
	AccountID string `json:"accountId"`
}

type Robot struct {
	ID          int64     `json:"id"`
	CreatedAt   time.Time `json:"createdAt"`
	UpdatedAt   time.Time `json:"updatedAt"`
	TestClassId string    `json:"testClassId"`
	Scores      string    `json:"scores"`
	Difficulty  string    `json:"difficulty"`
	Type        int8      `json:"name"`
}

type CreateRequest struct {
	Name        string     `json:"name"`
	Players     []string   `json:"players"`
	Description string     `json:"description"`
	Difficulty  string     `json:"difficulty"`
	StartedAt   *time.Time `json:"startedAt,omitempty"`
	ClosedAt    *time.Time `json:"closedAt,omitempty"`
}

func (CreateRequest) Validate() error {
	return nil
}

type UpdateRequest struct {
	CurrentRound int        `json:"currentRound"`
	Name         string     `json:"name"`
	Description  string     `json:"description"`
	StartedAt    *time.Time `json:"startedAt,omitempty"`
	ClosedAt     *time.Time `json:"closedAt,omitempty"`
}

func (UpdateRequest) Validate() error {
	return nil
}

type KeyType int64

func (c KeyType) Parse(s string) (KeyType, error) {
	a, err := strconv.ParseInt(s, 10, 64)
	return KeyType(a), err
}

func (k KeyType) AsInt64() int64 {
	return int64(k)
}

type IntervalType time.Time

func (IntervalType) Parse(s string) (IntervalType, error) {
	t, err := time.Parse(time.DateOnly, s)
	return IntervalType(t), err
}

func (k IntervalType) AsTime() time.Time {
	return time.Time(k)
}

type AccountIdType string

func (AccountIdType) Parse(s string) (AccountIdType, error) {
	return AccountIdType(s), nil
}

func (a AccountIdType) AsString() string {
	return string(a)
}

func fromModel(g *model.Game) Game {
	return Game{
		ID:          g.ID,
		Name:        g.Name,
		Round:       g.Round, // GRUPPO A3
		Class:       g.Class,
		Description: g.Description.String,
		Difficulty:  g.Difficulty,
		CreatedAt:   g.CreatedAt,
		UpdatedAt:   g.UpdatedAt,
		StartedAt:   g.StartedAt,
		ClosedAt:    g.ClosedAt,
		Players:     parsePlayers(g.Players),
		Robot:       g.Robot,
	}
}

func parsePlayers(players []model.Player) []Player {
	res := make([]Player, len(players))
	for i, player := range players {
		res[i] = Player{
			ID:        player.ID,
			AccountID: player.AccountID,
		}
	}
	return res
}

/*
func parseRobot(robot model.Robot) Robot {
	rob := Robot{
		ID: robot.ID,
		//aggiungi altri campi
	}

	return rob
}
*/
