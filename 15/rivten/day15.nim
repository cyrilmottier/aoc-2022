import strutils
import sequtils
import sets
import nre
import sugar

type P = tuple[x: int, y: int]

proc parseInput(line: string): (P, P) =
  let captures = line.find(re"Sensor at x=(-?\d+), y=(-?\d+): closest beacon is at x=(-?\d+), y=(-?\d+)").get.captures
  ((x: captures[0].parseInt, y: captures[1].parseInt), (x: captures[2].parseInt, y: captures[3].parseInt))
  
proc manahattanDistance(a: P, b: P): int =
  return abs(a.x - b.x) + abs(a.y - b.y)

proc canBeaconBeHere(p: P, sensors: seq[(P, int)]): bool =
  sensors.all((s) => manahattanDistance(s[0], p) > s[1])

let input = readFile("input.txt").splitLines()[0..^2]
let sensorsBeacon = input.map(parseInput)
let sensorsDistance = sensorsBeacon.map((t) => (t[0], manahattanDistance(t[0], t[1])))
let knownBeacons = sensorsBeacon.map((t) => t[1]).toHashSet()
echo knownBeacons

let m = min(sensorsDistance.map((s) => s[0].y - s[1]))
let M = max(sensorsDistance.map((s) => s[0].y + s[1]))

var count = 0
for x in countup(m, M):
  let b = (x: x, y: 2000000)
  if b notin knownBeacons and not canBeaconBeHere(b, sensorsDistance):
    count += 1

echo count
