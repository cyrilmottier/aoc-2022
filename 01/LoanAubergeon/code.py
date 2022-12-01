
with open((__file__.rstrip("code.py")+"input.txt"), 'r') as input_file:
    input = input_file.read()

input_as_lines = input.splitlines()

#part 1
def part1():
  max_cal = 0
  sum = 0
  for line in input_as_lines:
    if line != '':
      sum = sum + int(line)
    else:
      if sum > max_cal:
        max_cal = sum
      sum=0
  print(max_cal)

cal = []

#part 2
def part2():
  cal = []
  sum = 0
  for line in input_as_lines:
    if line != '':
      sum = sum + int(line)
    else:
      cal.append(sum)
      sum=0
  cal.sort(reverse=True)
  print(cal[0]+cal[1]+cal[2])


part1()
part2()
