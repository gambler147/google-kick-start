# https://codingcompetitions.withgoogle.com/kickstart/round/0000000000435c44/00000000007ec1cb
# reference: https://github.com/kamyu104/GoogleKickStart-2021/blob/main/Round%20C/binary_operator2.py

import traceback
from random import randint
seen = {}
BOUND = 10**9+7


def solve(): 
  global seen
  seen = {} # initialize
  N = int(input())
  expressions = []
  for i in range(N):
    expressions.append(input())
  res = evaluateAndGroup(expressions)
  return res


def evaluateAndGroup(expressions):
  n = len(expressions)
  groups = {}
  res = []
  for i in range(n):
    v = evaluate(expressions[i])
    if v not in groups:
      groups[v] = len(groups)+1
    res.append(str(groups[v]))
  return ' '.join(res)


def evaluate(expr):
  n = len(expr)
  operands, operators, operand = [], [], 0
  for i in range(n):
    if expr[i].isdigit():
      operand = operand*10 + int(expr[i])
      if i == n-1 or not expr[i+1].isdigit():
        operands.append(operand)
        operand = 0
    elif expr[i] == ')':
      b = operands.pop()
      a = operands.pop()
      ops = operators.pop()
      new_operand = calc(a,b,ops)
      operands.append(new_operand)
    elif expr[i] in ('+', '*', '#'):
      operators.append(expr[i])

  return operands.pop()


def calc(a,b, ops):
  if ops == '+':
    return a + b
  elif ops == '*':
    return a*b
  else:
    return myhash(a,b)

def myhash(a,b):
  if (a,b) not in seen:
    seen[(a,b)] = randint(0, BOUND)
  return seen[(a,b)]


if __name__ == '__main__':
  try:
    T = int(input())
    for i in range(1, T+1):
      res = solve()
      print("Case #{}: {}".format(i, res))
  except Exception as e:
    print(traceback.format_exc())
