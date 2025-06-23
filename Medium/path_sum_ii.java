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
    List<List<Integer>> ans = new ArrayList<>();

    private void dfs(TreeNode node , int rem , List<Integer> val) {
        if(node == null) return;
        
        val.add(node.val);
        rem -= node.val;

        if(node.left == null && node.right == null && rem == 0) {
            ans.add(new ArrayList<>(val));
        } else {
            dfs(node.left , rem , val);
            dfs(node.right , rem , val);
        }

        val.remove(val.size() - 1);
    }

    public List<List<Integer>> pathSum(TreeNode root, int targetSum) {
        List<Integer> val = new ArrayList<>();
        if(root == null) return ans;
        dfs(root , targetSum , val);
        return ans;
    }
}