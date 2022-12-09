import { readFile } from "fs/promises"

const isTreeVisibleFromOutside = (map, [x, y]) =>
  Array.from({
    length: y,
  })
    .map((_, k) => k)
    .every(y2 => map[y2][x] < map[y][x]) ||
  Array.from({
    length: map.length - 1 - y,
  })
    .map((_, k) => map.length - 1 - k)
    .every(y2 => map[y2][x] < map[y][x]) ||
  Array.from({
    length: x,
  })
    .map((_, k) => k)
    .every(x2 => map[y][x2] < map[y][x]) ||
  Array.from({
    length: map[0].length - 1 - x,
  })
    .map((_, k) => map[0].length - 1 - k)
    .every(x2 => map[y][x2] < map[y][x])

console.log(
  (await readFile("./input.txt", { encoding: "utf8" }))
    .split("\n")
    .map(line => line.split(""))
    .reduce(
      (acc, currentLine, indexY, parsedMap) =>
        acc +
        currentLine.reduce(
          (acc2, _, indexX) =>
            acc2 +
            (isTreeVisibleFromOutside(parsedMap, [indexX, indexY]) ? 1 : 0),
          0
        ),
      0
    )
)
