
with open((__file__.rstrip("code.py")+"input.txt"), 'r') as input_file:
    input = input_file.read()

input_as_lines = input.splitlines()


ROCK_PTS = 1
PAPER_PTS = 2
SCISSOR_PTS = 3

WIN = 6
LOOSE = 0
EQ = 3

toto = 0

def getScore1(match):
  if (match == 'AX'):
    return ROCK_PTS + EQ
  if (match == 'AY'):
    return PAPER_PTS + WIN
  if (match == 'AZ'):
    return SCISSOR_PTS + LOOSE
  if (match == 'BX'):
    return ROCK_PTS + LOOSE
  if (match == 'BY'): 
    return PAPER_PTS + EQ
  if (match == 'BZ'):
    return SCISSOR_PTS  + WIN
  if (match == 'CX'):
    return ROCK_PTS + WIN
  if (match == 'CY'):
    return PAPER_PTS + LOOSE
  if (match == 'CZ'):
    return SCISSOR_PTS + EQ


def getScore2(match):
  # ROCK LOSE
  if (match == 'AX'):
    return SCISSOR_PTS + LOOSE
  # ROCK EQ
  if (match == 'AY'):
    return ROCK_PTS + EQ
  # ROCK WIN
  if (match == 'AZ'):
    return PAPER_PTS + WIN

  # PAPER LOSE
  if (match == 'BX'):
    return ROCK_PTS + LOOSE
  # PAPER EQ
  if (match == 'BY'): 
    return PAPER_PTS + EQ
  # PAPER WIN
  if (match == 'BZ'):
    return SCISSOR_PTS  + WIN

  # SCI LOSE
  if (match == 'CX'):
    return PAPER_PTS + LOOSE
  # SCI EQ
  if (match == 'CY'):
    return SCISSOR_PTS + EQ
  # SCI WIN
  if (match == 'CZ'):
    return ROCK_PTS + WIN


for line in input_as_lines:
  toto+=getScore2(line.replace(' ', ''))

print(toto)