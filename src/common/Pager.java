package common;


import org.javatuples.KeyValue;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Pager <T>{
    public static int DEFAULT_PAGE_SIZE = 20;
    public static int HOME_PAGE_SIZE = 20;
    public static int LIST_PAGE_SIZE = 40;
    private static HttpServletRequest request;

    static {
        request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    public static int getPageIndexFromQueryString() {
        String pageStr = request.getParameter("page");
        return computePageIndex( pageStr );
    }

    private static int computePageIndex( String pageString ){
        int page = 1;
        if( pageString == null ) {
            return page;
        }
        boolean isInt = Pattern.compile("^\\d+$").matcher(pageString).matches();
        if( !isInt ) {
            return page;
        }
        page = Integer.parseInt(pageString);
        return page;
    }

    private int pageIndex;
    private int pageSize;
    private int dataCount;
    private int numericButtonCount;
    private List<T> dataList;

    public Pager(int pageSize, int pageIndex, List<T> dataList) {
        request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        this.dataList = dataList;
    }

    public void setDataList(List newDataList) {
        this.dataList = newDataList;
    }

    public List<T> getDataList() {
        return this.dataList;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    private int getPageCount() {
        if (dataCount == 0) {
            return 1;
        }
        return (int)Math.ceil((double)dataCount / (double)pageSize);
    }

    public void setDataCount( int dataCount ) {
        this.dataCount = dataCount;
    }

    public int getDataCount() {
        return this.dataCount;
    }

    public void setNumericButtonCount( int numericButtonCount ) {
        this.numericButtonCount = numericButtonCount;
    }

    public KeyValue<Integer, Integer> getRange() {
        int start = pageSize * ( pageIndex - 1 ) + 1;
        int end = start + pageSize - 1;
        KeyValue range = KeyValue.with(start, end);
        return range;
        //return pageSize * ( pageIndex - 1 ) + "," + pageSize;
    }

    private String getLink( int page ) {
        String link = "";
        String oldUrl =  request.getRequestURI().toString();
        String queryString = request.getQueryString();
        if( !Common.isNullOrEmpty(queryString)) {
            oldUrl += "?"+ queryString;
        }
        if( oldUrl.indexOf("?") == -1 ) {
            link += oldUrl + "?page=" + page;
        } else {
            Pattern regex = Pattern.compile("page=\\d*", Pattern.CASE_INSENSITIVE);
            Matcher matcher = regex.matcher( oldUrl );
            if( matcher.find() ) {
                link = matcher.replaceAll("page=" + page);
            } else {
                link = oldUrl + "&page=" + page;
            }
        }
        return link;
    }

    public String toString( int numericButtonCount ) {
        this.setNumericButtonCount(numericButtonCount);
        return toString();
    }

    public String toString()
    {
        int start;
        int end;
        int interval = (int)Math.floor((double)numericButtonCount / 2);
        StringBuilder pageHtml = new StringBuilder();

        if (pageIndex <= interval)
        {
            start = 1;
            end = numericButtonCount < getPageCount() ? numericButtonCount : getPageCount();
        }
        else if (pageIndex > getPageCount() - interval)
        {
            start = numericButtonCount < getPageCount() ? getPageCount() - numericButtonCount + 1 : 1;
            end = getPageCount();
        }
        else
        {
            start = pageIndex - interval;
            end = numericButtonCount % 2 != 0 ? pageIndex + interval : pageIndex + interval - 1;
        }

        if (pageIndex == 1)
        {
            pageHtml.append("<li class='disabled'><a href='javascript:;'>首页</a></li>");
            pageHtml.append("<li class='disabled'><a href='javascript:;'>上页</a></li>");
        }
        else
        {
            pageHtml.append("<li><a href='"+ getLink(1) +"'>首页</a></li>");
            pageHtml.append("<li><a href='" + getLink(pageIndex - 1) + "'>上页</a></li>");
        }

        for (int i = start; i <= end; i++)
        {
            if (pageIndex == i) pageHtml.append("<li class='active'><a href='javascript:;'>" + i + "</a></li>");
            else pageHtml.append("<li><a href='"+ getLink(i) +"'>" + i + "</a></li>");
        }

        if (pageIndex == getPageCount())
        {
            pageHtml.append("<li  class='disabled'><a href='javascript:;'>下页</a></li>");
            pageHtml.append("<li  class='disabled'><a href='javascript:;'>末页</a></li>");
        }
        else
        {
            pageHtml.append("<li><a href='" + getLink(pageIndex + 1) + "'>下页</a></li>");
            pageHtml.append("<li><a href='" + getLink(getPageCount()) + "'>末页</a></li>");
        }
        return pageHtml.toString();
    }
}