
with open((__file__.rstrip("code.py")+"input.txt"), 'r') as input_file:
    input = input_file.read()

input_as_lines = input.splitlines()

grid=[]
for line in input_as_lines:
  grid.append(line)

def isVisible(row, col):
  tree = grid[row][col]

  leftVisible = True
  for i in range(1, col+1):
    if (grid[row][col-i] >= tree):
      leftVisible = False
      break

  topVisible = True
  for i in range(1, row+1):
    if (grid[row-i][col] >= tree):
      topVisible = False
      break

  rightVisible = True
  for i in range(col+1, 99):
    if (grid[row][i] >= tree):
      rightVisible = False
      break

  
  bottomVisible = True
  for i in range(row+1, 99):
    if (grid[i][col] >= tree):
      bottomVisible = False
      break

  return leftVisible or topVisible or rightVisible or bottomVisible

def visibleLength(row, col):
  tree = grid[row][col]
  left, top, right, bottom = 0,0,0,0

  for i in range(col-1, -1, -1):
    left+=1
    if (grid[row][i] >= tree): break

  for i in range(row-1, -1, -1):
    top+= 1
    if (grid[i][col] >= tree):break

  for i in range(col+1, 99):
    right+=1
    if (grid[row][i] >= tree): break

  for i in range(row+1, 99):
    bottom+=1
    if (grid[i][col] >= tree):break

  return left*right*top*bottom
    

def foret1():
  visible = 4 * 99 - 4
  for x in range(1, 98):
    for y in range(1,98):
      if isVisible(x, y):
        visible = visible + 1
  print(visible)

def foret2():
  max = 0
  for x in range(0, 99):
    for y in range(0,99):
      lenght = visibleLength(x,y)
      if (lenght > max): max = lenght
  print(max)


foret1()
foret2()

