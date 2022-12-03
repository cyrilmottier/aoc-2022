import { readFile } from "fs/promises";

console.log(
  (await readFile("./input.txt", { encoding: "utf8" }))
    .split("\n")
    .map((rucksack) => [
      rucksack.slice(0, rucksack.length / 2).split(""),
      rucksack.slice(rucksack.length / 2, rucksack.length).split(""),
    ])
    .map(
      ([firstCompartment, secondCompartment]) =>
        firstCompartment.filter((c) => secondCompartment.includes(c))[0]
    )
    .map((c) => c.charCodeAt(0) - (c === c.toLowerCase() ? 96 : 38))
    .reduce((acc, current) => acc + current, 0)
);
