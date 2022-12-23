import strutils
import algorithm
import tables
import sets
import options
import nre
import sequtils
import sugar
import deques
import strformat

proc getEdges(v: string, data: seq[(string, int, seq[string])]): seq[string] =
  for d in data:
    if d[0] == v:
      return d[2]

  assert false
  return @[]

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
  #echo line
  let captures = line.find(re"Valve (\w{2}) has flow rate=(\d+); tunnels? leads? to valves? (.*)").get.captures
  (captures[0], captures[1].parseInt, captures[2].split(", "))

let input = readFile("input.txt").splitLines()[0..^2]
let data = input.map(parseInput)

#echo data

var distances = initTable[(string, string), int]()
var realValves = data.filter((valve) => valve[1] != 0).map((valve) => valve[0])
for v0 in realValves:
  distances[("AA", v0)] = getDist("AA", v0, data)
  distances[(v0, "AA")] = getDist(v0, "AA", data)
  for v1 in realValves:
    distances[(v0, v1)] = getDist(v0, v1, data)

var flows = initTable[string, int]()
for v in realValves:
  for d in data:
    if d[0] == v:
      flows[v] = d[1]
      break

proc getFlowRate(v: string): int =
  flows[v]

# r0 > r1 > r2 > r3 > ...
proc cmpValve(a, b: string): int =
  cmp(getFlowRate(a), getFlowRate(b))

realValves.sort(cmpValve, Descending)

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
      currentFlowRate += getFlowRate(valve)
      break
    pressure += (d + 1) * currentFlowRate
    time += d + 1
    currentFlowRate += getFlowRate(valve)
    currentValve = valve

  (pressure, time, currentFlowRate)

proc bestPressureEstimate(currentPressure: int, time: int, currentFlowRate: int, remaining: HashSet[string]): int =
  currentPressure + (30 - time) * (currentFlowRate + remaining.items.toSeq.foldl(a + getFlowRate(b), 0))

proc getBestRemaining(remaining: var HashSet[string]): string =
  for v in realValves:
    if v in remaining:
      remaining.excl(v)
      return v
  assert false
  return ""

proc getMinDistBetweenRemainingValves(r: HashSet[string]): int =
  result = 1000000
  for v0 in r.items:
    for v1 in r.items:
      if v0 != v1:
        result = min(result, distances[(v0, v1)])
        if result == 2:
          return result

type
  GetPressureRealeased2Result = object
    currentValveElephant: string
    currentValveYou: string
    pressure: int
    time: int
    currentFlowRate: int
    timeAdvancedY: int
    timeAdvancedE: int

proc getMinTimeToGoToRemaining(p: GetPressureRealeased2Result, remaining: HashSet[string]): int =
  result = 100000
  for r in remaining.items:
    result = min(result, max(distances[(p.currentValveElephant, r)] - p.timeAdvancedE, 0))
    result = min(result, max(distances[(p.currentValveYou, r)] - p.timeAdvancedY, 0))
    if result <= 1:
      return result

proc bestPressureEstimate2(currentPressure: int, time: int, currentFlowRate: int, remaining: HashSet[string], p: GetPressureRealeased2Result): int =
  var r = remaining
  var timeRemaining = 26 - time + getMinTimeToGoToRemaining(p, remaining) - 1
  result = currentPressure + (currentFlowRate * timeRemaining)
  while true:
    if timeRemaining <= 0:
      break
    if r.len == 0:
      break
    var v = getBestRemaining(r)
    result += getFlowRate(v) * timeRemaining
    if r.len == 0:
      break
    v = getBestRemaining(r)
    result += getFlowRate(v) * timeRemaining
    timeRemaining -= getMinDistBetweenRemainingValves(r) + 1

  #currentPressure + (26 - time + lastDistDone - 2) * (currentFlowRate + remaining.items.toSeq.foldl(a + getFlowRate(b, data), 0))

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
    #echo "our best estimate with ", l, " is ", estimate, " and will not match current max"
    return

  for r in remaining.items():
    var l2 = l
    l2.addLast(r)
    var r2 = remaining
    r2.excl(r)
    perm(l2, r2, maxFound)

#var maxFound = 0
#perm(initDeque[string](), realValves.toHashSet(), maxFound)
#echo maxFound

proc advance(
  l: var Deque[string],
  currentValve: var string,
  pressure: var int,
  time: var int,
  currentFlowRate: var int,
  timeAdvance: var int
  ): bool =
  #echo "advancing ", l, " ", currentValve, " ", pressure, " ", time, " ", currentFlowRate
  let d = distances[(currentValve, l.peekFirst)] - timeAdvance
  timeAdvance = 0
  if time + d >= 26:
    pressure += (26 - time) * currentFlowRate
    time = 26
    return true
  if time + d + 1 >= 26:
    pressure += (26 - time - 1) * currentFlowRate
    time = 26
    currentFlowRate += getFlowRate(l.peekFirst)
    return true
  pressure += (d + 1) * currentFlowRate
  time += d + 1
  currentFlowRate += getFlowRate(l.peekFirst)
  currentValve = l.popFirst
  return false

proc getPressureReleased2(you: Deque[string], elephant: Deque[string]): GetPressureRealeased2Result =
  var currentValveElephant = "AA"
  var currentValveYou = "AA"
  var pressure = 0
  var time = 0
  var currentFlowRate = 0
  var y = you
  var e = elephant
  var timeAdvancedY = 0
  var timeAdvancedE = 0
  while e.len != 0 or y.len != 0:
    #echo &"debug>> {e} {y} time:{time} pressure:{pressure} currentFlowRate:{currentFlowRate} timeAdvancedE:{timeAdvancedE} timeAdvancedY:{timeAdvancedY} currentValveElephant:{currentValveElephant} currentValveYou:{currentValveYou}"
    if e.len != 0 and y.len != 0:
      let de = distances[(currentValveElephant, e.peekFirst)] - timeAdvancedE
      let dy = distances[(currentValveYou, y.peekFirst)] - timeAdvancedY
      if de < dy:
        if advance(e, currentValveElephant, pressure, time, currentFlowRate, timeAdvancedE):
          break
        timeAdvancedY += de + 1
      elif de > dy:
        if advance(y, currentValveYou, pressure, time, currentFlowRate, timeAdvancedY):
          break
        timeAdvancedE += dy + 1
      else: # de == dy
        let d = de
        timeAdvancedE = 0
        timeAdvancedY = 0
        if time + d >= 26:
          pressure += (26 - time) * currentFlowRate
          time = 26
          break
        if time + d + 1 >= 26:
          pressure += (26 - time - 1) * currentFlowRate
          time = 26
          currentFlowRate += getFlowRate(e.peekFirst) + getFlowRate(y.peekFirst)
          break
        pressure += (d + 1) * currentFlowRate
        time += d + 1
        currentFlowRate += getFlowRate(e.peekFirst) + getFlowRate(y.peekFirst)
        currentValveElephant = e.popFirst
        currentValveYou = y.popFirst
    elif e.len != 0:
      if advance(e, currentValveElephant, pressure, time, currentFlowRate, timeAdvancedE):
        break
    else: # y.len != 0
      if advance(y, currentValveYou, pressure, time, currentFlowRate, timeAdvancedY):
        break

  GetPressureRealeased2Result(pressure: pressure, time: time, currentFlowRate: currentFlowRate, currentValveElephant: currentValveElephant, currentValveYou: currentValveYou, timeAdvancedY: timeAdvancedY, timeAdvancedE: timeAdvancedE)

var impossible = initHashSet[(seq[string], seq[string])]()

proc perm2(you: Deque[string], elephant: Deque[string], remaining: HashSet[string], maxFound: var int) =
  if (you.items.toSeq, elephant.items.toSeq) in impossible:
    return
  let p = getPressureReleased2(you, elephant)
  var currentPressureReleased = p.pressure
  let time = p.time
  let currentFlowRate = p.currentFlowRate
  #echo "you:", you, " elephant:", elephant, " max found:", maxFound, " current pressure released:", currentPressureReleased, " time:", time, " current flow rate:", currentFlowRate, " max rate remaining: ", remaining.items.toSeq.foldl(a + getFlowRate(b, data), 0)
  if remaining.card == 0 or time >= 26:
    currentPressureReleased += (26 - time) * currentFlowRate
    if currentPressureReleased > maxFound:
      maxFound = currentPressureReleased
      echo "new max found with you:", you, " elephant:", elephant, " ", currentPressureReleased
      impossible.incl((you.items.toSeq, elephant.items.toSeq))
      impossible.incl((elephant.items.toSeq, you.items.toSeq))
      return

  let estimate = bestPressureEstimate2(currentPressureReleased, time, currentFlowRate, remaining, p)
  if estimate <= maxFound:
    impossible.incl((you.items.toSeq, elephant.items.toSeq))
    impossible.incl((elephant.items.toSeq, you.items.toSeq))
    return

  if time >= 26:
    impossible.incl((you.items.toSeq, elephant.items.toSeq))
    impossible.incl((elephant.items.toSeq, you.items.toSeq))
    return

  for r in remaining.items():
    var r2 = remaining
    r2.excl(r)
    var you2 = you
    var elephant2 = elephant
    you2.addLast(r)
    perm2(you2, elephant2, r2, maxFound)
    you2.popLast()
    elephant2.addLast(r)
    perm2(you2, elephant2, r2, maxFound)


var maxFound = 0
perm2(initDeque[string](), initDeque[string](), realValves.toHashSet, maxFound)
echo maxFound
