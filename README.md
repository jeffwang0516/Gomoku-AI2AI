# Gomoku-AI2AI

## Setup
### Define first, (second) steps for player1(black)
```
// In class GameModel

// Set first move and second move for player 1
public Move firstStep = new Move(7, 7);
public Move secStep = new Move(7, 4);
```
### Choose between rules to follow: **Standard/PRO**

- Rule reference: http://gomokuworld.com/gomoku/2

```
// In class GameModel

// Set this to 1 for Standard, 2 for Pro
private int proRuleStepsCnt = 1; 
```


### Set player 1 mode

- Based on minimax+alphabeta, enable/disable **finding must kill algorithm**

```
// In class GameModel

// Enable minimax+AB with/without finding must kill steps
private boolean player1_findMustKillEnabled = true; 
```

### Set player 2 mode
- 0 -> minimax + alpha
- 1 -> Greedy

```
// 0 for minimax, 1 for greedy
private int player2_minimaxOrGreedy = 0; 
```

### Settings in player1's algorithm

#### Search Depth

- Set minimax search depth ( Should be even, <=6, it takes lots of time to perform one step if deeper than 6)
- Set depth for finding must kills

```
// In class AIStrategyABAndMustKill
..

int minimaxDepth = 4; // Should be even WHEN this is a max node
int findKillDepth = 7;
..
```

#### Enable/Disable Zobrist hash function
- It's for improving calculate speed, main idea is to reduce recalculated states. [REFERENCE](https://www.geeksforgeeks.org/minimax-algorithm-in-game-theory-set-5-zobrist-hashing/)
- The performance improved is about 5% or lower, **NOT GOOD**
- In one of my tests, the result is
  - With zobrist: 85025 ms
  - Without zobrist: 92481 ms
```
// In class AIStrategyABAndMustKill
..

private boolean isZobristEnabled = false;

..
```

## Execution
Run GomokuMain.java
