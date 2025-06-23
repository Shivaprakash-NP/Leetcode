/**
 * Definition for singly-linked list.
 * public class ListNode {
 *     int val;
 *     ListNode next;
 *     ListNode() {}
 *     ListNode(int val) { this.val = val; }
 *     ListNode(int val, ListNode next) { this.val = val; this.next = next; }
 * }
 */
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
    private TreeNode dfs(ListNode head) {
        if(head == null) return null;
        if(head.next == null) return new TreeNode(head.val);

        ListNode s = head;
        ListNode f = head;
        ListNode p = s;
        while(f != null && f.next != null) {
            p = s;
            s = s.next;
            f = f.next.next;
        }

        if(p.next!=null) p.next = null;

        TreeNode node = new TreeNode(s.val);

        node.left = dfs(head);
        node.right = dfs(s.next);
        
        return node;
    }

    public TreeNode sortedListToBST(ListNode head) {
        return dfs(head);
    }
}