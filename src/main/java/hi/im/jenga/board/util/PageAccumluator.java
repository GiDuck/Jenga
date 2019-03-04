package hi.im.jenga.board.util;

public class PageAccumluator {

    public static final int PAGE_LIMIT = 20;

    public static Paging getPageInfo(final double page, final double boardCount){

        double headOfPageNum = (page - 1) * 10 + 1;
        double tailOfPageNum = headOfPageNum + PAGE_LIMIT - 1;
        double startPageNum = (((page / 10 + 0.9)) - 1) * 10 + 1;
        double endPageNum = (boardCount / PAGE_LIMIT) + 0.95;

        double syncPageNum = startPageNum + 10 - 1;
        if(endPageNum > syncPageNum){ endPageNum = syncPageNum; }

        Paging newPage = new Paging();
        newPage.setNowPage(page);
        newPage.setHeadPageNum(headOfPageNum);
        newPage.setTailPageNum(tailOfPageNum);
        newPage.setStartPage(startPageNum);
        newPage.setEndPage(endPageNum);

        return newPage;

    }

}
