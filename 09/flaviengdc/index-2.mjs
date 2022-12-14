import { readFile } from "fs/promises"

console.log(
  (await readFile("./input.txt", { encoding: "utf8" }))
    .split("\n")
    .reduce((acc, instructionRaw) => {
      const [direction, n] = instructionRaw.split(" ")

      return [
        ...acc,
        ...Array.from({
          length: Number(n),
        }).map(_ => direction),
      ]
    }, [])
    .reduce(
      (state, instruction) => {
        const { rope, tailPositionHistory } = state

        const knotHead = rope.at(0)
        const knotTail = rope.at(-1)

        if (instruction === "U") knotHead[1] = knotHead[1] - 1
        if (instruction === "D") knotHead[1] = knotHead[1] + 1
        if (instruction === "R") knotHead[0] = knotHead[0] + 1
        if (instruction === "L") knotHead[0] = knotHead[0] - 1

        const updateNextKnot = (knot, nextKnot) => {
          if (
            Math.abs(knot[0] - nextKnot[0]) > 1 &&
            Math.abs(knot[1] - nextKnot[1]) > 1
          ) {
            nextKnot[0] = knot[0] > nextKnot[0] ? knot[0] - 1 : knot[0] + 1
            nextKnot[1] = knot[1] > nextKnot[1] ? knot[1] - 1 : knot[1] + 1
          } else if (Math.abs(knot[0] - nextKnot[0]) > 1) {
            nextKnot[0] = knot[0] > nextKnot[0] ? knot[0] - 1 : knot[0] + 1

            if (Math.abs(knot[1] - nextKnot[1]) > 0) nextKnot[1] = knot[1]
          } else if (Math.abs(knot[1] - nextKnot[1]) > 1) {
            nextKnot[1] = knot[1] > nextKnot[1] ? knot[1] - 1 : knot[1] + 1

            if (Math.abs(knot[0] - nextKnot[0]) > 0) nextKnot[0] = knot[0]
          }
        }

        rope.forEach((currentKnot, index) => {
          if (rope[index + 1]) updateNextKnot(currentKnot, rope[index + 1])
        })

        if (!tailPositionHistory.includes(JSON.stringify(knotTail))) {
          tailPositionHistory.push(JSON.stringify(knotTail))
        }

        return {
          rope,
          tailPositionHistory,
        }
      },
      {
        rope: [
          [0, 0],
          [0, 0],
          [0, 0],
          [0, 0],
          [0, 0],
          [0, 0],
          [0, 0],
          [0, 0],
          [0, 0],
          [0, 0],
        ],
        tailPositionHistory: [],
      }
    ).tailPositionHistory.length
)
