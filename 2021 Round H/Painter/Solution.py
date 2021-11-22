import traceback
from math import *


def solve(): 
  """
  Linear scan, in fact we can separate them into 3 channels, red, yellow and blue and keep track of consecutive squares that 
  contain same color.

  in binary representation,
  red is 1 (1 for int), 
  yellow is 10 (2 for int), 
  blue is 100 (4 for int),
  for other combined colors, add base colors up
  """

  n = int(input())
  S = input()

  colors = ['U', 'R', 'Y', 'O', 'B', 'P', 'G', 'A']
  color_to_int = {c: i for i,c in enumerate(colors)}

  # loop through the squares and keep track of consecutive colors
  rflag, yflag, bflag = False, False, False

  res = 0 

  for c in S:
    # get red, yellow and blue values
    val = color_to_int[c]
    val, r = divmod(val, 2)
    val, y = divmod(val, 2)
    val, b = divmod(val, 2)
    # check if r, y, b are 1
    if r == 1:
      rflag = True
    elif rflag == True:
      res += 1
      rflag = False

    if y == 1:
      yflag = True
    elif yflag == True:
      res += 1
      yflag = False

    if b == 1:
      bflag = True
    elif bflag == True:
      res += 1
      bflag = False

  res += int(rflag) + int(yflag) + int(bflag)
  return res

      

if __name__ == '__main__':
  try:
    T = int(input())
    for i in range(1, T+1):
      res = solve()
      print("Case #{}: {}".format(i, res))
  except Exception as e:
    print(traceback.format_exc())

