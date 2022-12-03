
with open((__file__.rstrip("code.py")+"input.txt"), 'r') as input_file:
    input = input_file.read()

input_as_lines = input.splitlines()


def getPriority(char):
  n = ord(char) - 96
  if (n < 0):
    return n + 32 + 26
  return n


def part1():
  toto = 0
  for line in input_as_lines:
    chunks, chunk_size = len(line), len(line)//2
    res = [line[i:i+chunk_size] for i in range(0, chunks, chunk_size)]
    common = ''.join(set(res[0]).intersection(res[1]))
    toto+=getPriority(common)
  print(toto)

def part2():
  toto = 0
  for i in range(0, len(input_as_lines), 3):
    common = ''.join(set.intersection(*map(set,input_as_lines[i:i+3])))
    toto+=getPriority(common)
  print(toto)

part1()
part2()