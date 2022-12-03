import { readFile } from "fs/promises";

console.log(
  (await readFile("./input.txt", { encoding: "utf8" }))
    .split("\n")
    .reduce(
      (acc, current, i) =>
        i % 3 === 0
          ? [...acc, [current.split("")]]
          : [
              ...acc.slice(0, acc.length - 1),
              [...acc.at(-1), current.split("")],
            ],
      []
    )
    .map(
      ([firstElf, secondElf, thirdElf]) =>
        firstElf
          .filter((c) => secondElf.includes(c))
          .filter((c) => thirdElf.includes(c))[0]
    )
    .map((c) => c.charCodeAt(0) - (c === c.toLowerCase() ? 96 : 38))
    .reduce((acc, current) => acc + current, 0)
);
