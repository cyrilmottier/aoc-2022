import { readFile } from "fs/promises"

console.log(
  (await readFile("./input.txt", { encoding: "utf8" }))
    .split("\n")
    .map(pair => {
      const [firstElf, secondElf] = pair.split(",")
      const [firstElfStart, firstElfEnd] = firstElf.split("-")
      const [secondElfStart, secondElfEnd] = secondElf.split("-")
      return [
        [Number(firstElfStart), Number(firstElfEnd)],
        [Number(secondElfStart), Number(secondElfEnd)],
      ]
    })
    .filter(
      ([firstElf, secondElf]) =>
        (firstElf[0] <= secondElf[0] && firstElf[1] >= secondElf[1]) ||
        (secondElf[0] <= firstElf[0] && secondElf[1] >= firstElf[1])
    ).length
)
