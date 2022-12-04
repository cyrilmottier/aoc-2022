
with open((__file__.rstrip("code.py")+"input.txt"), 'r') as input_file:
    input = input_file.read()

input_as_lines = input.splitlines()

# 2-4,6-8

def doesContain(a, b):
  return (a[0] >= b[0] and a[1] <= b[1])

def doesOverlap(a, b):
    return a[0] <= b[1] and a[1] >= b[0]

def part1():
  toto=0
  for line in input_as_lines:
    pairs=line.split(',')
    one, two = [int(s) for s in pairs[0].split("-")], [int(s) for s in pairs[1].split("-")]
    if (doesContain(one, two) or doesContain(two, one)): 
      toto=toto+1
  print(toto)

def part2():
  toto=0
  for line in input_as_lines:
    pairs=line.split(',')
    one, two = [int(s) for s in pairs[0].split("-")], [int(s) for s in pairs[1].split("-")]
    if (doesOverlap(one, two) or doesOverlap(two, one)): 
      toto=toto+1
  print(toto)

part1()
part2()
