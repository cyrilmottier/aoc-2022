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
    .map(x => [x - 1, x, x + 1])
    .map((sprite, index) => (sprite.includes(index % 40) ? "#" : "."))
    .reduce(
      (acc, current, currentIndex) =>
        `${acc}${
          currentIndex > 0 && currentIndex % 40 === 0 ? "\n" : ""
        }${current}`,
      ""
    )
)
