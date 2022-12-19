import { readFile } from "fs/promises"

console.log(
  (await readFile("./input.txt", { encoding: "utf8" }))
    .split("\n")
    .map(line => line.split(" "))
    .reduce(
      (history, [instruction, value]) =>
        instruction === "noop"
          ? [...history, history.at(-1)]
          : [...history, history.at(-1), history.at(-1) + Number(value)],
      [1]
    )
    .reduce(
      (acc, currentValue, currentIndex) =>
        (currentIndex - 19) % 40 === 0
          ? acc + (currentIndex + 1) * currentValue
          : acc,
      0
    )
)
