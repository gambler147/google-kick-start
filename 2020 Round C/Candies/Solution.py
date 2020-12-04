# Problem Carl has an array of N candies. The i-th element of the array (indexed
# starting from 1) is Ai representing sweetness value of the i-th candy. He
# would like to perform a series of Q operations. There are two types of
# operation: Update the sweetness value of a candy in the array. Query the
# sweetness score of a subarray.

# The sweetness score of a subarray from index l to r is: Al × 1 - Al+1 × 2 +
# Al+2 × 3 - Al+3 × 4 + Al+4 × 5 ...

# More formally, the sweetness score is the sum of (-1)i-lAi × (i - l + 1), for
# all i from l to r inclusive.

# For example, the sweetness score of: [3, 1, 6] is 3 × 1 - 1 × 2 + 6 × 3 = 19
# [40, 30, 20, 10] is 40 × 1 - 30 × 2 + 20 × 3 - 10 × 4 = 0 [2, 100] is 2 × 1 -
# 100 × 2 = -198

# Carl is interested in finding out the total sum of sweetness scores of all
# queries. If there is no query operation, the sum is considered to be 0. Can
# you help Carl find the sum?

# Input The first line of the input gives the number of test cases, T. T test
# cases follow. Each test case begins with a line containing N and Q. The second
# line contains N integers describing the array. The i-th integer is Ai. The
# j-th of the following Q lines describe the j-th operation. Each line begins
# with a single character describing the type of operation (U for update, Q for
# query). For an update operation, two integers Xj and Vj follow, indicating
# that the Xj-th element of the array is changed to Vj. For a query operation,
# two integers Lj and Rj follow, querying the sweetness score of the subarray
# from the Lj-th element to the Rj-th element (inclusive).

# Output For each test case, output one line containing Case #x: y, where x is
# the test case number (starting from 1) and y is the total sum of sweetness
# scores of all the queries.

# Limits Time limit: 20 seconds per test set. Memory limit: 1GB. 1 ≤ T ≤ 100. 1
# ≤ Ai ≤ 100, for all i. 1 ≤ N ≤ 2 × 105 and 1 ≤ Q ≤ 105 for at most 6 test
# cases. For the remaining cases, 1 ≤ N ≤ 300 and 1 ≤ Q ≤ 300. If the j-th
# operation is an update operation, 1 ≤ Xj ≤ N and 1 ≤ Vj ≤ 100. If the j-th
# operation is a query operation, 1 ≤ Lj ≤ Rj ≤ N.

import math

class SegmentTree:
    def __init__(self, arr):
        self.arr = arr
        self.arrSize = len(self.arr)
        self.treeSize = 1 << math.ceil(math.log(self.arrSize, 2)) + 1

        self.tree = [None] * self.treeSize
        self.__buildTree__(0,0,self.arrSize-1)
        self.mtree = [None] * self.treeSize
        self.__buildMTree__(0,0,self.arrSize-1)


    def __buildTree__(self, pos, i, j):
        # for this tree, the leaf has value of (-1)^{i}*arr[i]
        if i == j:
            # leaf node
            self.tree[pos] = (-1)**i * self.arr[i]
        else:
            # not leaf node
            mid = (i+j) >> 1
            left,right = 2*pos+1, 2*pos+2 # left and right children indices
            self.__buildTree__(left, i, mid)
            self.__buildTree__(right, mid+1, j)
            # find sum of current node
            self.tree[pos] = self.tree[left] + self.tree[right]
        

    def __buildMTree__(self,pos,i,j):
        # for this tree, the leaf has value of (-1)^{i}*arr[i]*(i+1)
        if i == j:
            # leaf node, base case
            self.mtree[pos] = (-1)**i * self.arr[i] * (i+1)
        else:
            # not leaf node
            mid = (i+j) >> 1
            left,right = 2*pos+1, 2*pos+2
            self.__buildMTree__(left, i, mid)
            self.__buildMTree__(right, mid+1, j)
            # find sum of current node
            self.mtree[pos] = self.mtree[left] + self.mtree[right]

    def update(self, i, val):
        self.__update__(0,0,self.arrSize-1,i,val)
        self.__mupdate__(0,0,self.arrSize-1,i,val)
        self.arr[i] = val

    def __update__(self, pos, l, r, i, val):
        # this function updates self.tree, changing ith value to val
        # [l,r] is the range that self.tree[pos] covers
        if l <= i <= r:
            # add (-1)**i * val  minus (-1)**i * arr[i]
            self.tree[pos] += (-1)**i*val - (-1)**i*self.arr[i]
            if l == r: return # this is already leaf node
            # otherwise, go to child node
            mid = (l+r) >> 1
            self.__update__(2*pos+1, l, mid, i, val)
            self.__update__(2*pos+2, mid+1, r, i, val)
            
    def __mupdate__(self, pos, l, r, i, val):
        # this function updates self.mtree
        if l <= i <= r:
            # add (-1)**i * val  minus (-1)**i * arr[i]
            self.mtree[pos] += (-1)**i*val*(i+1) - (-1)**i*self.arr[i]*(i+1)
            if l == r: return # this is already leaf node
            # otherwise, go to child node
            mid = (l+r) >> 1
            self.__mupdate__(2*pos+1, l, mid, i, val)
            self.__mupdate__(2*pos+2, mid+1, r, i, val)


    def query(self, i, j):
        # this function returns sweetness of candies from index i to index j, inclusive
        t = self.__query__(self.tree, 0, 0, self.arrSize-1, i, j)
        mt = self.__query__(self.mtree, 0, 0, self.arrSize-1, i, j)
        return (-1)**i * (mt - i * t)
        
    def __query__(self, tree, pos, l, r, i, j):
        # query (-1)**i arr[i] + ... + (-1)**(j) arr[j] or (-1)**i arr[i]*i + ... + (-1)**(j) arr[j]*j
        # [l,r] is the range that self.tree[pos] or self.mtree[pos] covers
        if i > r or j < l:
            # [l,r] and [i,j] has no intersection
            return 0
        
        elif l >= i and r <= j:
            return tree[pos]

        mid = (l+r) >> 1
        left = self.__query__(tree, 2*pos+1, l, mid, i, j)
        right = self.__query__(tree, 2*pos+2, mid+1, r, i, j)
        return left + right


if __name__ == '__main__':
    T = int(input()) # number of test cases
    for case in range(1,T+1):
        N, Q = list(map(int, input().split())) # array length and number of queries
        arr = list(map(int, input().split())) # candies array
        # build a tree
        tree = SegmentTree(arr)
        ans = 0  # print value
        for _ in range(Q):
            op, i,j = input().split()
            i,j = int(i), int(j)
            if op == 'Q':
                ans += tree.query(i-1, j-1) # here i-1,j-1 are starting index and end index
                # print(ans)
            else:
                # op == 'U'
                tree.update(i-1, j) # here value j is added to ith number
        print('Case #' + str(case) + ': ' + str(ans))



