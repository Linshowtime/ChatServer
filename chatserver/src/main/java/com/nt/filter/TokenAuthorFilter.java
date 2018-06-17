package com.nt.filter;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSON;
import com.nt.entity.User;
import com.nt.net.Result;
import com.nt.util.JwtUtils;

/**
 * token验证拦截
 * 
 * @author showtime
 * @time 2018/5/27
 *
 */
@WebFilter(filterName="tokenAutuorFilter",urlPatterns= {"/api/auth/*"})
public class TokenAuthorFilter implements Filter {
	private static Logger logger = LoggerFactory.getLogger(TokenAuthorFilter.class);

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse rep = (HttpServletResponse) response;
		rep.setHeader("Access-Control-Allow-Origin", "*");
		rep.setHeader("Access-Controller-Allow-Methods", "Post,GET,OPTIONS,DELETE,PATCH");
		rep.setCharacterEncoding("UTF-8");
		rep.setContentType("application/json;charset=utf-8");
		String token = req.getHeader("token");
		boolean isFilter = false;
		Result result = new Result();
		if (null == token || token.isEmpty()) {
			result.setCode(4);
			result.setMsg("用户授权认证没有通过!客户端请求参数中无token信息");
		} else {
			if (JwtUtils.decode(token, User.class) != null) {
				result.setCode(5);
				result.setMsg("用户授权认证通过!");
				request.setAttribute("userid", JwtUtils.decode(token, User.class).getId());
				isFilter = true;
			} else {
				result.setCode(4);
				result.setMsg("用户授权认证没有通过!客户端请求参数token信息无效");
			}
		}
		if (result.getCode() == 4) {// 验证失败
			PrintWriter writer = null;
			OutputStreamWriter osw = null;
			try {
				osw = new OutputStreamWriter(response.getOutputStream(), "UTF-8");
				writer = new PrintWriter(osw, true);
				String jsonStr = JSON.toJSONString(result);
				writer.write(jsonStr);
				writer.flush();
				writer.close();
				osw.close();
			} catch (UnsupportedEncodingException e) {
				logger.error("过滤器返回信息失败:" + e.getMessage(), e);
			} catch (IOException e) {
				logger.error("过滤器返回信息失败:" + e.getMessage(), e);
			} finally {
				if (null != writer) {
					writer.close();
				}
				if (null != osw) {
					osw.close();
				}
			}
			return;
		}

		if (isFilter) {
			logger.info("token filter过滤ok!");
			chain.doFilter(request, response);
		}
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}

}
