class Solution {
    public List<Integer> findCoins(int[] numWays) {
        List<Integer> way = Arrays.stream(numWays).boxed().collect(Collectors.toList());
        List<Integer> ans = new ArrayList<>();
        int n = numWays.length;

        while (true) {
            int ind = way.indexOf(1); 
            if (ind == -1) break;
            ans.add(ind + 1);  
            int coin = ind + 1;
            for (int i = n-1; i >= coin; i--) {
                way.set(i, way.get(i) - way.get(i - coin));
            }
            way.set(ind, 0);
        }

        for (int i = 1; i < n; i++) {
            if (way.get(i) != 0) return new ArrayList<>();
        }

        return ans;
    }
}
