import { readFile } from "fs/promises"

const cdRegex = /^\$ cd (?<name>.*)$/
const lsRegex = /^\$ ls$/
const dirRegex = /^dir (?<name>.*)$/
const fileRegex = /^(?<size>\d*) (?<name>.*)$/

const findRoot = folder => (folder.parent ? findRoot(folder.parent) : folder)
const findFolders = folder =>
  folder.children.reduce((acc, current) => {
    if (!current.size) return [...acc, current, ...findFolders(current)]
    return acc
  }, [])
const computeFolderSize = folder =>
  folder.children.reduce(
    (acc, current) =>
      acc + (current.size ? Number(current.size) : computeFolderSize(current)),
    0
  )

const fileTree = (await readFile("./input.txt", { encoding: "utf8" }))
  .split("\n")
  .reduce(
    (acc, current) =>
      current.startsWith("$")
        ? [...acc, { input: current }]
        : [
            ...acc.slice(0, acc.length - 1),
            { ...acc.at(-1), output: [...(acc.at(-1).output ?? []), current] },
          ],
    []
  )
  .reduce((acc, current, index, instructions) => {
    if (cdRegex.test(current.input)) {
      const { name } = cdRegex.exec(current.input).groups

      if (name === "/")
        return {
          name: "/",
          children: [],
        }
      if (name === "..") return acc.parent
      return acc.children.find(child => child.name === name)
    }
    if (lsRegex.test(current.input)) {
      acc.children =
        current.output?.map(lsOutput => {
          if (dirRegex.test(lsOutput)) {
            const { name } = dirRegex.exec(lsOutput).groups

            return {
              name,
              parent: acc,
              children: [],
            }
          }

          if (fileRegex.test(lsOutput)) {
            const { name, size } = fileRegex.exec(lsOutput).groups

            return {
              name,
              size,
              parent: acc,
            }
          }

          return lsOutput
        }) ?? []
    }

    return index === instructions.length - 1 ? findRoot(acc) : acc
  }, {})

const TOTAL_SPACE = 70_000_000
const FREE_SPACE = TOTAL_SPACE - computeFolderSize(fileTree)
const REQUIRED_SPACE = 30_000_000

console.log(
  findFolders(fileTree)
    .map(folder => ({
      name: folder.name,
      size: computeFolderSize(folder),
    }))
    .filter(folder => folder.size >= REQUIRED_SPACE - FREE_SPACE)
    .sort((folder1, folder2) => folder1.size - folder2.size)[0].size
)
