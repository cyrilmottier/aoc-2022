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
import std/enumerate

let input = readFile("input.txt").splitLines()[0..^2].map((line) => line.split(re"[ s=;,]+"))

var G = initTable[string, HashSet[string]]()
for x in input:
  G[x[1]] = x[10..^1].toHashSet

var F = initTable[string, int]()
for x in input:
  if x[5].parseInt != 0:
    F[x[1]] = x[5].parseInt

var I = initTable[string, int]()
for i, x in enumerate(F.keys):
  I[x] = 1 shl i

var T = initTable[string, Table[string, int]]()
for x in G.keys:
  T[x] = initTable[string, int]()
  for y in G.keys:
    T[x][y] = if y in G[x]: 1 else: 10000
    
for k in T.keys:
  for i in T.keys:
    for j in T.keys:
      T[i][j] = min(T[i][j], T[i][k] + T[k][j])

proc visit(v: string, budget: int, state: int, flow: int, answer: var Table[int, int]): Table[int, int] {.discardable.} =
  answer[state] = max(answer.getOrDefault(state, 0), flow)
  for u in F.keys:
    let newbudget = budget - T[v][u] - 1
    if (I[u] and state) != 0 or newbudget <= 0:
      continue
    visit(u, newbudget, state or I[u], flow + newbudget * F[u], answer)
  return answer

var answer = initTable[int, int]()
let total1 = max(visit("AA", 30, 0, 0, answer).values.toSeq)
echo total1
answer = initTable[int, int]()
let visited2 = visit("AA", 26, 0, 0, answer)

iterator t(): int =
  for k1, v1 in visited2.pairs:
    for k2, v2 in visited2.pairs:
      if (k1 and k2) == 0:
        yield v1 + v2

let total2 = max(t().toSeq)
echo total2
