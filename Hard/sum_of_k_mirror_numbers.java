class Solution {
    private boolean isKPalindrome(long num, int k) {
        StringBuilder sb = new StringBuilder();
        while (num > 0) {
            sb.append(num % k);
            num /= k;
        }
        int l = 0, r = sb.length() - 1;
        while (l < r) {
            if (sb.charAt(l++) != sb.charAt(r--)) return false;
        }
        return true;
    }

    public long kMirror(int k, int n) {
        long ans = 0;
        int len = 1;

        while (n > 0) {
            int start = (len == 1) ? 1 : (int)Math.pow(10, len - 1);
            int end = start * 10;

            for (int i = start; i < end && n > 0; i++) {
                String left = String.valueOf(i);
                String right = new StringBuilder(left.substring(0, left.length() - 1)).reverse().toString();
                long val = Long.parseLong(left + right);
                if (isKPalindrome(val, k)) {
                    ans += val;
                    n--;
                }
            }

            for (int i = start; i < end && n > 0; i++) {
                String left = String.valueOf(i);
                String right = new StringBuilder(left).reverse().toString();
                long val = Long.parseLong(left + right);
                if (isKPalindrome(val, k)) {
                    ans += val;
                    n--;
                }
            }

            len++;
        }

        return ans;
    }
}
