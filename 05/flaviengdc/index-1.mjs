import { readFile } from "fs/promises"

console.log(
  await (async () => {
    const parsed = (await readFile("./input.txt", { encoding: "utf8" })).split(
      "\n"
    )

    const separatorIndex = parsed.findIndex(line => line === "")

    const initialState = parsed
      .slice(0, separatorIndex - 1)
      .map(line => Array.from(line).filter((_char, i) => i % 4 === 1))
      .reduce((acc, current, indexX) => {
        current.forEach((char, indexY) => {
          indexX === 0 ? (acc[indexY] = [char]) : (acc[indexY][indexX] = char)
        })

        return acc
      }, [])
      .map(char => char.reverse().filter(char => char !== " "))

    return parsed
      .slice(separatorIndex + 1)
      .map(instruction => {
        const exec = /move (?<amount>\d*) from (?<from>\d*) to (?<to>\d*)/.exec(
          instruction
        )

        return {
          from: Number(exec.groups.from),
          to: Number(exec.groups.to),
          amount: Number(exec.groups.amount),
        }
      })
      .reduce((acc, instruction) => {
        const fromStack = acc[instruction.from - 1]
        const removed = fromStack.splice(
          fromStack.length - instruction.amount,
          instruction.amount
        )

        acc[instruction.to - 1].push(...removed.reverse())

        return acc
      }, initialState)
      .map(line => line.at(-1))
      .join("")
  })()
)
