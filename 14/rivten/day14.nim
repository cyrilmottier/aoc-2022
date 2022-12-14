import strutils
import sets
import sequtils
import nre

proc parseWall(wall: string): tuple[x:int, y:int] =
  let captures = wall.find(re"(\d+),(\d+)").get.captures
  (x: captures[0].parseInt(), y: captures[1].parseInt())

proc parseLine(line: string): seq[tuple[x:int, y:int]] =
  line.split("->").map(parseWall)

let walls = readFile("input.txt")[0..^2].splitLines().map(parseLine)
var elems: HashSet[(int, int)]
for wall in walls:
  for i, s in wall[0..^2]:
    let e = wall[i + 1]
    if s.x == e.x:
      for y in countup(min(s.y, e.y), max(s.y, e.y)):
        elems.incl((s.x, y))
    else:
      for x in countup(min(s.x, e.x), max(s.x, e.x)):
        elems.incl((x, s.y))

let maxHeight =
  block:
    var m = 0
    for v in elems.items:
      m = max(v[1], m)
    m

echo maxHeight

var unit = 0
let sandStart = (500, 0)

while true:
  var currentSandP = sandStart
  echo "new sand"
  while true:
    if currentSandP[1] > maxHeight:
      echo unit
      quit()
    if (currentSandP[0], currentSandP[1] + 1) notin elems:
      currentSandP[1] += 1
      echo "sand", currentSandP
      continue
    if (currentSandP[0] - 1, currentSandP[1] + 1) notin elems:
      currentSandP[0] -= 1
      echo "sand", currentSandP
      continue
    if (currentSandP[0] + 1, currentSandP[1] + 1) notin elems:
      currentSandP[0] += 1
      echo "sand", currentSandP
      continue
    elems.incl(currentSandP)
    echo "adding sand at ", currentSandP
    break
  unit += 1

