import { readFile } from "fs/promises"

function computeScenicScore(map, [x, y]) {
  const tree = map[y][x]

  const topScore = Array.from({
    length: y,
  })
    .map((_, k) => k)
    .reverse()
    .map(y2 => map[y2][x])
    .reduce(
      (acc, current) =>
        acc.every(visibleTree => visibleTree < tree) ? [...acc, current] : acc,
      []
    ).length

  const bottomScore = Array.from({
    length: map.length - 1 - y,
  })
    .map((_, k) => map.length - 1 - k)
    .reverse()
    .map(y2 => map[y2][x])
    .reduce(
      (acc, current) =>
        acc.every(visibleTree => visibleTree < tree) ? [...acc, current] : acc,
      []
    ).length

  const leftScore = Array.from({
    length: x,
  })
    .map((_, k) => k)
    .reverse()
    .map(x2 => map[y][x2])
    .reduce(
      (acc, current) =>
        acc.every(visibleTree => visibleTree < tree) ? [...acc, current] : acc,
      []
    ).length

  const rightScore = Array.from({
    length: map[0].length - 1 - x,
  })
    .map((_, k) => map[0].length - 1 - k)
    .reverse()
    .map(x2 => map[y][x2])
    .reduce(
      (acc, current) =>
        acc.every(visibleTree => visibleTree < tree) ? [...acc, current] : acc,
      []
    ).length

  return topScore * leftScore * bottomScore * rightScore
}

console.log(
  (await readFile("./input.txt", { encoding: "utf8" }))
    .split("\n")
    .map(line => line.split(""))
    .reduce(
      (acc, currentLine, indexY, parsedMap) =>
        Math.max(
          acc,
          currentLine.reduce(
            (acc2, _, indexX) =>
              Math.max(acc2, computeScenicScore(parsedMap, [indexX, indexY])),
            0
          )
        ),
      0
    )
)
