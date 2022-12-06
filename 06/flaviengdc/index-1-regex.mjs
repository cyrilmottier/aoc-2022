import { readFile } from "fs/promises"

console.log(
  (await readFile("./input.txt", { encoding: "utf8" })).match(
    /(.)(?!\1)(.)(?!\1|\2)(.)(?!\1|\2|\3)/
  ).index + 4
)
