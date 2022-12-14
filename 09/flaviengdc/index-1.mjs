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
        let { H, T, tailPositionHistory } = state

        if (instruction === "U") H = [H[0], H[1] - 1]
        if (instruction === "D") H = [H[0], H[1] + 1]
        if (instruction === "R") H = [H[0] + 1, H[1]]
        if (instruction === "L") H = [H[0] - 1, H[1]]

        if (Math.abs(H[0] - T[0]) > 1) {
          T[0] = H[0] > T[0] ? H[0] - 1 : H[0] + 1
          if (Math.abs(H[1] - T[1]) > 0) T[1] = H[1]
        }
        if (Math.abs(H[1] - T[1]) > 1) {
          T[1] = H[1] > T[1] ? H[1] - 1 : H[1] + 1
          if (Math.abs(H[0] - T[0]) > 0) T[0] = H[0]
        }

        if (!tailPositionHistory.includes(JSON.stringify(T))) {
          tailPositionHistory.push(JSON.stringify(T))
        }

        return { H, T, tailPositionHistory }
      },
      {
        H: [0, 0],
        T: [0, 0],
        tailPositionHistory: [],
      }
    ).tailPositionHistory.length
)
