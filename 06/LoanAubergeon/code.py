
input = open("input.txt").read().strip()

def detect(size: int):
    for i in range(len(input) - size + 1):
        if len(set(input[i : i + size])) == size: 
          return i + size
    
print(detect(14))