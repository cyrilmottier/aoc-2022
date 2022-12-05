
with open((__file__.rstrip("code.py")+"input.txt"), 'r') as input_file:
    input = input_file.read()

input_as_lines = input.splitlines()

tab = [['L', 'N', 'W', 'T' ,'D'],
['C', 'P', 'H'],
['W','P','H','N','D','G','M','J'],
['C','W','S','N','T','Q','L'],
['P','H','C','N'],
['T','H','N','D','M','W','Q','B'],
['M', 'B', 'R', 'J', 'G', 'S','L'],
['Z', 'N', 'W', 'G', 'V', 'B', 'R', 'T'],[
  'W', 'G', 'D', 'N', 'P', 'L'
]
]

def printTab():
  for row in tab:
    print(row)

def part1():
  for line in input_as_lines:
    splitedLine = line.split(' ')
    nb, froum, tou = int(splitedLine[1]), int(splitedLine[3]), int(splitedLine[5])
    for i in range(0, nb):
      value = tab[froum-1].pop()
      tab[tou-1].append(value)


def part2():
  for line in input_as_lines:
    splitedLine = line.split(' ')
    nb, froum, tou = int(splitedLine[1]), int(splitedLine[3]), int(splitedLine[5])
    temp = []
    for i in range(0, nb):
      temp.append(tab[froum-1].pop())
    for t in temp[::-1]:
      tab[tou-1].append(t)


  

part2()
for col in tab:
    print(col.pop(), end = '')