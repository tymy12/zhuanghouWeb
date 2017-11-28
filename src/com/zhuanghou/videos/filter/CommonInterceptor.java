package com.zhuanghou.videos.filter;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by duhui on 2017/11/27.
 */
public class CommonInterceptor extends HandlerInterceptorAdapter {
    // private final Logger log = LoggerFactory.getLogger(CommonInterceptor.class);
    public static final String LAST_PAGE = "com.alibaba.lastPage";
    /*
     * 利用正则映射到需要拦截的路径

    private String mappingURL;

    public void setMappingURL(String mappingURL) {
               this.mappingURL = mappingURL;
    }
  */

    /**
     * 在业务处理器处理请求之前被调用
     * 如果返回false
     * 从当前的拦截器往回执行所有拦截器的afterCompletion(),再退出拦截器链
     * 如果返回true
     * 执行下一个拦截器,直到所有的拦截器都执行完毕
     * 再执行被拦截的Controller
     * 然后进入拦截器链,
     * 从最后一个拦截器往回执行所有的postHandle()
     * 接着再从最后一个拦截器往回执行所有的afterCompletion()
     */
    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {
        if ("GET".equalsIgnoreCase(request.getMethod())) {
            //RequestUtil.saveRequest();
        }
        //log.info("==============执行顺序: 1、preHandle================");
        String requestUri = request.getRequestURI();
        String contextPath = request.getContextPath();
        String url = requestUri.substring(contextPath.length());


        //log.info("requestUri:"+requestUri);
        //log.info("contextPath:"+contextPath);
        //log.info("url:"+url);

        Map<String, Cookie> cookieMap = readCookieMap(request);
        String admin = "";
        String user = "";
        try {
            admin = cookieMap.get("admin").getValue();
        } catch (Exception e) {

        }

        try {
            user = cookieMap.get("user").getValue();
        } catch (Exception e) {

        }

        System.out.println("requestUri=" + requestUri);
//        System.out.println("user=" + user);
//        System.out.println("admin=" + admin);


        String[] urls = {"/admin/login", "/WEB-INF/admin_login.html", "/admin/loginAjax", "/user/login", "/WEB-INF/user_login.html"};

        for (int i = 0; i < urls.length; i++) {
            if (urls[i].equals(requestUri)) {
                return true;
            }
        }

        String[] strings = requestUri.split("/");

        System.out.println("strings[1]"+strings[1]);

        if (strings[1].equals("adminManage") || strings[1].equals("admin")) {

            if (!admin.equals("admin")) {
                request.getRequestDispatcher("/admin/login").forward(request, response);

                //log.info("Interceptor：跳转到login页面！");
                //response.sendRedirect("/admin/login");
            }
            //return  false;
            return true;
        } else if (strings[1].equals("userManage") || strings[1].equals("user")) {

            if (!user.equals("user")) {
                request.getRequestDispatcher("/user/login").forward(request, response);
            }
            return true;

        }else {
            if(!strings[1].equals("WEB-INF")){
                request.getRequestDispatcher("/user/login").forward(request, response);

            }
            return  true;
        }
    }


    /**
     * 在业务处理器处理请求执行完成后,生成视图之前执行的动作
     * 可在modelAndView中加入数据，比如当前时间
     */
    @Override
    public void postHandle(HttpServletRequest request,
                           HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        //log.info("==============执行顺序: 2、postHandle================");
        if (modelAndView != null) {  //加入当前时间
            modelAndView.addObject("var", "测试postHandle");
        }
    }

    /**
     * 在DispatcherServlet完全处理完请求后被调用,可用于清理资源等
     * <p>
     * 当有拦截器抛出异常时,会从当前拦截器往回执行所有的拦截器的afterCompletion()
     */
    @Override
    public void afterCompletion(HttpServletRequest request,
                                HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        //log.info("==============执行顺序: 3、afterCompletion================");
    }


    private Map<String, Cookie> readCookieMap(HttpServletRequest request) {
        Map<String, Cookie> cookieMap = new HashMap<>();
        Cookie[] cookies = request.getCookies();
        if (null != cookies) {
            for (Cookie cookie : cookies) {
                cookieMap.put(cookie.getName(), cookie);
            }
        }
        return cookieMap;
    }
}
