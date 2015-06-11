package com.io.spring.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.io.spring.annotation.Layout;

public class ThymeleafLayoutInterceptor extends HandlerInterceptorAdapter {

	private static final String DEFAULT_LAYOUT = "layout/default";
	private static final String DEFAULT_VIEW_ATTRIBUTE_NAME = "view";

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		if (modelAndView == null || !modelAndView.hasView()) {
			return;
		}
		String originalViewName = modelAndView.getViewName();
		if (isRedirectOrForward(originalViewName)) {
            return;
        }
		String layoutName = getLayoutName(handler);
        modelAndView.setViewName(layoutName);
		modelAndView.addObject(DEFAULT_VIEW_ATTRIBUTE_NAME, originalViewName);
	}

	private boolean isRedirectOrForward(String viewName) {
        return viewName.startsWith("redirect:") || viewName.startsWith("forward:");
    }

	private String getLayoutName(Object handler) {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Layout layout = getMethodOrTypeAnnotation(handlerMethod);
        if (layout == null) {
            return DEFAULT_LAYOUT;
        } else {
            return layout.value();
        }
    }

	private Layout getMethodOrTypeAnnotation(HandlerMethod handlerMethod) {
        Layout layout = handlerMethod.getMethodAnnotation(Layout.class);
        if (layout == null) {
            return handlerMethod.getBeanType().getAnnotation(Layout.class);
        }
        return layout;
    }
	
}