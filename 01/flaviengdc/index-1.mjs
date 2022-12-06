import { readFile } from "fs/promises"
console.log(
  (await readFile("./input.txt", { encoding: "utf8" }))
    .split("\n")
    .reduce(
      (acc, current) =>
        current === ""
          ? [...acc, 0]
          : [...acc.slice(0, -1), acc.at(-1) + current * 1],
      [0]
    )
    .reduce((acc, current) => (current > acc ? current : acc), 0)
)
