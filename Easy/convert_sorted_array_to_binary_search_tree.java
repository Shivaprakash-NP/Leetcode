/**
 * Definition for a binary tree node.
 * public class TreeNode {
 *     int val;
 *     TreeNode left;
 *     TreeNode right;
 *     TreeNode() {}
 *     TreeNode(int val) { this.val = val; }
 *     TreeNode(int val, TreeNode left, TreeNode right) {
 *         this.val = val;
 *         this.left = left;
 *         this.right = right;
 *     }
 * }
 */
class Solution {
    private TreeNode dfs(int[] arr) {
        int n = arr.length;
        if(0 == n) return null;
        if(1 == n) return new TreeNode(arr[0]);

        int m = n/2;
        TreeNode node = new TreeNode(arr[m]);
        node.left = dfs(Arrays.copyOfRange(arr , 0 , m));
        node.right = dfs(Arrays.copyOfRange(arr , m+1 , n));

        return node;
    }
    public TreeNode sortedArrayToBST(int[] nums) {
        return dfs(nums);
    }
}