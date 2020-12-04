# Problem Apollo is playing a game involving polyominos. A polyomino is a shape
# made by joining together one or more squares edge to edge to form a single
# connected shape. The game involves combining N polyominos into a single
# rectangular shape without any holes. Each polyomino is labeled with a unique
# character from A to Z.

# Apollo has finished the game and created a rectangular wall containing R rows
# and C columns. He took a picture and sent it to his friend Selene. Selene
# likes pictures of walls, but she likes them even more if they are stable
# walls. A wall is stable if it can be created by adding polyominos one at a
# time to the wall so that each polyomino is always supported. A polyomino is
# supported if each of its squares is either on the ground, or has another
# square below it.

# Apollo would like to check if his wall is stable and if it is, prove that fact
# to Selene by telling her the order in which he added the polyominos.

# Input The first line of the input gives the number of test cases, T. T test
# cases follow. Each test case begins with a line containing the two integers R
# and C. Then, R lines follow, describing the wall from top to bottom. Each line
# contains a string of C uppercase characters from A to Z, describing that row
# of the wall.

# Output For each test case, output one line containing Case #x: y, where x is
# the test case number (starting from 1) and y is a string of N uppercase
# characters, describing the order in which he built them. If there is more than
# one such order, output any of them. If the wall is not stable, output -1
# instead.

# Limits Time limit: 20 seconds per test set. Memory limit: 1GB. 1 ≤ T ≤ 100. 1
# ≤ R ≤ 30. 1 ≤ C ≤ 30. No two polyominos will be labeled with the same letter.
# The input is guaranteed to be valid according to the rules described in the
# statement.


# idea this is like a topology sorting
from collections import deque, defaultdict
def solve(matrix, m, n):
    # m,n are number of rows and cols respectively
    # make a set of set
    edges = defaultdict(set)  # for each key, the value is a set of values which are required for stable wall
    # initialize polyominos
    polyominos = set(matrix[m-1])

    # move one row up at a time
    for i in range(m-2, -1, -1):
        for j in range(n):
            polyominos.add(matrix[i][j])
            # require matrix[i+1][j]
            if matrix[i+1][j] != matrix[i][j]:
                edges[matrix[i+1][j]].add(matrix[i][j])

    # now topology sorting
    indegree = defaultdict(int)
    for p in polyominos:
        for v in edges[p]:
            indegree[v] += 1

    queue = deque()
    for p in polyominos:
        if indegree[p] == 0:
            queue.append(p)

    count = 0
    res = []
    while queue:
        p = queue.popleft()
        res.append(p)
        for v in edges[p]:
            indegree[v] -= 1
            if indegree[v] == 0:
                queue.append(v)
        count += 1
    
    return ''.join(res) if count == len(polyominos) else -1


if __name__ == '__main__':
    T = int(input()) # number of test cases
    for i in range(T):
        m, n = list(map(int, input().split()))
        matrix = []
        for j in range(m):
            matrix.append(list(input()))
        print('Case #' + str(i+1) + ': ' + str(solve(matrix, m, n)))
