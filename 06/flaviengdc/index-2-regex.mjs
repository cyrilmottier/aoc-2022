import { readFile } from "fs/promises"

const markerLength = 14

function buildRegex(n) {
  return Array.from(new Array(n - 1)).reduce((acc, _c, index) => {
    return `${acc}(.)(?!${Array.from(new Array(index + 1).keys())
      .map(i => `\\${i + 1}`)
      .join("|")})`
  }, "")
}

console.log(
  (await readFile("./input.txt", { encoding: "utf8" })).match(
    new RegExp(buildRegex(markerLength))
  ).index + markerLength
)
