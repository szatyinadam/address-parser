package hu.novaservices;

class PartialResult {
    private final int left;
    private final int right;
    private final int contentleft;
    private final int contentright;

    public PartialResult(int left, int right, int contentleft, int contentright) {
        this.left = left;
        this.right = right;
        this.contentleft = contentleft;
        this.contentright = contentright;
    }

    public int getContentleft() {
        return contentleft;
    }

    public int getContentright() {
        return contentright;
    }

    public int getLeft() {
        return left;
    }

    public int getRight() {
        return right;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || o.getClass() != PartialResult.class)
            return false;
        PartialResult other = (PartialResult) o;
        return (left == other.left && right == other.right && contentleft == other.contentleft && contentright == other.contentright);
    }
}
