package pers.hanchao.springclouddemo.servicezuul.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * <p>自定义Zuul过滤器，对请求进行token验证</p>
 * @author hanchao 2018/5/19 19:30
 **/
@Component
public class MyFilter extends ZuulFilter {
    private static Logger log = LoggerFactory.getLogger(ZuulFilter.class);

    /**
     * 过滤类型：
     * pre(路由转发之前),
     * routing(路由之时),
     * post(路由之后),
     * error(发生出错之时)
     * @return
     */
    @Override
    public String filterType() {
        return "pre";
    }

    /**
     * 过滤顺序
     * @return
     */
    @Override
    public int filterOrder() {
        return 0;
    }

    /**
     * 过滤规则：哪种请求进行过滤，true-都过滤
     * @return
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * 过滤逻辑
     * @return
     */
    @Override
    public Object run() {
        //获取请求上下文
        RequestContext ctx = RequestContext.getCurrentContext();
        //获取请求
        HttpServletRequest request = ctx.getRequest();
        //定义请求信息
        log.info(String.format("%s >> %s",request.getMethod(),request.getRequestURL().toString()));
        //获取token参数
        Object token = request.getParameter("token");
        //如果token不存在，则直接返回401，不再路由
        if (null == token){
            log.warn("token is empty.");
            //不再进行Zuul路由
            ctx.setSendZuulResponse(false);
            //设置响应状态码
            ctx.setResponseStatusCode(401);
            try{
                ctx.getResponse().getWriter().write("token is empty!");
            } catch (IOException e) {
                //e.printStackTrace();
            }
            log.info("ok");
            //不再进行后续过滤
            return null;
        }
        return null;
    }
}
