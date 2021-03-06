package com.shi.servlet.user;

import com.alibaba.fastjson.JSONArray;
import com.mysql.cj.util.StringUtils;
import com.shi.Utiles.Constant;
import com.shi.Utiles.PageSupport;
import com.shi.pojo.Role;
import com.shi.pojo.User;
import com.shi.service.role.roleServiceImpl;
import com.shi.service.user.userServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getParameter("method");
        if(method != null && method.equals("query")){
            this.query(req, resp);
        }
        if(method != null && method.equals("getRoleList")){
            this.getRoleList(req, resp);
        }
        if(method != null && method.equals("userCodeExist")){
            this.userCodeExist(req, resp);
        }
        if(method != null && method.equals("addUser")){
            this.addUser(req, resp);
        }
        if(method != null && method.equals("viewUser")){
            this.getUserById(req, resp, "userview.jsp");
        }
        if(method != null && method.equals("modifyUser")){
            this.getUserById(req, resp, "usermodify.jsp");
        }
        if(method != null && method.equals("modifydata")){
            this.modifyUser(req, resp);
        }
        if(method != null && method.equals("deluser")){
            this.delUser(req, resp);
        }
        if(method != null && method.equals("pwdmodify")){
            this.pwdmodify(req, resp);
        }
        if(method != null && method.equals("savepwd")){
            this.savepwd(req, resp);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }

    // ????????????
    // 1.??????????????????
    private void query(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException{
        // ?????????????????????
        String queryUserName = req.getParameter("queryUserName"); // ???????????????????????????null
        String temp = req.getParameter("queryUserRole"); // ???????????????0?????????
        String pageIndex = req.getParameter("pageIndex"); // ??????
        int queryUserRole = 0;
        if(temp != null){
            queryUserRole = Integer.parseInt(temp);
        }

        //?????????service??????
        userServiceImpl userService = new userServiceImpl();
        roleServiceImpl roleService = new roleServiceImpl();

        //????????????
        int pageSize = Constant.pageSize;
        int currentPageNo = 1;
        if(pageIndex != null){ // ????????????????????????????????????????????????????????????
            currentPageNo = Integer.parseInt(pageIndex);
        }
        int totalCount = userService.getUserCount(queryUserName, queryUserRole);
        PageSupport pageSupport = new PageSupport();
        pageSupport.setPageSize(pageSize);
        pageSupport.setCurrentPageNo(currentPageNo);
        pageSupport.setTotalCount(totalCount);
        int totalPageCount = pageSupport.getTotalPageCount();
        if(currentPageNo < 1){
            currentPageNo = 1;
        }else if(currentPageNo > totalPageCount){
            currentPageNo = totalPageCount;
        }

        // ????????????
        List<Role> roleList = roleService.getRoleList();
        List<User> userList = userService.getUserList(queryUserName, queryUserRole, currentPageNo, pageSize);

        // ????????????
        req.setAttribute("userList", userList);
        req.setAttribute("roleList", roleList);
        req.setAttribute("queryUserName", queryUserName);
        req.setAttribute("queryUserRole", queryUserRole);
        req.setAttribute("totalPageCount", totalPageCount);
        req.setAttribute("currentPageNo", currentPageNo);
        req.setAttribute("totalCount", totalCount);
        req.getRequestDispatcher("userlist.jsp").forward(req, resp);
    }

    // 2.??????????????????
    private void getRoleList(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException{
        roleServiceImpl roleService = new roleServiceImpl();
        List<Role> roleList = roleService.getRoleList();
        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();
        writer.write(JSONArray.toJSONString(roleList));
        writer.flush();
        writer.close();
    }

    // 3.??????userCode??????????????????
    private void userCodeExist(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException{
        String userCode = req.getParameter("userCode");
        Map<String, String> resultMap = new HashMap<>();
        if(StringUtils.isNullOrEmpty(userCode)){
            resultMap.put("userCode", "exist");
        }else{
            userServiceImpl userService = new userServiceImpl();
            User user = userService.userCodeExist(userCode);
            if(user != null){
                resultMap.put("userCode", "exist");
            }else{
                resultMap.put("userCode", "not-exist");
            }
        }
        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();
        writer.write(JSONArray.toJSONString(resultMap));
        writer.flush();
        writer.close();
    }

    // ????????????
    private void addUser(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException{
        // ?????????????????????
        String userCode = req.getParameter("userCode");
        String userName = req.getParameter("userName");
        String userPassword = req.getParameter("userPassword");
        String gender = req.getParameter("gender");
        String birthday = req.getParameter("birthday");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");
        String userRole = req.getParameter("userRole");

        User user = new User();
        user.setUserCode(userCode);
        user.setUserName(userName);
        user.setUserPassword(userPassword);
        user.setGender(Integer.valueOf(gender));
        try {
            user.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse(birthday));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        user.setPhone(phone);
        user.setAddress(address);
        user.setUserRole(Integer.valueOf(userRole));
        User creator = (User) req.getSession().getAttribute(Constant.USER_SESSION);
        user.setCreateBy(creator.getId());
        user.setCreationDay(new Date());

        // ????????????
        userServiceImpl userService = new userServiceImpl();
        boolean flag = userService.addUser(user);
        if(flag){
            resp.sendRedirect(req.getContextPath() + "/jsp/user.do?method=query");
        }else{
            req.getRequestDispatcher("useradd.jsp").forward(req, resp);
        }
    }

    private void getUserById(HttpServletRequest req, HttpServletResponse resp, String url)throws ServletException, IOException {
        String id = req.getParameter("uid");
        if(!StringUtils.isNullOrEmpty(id)){
            userServiceImpl userService = new userServiceImpl();
            int userid = Integer.parseInt(id);
            User user = userService.getUserById(userid);
            req.setAttribute("user",user);
            req.getRequestDispatcher(url).forward(req, resp);
        }

    }
    private void modifyUser(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException {
        String id = req.getParameter("uid");
        String userName = req.getParameter("userName");
        String gender = req.getParameter("gender");
        String birthday = req.getParameter("birthday");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");
        String userRole = req.getParameter("userRole");

        User user = new User();
        user.setId(Integer.valueOf(id));
        user.setUserName(userName);
        user.setGender(Integer.valueOf(gender));
        try {
            user.setBirthday(new SimpleDateFormat("yyyy-MM-dd").parse(birthday));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        user.setPhone(phone);
        user.setAddress(address);
        user.setUserRole(Integer.valueOf(userRole));
        User modifior = (User) req.getSession().getAttribute(Constant.USER_SESSION);
        user.setModifyBy(modifior.getId());
        user.setModifyDate(new Date());

        userServiceImpl userService = new userServiceImpl();
        boolean flag = userService.modifyUser(user);
        if(flag){
            resp.sendRedirect(req.getContextPath() + "/jsp/user.do?method=query");
        }else{
            req.getRequestDispatcher("usermodify.jsp").forward(req, resp);
        }
    }
    private void delUser(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException {
        String id = req.getParameter("uid");
        int delid = Integer.parseInt(id);
        Map<String, String> resultMap = new HashMap<>();
        if(delid <= 0){
            resultMap.put("delResult", "notExist");
        }else{
            userServiceImpl userService = new userServiceImpl();
            boolean flag = userService.delUser(delid);
            if(flag){
                resultMap.put("delResult", "true");
            }else{
                resultMap.put("delResult", "false");
            }
        }
        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();
        writer.write(JSONArray.toJSONString(resultMap));
        writer.flush();
        writer.close();
    }

    // ??????????????????
    // ???????????????????????????
    private void pwdmodify(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException{
        Object obj = req.getSession().getAttribute(Constant.USER_SESSION);
        String oldpassword = req.getParameter("oldPassword");
        Map<String, String> resultMap = new HashMap<>();
        if(obj == null){
            resultMap.put("result", "sessionError");
        }else if(StringUtils.isNullOrEmpty(oldpassword)){
            resultMap.put("result", "oldPasswordError");
        }else{
            User user = (User) obj;
            if(user.getUserPassword().equals(oldpassword)){
                resultMap.put("result", "true");
            }else{
                resultMap.put("result", "false");
            }
        }

        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();
        writer.write(JSONArray.toJSONString(resultMap));
        writer.flush();
        writer.close();

    }

    private void savepwd(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException{
        Object obj = req.getSession().getAttribute(Constant.USER_SESSION);
        String newpassword = req.getParameter("newpassword");
        boolean flag = false;
        if(obj != null && !StringUtils.isNullOrEmpty(newpassword)){
            User user = (User) obj;
            userServiceImpl userService = new userServiceImpl();
            flag = userService.updateUserPwd(user.getId(), newpassword);
            if(flag){
                req.setAttribute(Constant.SYS_MESSAGE, "??????????????????????????????????????????????????????");
                // ???????????????????????????session
                req.getSession().removeAttribute(Constant.USER_SESSION);
            }
            else{
                req.setAttribute(Constant.SYS_MESSAGE, "??????????????????");
            }
        }else if(obj == null){
            resp.sendRedirect(req.getContextPath() + "/error.jsp");
        }else{
            req.setAttribute(Constant.SYS_MESSAGE, "??????????????????,??????????????????");
        }
        req.getRequestDispatcher("pwdmodify.jsp").forward(req, resp);
    }

}
