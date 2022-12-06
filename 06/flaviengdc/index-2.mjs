import { readFile } from "fs/promises"

console.log(
  (await readFile("./input.txt", { encoding: "utf8" }))
    .split("")
    .map((_char, index, arr) => arr.slice(index - 14, index))
    .map(marker => Array.from(new Set(marker)))
    .findIndex(marker => marker.length === 14)
)
