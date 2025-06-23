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
class Solution {
    private ListNode rev(ListNode cur) {
        ListNode pre = null;

        while(cur != null) {
            ListNode t = cur.next;
            cur.next = pre;
            pre = cur;
            cur = t;
        }

        return pre;
    }
    public void reorderList(ListNode head) {
        if(head == null || head.next == null) return;

        ListNode s = head;
        ListNode f = head;

        while(f != null && f.next != null && null != f.next.next) {
            s = s.next;
            f = f.next.next;
        }

        ListNode sh = rev(s.next);
        s.next = null;
        ListNode fh = head;

        while(sh != null && fh != null) {
            ListNode n1 = fh.next;
            ListNode n2 = sh.next;

            fh.next = sh;
            sh.next = n1;
            
            fh = n1;
            sh = n2;
        }
    }
}