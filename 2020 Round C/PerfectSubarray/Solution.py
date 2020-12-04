# Problem Cristobal has an array of N (possibly negative) integers. The i-th
# integer in his array is Ai. A contiguous non-empty subarray of Cristobal's
# array is perfect if its total sum is a perfect square. A perfect square is a
# number that is the product of a non-negative integer with itself. For example,
# the first five perfect squares are 0, 1, 4, 9 and 16.

# How many subarrays are perfect? Two subarrays are different if they start or
# end at different indices in the array, even if the subarrays contain the same
# values in the same order.

# Input The first line of the input gives the number of test cases, T. T test
# cases follow. The first line of each test case contains the integer N. The
# second line contains N integers describing Cristobal's array. The i-th integer
# is Ai.

# Output For each test case, output one line containing Case #x: y, where x is
# the test case number (starting from 1) and y is the number of perfect
# subarrays.

import collections
def solve(arr, n):
    # prefix sum
    psum = {}
    psum[0] = 1
    # store possible perfect square
    psquare =[]
    i = 0
    while i*i <= n*max(arr):
        psquare.append(i*i)
        i += 1

    s = 0
    res = 0
    for a in arr:
        s += a
        for ps in psquare:
            if s-ps in psum: res += psum[s-ps]
        psum[s] = psum.get(s, 0) + 1

    return res


if __name__ == '__main__':
    T = int(input()) # number of test cases
    for i in range(T):
        n = int(input())
        arr = list(map(int, input().split()))
        print('Case #' + str(i+1) + ': ' + str(solve(arr, n)))