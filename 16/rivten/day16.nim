import strutils
import tables
import sets
import options
import nre
import sequtils
import sugar
import deques

proc getEdges(v: string, data: seq[(string, int, seq[string])]): seq[string] =
  for d in data:
    if d[0] == v:
      return d[2]

  assert false
  return @[]

proc getFlowRate(v: string, data: seq[(string, int, seq[string])]): int =
  for d in data:
    if d[0] == v:
      return d[1]

  assert false
  return 0

proc getDist(v0: string, v1: string, data: seq[(string, int, seq[string])]): int =
  var q = initDeque[(string, int)]()
  var explored = [v0].toHashSet()
  q.addLast((v0, 0))
  while q.len != 0:
    let vd = q.popFirst
    let v = vd[0]
    let d = vd[1]
    if v == v1:
      return d
    for e in getEdges(v, data):
      if not explored.contains(e):
        explored.incl(e)
        q.addLast((e, d + 1))
  assert false
  return 0


proc parseInput(line: string): (string, int, seq[string]) =
  echo line
  let captures = line.find(re"Valve (\w{2}) has flow rate=(\d+); tunnels? leads? to valves? (.*)").get.captures
  (captures[0], captures[1].parseInt, captures[2].split(", "))

let input = readFile("input.txt").splitLines()[0..^2]
let data = input.map(parseInput)

echo data

var distances = initTable[(string, string), int]()
let realValves = data.filter((valve) => valve[1] != 0).map((valve) => valve[0])
for v0 in realValves:
  distances[("AA", v0)] = getDist("AA", v0, data)
  distances[(v0, "AA")] = getDist(v0, "AA", data)
  for v1 in realValves:
    distances[(v0, v1)] = getDist(v0, v1, data)

echo distances

proc getPressureReleased(l: Deque[string]): (int, int, int) =
  var currentValve = "AA"
  var time = 0
  var pressure = 0
  var currentFlowRate = 0
  for valve in l.items:
    let d = distances[(currentValve, valve)]
    #echo "from ", currentValve, " to ", valve, " : ", d
    if time + d >= 30:
      pressure += (30 - time) * currentFlowRate
      time = 30
      break
    if time + d + 1 >= 30:
      pressure += (30 - time - 1) * currentFlowRate
      time = 30
      currentFlowRate += getFlowRate(valve, data)
      break
    pressure += (d + 1) * currentFlowRate
    time += d + 1
    currentFlowRate += getFlowRate(valve, data)
    currentValve = valve

  (pressure, time, currentFlowRate)

proc bestPressureEstimate(currentPressure: int, time: int, currentFlowRate: int, remaining: HashSet[string]): int =
  currentPressure + (30 - time) * (currentFlowRate + remaining.items.toSeq.foldl(a + getFlowRate(b, data), 0))


proc perm(l: Deque[string], remaining: HashSet[string], maxFound: var int) =
  let p = getPressureReleased(l)
  var currentPressureReleased = p[0]
  let time = p[1]
  let currentFlowRate = p[2]
  if remaining.card == 0:
    currentPressureReleased += (30 - time) * currentFlowRate
    if currentPressureReleased > maxFound:
      maxFound = currentPressureReleased
      echo "new max found with ", l, " ", currentPressureReleased
      return

  let estimate = bestPressureEstimate(currentPressureReleased, time, currentFlowRate, remaining)
  if estimate <= maxFound:
    echo "our best estimate with ", l, " is ", estimate, " and will not match current max"
    return

  for r in remaining.items():
    var l2 = l
    l2.addLast(r)
    var r2 = remaining
    r2.excl(r)
    perm(l2, r2, maxFound)


var maxFound = 0
perm(initDeque[string](), realValves.toHashSet(), maxFound)
echo maxFound
