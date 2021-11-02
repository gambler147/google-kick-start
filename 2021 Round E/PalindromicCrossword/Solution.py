
import traceback
from collections import Counter
from bisect import bisect_right

def solve(): 
  """
  This is an O(NMlog(MN)) solution 
  """
  # size of matrix
  m,n = list(map(int, input().split()))

  # matrix
  mat = []
  for i in range(m):
    mat.append(list(input()))

  # construct intervals by rows and cols separately
  row_intvl_starts = [[] for _ in range(m)]
  col_intvl_starts = [[] for _ in range(n)]
  row_intvl_lengths = [[] for _ in range(m)]
  col_intvl_lengths = [[] for _ in range(n)]

  # row
  for i in range(m):
    start, length = 0, 0 # size of intvl
    for j in range(n):
      if mat[i][j] == '#':
        # if length is greater than 0, append to intvl
        if length > 0:
          row_intvl_starts[i].append(start)
          row_intvl_lengths[i].append(length)
        length = 0
        start = j+1
      else:
        length += 1
    # check for last element
    if length > 0:
      row_intvl_starts[i].append(start)
      row_intvl_lengths[i].append(length)
    
  # col
  for j in range(n):
    start, length = 0, 0
    for i in range(m):
      if mat[i][j] == '#':
        if length > 0:
          col_intvl_starts[j].append(start)
          col_intvl_lengths[j].append(length)
        length = 0
        start = i+1
      else:
        length += 1
    if length > 0:
      col_intvl_starts[j].append(start)
      col_intvl_lengths[j].append(length)
  
  # now use dfs to fill the array
  def handle(i,j):
    # get all counterparts of cell (i,j)
    nonlocal cnt
    empty_cells = []
    stack = [(i,j)]
    visited.add((i,j))
    
    while stack:
      x,y = stack.pop()
      # find (i,j)'s reflection in both positions
      rint_idx = bisect_right(row_intvl_starts[x], y) - 1
      rstart = row_intvl_starts[x][rint_idx]
      rlen = row_intvl_lengths[x][rint_idx]

      cint_idx = bisect_right(col_intvl_starts[y], x) - 1
      cstart = col_intvl_starts[y][cint_idx]
      clen = col_intvl_lengths[y][cint_idx]

      # next positions
      rj = 2*rstart + rlen - y - 1
      ci = 2*cstart + clen - x - 1

      if (x, rj) not in visited:
        stack.append((x,rj))
        visited.add((x, rj))
        empty_cells.append((x,rj))
      
      if (ci, y) not in visited:
        stack.append((ci, y))
        visited.add((ci, y))
        empty_cells.append((ci, y))

    # count number of cells that are '.'
    for x,y in empty_cells:
      if mat[x][y] == '.':
        cnt += 1
        mat[x][y] = mat[i][j]
    
  cnt = 0
  visited = set()
  for i in range(m):
    for j in range(n):
      if mat[i][j] not in ('.', '#'):
        handle(i, j)

  return str(cnt) + '\n' + '\n'.join([''.join(a) for a in mat])
      

if __name__ == '__main__':
  try:
    T = int(input())
    for i in range(1, T+1):
      res = solve()
      print("Case #{}: {}".format(i, res))
  except Exception as e:
    print(traceback.format_exc())
