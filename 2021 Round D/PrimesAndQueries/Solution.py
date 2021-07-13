# reference:
# https://github.com/kamyu104/GoogleKickStart-2021/blob/main/Round%20D/primes_and_queries.py
# https://en.wikipedia.org/wiki/Lifting-the-exponent_lemma
# https://proofwiki.org/wiki/Lifting_The_Exponent_Lemma_for_p%3D2

import traceback

class BIT():
  def __init__(self, n):
    self.tree = [0] * (n+1)

  def insert(self, i, val):
    i += 1
    while i < len(self.tree):
      self.tree[i] += val
      i += (i & -i)

  def query(self, i):
    i += 1
    res = 0
    while i > 0:
      res += self.tree[i]
      i -= (i & -i)
    return res


class PrimeQueries():
  def __init__(self, N, P):
    self.p = P # prime
    self.bits = [BIT(N) for _ in range(4 if P==2 else 3)]

  def update(self, i, prev, val):
    # update value for A[i], need to modify all 3 (or 4) BITs
    self.add(i, prev, -1)
    self.add(i, val, 1)
    
  def add(self, i, val, sign):
    x, y = val, val%self.p
    if x == y:
      return
    if y == 0:
      self.bits[0].insert(i, sign * vp(self.p, x))
    else:
      self.bits[1].insert(i, sign*1)
      self.bits[2].insert(i, sign*vp(self.p,x-y))
      if self.p == 2:
        self.bits[3].insert(i, sign * (vp(self.p, x+y)-1))

  def __query(self, i, S):
    # prefix result 
    res = S*self.bits[0].query(i)
    res += vp(self.p, S)*self.bits[1].query(i)
    res += self.bits[2].query(i)
    if self.p==2 and S%2==0:
      res += self.bits[3].query(i)
    return res

  def query(self, L, R, S):
    left = self.__query(L, S)
    right = self.__query(R, S)
    return right - left
    

def vp(p,x):
  res = 0
  while x > 0:
    if x%p != 0:
      break
    x//=p
    res+=1
  return res


def solve(): 
  """
  Lifting the exponential theorem: if x, y both cannot be divided by prime p but p divides x-y, then
  1. if x == y then V(x^n - y^n) = 0 = V(x-y) = V(x+y)
  2. if x != y then
    (1) if y = 0 then V(x^n - y^n) = n*V(x)
    (2) if y!= 0 then
      <1> if p!=2 or n is odd then V(x^n - y^n) = V(x-y) + V(n)
      <2> if p==2 and n is even then V(x^n - y^n) = V(x-y) + V(x+y) + V(n) - 1

  therefore, we use 4 trees to store:
    1. V(x) (only for those value x such that x%P == 0)
    2. 1 (for those value x such that x%P == 0 but x!=0; used to count how many V(n) in the sum)
    3. V(x-x%P) (for those whose value x such that x%P != 0)
    4. V(x+x%P) (for those whose value x such that x%P != 0)

  when query comes, 
    if n is odd or p != 2, we sum results from tree 1, 2 and 3
    if n is even and p == 2, we sum results from tree 1, 2, 3 and 4
  """  
  N, Q, P = list(map(int, input().split()))
  A = list(map(int, input().split()))
  primeQueries = PrimeQueries(N, P)
  for i, val in enumerate(A):
    primeQueries.add(i, val, 1)
  # do queries
  res = []
  for q in range(Q):
    query = list(map(int, input().split()))
    if query[0] == 1:
      # type 1 query, update value to BITs
      tp, i, val = query
      i -= 1
      primeQueries.update(i, A[i], val)
      A[i] = val
    else:
      # type 2 query, get query result
      tp, S, L, R = query
      ans = primeQueries.query(L-2,R-1,S)
      res.append(ans)

  return ' '.join(map(str, res))




if __name__ == '__main__':
  try:
    T = int(input())
    for i in range(1, T+1):
      res = solve()
      print("Case #{}: {}".format(i, res))
  except Exception as e:
    print(traceback.format_exc())
