
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>Title</title>
</head>
<body>
    <div class="page-bar">
        <ul class="page-num-ul clearfix">
            <li>共${param.totalCount}条记录&nbsp;&nbsp; ${param.currentPageNo}/${param.totalPageCount}页</li>
            <c:if test="${param.currentPageNo > 1}">
                <a href="javascript:page_nav(document.forms[0],1);">首页</a>
                <a href="javascript:page_nav(document.forms[0],${param.currentPageNo-1});">上一页</a>
            </c:if>
            <c:if test="${param.currentPageNo < param.totalPageCount }">
                <a href="javascript:page_nav(document.forms[0],${param.currentPageNo+1});">下一页</a>
                <a href="javascript:page_nav(document.forms[0],${param.totalPageCount});">最后一页</a>
            </c:if>
        </ul>
        <span class="page-go-form"><label>跳转至</label>
            <input type="text" name="inputPage" id="inputPage" class="page-key" />页
            <button type="button" class="page-btn" onClick='jump_to(document.forms[0],document.getElementById("inputPage").value)'>GO</button>
        </span>

    </div>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/rollpage.js"></script>

</body>
</html>
