import { readFile } from "fs/promises"

const MATCH_POINTS = {
  LOOSE: 0,
  DRAW: 3,
  WIN: 6,
}

const PICK_POINTS = {
  X: 1,
  Y: 2,
  Z: 3,
}

const SCORE_TABLE = {
  X: {
    A: PICK_POINTS.X + MATCH_POINTS.DRAW,
    B: PICK_POINTS.X + MATCH_POINTS.LOOSE,
    C: PICK_POINTS.X + MATCH_POINTS.WIN,
  },
  Y: {
    A: PICK_POINTS.Y + MATCH_POINTS.WIN,
    B: PICK_POINTS.Y + MATCH_POINTS.DRAW,
    C: PICK_POINTS.Y + MATCH_POINTS.LOOSE,
  },
  Z: {
    A: PICK_POINTS.Z + MATCH_POINTS.LOOSE,
    B: PICK_POINTS.Z + MATCH_POINTS.WIN,
    C: PICK_POINTS.Z + MATCH_POINTS.DRAW,
  },
}

console.log(
  (await readFile("./input.txt", { encoding: "utf8" }))
    .split("\n")
    .map(l => l.split(" "))
    .reduce(
      (acc, [elfPick, myPick]) => (acc += SCORE_TABLE[myPick][elfPick]),
      0
    )
)
