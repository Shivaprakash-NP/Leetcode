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
    boolean ans = false;
    private void dfs(int rem , TreeNode node) {
        if(node == null) return;
        if(node.left == null && node.right == null && rem == node.val) ans = true;

        dfs(rem-node.val , node.left);
        dfs(rem-node.val , node.right);
    }
    public boolean hasPathSum(TreeNode root, int targetSum) {
        dfs(targetSum , root);
        return ans;
    }
}