<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>

<%@include file="common/header.jsp"%>
<div class="right">
    <div class="location">
        <strong>你现在所在的位置是:</strong>
        <span>用户管理页面 >> 用户修改页面</span>
    </div>
    <div class="providerAdd">
        <form id="billForm" name="billForm" method="post" action="${pageContext.request.contextPath }/jsp/bill.do">
            <input type="hidden" name="method" value="modify">
            <input type="hidden" name="id" value="${bill.id}">
            <div class="">
                <label for="billCode">订单编码：</label>
                <input type="text" name="billCode" id="billCode" value="${bill.billCode }" readonly="readonly">
            </div>
            <div>
                <label for="productName">商品名称：</label>
                <input type="text" name="productName" id="productName" value="${bill.productName }">
                <font color="red"></font>
            </div>
            <div>
                <label for="productUnit">商品单位：</label>
                <input type="text" name="productUnit" id="productUnit" value="${bill.productUnit }">
                <font color="red"></font>
            </div>
            <div>
                <label for="productCount">商品数量：</label>
                <input type="text" name="productCount" id="productCount" value="${bill.productCount }">
                <font color="red"></font>
            </div>
            <div>
                <label for="totalPrice">总金额：</label>
                <input type="text" name="totalPrice" id="totalPrice" value="${bill.totalPrice }">
                <font color="red"></font>
            </div>
            <div>
                <label for="providerId">供应商：</label>
                <input type="hidden" value="${bill.providerId}" id="pid" />
                <select name="providerId" id="providerId">
                </select>
                <font color="red"></font>
            </div>
            <div>
                <label >是否付款：</label>
                <input type="radio" name="isPayment" value="1" <c:if test="${bill.isPayment == 1}">checked="checked"</c:if>/>未付款
                <input type="radio" name="isPayment" value="2" <c:if test="${bill.isPayment == 2}">checked="checked"</c:if>/>已付款
            </div>
            <div class="providerAddBtn">
                <input type="button" id="saveBtn" name="saveBtn"  value="保存">
                <input type="button" id="backBtn" name="backBtn" value="返回" >
            </div>
        </form>
    </div>
</div>
</section>
<%@include file="common/footer.jsp"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/billmodify.js"></script>