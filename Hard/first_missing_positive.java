class Solution {
    public int firstMissingPositive(int[] nums) {
        int n = nums.length;
        for(int i = 0 ; i < nums.length ; i++) {
            while(nums[i]>=1 && nums[i]<=n && nums[i] != nums[nums[i]-1]) {
                int crtind = nums[i]-1;
                int tem = nums[crtind];
                nums[crtind] = nums[i];
                nums[i] = tem;
            }
        }

        for(int i = 0 ; i<n ; i++) {
            if(nums[i] != i+1) return i+1;
        }
        
        return n+1;
    }
}