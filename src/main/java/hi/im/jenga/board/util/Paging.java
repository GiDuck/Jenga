package hi.im.jenga.board.util;

public class Paging {

    private double nowPage;
    private double startPage;
    private double endPage;
    private double maxPage;
    private double headPageNum;
    private double tailPageNum;

    public double getNowPage() {
        return nowPage;
    }

    public void setNowPage(double nowPage) {
        this.nowPage = nowPage;
    }

    public double getStartPage() {
        return startPage;
    }

    public void setStartPage(double startPage) {
        this.startPage = startPage;
    }

    public double getEndPage() {
        return endPage;
    }

    public void setEndPage(double endPage) {
        this.endPage = endPage;
    }

    public double getMaxPage() {
        return maxPage;
    }

    public void setMaxPage(double maxPage) {
        this.maxPage = maxPage;
    }

    public double getHeadPageNum() {
        return headPageNum;
    }

    public void setHeadPageNum(double headPageNum) {
        this.headPageNum = headPageNum;
    }

    public double getTailPageNum() {
        return tailPageNum;
    }

    public void setTailPageNum(double tailPageNum) {
        this.tailPageNum = tailPageNum;
    }
}
